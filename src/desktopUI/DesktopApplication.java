package desktopUI;

import control.Database;
import control.Item;
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextArea;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import util.Pair;
import util.Util;
import util.Maybe;

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
	private Maybe<Item> activeItem;
	private RightDisplay rightDisplay;
	private Database database;
	private HBox rootPane;
	private static final String ROOT_PANE_ID = "rootPane";
	private VBox listPane;
	private static final String LIST_PANE_ID = "listPane";
	private VBox infoPane;
	private static final String INFO_PANE_ID = "infoPane";
	private VBox addItemPane;
	private static final String ADD_ITEM_PANE_ID = "addItemPane";

	/**
	 * Constructs the infoPane which is the detailed display for a selected Item
	 */
	private void constructInfoPane(){
		this.infoPane.getChildren().clear();
		this.addItemPane.getChildren().clear();
		final double WIDTH_PERCENT = .66;
		final double WIDTH = StageConstants.DEFAULT_SIZE.width * WIDTH_PERCENT;
		this.infoPane.setMaxWidth(WIDTH);
		this.infoPane.setPrefWidth(this.infoPane.getMaxWidth());
		this.infoPane.getStyleClass().add("infoPane");
		{
			StackPane itemDisplay = new StackPane();
			{
				itemDisplay.getStyleClass().add("itemDisplay");

				final int BACKGROUND_HEIGHT = 605;//from testing on 5/25/17
				final int BACKGROUND_WIDTH = (int) (WIDTH - (2 * StageConstants.PADDING));
				Rectangle background = new Rectangle(BACKGROUND_WIDTH, BACKGROUND_HEIGHT);
				background.getStyleClass().add("itemDisplayBackground");

				StackPane itemDisplayInfoBorder = new StackPane();//used to align text in the upper left-hand corner with some padding
				{
					itemDisplayInfoBorder.getStyleClass().add("itemDisplayInfoBorder");

					Text itemDisplayInfo = new Text();
					{
						itemDisplayInfo.getStyleClass().add("itemDisplayInfo");
						if(this.activeItem.isValid()){
							itemDisplayInfo.setText(this.activeItem.get().getDescription());
						} else{
							itemDisplayInfo.setText("");
						}
					}
					itemDisplayInfoBorder.getChildren().addAll(itemDisplayInfo);
				}
				itemDisplay.getChildren().addAll(background,itemDisplayInfoBorder);
			}
			HBox editItemMenu = new HBox(StageConstants.PADDING);
			{//set the content of the item list menu
				editItemMenu.setMinSize(WIDTH, SECTION_HEIGHT);
				editItemMenu.setMaxSize(WIDTH, SECTION_HEIGHT);
				editItemMenu.setPrefSize(WIDTH, SECTION_HEIGHT);
				editItemMenu.getStyleClass().add("itemListMenu");

				final int NUMBER_OF_BUTTONS = 3;
				final int BUTTON_WIDTH = (int)((WIDTH - (NUMBER_OF_BUTTONS + 1) * StageConstants.PADDING) * (1.0 / NUMBER_OF_BUTTONS)),
						BUTTON_HEIGHT = SECTION_HEIGHT - 2 * StageConstants.PADDING;
				ToggleButton toggleFinished = new ToggleButton("Toggle Finished");
				{
					final Pair<Integer> BUTTON_SIZE = new Pair<>(BUTTON_WIDTH,BUTTON_HEIGHT);
					toggleFinished.setMinSize(BUTTON_SIZE.getFirst(), BUTTON_SIZE.getSecond());
					toggleFinished.setMaxSize(BUTTON_SIZE.getFirst(), BUTTON_SIZE.getSecond());
					toggleFinished.setPrefSize(BUTTON_SIZE.getFirst(), BUTTON_SIZE.getSecond());
					toggleFinished.getStyleClass().add("toggleFinishedButton");
					toggleFinished.setOnAction(
							(ActionEvent event) ->
									Util.nyi(Util.getFileName(), Util.getLineNumber())//TODO
					);
				}
				Button editItem = new Button("Edit");
				{
					final Pair<Integer> BUTTON_SIZE = new Pair<>(BUTTON_WIDTH,BUTTON_HEIGHT);
					editItem.setMinSize(BUTTON_SIZE.getFirst(), BUTTON_SIZE.getSecond());
					editItem.setMaxSize(BUTTON_SIZE.getFirst(), BUTTON_SIZE.getSecond());
					editItem.setPrefSize(BUTTON_SIZE.getFirst(), BUTTON_SIZE.getSecond());
					editItem.getStyleClass().add("editItemButton");
					editItem.setOnAction(
							(ActionEvent event) ->
									Util.nyi(Util.getFileName(), Util.getLineNumber())//TODO
					);
				}
				Button deleteItem = new Button("Delete");
				{
					final Pair<Integer> BUTTON_SIZE = new Pair<>(BUTTON_WIDTH,BUTTON_HEIGHT);
					deleteItem.setMinSize(BUTTON_SIZE.getFirst(), BUTTON_SIZE.getSecond());
					deleteItem.setMaxSize(BUTTON_SIZE.getFirst(), BUTTON_SIZE.getSecond());
					deleteItem.setPrefSize(BUTTON_SIZE.getFirst(), BUTTON_SIZE.getSecond());
					deleteItem.getStyleClass().add("deleteItemButton");
					deleteItem.setOnAction(
							(ActionEvent event) ->
									Util.nyi(Util.getFileName(), Util.getLineNumber())//TODO
					);
				}
				editItemMenu.getChildren().addAll(toggleFinished,editItem,deleteItem);
			}
			this.infoPane.getChildren().addAll(itemDisplay,editItemMenu);
		}
	}

	/**
	 * Constructs the listPane which is the list of Items which the user can select to view more details
	 */
	private void constructListPane(){
		this.listPane.getChildren().clear();
		this.database.fillList();
		final double WIDTH_PERCENT = .33;
		final double WIDTH = StageConstants.DEFAULT_SIZE.width * WIDTH_PERCENT;

		this.listPane.setMaxWidth(WIDTH);
		this.listPane.setPrefWidth(this.listPane.getMaxWidth());
		this.listPane.getStyleClass().add("listPane");
		{//the display for the list of items
			HBox itemListMenu = new HBox(StageConstants.PADDING);
			{//set the content of the item list menu
				itemListMenu.setMinSize(WIDTH, SECTION_HEIGHT);
				itemListMenu.setMaxSize(WIDTH, SECTION_HEIGHT);
				itemListMenu.setPrefSize(WIDTH, SECTION_HEIGHT);
				itemListMenu.getStyleClass().add("itemListMenu");

				final int NUMBER_OF_BUTTONS = 3;
				final int BUTTON_WIDTH = (int)((WIDTH - (NUMBER_OF_BUTTONS + 1) * StageConstants.PADDING) * (1.0 / NUMBER_OF_BUTTONS)),
					BUTTON_HEIGHT = SECTION_HEIGHT - 2 * StageConstants.PADDING;
				final Pair<Integer> BUTTON_SIZE = new Pair<>(BUTTON_WIDTH,BUTTON_HEIGHT);
				Button addNew = new Button("Add New");
				{
					addNew.getStyleClass().add("addNewButton");

					addNew.setMinSize(BUTTON_SIZE.getFirst(), BUTTON_SIZE.getSecond());
					addNew.setMaxSize(BUTTON_SIZE.getFirst(), BUTTON_SIZE.getSecond());
					addNew.setPrefSize(BUTTON_SIZE.getFirst(), BUTTON_SIZE.getSecond());

					addNew.setOnAction(
							(ActionEvent event) ->
							{
								this.rightDisplay = RightDisplay.ADD_NEW_ITEM;
								constructRootPane();
							}
					);
				}
				Button sortBy = new Button("Sort By");//TODO: change out with ComboBox
				{
					sortBy.getStyleClass().add("sortBy");

					sortBy.setMinSize(BUTTON_SIZE.getFirst(), BUTTON_SIZE.getSecond());
					sortBy.setMaxSize(BUTTON_SIZE.getFirst(), BUTTON_SIZE.getSecond());
					sortBy.setPrefSize(BUTTON_SIZE.getFirst(), BUTTON_SIZE.getSecond());

					sortBy.setOnAction(
							(ActionEvent event) ->
									Util.nyi(Util.getFileName(),Util.getLineNumber())
					);
				}
				Button filter = new Button("Filter By");//TODO: change out with ComboBox
				{
					filter.getStyleClass().add("filter");

					filter.setMinSize(BUTTON_SIZE.getFirst(), BUTTON_SIZE.getSecond());
					filter.setMaxSize(BUTTON_SIZE.getFirst(), BUTTON_SIZE.getSecond());
					filter.setPrefSize(BUTTON_SIZE.getFirst(), BUTTON_SIZE.getSecond());

					filter.setOnAction(
							(ActionEvent event) ->
									Util.nyi(Util.getFileName(),Util.getLineNumber())//TODO
					);
				}
				itemListMenu.getChildren().addAll(addNew,sortBy,filter);
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
										this.activeItem.set(item);
										constructRootPane();
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
	private void constructAddItemPane(){
		this.addItemPane.getChildren().clear();
		this.infoPane.getChildren().clear();
		final double WIDTH_PERCENT = .66;
		final double WIDTH = StageConstants.DEFAULT_SIZE.width * WIDTH_PERCENT;

		this.addItemPane.setMaxWidth(WIDTH);
		this.addItemPane.setPrefWidth(this.addItemPane.getMaxWidth());
		this.addItemPane.getStyleClass().add("addItemPane");

		StackPane addItemFieldsBorder = new StackPane();

		//these three inputs are listed in this scope so that the "Add Item" button can access their values to write to the database
		TextField setName = new TextField();
		ComboBox<Item.Priority> prioritySelector = new ComboBox<>();
		TextArea setDescription = new TextArea();
		{
			addItemFieldsBorder.getStyleClass().add("addItemFieldsBorder");

			final int BACKGROUND_WIDTH = (int)(WIDTH - 2 * StageConstants.PADDING), BACKGROUND_HEIGHT = 605;
			Rectangle background = new Rectangle(BACKGROUND_WIDTH,BACKGROUND_HEIGHT);
			background.getStyleClass().add("addItemFieldsBackground");

			VBox addItemFields = new VBox(StageConstants.PADDING);
			{
				final int TEXT_INPUT_WIDTH = (int)(WIDTH - 4 * StageConstants.PADDING);
				addItemFields.getStyleClass().add("addItemFields");
				{
					setName.getStyleClass().add("setName");
					setName.setPromptText("Add item name");
					setName.setPrefColumnCount(Item.MAX_DISPLAY_NAME_LENGTH);
					setName.setMinWidth(TEXT_INPUT_WIDTH);
					setName.setMaxWidth(TEXT_INPUT_WIDTH);
					setName.setPrefWidth(TEXT_INPUT_WIDTH);
				}
				HBox setPriority = new HBox(StageConstants.PADDING);
				{
					setPriority.getStyleClass().add("setPriority");

					Label priorityLabel = new Label("Priority:");
					{
						priorityLabel.getStyleClass().add("priorityLabel");
					}
					{
						prioritySelector.getStyleClass().add("prioritySelector");
						prioritySelector.setPromptText("Priority...");
						prioritySelector.setValue(Item.Priority.LOW);
						prioritySelector.setItems(FXCollections.observableArrayList(
								Item.Priority.LOW, Item.Priority.MEDIUM, Item.Priority.HIGH
						));
					}
					setPriority.getChildren().addAll(priorityLabel,prioritySelector);
				}
				{
					final int SET_DESCRIPTION_HEIGHT = 510;//from testing on 5/25/2017
					setDescription.getStyleClass().add("setDescription");
					setDescription.setPromptText("Add item description");
					setDescription.setMinSize(TEXT_INPUT_WIDTH,SET_DESCRIPTION_HEIGHT);
					setDescription.setMaxSize(TEXT_INPUT_WIDTH,SET_DESCRIPTION_HEIGHT);
					setDescription.setPrefSize(TEXT_INPUT_WIDTH,SET_DESCRIPTION_HEIGHT);
				}
				addItemFields.getChildren().addAll(setName, setPriority, setDescription);
			}
			addItemFieldsBorder.getChildren().addAll(background,addItemFields);
		}
		HBox addItemMenu = new HBox(StageConstants.PADDING);
		{
			final int NUMBER_OF_BUTTONS = 2;
			final int BUTTON_WIDTH = (int)((WIDTH - (NUMBER_OF_BUTTONS + 1) * StageConstants.PADDING) * (1.0 / NUMBER_OF_BUTTONS)),
					BUTTON_HEIGHT = SECTION_HEIGHT - 2 * StageConstants.PADDING;
			addItemMenu.getStyleClass().add("addItemMenu");
			Button addItemButton = new Button("Add Item");
			{
				final Pair<Integer> BUTTON_SIZE = new Pair<>(BUTTON_WIDTH,BUTTON_HEIGHT);
				addItemButton.setMinSize(BUTTON_SIZE.getFirst(), BUTTON_SIZE.getSecond());
				addItemButton.setMaxSize(BUTTON_SIZE.getFirst(), BUTTON_SIZE.getSecond());
				addItemButton.setPrefSize(BUTTON_SIZE.getFirst(), BUTTON_SIZE.getSecond());
				addItemButton.getStyleClass().add("addItemButton");
				addItemButton.setOnAction(
						(ActionEvent event) ->
						{
							this.database.writeItem(new Item(setName.getText(), setDescription.getText(), prioritySelector.getValue()));
							this.constructRootPane();
						}
				);
			}
			Button cancelItemAddition = new Button("Cancel");
			{
				final Pair<Integer> BUTTON_SIZE = new Pair<>(BUTTON_WIDTH,BUTTON_HEIGHT);
				cancelItemAddition.setMinSize(BUTTON_SIZE.getFirst(), BUTTON_SIZE.getSecond());
				cancelItemAddition.setMaxSize(BUTTON_SIZE.getFirst(), BUTTON_SIZE.getSecond());
				cancelItemAddition.setPrefSize(BUTTON_SIZE.getFirst(), BUTTON_SIZE.getSecond());
				cancelItemAddition.getStyleClass().add("cancelItemAddition");
				cancelItemAddition.setOnAction(
						(ActionEvent event) ->
						{
							this.rightDisplay = RightDisplay.ITEM_INFO;
							constructRootPane();
						}
				);
			}
			addItemMenu.getChildren().addAll(addItemButton,cancelItemAddition);
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
				constructInfoPane();
				this.rootPane.getChildren().add(this.infoPane);
				break;
			case ADD_NEW_ITEM:
				constructAddItemPane();
				this.rootPane.getChildren().add(this.addItemPane);
				break;
			default:
				Util.nyi(util.Util.getFileName(),util.Util.getLineNumber());
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
		this.database.fillList();

		this.rightDisplay = RightDisplay.ITEM_INFO;
		this.activeItem = new Maybe<>();

		this.rootPane = new HBox();
		this.rootPane.setId(ROOT_PANE_ID);

		this.listPane = new VBox(StageConstants.PADDING);
		this.listPane.setId(LIST_PANE_ID);

		this.infoPane = new VBox();
		this.infoPane.setId(INFO_PANE_ID);

		this.addItemPane = new VBox();
		this.addItemPane.setId(ADD_ITEM_PANE_ID);
	}

	/**
	 * Used to launch the application with arguments
	 * @param args the arguments to launch the application which
	 */
	public static void main(String[] args){
		launch(args);
	}
}
