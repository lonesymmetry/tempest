package main.java.desktopUI;

import javafx.collections.ListChangeListener;
import javafx.scene.control.Button;
import javafx.scene.control.ToggleButton;
import javafx.scene.Node;
import javafx.scene.control.ListView;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextArea;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import main.java.control.Analytics;
import main.java.control.Database;
import main.java.control.Item;
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.scene.Scene;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import main.java.util.Pair;
import main.java.util.Maybe;
import main.java.util.Util;

import java.awt.Dimension;
import java.util.ArrayList;

/**
 * Runs the desktop application that displays the Items and provides for their management
 *
 * @author Logan Traffas
 */
public class DesktopApplication extends Application{
	//padding constants
	private static final int EXPERIMENTAL_HORIZONTAL_INSETS = 0;//from testing with Windows 10
	private static final int EXPERIMENTAL_VERTICAL_INSETS = 31;//from testing with Windows 10
	private static final Dimension DEFAULT_SIZE = new Dimension(
			1366 - EXPERIMENTAL_HORIZONTAL_INSETS,
			768 - EXPERIMENTAL_VERTICAL_INSETS - main.java.util.Graphics.TASKBAR_HEIGHT
	);
	private static final int PADDING = 5;//px ?

	//style/usability constants
	private static final String STYLESHEET_SOURCE = "/main/res/DesktopApplicationStylesheet.css";
	private static final int SECTION_HEIGHT = 75;
	private static final boolean RESIZABLE = false;

	public enum RightDisplay{ITEM_INFO,ADD_NEW_ITEM}
	private RightDisplay rightDisplay;

	private Stage mainStage;

	private Maybe<Pair<Item,Integer>> activeItem;//first stores the Item, second stores its index in database
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
	 * Replaces a node with a given ID
	 * @param ID the ID of the node to replace
	 * @param newNode the new node
	 * @return true if it was successful
	 */
	private boolean replaceNode(String ID,Node newNode){
		if(this.rootPane.getChildren().size() == 0){
			this.rootPane.getChildren().add(newNode);
			return true;
		}
		for(int i = 0; i < this.rootPane.getChildren().size(); i++){
			Node child = this.rootPane.getChildren().get(i);
			if(child.getId().equals(ID)){
				this.rootPane.getChildren().remove(i);
				this.rootPane.getChildren().add(i,newNode);
				return true;
			}
		}
		return false;
	}

	/**
	 * Removes a node with a given ID
	 * @param ID the ID of the node to remove
	 */
	private void removeNode(String ID){
		ID = "#" + ID;
		Node toRemove = this.rootPane.lookup(ID);
		if(toRemove != null) {
			this.rootPane.getChildren().remove(toRemove);
		}
	}

	/**
	 * Builds the left side of the display
	 */
	private void updateLeftPane(){
		updateListPane();
		replaceNode(LIST_PANE_ID,this.listPane);
	}

	/**
	 * Searches for the currently active right display
	 * @return the active right display, null if no known right displays are in use
	 */
	private RightDisplay getActiveRightDisplay(){
		for(int i = 0; i < this.rootPane.getChildren().size(); i++){
			Node child = this.rootPane.getChildren().get(i);
			if(child.getId().equals(INFO_PANE_ID)){
				return RightDisplay.ITEM_INFO;
			}
			if(child.getId().equals(ADD_ITEM_PANE_ID)){
				return RightDisplay.ADD_NEW_ITEM;
			}
		}
		return null;
	}

	/**
	 * Builds the right side of the display, handling the fact that multiple different displays may appear there
	 */
	private void updateRightPane(){
		updateInfoPane();
		updateAddItemPane();
		switch(this.rightDisplay) {
			case ITEM_INFO:
				if(getActiveRightDisplay() == null){
					this.rootPane.getChildren().add(this.infoPane);
				} else{
					switch(getActiveRightDisplay()){
						case ITEM_INFO:
							replaceNode(INFO_PANE_ID, this.infoPane);
							break;
						case ADD_NEW_ITEM:
							replaceNode(ADD_ITEM_PANE_ID, this.infoPane);
							break;
						default:
							Util.nyi(main.java.util.Util.getFileName(), main.java.util.Util.getLineNumber());
					}
				}
				break;
			case ADD_NEW_ITEM:
				if(getActiveRightDisplay() == null){
					this.rootPane.getChildren().add(this.addItemPane);
				} else{
					switch(getActiveRightDisplay()){
						case ITEM_INFO:
							replaceNode(INFO_PANE_ID, this.addItemPane);
							break;
						case ADD_NEW_ITEM:
							replaceNode(ADD_ITEM_PANE_ID, this.addItemPane);
							break;
						default:
							Util.nyi(main.java.util.Util.getFileName(), main.java.util.Util.getLineNumber());
					}
				}
				break;
			default:
				Util.nyi(main.java.util.Util.getFileName(), main.java.util.Util.getLineNumber());
		}
	}

	/**
	 * Constructs the infoPane which is the detailed display for a selected Item
	 */
	private void updateInfoPane(){
		this.infoPane.getChildren().clear();
		final double WIDTH_PERCENT = .66;
		final double WIDTH = DEFAULT_SIZE.width * WIDTH_PERCENT;
		this.infoPane.setMaxWidth(WIDTH);
		this.infoPane.setPrefWidth(this.infoPane.getMaxWidth());
		this.infoPane.getStyleClass().add("infoPane");
		{
			StackPane itemDisplay = new StackPane();
			{
				itemDisplay.getStyleClass().add("itemDisplay");

				final int BACKGROUND_HEIGHT = 605;//from testing on 5/25/17
				final int BACKGROUND_WIDTH = (int) (WIDTH - (2 * PADDING));
				Rectangle background = new Rectangle(BACKGROUND_WIDTH, BACKGROUND_HEIGHT);
				background.getStyleClass().add("itemDisplayBackground");

				VBox itemDisplayInfoBorder = new VBox(PADDING);//used to align text in the upper left-hand corner with some padding
				{
					itemDisplayInfoBorder.getStyleClass().add("itemDisplayInfoBorder");
					if(this.activeItem.isValid()){
						final double WRAPPING_WIDTH = WIDTH - 4 * PADDING;
						Text itemDisplayName = new Text();
						{
							itemDisplayName.getStyleClass().add("itemDisplayName");
							itemDisplayName.setText(this.activeItem.get().getFirst().getDisplayName());
							itemDisplayName.setWrappingWidth(WRAPPING_WIDTH);
						}
						Text itemDisplayPriority = new Text();
						{
							itemDisplayPriority.getStyleClass().add("itemDisplayPriority");
							itemDisplayPriority.setText("Priority: " + this.activeItem.get().getFirst().getPriority().toString());
							itemDisplayPriority.setWrappingWidth(WRAPPING_WIDTH);
						}
						Text itemDisplayDescription= new Text();
						{
							itemDisplayDescription.getStyleClass().add("itemDisplayDescription");
							itemDisplayDescription.setText(this.activeItem.get().getFirst().getDescription());
							itemDisplayDescription.setWrappingWidth(WRAPPING_WIDTH);
						}
						Text itemDisplayDate = new Text();
						{
							itemDisplayDate.getStyleClass().add("itemDisplayDate");
							itemDisplayDate.setText("Created " + this.activeItem.get().getFirst().getDate().toString());
							itemDisplayDate.setWrappingWidth(WRAPPING_WIDTH);
						}
						itemDisplayInfoBorder.getChildren().addAll(itemDisplayName,itemDisplayPriority);
						if(!this.activeItem.get().getFirst().getDescription().equals("")){
							itemDisplayInfoBorder.getChildren().addAll(itemDisplayDescription);
						}
						itemDisplayInfoBorder.getChildren().addAll(itemDisplayDate);
					}
				}
				itemDisplay.getChildren().addAll(background,itemDisplayInfoBorder);
			}
			HBox editItemMenu = new HBox(PADDING);
			{//set the content of the item list menu
				editItemMenu.setMinSize(WIDTH, SECTION_HEIGHT);
				editItemMenu.setMaxSize(WIDTH, SECTION_HEIGHT);
				editItemMenu.setPrefSize(WIDTH, SECTION_HEIGHT);
				editItemMenu.getStyleClass().add("itemListMenu");

				final int NUMBER_OF_BUTTONS = 3;
				final int BUTTON_WIDTH = (int)((WIDTH - (NUMBER_OF_BUTTONS + 1) * PADDING) * (1.0 / NUMBER_OF_BUTTONS)),
						BUTTON_HEIGHT = SECTION_HEIGHT - 2 * PADDING;
				Button toggleFinished = new Button(this.activeItem.isValid() ? this.activeItem.get().getFirst().getStatus().toString() : "Toggle Finished");
				{
					final Pair<Integer,Integer> BUTTON_SIZE = new Pair<>(BUTTON_WIDTH,BUTTON_HEIGHT);
					toggleFinished.setMinSize(BUTTON_SIZE.getFirst(), BUTTON_SIZE.getSecond());
					toggleFinished.setMaxSize(BUTTON_SIZE.getFirst(), BUTTON_SIZE.getSecond());
					toggleFinished.setPrefSize(BUTTON_SIZE.getFirst(), BUTTON_SIZE.getSecond());
					toggleFinished.getStyleClass().add("toggleFinishedButton");
					toggleFinished.setOnAction(
							(ActionEvent event) ->
							{
								if(this.activeItem.isValid()){
									this.activeItem.get().getFirst().setStatus(Item.Status.not(this.activeItem.get().getFirst().getStatus()));
									this.database.editItem(this.activeItem.get().getSecond(),this.activeItem.get().getFirst());
									this.activeItem.set(new Pair<>(this.database.getItems().get(this.activeItem.get().getSecond()),this.activeItem.get().getSecond()));
									updateRightPane();
								}

							}
					);
				}
				Button editItem = new Button("Edit");
				{
					final Pair<Integer,Integer> BUTTON_SIZE = new Pair<>(BUTTON_WIDTH,BUTTON_HEIGHT);
					editItem.setMinSize(BUTTON_SIZE.getFirst(), BUTTON_SIZE.getSecond());
					editItem.setMaxSize(BUTTON_SIZE.getFirst(), BUTTON_SIZE.getSecond());
					editItem.setPrefSize(BUTTON_SIZE.getFirst(), BUTTON_SIZE.getSecond());
					editItem.getStyleClass().add("editItemButton");
					editItem.setOnAction(
							(ActionEvent event) ->
									Util.nyi(Util.getFileName(), Util.getLineNumber())//TODO must wait for ability to edit items
					);
				}
				Button deleteItem = new Button("Delete");
				{
					final Pair<Integer,Integer> BUTTON_SIZE = new Pair<>(BUTTON_WIDTH,BUTTON_HEIGHT);
					deleteItem.setMinSize(BUTTON_SIZE.getFirst(), BUTTON_SIZE.getSecond());
					deleteItem.setMaxSize(BUTTON_SIZE.getFirst(), BUTTON_SIZE.getSecond());
					deleteItem.setPrefSize(BUTTON_SIZE.getFirst(), BUTTON_SIZE.getSecond());
					deleteItem.getStyleClass().add("deleteItemButton");
					deleteItem.setOnAction(
							(ActionEvent event) ->
									Util.nyi(Util.getFileName(), Util.getLineNumber())//TODO: must wait for ability to edit Items
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
	private void updateListPane(){
		this.listPane.getChildren().clear();
		this.database.fillList();
		final double WIDTH_PERCENT = .33;
		final double WIDTH = DEFAULT_SIZE.width * WIDTH_PERCENT;

		this.listPane.setMaxWidth(WIDTH);
		this.listPane.setPrefWidth(this.listPane.getMaxWidth());
		this.listPane.getStyleClass().add("listPane");
		{//the display for the list of items
			HBox itemListMenu = new HBox(PADDING);
			{//set the content of the item list menu
				itemListMenu.setMinSize(WIDTH, SECTION_HEIGHT);
				itemListMenu.setMaxSize(WIDTH, SECTION_HEIGHT);
				itemListMenu.setPrefSize(WIDTH, SECTION_HEIGHT);
				itemListMenu.getStyleClass().add("itemListMenu");

				final int NUMBER_OF_BUTTONS = 3;
				final int BUTTON_WIDTH = (int)((WIDTH - (NUMBER_OF_BUTTONS + 1) * PADDING) * (1.0 / NUMBER_OF_BUTTONS)),
					BUTTON_HEIGHT = SECTION_HEIGHT - 2 * PADDING;
				final Pair<Integer,Integer> BUTTON_SIZE = new Pair<>(BUTTON_WIDTH,BUTTON_HEIGHT);
				Button addNew = new Button("Add New");
				{
					addNew.getStyleClass().add("addNewButton");

					addNew.setMinSize(BUTTON_SIZE.getFirst(), BUTTON_SIZE.getSecond());
					addNew.setMaxSize(BUTTON_SIZE.getFirst(), BUTTON_SIZE.getSecond());
					addNew.setPrefSize(BUTTON_SIZE.getFirst(), BUTTON_SIZE.getSecond());

					addNew.setOnAction(
							(ActionEvent event) ->
							{
								if(getActiveRightDisplay() != RightDisplay.ADD_NEW_ITEM){
									this.rightDisplay = RightDisplay.ADD_NEW_ITEM;
									updateRightPane();
								}
							}
					);
				}
				ComboBox<Analytics.SortMode> sortBy = new ComboBox<>();
				{
					sortBy.setPromptText("Sort By");
					sortBy.setItems(FXCollections.observableArrayList(Analytics.SortMode.values()));
					sortBy.getStyleClass().add("sortBy");
					//TODO: figure out text-cropping and alignment of "PRIORITY"

					sortBy.setMinSize(BUTTON_SIZE.getFirst(), BUTTON_SIZE.getSecond());
					sortBy.setMaxSize(BUTTON_SIZE.getFirst(), BUTTON_SIZE.getSecond());
					sortBy.setPrefSize(BUTTON_SIZE.getFirst(), BUTTON_SIZE.getSecond());

					sortBy.setOnAction(
							(ActionEvent event) ->
									Util.nyi(Util.getFileName(),Util.getLineNumber())//TODO: wait for ability to sort
					);
				}
				ComboBox<Analytics.FilterMode> filterBy = new ComboBox<>();
				{
					filterBy.setPromptText("Filter By");
					filterBy.setItems(FXCollections.observableArrayList(Analytics.FilterMode.values()));
					//TODO: change how filtering displays FilterMode names
					filterBy.getStyleClass().add("filterBy");

					filterBy.setMinSize(BUTTON_SIZE.getFirst(), BUTTON_SIZE.getSecond());
					filterBy.setMaxSize(BUTTON_SIZE.getFirst(), BUTTON_SIZE.getSecond());
					filterBy.setPrefSize(BUTTON_SIZE.getFirst(), BUTTON_SIZE.getSecond());

					filterBy.setOnAction(
							(ActionEvent event) ->
									Util.nyi(Util.getFileName(),Util.getLineNumber())//TODO: wait for ability to filter
					);
				}
				itemListMenu.getChildren().addAll(addNew,sortBy,filterBy);
			}
			AnchorPane itemListBorder = new AnchorPane();
			{
				itemListBorder.setMinWidth(WIDTH);
				itemListBorder.setMaxWidth(WIDTH);
				itemListBorder.setPrefWidth(WIDTH);
				final int ITEM_LIST_BORDER_HEIGHT = 1000;//this is just set to something really big, it's cropped down
				itemListBorder.setPrefHeight(ITEM_LIST_BORDER_HEIGHT);
				itemListBorder.getStyleClass().add("itemListBorder");

				{
					ListView<String> itemList = new ListView<>();
					itemList.getStyleClass().add("itemList");

					itemList.setMinWidth(WIDTH);
					itemList.setMaxWidth(WIDTH);
					itemList.setPrefWidth(WIDTH);
					//itemList.setFixedCellSize(SECTION_HEIGHT);//used to set cell height

					ArrayList<String> itemNames = new ArrayList<>();
					for(Item item: this.database.getItems()){
						itemNames.add(item.shortenName());
					}
					itemList.setItems(FXCollections.observableArrayList(itemNames));
					if(this.activeItem.isValid()){
						itemList.scrollTo(this.activeItem.get().getSecond());
						itemList.getFocusModel().focus(this.activeItem.get().getSecond());//note: focused object is the one object in the entire operating system that receives keyboard input
						itemList.getSelectionModel().select(this.activeItem.get().getSecond());//note: selected object means it is marked
					}

					itemList.getSelectionModel().getSelectedIndices().addListener(
						(ListChangeListener.Change<? extends Integer> c) ->
						{
							this.rightDisplay = RightDisplay.ITEM_INFO;
							this.activeItem.set(new Pair<>(this.database.getItems().get(itemList.getSelectionModel().getSelectedIndex()),itemList.getSelectionModel().getSelectedIndex()));
							updateRightPane();
						}
					);

					{
						Rectangle itemListPlaceHolder = new Rectangle(WIDTH - 4 * PADDING,600);
						itemListPlaceHolder.getStyleClass().add("itemListPlaceHolder");
						itemList.setPlaceholder(itemListPlaceHolder);
					}

					itemListBorder.getChildren().add(itemList);
					AnchorPane.setBottomAnchor(itemList,(double) PADDING);
					AnchorPane.setTopAnchor(itemList,(double) PADDING);
					AnchorPane.setRightAnchor(itemList,(double) PADDING);
					AnchorPane.setLeftAnchor(itemList,(double) PADDING);
				}
			}
			this.listPane.getChildren().addAll(itemListMenu, itemListBorder);//order matters
		}
	}

	/**
	 * Constructs a pane which provides for the ability of users to add new Items to the Database
	 */
	private void updateAddItemPane(){
		this.addItemPane.getChildren().clear();
		final double WIDTH_PERCENT = .66;
		final double WIDTH = DEFAULT_SIZE.width * WIDTH_PERCENT;

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

			final int BACKGROUND_WIDTH = (int)(WIDTH - 2 * PADDING), BACKGROUND_HEIGHT = 605;
			Rectangle background = new Rectangle(BACKGROUND_WIDTH,BACKGROUND_HEIGHT);
			background.getStyleClass().add("addItemFieldsBackground");

			VBox addItemFields = new VBox(PADDING);
			{
				final int TEXT_INPUT_WIDTH = (int)(WIDTH - 4 * PADDING);
				addItemFields.getStyleClass().add("addItemFields");
				{
					setName.getStyleClass().add("setName");
					setName.setPromptText("Add item name");
					setName.setMinWidth(TEXT_INPUT_WIDTH);
					setName.setMaxWidth(TEXT_INPUT_WIDTH);
					setName.setPrefWidth(TEXT_INPUT_WIDTH);
				}
				HBox setPriority = new HBox(PADDING);
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
		HBox addItemMenu = new HBox(PADDING);
		{
			final int NUMBER_OF_BUTTONS = 2;
			final int BUTTON_WIDTH = (int)((WIDTH - (NUMBER_OF_BUTTONS + 1) * PADDING) * (1.0 / NUMBER_OF_BUTTONS)),
					BUTTON_HEIGHT = SECTION_HEIGHT - 2 * PADDING;
			final Pair<Integer,Integer> BUTTON_SIZE = new Pair<>(BUTTON_WIDTH,BUTTON_HEIGHT);
			addItemMenu.getStyleClass().add("addItemMenu");
			Button saveNewItemButton = new Button("Save");
			{
				saveNewItemButton.setMinSize(BUTTON_SIZE.getFirst(), BUTTON_SIZE.getSecond());
				saveNewItemButton.setMaxSize(BUTTON_SIZE.getFirst(), BUTTON_SIZE.getSecond());
				saveNewItemButton.setPrefSize(BUTTON_SIZE.getFirst(), BUTTON_SIZE.getSecond());
				saveNewItemButton.getStyleClass().add("saveNewItemButton");
				saveNewItemButton.setOnAction(
						(ActionEvent event) ->
						{
							String itemName = setName.getText(), itemDescription = setDescription.getText();
							Item.Priority itemPriority = prioritySelector.getValue();
							if(!itemName.equals("")){
								this.database.writeItem(new Item(itemName, itemDescription, itemPriority));
								updateLeftPane();
								this.rightDisplay = RightDisplay.ITEM_INFO;
								updateRightPane();
							}
						}
				);
			}
			Button cancelItemAddition = new Button("Cancel");
			{
				cancelItemAddition.setMinSize(BUTTON_SIZE.getFirst(), BUTTON_SIZE.getSecond());
				cancelItemAddition.setMaxSize(BUTTON_SIZE.getFirst(), BUTTON_SIZE.getSecond());
				cancelItemAddition.setPrefSize(BUTTON_SIZE.getFirst(), BUTTON_SIZE.getSecond());
				cancelItemAddition.getStyleClass().add("cancelItemAddition");
				cancelItemAddition.setOnAction(
						(ActionEvent event) ->
						{
							this.rightDisplay = RightDisplay.ITEM_INFO;
							updateRightPane();
						}
				);
			}
			addItemMenu.getChildren().addAll(saveNewItemButton,cancelItemAddition);
		}
		this.addItemPane.getChildren().addAll(addItemFieldsBorder,addItemMenu);
	}

	/**
	 * Constructs the primary pane of the application which contains the listPane and either the infoPane or the addItemPane
	 */
	private void updateRootPane(){
		this.rootPane.getChildren().clear();
		this.rootPane.setMaxSize(DEFAULT_SIZE.width, DEFAULT_SIZE.height);
		this.rootPane.getStyleClass().add("rootPane");

		updateLeftPane();
		updateRightPane();
	}

	/**
	 * The main entry point for the JavaFX application which runs all of the graphics
	 * @param primaryStage the primary stage for the application where the application scene is set
	 */
	@Override
	public void start(Stage primaryStage) throws Exception {
		initialize();
		this.mainStage = primaryStage;

		updateListPane();
		updateInfoPane();
		updateAddItemPane();
		updateRootPane();

		this.mainStage.setTitle("Tempest");
		this.mainStage.setResizable(RESIZABLE);
		this.mainStage.setScene(new Scene(this.rootPane, DEFAULT_SIZE.width, DEFAULT_SIZE.height));
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
		this.activeItem = (this.database.getItems().size() > 0) ? new Maybe<>(new Pair<>(this.database.getItems().get(0),0)) : new Maybe<>();

		this.rootPane = new HBox();
		this.rootPane.setId(ROOT_PANE_ID);

		this.listPane = new VBox(PADDING);
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
