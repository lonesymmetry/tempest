package desktopUI;

import control.Database;
import control.Item;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import javafx.stage.Stage;

/**
 * Runs the desktop application that displays the Items and provides for their management
 *
 * @author Logan Traffas
 */
public class DesktopApplication extends Application{
	private Stage mainStage;
	private static final String STYLESHEET_SOURCE = "/res/DesktopApplicationStylesheet.css";
	private static final boolean RESIZABLE = false;
	private Database database;
	private HBox rootPane;
	private VBox listPane;
	private VBox infoPane;
	private VBox addItemPane;

	public void constructInfoPane(util.Maybe<Item> activeItem){
		final double WIDTH_PERCENT = .67;
		final double WIDTH = StageConstants.DEFAULT_SIZE.width * WIDTH_PERCENT;
		this.infoPane.getChildren().clear();
		this.infoPane.setMaxWidth(WIDTH);
		this.infoPane.setPrefWidth(this.infoPane.getMaxWidth());
		this.infoPane.getStyleClass().add("infoPane");
		this.infoPane.setPadding(StageConstants.PADDING_INSETS);
		{
			StackPane itemDisplay = new StackPane();
			itemDisplay.setPadding(StageConstants.PADDING_INSETS);
			itemDisplay.getStyleClass().add("itemDisplay");

			final int BACKGROUND_HEIGHT = 500;
			Rectangle background = new Rectangle(WIDTH - (4 * StageConstants.PADDING),BACKGROUND_HEIGHT);
			background.getStyleClass().add("itemDisplayBackground");

			Text itemDisplayInfo = new Text();
			if(activeItem.isValid()){
				itemDisplayInfo.setText(activeItem.get().getDescription());
			} else {
				itemDisplayInfo.setText("");
			}
			itemDisplayInfo.getStyleClass().add("itemDisplayInfo");

			itemDisplay.getChildren().addAll(background,itemDisplayInfo);

			this.infoPane.getChildren().addAll(itemDisplay);
		}
	}

	private void constructListPane(){
		final double WIDTH_PERCENT = .33;
		final double WIDTH = StageConstants.DEFAULT_SIZE.width * WIDTH_PERCENT;

		this.listPane.setPadding(StageConstants.PADDING_INSETS);
		this.listPane.setMaxWidth(WIDTH);
		this.listPane.setPrefWidth(this.listPane.getMaxWidth());
		this.listPane.getStyleClass().add("listPane");
		{//the display for the list of items
			HBox itemListMenu = new HBox();
			{//set the content of the item list menu
				final double ITEM_LIST_MENU_HEIGHT = 50;//TODO: make same height as Buttons that contain the Item displayNames?
				itemListMenu.setMinSize(WIDTH, ITEM_LIST_MENU_HEIGHT);
				itemListMenu.setMaxSize(WIDTH, ITEM_LIST_MENU_HEIGHT);
				itemListMenu.setPrefSize(WIDTH, ITEM_LIST_MENU_HEIGHT);
				itemListMenu.getStyleClass().add("itemListMenu");

				Label temp = new Label("Menu");//TODO
				itemListMenu.getChildren().addAll(temp);
			}
			AnchorPane itemListBorder = new AnchorPane();
			{
				itemListBorder.setMinWidth(WIDTH);
				itemListBorder.setMaxWidth(WIDTH);
				itemListBorder.setPrefWidth(WIDTH);
				itemListBorder.getStyleClass().add("itemListBorder");

				{//set the content of the list of items
					ScrollPane itemList = new ScrollPane();

					final boolean PANNABLE = false;
					itemList.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
					itemList.setPannable(PANNABLE);
					itemList.getStyleClass().add("itemList");

					itemList.setHmax(WIDTH);
					itemList.setMinWidth(WIDTH);
					itemList.setMaxWidth(WIDTH);
					itemList.setPrefWidth(WIDTH);
					itemList.setPrefViewportWidth(WIDTH);
					{
						VBox items = new VBox(StageConstants.PADDING);
						final int BUTTON_HEIGHT = 75;
						util.Pair<Integer> BUTTON_SIZE = new util.Pair<>(
								(int)(itemList.getPrefViewportWidth() - (4 * StageConstants.PADDING + util.Graphics.SCROLL_BAR_WIDTH)),
								BUTTON_HEIGHT
						);
						for(Item item : database.getItems()){
							StackPane itemName = new StackPane();//TODO: remove if unneeded

							Button button = new Button(item.getDisplayName());
							button.setMinSize(BUTTON_SIZE.getFirst(), BUTTON_SIZE.getSecond());
							button.setMaxSize(BUTTON_SIZE.getFirst(), BUTTON_SIZE.getSecond());
							button.setPrefSize(BUTTON_SIZE.getFirst(), BUTTON_SIZE.getSecond());
							button.getStyleClass().add("itemName");
							button.setOnAction(
									(ActionEvent event) ->
											constructInfoPane(new util.Maybe<>(item))
							);

							itemName.getChildren().addAll(button);
							items.getChildren().add(itemName);
						}
						itemList.setContent(items);
					}
					itemListBorder.getChildren().add(itemList);
					itemListBorder.setBottomAnchor(itemList, (double)StageConstants.PADDING);
					itemListBorder.setTopAnchor(itemList, (double)StageConstants.PADDING);
					itemListBorder.setRightAnchor(itemList, (double)StageConstants.PADDING);
					itemListBorder.setLeftAnchor(itemList, (double)StageConstants.PADDING);
				}
			}
			this.listPane.getChildren().addAll(itemListMenu, itemListBorder);//order matters
		}
	}

	private void constructAddItemPane(){
		final double WIDTH_PERCENT = .67;
		final double WIDTH = StageConstants.DEFAULT_SIZE.width * WIDTH_PERCENT;

		this.addItemPane.setMaxWidth(WIDTH);
		this.addItemPane.setPrefWidth(this.addItemPane.getMaxWidth());
		this.addItemPane.getStyleClass().add("addItemPane");
	}

	private void constructRootPane(){
		this.rootPane.setMaxSize(StageConstants.DEFAULT_SIZE.width,StageConstants.DEFAULT_SIZE.height);
		this.rootPane.getStyleClass().add("rootPane");
		constructListPane();
		constructInfoPane(new util.Maybe<>());
		constructAddItemPane();
		this.rootPane.getChildren().addAll(this.listPane,this.infoPane);//ordered from left to right
	}

	@Override
	public void start(Stage primaryStage) throws Exception {
		initialize();
		this.mainStage = primaryStage;

		constructRootPane();

		this.mainStage.setTitle("Tempest");
		this.mainStage.setResizable(RESIZABLE);
		this.mainStage.setScene(new Scene(this.rootPane,StageConstants.DEFAULT_SIZE.width,StageConstants.DEFAULT_SIZE.height));
		this.mainStage.getScene().getStylesheets().add(STYLESHEET_SOURCE);
		this.mainStage.show();
	}

	private void initialize(){
		{
			//TODO: for testing only
			Database.testWrite();
		}
		this.mainStage = new Stage();
		this.database = new Database();
		this.database.fillList();
		this.rootPane = new HBox();
		this.listPane = new VBox(StageConstants.PADDING);
		this.infoPane = new VBox();
		this.addItemPane = new VBox();
	}

	public static void main(String[] args){
		launch(args);
	}
}
