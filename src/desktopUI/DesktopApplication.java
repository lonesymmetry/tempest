package desktopUI;

import control.Database;
import control.Item;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import util.Util;

/**
 * Runs the desktop application that displays the Items and provides for their management
 *
 * @author Logan Traffas
 */
public class DesktopApplication extends Application{
	private Stage mainStage;
	private static final String STYLESHEET_SOURCE = "/res/DesktopApplicationStylesheet.css";
	private static final int SECTION_HEIGHT = 75;
	private static final boolean RESIZABLE = false;
	public enum RightDisplay{ITEM_INFO,ADD_NEW_ITEM}
	private RightDisplay rightDisplay;
	private Database database;
	private HBox rootPane;
	private VBox listPane;
	private VBox infoPane;
	private VBox addItemPane;

	/**
	 * Constructs the infoPane which is the detailed display for a selected Item
	 * @param activeItem
	 */
	private void constructInfoPane(util.Maybe<Item> activeItem){
		this.infoPane.getChildren().clear();
		this.addItemPane.getChildren().clear();
		final double WIDTH_PERCENT = .665;//extra 0.05% to make room for padding on the right side
		final double WIDTH = StageConstants.DEFAULT_SIZE.width * WIDTH_PERCENT;
		this.infoPane.setMaxWidth(WIDTH);
		this.infoPane.setPrefWidth(this.infoPane.getMaxWidth());
		this.infoPane.getStyleClass().add("infoPane");
		{
			StackPane itemDisplay = new StackPane();
			{
				itemDisplay.setPadding(StageConstants.PADDING_INSETS);
				itemDisplay.getStyleClass().add("itemDisplay");

				final int BACKGROUND_HEIGHT = 500;
				final int BACKGROUND_WIDTH = (int) (WIDTH - (2 * StageConstants.PADDING));
				Rectangle background = new Rectangle(BACKGROUND_WIDTH, BACKGROUND_HEIGHT);
				background.getStyleClass().add("itemDisplayBackground");

				StackPane itemDisplayInfoBorder = new StackPane();//used to align text in the upper left-hand corner with some padding
				{//TODO
					itemDisplayInfoBorder.getStyleClass().add("itemDisplayInfoBorder");
					Text itemDisplayInfo = new Text();
					if (activeItem.isValid()) {
						itemDisplayInfo.setText(activeItem.get().getDescription());
					} else {
						itemDisplayInfo.setText("");
					}
					itemDisplayInfo.getStyleClass().add("itemDisplayInfo");

					itemDisplayInfoBorder.getChildren().addAll(itemDisplayInfo);
					StackPane.setAlignment(itemDisplayInfo, Pos.TOP_LEFT);
				}
				itemDisplay.getChildren().addAll(background, itemDisplayInfoBorder);
			}
			HBox editItemMenu = new HBox();
			{
				{//set the content of the item list menu
					editItemMenu.setMinSize(WIDTH, SECTION_HEIGHT);
					editItemMenu.setMaxSize(WIDTH, SECTION_HEIGHT);
					editItemMenu.setPrefSize(WIDTH, SECTION_HEIGHT);
					editItemMenu.getStyleClass().add("itemListMenu");

					ToggleButton toggleFinished = new ToggleButton("Toggle Finished");
					{
						toggleFinished.getStyleClass().add("toggleFinishedButton");
						toggleFinished.setOnAction(
								(ActionEvent event) ->
										Util.nyi(Util.getFileName(), Util.getLineNumber())
						);
					}
					Button editItem = new Button("Edit");
					{
						editItem.getStyleClass().add("editItemButton");
						editItem.setOnAction(
								(ActionEvent event) ->
										Util.nyi(Util.getFileName(), Util.getLineNumber())
						);
					}
					Button deleteItem = new Button("Delete");
					{

						deleteItem.getStyleClass().add("deleteItemButton");
						deleteItem.setOnAction(
								(ActionEvent event) ->
										Util.nyi(Util.getFileName(), Util.getLineNumber())
						);
					}
					editItemMenu.getChildren().addAll(toggleFinished,editItem,deleteItem);
				}
			}
			this.infoPane.getChildren().addAll(itemDisplay,editItemMenu);
		}
	}

	/**
	 * Constructs the listPane which is the list of Items which the user can select to view more details
	 */
	private void constructListPane(){
		this.listPane.getChildren().clear();
		final double WIDTH_PERCENT = .33;
		final double WIDTH = StageConstants.DEFAULT_SIZE.width * WIDTH_PERCENT;

		this.listPane.setMaxWidth(WIDTH);
		this.listPane.setPrefWidth(this.listPane.getMaxWidth());
		this.listPane.getStyleClass().add("listPane");
		{//the display for the list of items
			HBox itemListMenu = new HBox();
			{//set the content of the item list menu
				itemListMenu.setMinSize(WIDTH, SECTION_HEIGHT);
				itemListMenu.setMaxSize(WIDTH, SECTION_HEIGHT);
				itemListMenu.setPrefSize(WIDTH, SECTION_HEIGHT);
				itemListMenu.getStyleClass().add("itemListMenu");

				Button addNew = new Button();
				addNew.getStyleClass().add("addNewButton");
				addNew.setOnAction(
						(ActionEvent event) ->
						{
							this.rightDisplay = RightDisplay.ADD_NEW_ITEM;
							constructRootPane();
							System.out.println("Pressed");
						}
				);

				itemListMenu.getChildren().addAll(addNew);
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
						util.Pair<Integer> BUTTON_SIZE = new util.Pair<>(
								(int)(itemList.getPrefViewportWidth() - (4 * StageConstants.PADDING + util.Graphics.SCROLL_BAR_WIDTH)),
								SECTION_HEIGHT
						);
						for(Item item : database.getItems()){
							Button button = new Button(item.getDisplayName());
							button.setMinSize(BUTTON_SIZE.getFirst(), BUTTON_SIZE.getSecond());
							button.setMaxSize(BUTTON_SIZE.getFirst(), BUTTON_SIZE.getSecond());
							button.setPrefSize(BUTTON_SIZE.getFirst(), BUTTON_SIZE.getSecond());
							button.getStyleClass().add("itemName");
							button.setOnAction(
									(ActionEvent event) ->
									{
										this.rightDisplay = RightDisplay.ITEM_INFO;
										constructRootPane();
										constructInfoPane(new util.Maybe<>(item));
									}
							);
							items.getChildren().add(button);
						}
						itemList.setContent(items);
					}
					itemListBorder.getChildren().add(itemList);
					AnchorPane.setBottomAnchor(itemList,(double)StageConstants.PADDING);
					AnchorPane.setTopAnchor(itemList,(double)StageConstants.PADDING);
					AnchorPane.setRightAnchor(itemList,(double)StageConstants.PADDING);
					AnchorPane.setLeftAnchor(itemList,(double)StageConstants.PADDING);
				}
			}
			this.listPane.getChildren().addAll(itemListMenu, itemListBorder);//order matters
		}
	}

	/**
	 * Constructs a pane which provides for the ability of users to add new Items to the Database
	 */
	private void constructAddItemPane(){//TODO
		this.addItemPane.getChildren().clear();
		this.infoPane.getChildren().clear();
		final double WIDTH_PERCENT = .665;//extra 0.05% to make room for padding on the right side
		final double WIDTH = StageConstants.DEFAULT_SIZE.width * WIDTH_PERCENT;

		this.addItemPane.setMaxWidth(WIDTH);
		this.addItemPane.setPrefWidth(this.addItemPane.getMaxWidth());
		this.addItemPane.getStyleClass().add("addItemPane");
		StackPane addItemFieldsBorder = new StackPane();
		{
			addItemFieldsBorder.setPadding(StageConstants.PADDING_INSETS);
			addItemFieldsBorder.getStyleClass().add("addItemFieldsBorder");

			final int BACKGROUND_WIDTH = (int)(WIDTH - 2 * StageConstants.PADDING), BACKGROUND_HEIGHT = 500;
			Rectangle background = new Rectangle(BACKGROUND_WIDTH,BACKGROUND_HEIGHT);
			background.getStyleClass().add("addItemFieldsBackground");

			VBox addItemFields = new VBox();
			{
				final int TEXTFIELD_WIDTH = (int)(WIDTH - 4 * StageConstants.PADDING);
				addItemFields.getStyleClass().add("itemFields");

				TextField setName = new TextField();
				{
					setName.getStyleClass().add("addName");
					setName.setPromptText("Name...");
					setName.setPrefColumnCount(Item.MAX_DISPLAY_NAME_LENGTH);
					setName.setMinWidth(TEXTFIELD_WIDTH);
					setName.setMaxWidth(TEXTFIELD_WIDTH);
					setName.setPrefWidth(TEXTFIELD_WIDTH);
				}
				ComboBox setPriority = new ComboBox();
				{
					setPriority.getStyleClass().add("setPriority");
					setPriority.setPromptText("Priority...");
				}
				TextField setDescription = new TextField();
				{
					final int SET_DESCRIPTION_HEIGHT = 100;
					setDescription.getStyleClass().add("setDescription");
					setDescription.setPromptText("Description...");
					setDescription.setMinSize(TEXTFIELD_WIDTH,SET_DESCRIPTION_HEIGHT);
					setDescription.setMaxSize(TEXTFIELD_WIDTH,SET_DESCRIPTION_HEIGHT);
					setDescription.setPrefSize(TEXTFIELD_WIDTH,SET_DESCRIPTION_HEIGHT);
				}
				addItemFields.getChildren().addAll(setName, setPriority, setDescription);
			}
			addItemFieldsBorder.getChildren().addAll(background,addItemFields);
		}
		HBox addItemMenu = new HBox();
		{
			addItemMenu.getStyleClass().add("addItemMenu");
			{

			}
			{

			}
			{

			}
		}
		this.addItemPane.getChildren().addAll(addItemFieldsBorder,addItemMenu);
	}

	/**
	 * Constructs the primary pane of the application which contains the listPane and either the infoPane or the addItemPane
	 */
	private void constructRootPane(){
		this.rootPane.getChildren().clear();
		this.rootPane.setMaxSize(StageConstants.DEFAULT_SIZE.width,StageConstants.DEFAULT_SIZE.height);
		this.rootPane.getStyleClass().add("rootPane");

		constructListPane();
		this.rootPane.getChildren().add(this.listPane);

		switch(this.rightDisplay) {
			case ITEM_INFO:
				constructInfoPane(new util.Maybe<>());
				this.rootPane.getChildren().add(this.infoPane);
				break;
			case ADD_NEW_ITEM:
				constructAddItemPane();
				this.rootPane.getChildren().add(this.addItemPane);
				break;
			default:
				util.Util.nyi(util.Util.getFileName(),util.Util.getLineNumber());
		}


	}

	/**
	 * The main entry point for the JavaFX application which runs all of the graphics
	 * @param primaryStage the primary stage for the application where the application scene is set
	 */
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

	/**
	 * Acts as the constructor for this class
	 */
	private void initialize(){
		{
			//TODO: for testing only
			//Database.testWrite();
		}
		this.mainStage = new Stage();
		this.database = new Database();
		this.rightDisplay = RightDisplay.ITEM_INFO;
		this.database.fillList();
		this.rootPane = new HBox();
		this.listPane = new VBox(StageConstants.PADDING);
		this.infoPane = new VBox();
		this.addItemPane = new VBox();
	}

	/**
	 * Used to launch the application with arguments
	 * @param args the arguments to launch the application which
	 */
	public static void main(String[] args){
		launch(args);
	}
}
