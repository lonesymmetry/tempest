package desktopUI;

import control.Database;
import control.Item;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.layout.HBox;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.scene.control.Button;
import util.Maybe;

/**
 *
 *
 * @author Logan Traffas
 */
public class MainStage extends Stage{
	private static final String STYLESHEET_SOURCE = "/res/MainStageStylesheet.css";
	private static final boolean RESIZABLE = false;
	private Database database;
	private HBox rootPane;
	private VBox listPane;
	private VBox infoPane;
	private VBox addItemPane;

	private util.Maybe<Item> activeItemDisplay;//TODO: make own class?
	private BooleanProperty updateItemDisplay;

	public void updateActiveItem(){

	}

	public void constructInfoPane(final double INFO_PANE_WIDTH){
		this.infoPane = new VBox();
		this.infoPane.setMaxWidth(INFO_PANE_WIDTH);
		this.infoPane.setPrefWidth(this.infoPane.getMaxWidth());
		this.infoPane.getStyleClass().add("infoPane");
		this.infoPane.setPadding(StageConstants.PADDING_INSETS);
		{
			StackPane itemDisplay = new StackPane();
			itemDisplay.setPadding(StageConstants.PADDING_INSETS);
			itemDisplay.getStyleClass().add("itemDisplay");

			final int BACKGROUND_HEIGHT = 500;
			Rectangle background = new Rectangle(INFO_PANE_WIDTH - (4 * StageConstants.PADDING),BACKGROUND_HEIGHT);
			background.getStyleClass().add("itemDisplayBackground");

			final Text itemDisplayInfo = new Text();
			itemDisplayInfo.setText("ONE");
			if(this.activeItemDisplay.isValid()){
				itemDisplayInfo.setText("TWO" + this.activeItemDisplay.get().getDescription());
				System.out.println("Set text");
			} else {
				itemDisplayInfo.setText("");
				System.out.println("Cleared text");
			}
			//itemDisplayInfo.setText("THREE");
			itemDisplayInfo.getStyleClass().add("itemDisplayInfo");

			itemDisplay.getChildren().addAll(background,itemDisplayInfo);

			this.infoPane.getChildren().addAll(itemDisplay);
		}
	}

	public MainStage(final Database database){
		this.database = new Database();
		this.setResizable(RESIZABLE);

		{//initialize layout
			this.rootPane = new HBox();
			//this.rootPane.setPadding(StageConstants.PADDING_INSETS);//unnecessary
			this.rootPane.setMaxSize(StageConstants.DEFAULT_SIZE.width,StageConstants.DEFAULT_SIZE.height);
			this.rootPane.getStyleClass().add("rootPane");
		}
		{
			this.activeItemDisplay = new Maybe<>();
			this.updateItemDisplay = new SimpleBooleanProperty();
			this.updateItemDisplay.addListener(new ChangeListener<Boolean>() {
				@Override
				public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
					constructInfoPane(100.0);
				}
			});

		}
		{//initialize listPane //TODO: Make a method?
			final double WIDTH_PERCENT = .33;
			final double LIST_PANE_WIDTH = StageConstants.DEFAULT_SIZE.width * WIDTH_PERCENT;

			this.listPane = new VBox(StageConstants.PADDING);
			this.listPane.setPadding(StageConstants.PADDING_INSETS);
			this.listPane.setMaxWidth(LIST_PANE_WIDTH);
			this.listPane.setPrefWidth(this.listPane.getMaxWidth());
			this.listPane.getStyleClass().add("listPane");
			{//the display for the list of items
				HBox itemListMenu = new HBox();
				{//set the content of the item list menu
					final double ITEM_LIST_MENU_HEIGHT = 50;//TODO: make same height as Buttons that contain the Item displayNames?
					itemListMenu.setMinSize(LIST_PANE_WIDTH, ITEM_LIST_MENU_HEIGHT);
					itemListMenu.setMaxSize(LIST_PANE_WIDTH, ITEM_LIST_MENU_HEIGHT);
					itemListMenu.setPrefSize(LIST_PANE_WIDTH, ITEM_LIST_MENU_HEIGHT);
					itemListMenu.getStyleClass().add("itemListMenu");

					Label temp = new Label("Menu");//TODO
					itemListMenu.getChildren().addAll(temp);
				}
				AnchorPane itemListBorder = new AnchorPane();
				{
					itemListBorder.setMinWidth(LIST_PANE_WIDTH);
					itemListBorder.setMaxWidth(LIST_PANE_WIDTH);
					itemListBorder.setPrefWidth(LIST_PANE_WIDTH);
					itemListBorder.getStyleClass().add("itemListBorder");

					{//set the content of the list of items
						ScrollPane itemList = new ScrollPane();

						final boolean PANNABLE = false;
						itemList.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
						itemList.setPannable(PANNABLE);
						itemList.getStyleClass().add("itemList");

						itemList.setHmax(LIST_PANE_WIDTH);
						itemList.setMinWidth(LIST_PANE_WIDTH);
						itemList.setMaxWidth(LIST_PANE_WIDTH);
						itemList.setPrefWidth(LIST_PANE_WIDTH);
						itemList.setPrefViewportWidth(LIST_PANE_WIDTH);
						{
							VBox items = new VBox(StageConstants.PADDING);
							final int BUTTON_HEIGHT = 75;
							util.Pair<Integer> BUTTON_SIZE = new util.Pair<>(
									(int)(itemList.getPrefViewportWidth() - (4 * StageConstants.PADDING + util.Graphics.SCROLL_BAR_WIDTH)),
									BUTTON_HEIGHT
							);
							for(int i = 0; i < database.getItems().size(); i++){
								StackPane itemName = new StackPane();//TODO: remove if unneeded

								Button button = new Button(database.getItems().get(i).getDisplayName());
								button.setMinSize(BUTTON_SIZE.getFirst(),BUTTON_SIZE.getSecond());
								button.setMaxSize(BUTTON_SIZE.getFirst(),BUTTON_SIZE.getSecond());
								button.setPrefSize(BUTTON_SIZE.getFirst(),BUTTON_SIZE.getSecond());
								button.getStyleClass().add("itemName");
								final int ITEM_ACCESSOR = i;
								button.setOnAction(
										(ActionEvent event) ->
										{
											this.activeItemDisplay.set(database.getItems().get(ITEM_ACCESSOR));//TODO: get this working
											this.updateItemDisplay.setValue(!this.updateItemDisplay.getValue());
										}
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
				this.listPane.getChildren().addAll(itemListMenu,itemListBorder);//order matters
			}
		}
		{//TODO
			final double WIDTH_PERCENT = .67;
			final double INFO_PANE_WIDTH = StageConstants.DEFAULT_SIZE.width * WIDTH_PERCENT;
			{
				constructInfoPane(INFO_PANE_WIDTH);
				/*
				this.infoPane = new VBox();
				this.infoPane.setMaxWidth(INFO_PANE_WIDTH);
				this.infoPane.setPrefWidth(this.infoPane.getMaxWidth());
				this.infoPane.getStyleClass().add("infoPane");
				this.infoPane.setPadding(StageConstants.PADDING_INSETS);
				{
					StackPane itemDisplay = new StackPane();
					itemDisplay.setPadding(StageConstants.PADDING_INSETS);
					itemDisplay.getStyleClass().add("itemDisplay");

					final int BACKGROUND_HEIGHT = 500;
					Rectangle background = new Rectangle(INFO_PANE_WIDTH - (4 * StageConstants.PADDING),BACKGROUND_HEIGHT);
					background.getStyleClass().add("itemDisplayBackground");

					Text itemDisplayInfo = new Text();
					if(this.activeItemDisplay.isValid()){
						itemDisplayInfo.setText(this.activeItemDisplay.get().getDescription());
					} else {
						itemDisplayInfo.setText("");
					}
					itemDisplayInfo.getStyleClass().add("itemDisplayInfo");

					itemDisplay.getChildren().addAll(background,itemDisplayInfo);

					this.infoPane.getChildren().addAll(itemDisplay);
				}
				*/
			}
			{
				this.addItemPane = new VBox();
				this.addItemPane.setMaxWidth(StageConstants.DEFAULT_SIZE.width * WIDTH_PERCENT);
				this.addItemPane.setPrefWidth(this.addItemPane.getMaxWidth());
				this.addItemPane.getStyleClass().add("addItemPane");
			}
		}

		this.rootPane.getChildren().addAll(this.listPane,this.infoPane);//ordered from left to right
		this.setScene(new Scene(this.rootPane,StageConstants.DEFAULT_SIZE.width,StageConstants.DEFAULT_SIZE.height));
		this.getScene().getStylesheets().add(STYLESHEET_SOURCE);
	}
}
