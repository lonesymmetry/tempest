package main.java.desktopUI;

import javafx.scene.control.*;
import javafx.scene.*;
import javafx.scene.image.Image;
import javafx.scene.layout.*;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.event.ActionEvent;
import javafx.stage.Stage;

import main.java.util.*;
import main.java.control.*;

import java.awt.Dimension;

import java.util.ArrayList;
import java.util.Optional;

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
	private static final String[] ICON_SOURCES = {
			"/main/res/Icon_8.png",
			"/main/res/Icon_16.png",
			"/main/res/Icon_32.png",
			"/main/res/Icon_64.png",
			"/main/res/Icon_128.png",
			"/main/res/Icon_256.png",
			"/main/res/Icon_512.png"
	};
	private static final int SECTION_HEIGHT = 75;
	private static final boolean RESIZABLE = false;

	public enum RightDisplay{ITEM_INFO,ADD_NEW_ITEM,EDIT_ITEM}
	private RightDisplay rightDisplay;

	private Stage mainStage;

	private Maybe<Database.PositionedItem> activeItem;//first stores the Item, second stores its index in database
	private Database database;

	private HBox rootPane;
	private static final String ROOT_PANE_ID = "rootPane";

	private Analytics.SortMode sortMode;
	private Analytics.FilterMode filterMode;
	private VBox listPane;
	private static final String LIST_PANE_ID = "listPane";

	private VBox infoPane;
	private static final String INFO_PANE_ID = "infoPane";

	private VBox addItemPane;
	private static final String ADD_ITEM_PANE_ID = "addItemPane";

	private VBox editPane;
	private static final String EDIT_PANE_ID = "editPane";

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
		for(Node child: this.rootPane.getChildren()){
			String childId = child.getId();
			if(childId.equals(INFO_PANE_ID)){
				return RightDisplay.ITEM_INFO;
			}
			if(childId.equals(ADD_ITEM_PANE_ID)){
				return RightDisplay.ADD_NEW_ITEM;
			}
			if(childId.equals(EDIT_PANE_ID)){
				return RightDisplay.EDIT_ITEM;
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
		updateEditPane();
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
						case EDIT_ITEM:
							replaceNode(EDIT_PANE_ID, this.infoPane);
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
						case EDIT_ITEM:
							replaceNode(EDIT_PANE_ID, this.addItemPane);
							break;
						default:
							Util.nyi(main.java.util.Util.getFileName(), main.java.util.Util.getLineNumber());
					}
				}
				break;
			case EDIT_ITEM:
				if(getActiveRightDisplay() == null){
					this.rootPane.getChildren().add(this.editPane);
				} else{
					switch(getActiveRightDisplay()){
						case ITEM_INFO:
							replaceNode(INFO_PANE_ID, this.editPane);
							break;
						case ADD_NEW_ITEM:
							replaceNode(ADD_ITEM_PANE_ID, this.editPane);
							break;
						case EDIT_ITEM:
							replaceNode(EDIT_PANE_ID, this.editPane);
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
							itemDisplayName.getStyleClass().add("bodyText");
							itemDisplayName.setText(this.database.getItem(activeItem.get().getIndex()).getDisplayName());
							itemDisplayName.setWrappingWidth(WRAPPING_WIDTH);
						}
						Text itemDisplayPriority = new Text();
						{
							itemDisplayPriority.getStyleClass().add("bodyText");
							itemDisplayPriority.setText("Priority: " + this.database.getItem(activeItem.get().getIndex()).getPriority().toString());
							itemDisplayPriority.setWrappingWidth(WRAPPING_WIDTH);
						}
						Text itemDisplayDescription= new Text();
						{
							itemDisplayDescription.getStyleClass().add("bodyText");
							itemDisplayDescription.setText(this.database.getItem(activeItem.get().getIndex()).getDescription());
							itemDisplayDescription.setWrappingWidth(WRAPPING_WIDTH);
						}
						Text itemDisplayDate = new Text();
						{
							itemDisplayDate.getStyleClass().add("bodyText");
							itemDisplayDate.setText("Created " + this.database.getItem(activeItem.get().getIndex()).getDate().toString());
							itemDisplayDate.setWrappingWidth(WRAPPING_WIDTH);
						}
						itemDisplayInfoBorder.getChildren().addAll(itemDisplayName,itemDisplayPriority);
						if(!this.database.getItem(activeItem.get().getIndex()).getDescription().equals("")){
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
				editItemMenu.getStyleClass().add("paneBorder");

				final int NUMBER_OF_BUTTONS = 3;
				final int BUTTON_WIDTH = (int)((WIDTH - (NUMBER_OF_BUTTONS + 1) * PADDING) * (1.0 / NUMBER_OF_BUTTONS)),
						BUTTON_HEIGHT = SECTION_HEIGHT - 2 * PADDING;
				Button toggleFinished = new Button(this.activeItem.isValid() ? this.database.getItem(activeItem.get().getIndex()).getStatus().toString() : "Toggle Finished");
				{
					final Pair<Integer,Integer> BUTTON_SIZE = new Pair<>(BUTTON_WIDTH,BUTTON_HEIGHT);
					toggleFinished.setMinSize(BUTTON_SIZE.getFirst(), BUTTON_SIZE.getSecond());
					toggleFinished.setMaxSize(BUTTON_SIZE.getFirst(), BUTTON_SIZE.getSecond());
					toggleFinished.setPrefSize(BUTTON_SIZE.getFirst(), BUTTON_SIZE.getSecond());
					toggleFinished.getStyleClass().add("button");
					toggleFinished.setOnAction(
							(ActionEvent event) ->
							{
								if(this.activeItem.isValid()){
									this.database.getItem(activeItem.get().getIndex()).toggleStatus();
									this.database.editItem(this.activeItem.get());
									this.activeItem.set(this.database.getPositionedItem(this.activeItem.get().getIndex()));
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
					editItem.getStyleClass().add("button");
					editItem.setOnAction(
							(ActionEvent event) -> //TODO
							{
								this.rightDisplay = RightDisplay.EDIT_ITEM;
								updateRightPane();
							}
					);
				}
				Button deleteItem = new Button("Delete");
				{
					final Pair<Integer,Integer> BUTTON_SIZE = new Pair<>(BUTTON_WIDTH,BUTTON_HEIGHT);
					deleteItem.setMinSize(BUTTON_SIZE.getFirst(), BUTTON_SIZE.getSecond());
					deleteItem.setMaxSize(BUTTON_SIZE.getFirst(), BUTTON_SIZE.getSecond());
					deleteItem.setPrefSize(BUTTON_SIZE.getFirst(), BUTTON_SIZE.getSecond());
					deleteItem.getStyleClass().add("button");
					deleteItem.setOnAction(
							(ActionEvent event) ->
							{
								{
									Alert deleteConfirmationDialog = new Alert(Alert.AlertType.CONFIRMATION);//TODO: make fit theme //TODO: check out more on http://code.makery.ch/blog/javafx-dialogs-official/

									deleteConfirmationDialog.setTitle("Deletion Confirmation");
									deleteConfirmationDialog.setHeaderText("Would you like to delete the Item \"" + this.database.getItem(activeItem.get().getIndex()).getDisplayName() + "\"? It cannot be undone.");
									deleteConfirmationDialog.setContentText("Item description: \"" + this.database.getItem(activeItem.get().getIndex()).getDescription() + "\"");
									{
										ButtonType deleteButton = new ButtonType("Delete", ButtonBar.ButtonData.YES);
										ButtonType cancelButton = new ButtonType("Cancel", ButtonBar.ButtonData.CANCEL_CLOSE);
										deleteConfirmationDialog.getButtonTypes().setAll(deleteButton, cancelButton);
									}
									{
										Optional<ButtonType> deleteConfirmationDialogResult = deleteConfirmationDialog.showAndWait();
										if(deleteConfirmationDialogResult.isPresent()) {
											switch (deleteConfirmationDialogResult.get().getButtonData()) {
												case YES:
													this.database.deleteItem(this.activeItem.get().getIndex());
													this.database.fillList();
													this.activeItem = new Maybe<>();
													updateRootPane();
													break;
												case CANCEL_CLOSE:
													break;
												default:
													Util.nyi(Util.getFileName(), Util.getLineNumber());
											}
										}
									}
								}
							}
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
				itemListMenu.getStyleClass().add("paneBorder");

				final int NUMBER_OF_BUTTONS = 3;
				final int BUTTON_WIDTH = (int)((WIDTH - (NUMBER_OF_BUTTONS + 1) * PADDING) * (1.0 / NUMBER_OF_BUTTONS)),
					BUTTON_HEIGHT = SECTION_HEIGHT - 2 * PADDING;
				final Pair<Integer,Integer> BUTTON_SIZE = new Pair<>(BUTTON_WIDTH,BUTTON_HEIGHT);
				Button addNew = new Button("Add New");
				{
					addNew.getStyleClass().add("button");

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
					sortBy.getStyleClass().add("comboBoxMenu");

					sortBy.setMinSize(BUTTON_SIZE.getFirst(), BUTTON_SIZE.getSecond());
					sortBy.setMaxSize(BUTTON_SIZE.getFirst(), BUTTON_SIZE.getSecond());
					sortBy.setPrefSize(BUTTON_SIZE.getFirst(), BUTTON_SIZE.getSecond());

					sortBy.setOnAction(
							(ActionEvent event) ->
							{
								this.sortMode = sortBy.getValue();
								updateLeftPane();
							}
					);
				}
				ComboBox<Analytics.FilterMode> filterBy = new ComboBox<>();
				{
					filterBy.setPromptText("Filter By");
					filterBy.setItems(FXCollections.observableArrayList(Analytics.FilterMode.values()));
					filterBy.getStyleClass().add("comboBoxMenu");

					filterBy.setMinSize(BUTTON_SIZE.getFirst(), BUTTON_SIZE.getSecond());
					filterBy.setMaxSize(BUTTON_SIZE.getFirst(), BUTTON_SIZE.getSecond());
					filterBy.setPrefSize(BUTTON_SIZE.getFirst(), BUTTON_SIZE.getSecond());

					filterBy.setOnAction(
							(ActionEvent event) ->
							{
								if(this.filterMode != filterBy.getValue()){
									boolean resetRightPane = true;
									if(this.activeItem.isValid()) {
										for (Database.PositionedItem a : Analytics.filter(filterBy.getValue(), this.database.getPositionedItems())) {
											if(this.activeItem.get().getItem().equals(a.getItem())){
												resetRightPane = false;
												break;
											}
										}
										if(resetRightPane){
											this.activeItem = new Maybe<>();
											updateRightPane();
										}
									}
								}
								this.filterMode = filterBy.getValue();
								updateLeftPane();
							}
					);
				}
				itemListMenu.getChildren().addAll(addNew,sortBy,filterBy);
			}
			StackPane itemListBorder = new StackPane();
			{
				itemListBorder.setMinWidth(WIDTH);
				itemListBorder.setMaxWidth(WIDTH);
				itemListBorder.setPrefWidth(WIDTH);
				final int ITEM_LIST_BORDER_HEIGHT = 1000;//this is just set to something really big, it's cropped down
				itemListBorder.setPrefHeight(ITEM_LIST_BORDER_HEIGHT);
				itemListBorder.getStyleClass().add("paneBorder");

				{
					ListView<String> itemList = new ListView<>();
					itemList.getStyleClass().add("itemList");

					itemList.setMinWidth(WIDTH - 2 * PADDING);
					itemList.setMaxWidth(WIDTH - 2 * PADDING);
					itemList.setPrefWidth(WIDTH - 2 * PADDING);
					//itemList.setFixedCellSize(SECTION_HEIGHT);//used to set cell height

					ArrayList<String> itemNames = new ArrayList<>();

					final ArrayList<Database.PositionedItem> itemsToDisplay = Analytics.filter(this.filterMode,Analytics.sort(this.sortMode,this.database.getPositionedItems()));//final to be used in ListChangeListener

					for(Database.PositionedItem positionedItem: itemsToDisplay){
						itemNames.add(positionedItem.getItem().shortenName());
					}

					itemList.setItems(FXCollections.observableArrayList(itemNames));

					if(this.activeItem.isValid()){
						itemList.scrollTo(this.activeItem.get().getIndex());
						itemList.getFocusModel().focus(this.activeItem.get().getIndex());//note: focused object is the one object in the entire operating system that receives keyboard input
						itemList.getSelectionModel().select(this.activeItem.get().getIndex());//note: selected object means it is marked
					}

					itemList.getSelectionModel().getSelectedIndices().addListener(
						(ListChangeListener.Change<? extends Integer> c) ->
						{
							this.rightDisplay = RightDisplay.ITEM_INFO;
							this.activeItem.set(itemsToDisplay.get(itemList.getSelectionModel().getSelectedIndex()));
							updateRightPane();
						}
					);

					{
						Rectangle itemListPlaceHolder = new Rectangle(WIDTH - 4 * PADDING,600);
						itemListPlaceHolder.getStyleClass().add("itemListPlaceHolder");
						itemList.setPlaceholder(itemListPlaceHolder);
					}

					itemListBorder.getChildren().add(itemList);
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
			addItemFieldsBorder.getStyleClass().add("contentBorder");

			VBox addItemFields = new VBox(PADDING);
			{
				final int TEXT_INPUT_WIDTH = (int)(WIDTH - 4 * PADDING);
				addItemFields.getStyleClass().add("addItemFields");
				{
					setName.getStyleClass().add("bodyText");
					setName.setPromptText("Add item name");
					setName.setMinWidth(TEXT_INPUT_WIDTH);
					setName.setMaxWidth(TEXT_INPUT_WIDTH);
					setName.setPrefWidth(TEXT_INPUT_WIDTH);
				}
				HBox setPriority = new HBox(PADDING);
				{
					setPriority.getStyleClass().add("centerPriorityLabel");

					Label priorityLabel = new Label("Priority:");
					{
						priorityLabel.getStyleClass().add("bodyText");
					}
					{
						prioritySelector.getStyleClass().add("bodyText");
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
					setDescription.getStyleClass().add("bodyText");
					setDescription.setPromptText("Add item description");
					setDescription.setMinSize(TEXT_INPUT_WIDTH,SET_DESCRIPTION_HEIGHT);
					setDescription.setMaxSize(TEXT_INPUT_WIDTH,SET_DESCRIPTION_HEIGHT);
					setDescription.setPrefSize(TEXT_INPUT_WIDTH,SET_DESCRIPTION_HEIGHT);
				}
				addItemFields.getChildren().addAll(setName, setPriority, setDescription);
			}
			addItemFieldsBorder.getChildren().addAll(addItemFields);
		}
		HBox addItemMenu = new HBox(PADDING);
		{
			final int NUMBER_OF_BUTTONS = 2;
			final int BUTTON_WIDTH = (int)((WIDTH - (NUMBER_OF_BUTTONS + 1) * PADDING) * (1.0 / NUMBER_OF_BUTTONS)),
					BUTTON_HEIGHT = SECTION_HEIGHT - 2 * PADDING;
			final Pair<Integer,Integer> BUTTON_SIZE = new Pair<>(BUTTON_WIDTH,BUTTON_HEIGHT);
			addItemMenu.getStyleClass().add("paneBorder");
			Button saveNewItemButton = new Button("Save");
			{
				saveNewItemButton.setMinSize(BUTTON_SIZE.getFirst(), BUTTON_SIZE.getSecond());
				saveNewItemButton.setMaxSize(BUTTON_SIZE.getFirst(), BUTTON_SIZE.getSecond());
				saveNewItemButton.setPrefSize(BUTTON_SIZE.getFirst(), BUTTON_SIZE.getSecond());
				saveNewItemButton.getStyleClass().add("button");
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
				cancelItemAddition.getStyleClass().add("button");
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
	 * Constructs a pane which provides for the ability of users to edit existing Items in the Database
	 */
	private void updateEditPane(){//TODO
		this.editPane.getChildren().clear();
		final double WIDTH_PERCENT = .66;
		final double WIDTH = DEFAULT_SIZE.width * WIDTH_PERCENT;

		this.editPane.setMaxWidth(WIDTH);
		this.editPane.setPrefWidth(this.editPane.getMaxWidth());
		this.editPane.getStyleClass().add("editPane");//FIXME

		StackPane editItemFieldsBorder = new StackPane();

		//these three inputs are listed in this scope so that the "Add Item" button can access their values to write to the database
		TextField editName = new TextField();
		ComboBox<Item.Priority> prioritySelector = new ComboBox<>();
		TextArea editDescription = new TextArea();
		{
			editItemFieldsBorder.getStyleClass().add("contentBorder");

			VBox editItemFields = new VBox(PADDING);
			{
				final int TEXT_INPUT_WIDTH = (int)(WIDTH - 4 * PADDING);
				editItemFields.getStyleClass().add("editItemFields");
				{
					editName.getStyleClass().add("bodyText");
					editName.setPromptText("Add item name");
					editName.setText(this.activeItem.isValid() ? this.activeItem.get().getItem().getDisplayName() : "");
					editName.setMinWidth(TEXT_INPUT_WIDTH);
					editName.setMaxWidth(TEXT_INPUT_WIDTH);
					editName.setPrefWidth(TEXT_INPUT_WIDTH);
				}
				HBox editPriority = new HBox(PADDING);
				{
					editPriority.getStyleClass().add("centerPriorityLabel");

					Label priorityLabel = new Label("Priority:");
					{
						priorityLabel.getStyleClass().add("bodyText");
					}
					{
						prioritySelector.getStyleClass().add("bodyText");
						prioritySelector.setPromptText("Priority...");
						prioritySelector.setValue(this.activeItem.isValid() ? this.activeItem.get().getItem().getPriority() : Item.Priority.LOW);
						prioritySelector.setItems(FXCollections.observableArrayList(
								Item.Priority.LOW, Item.Priority.MEDIUM, Item.Priority.HIGH
						));
					}
					editPriority.getChildren().addAll(priorityLabel,prioritySelector);
				}
				{
					final int EDIT_DESCRIPTION_HEIGHT = 510;//from testing on 5/25/2017
					editDescription.getStyleClass().add("bodyText");
					editDescription.setPromptText("Add item description");
					editDescription.setText(this.activeItem.isValid() ? this.activeItem.get().getItem().getDescription() : "");
					editDescription.setMinSize(TEXT_INPUT_WIDTH,EDIT_DESCRIPTION_HEIGHT);
					editDescription.setMaxSize(TEXT_INPUT_WIDTH,EDIT_DESCRIPTION_HEIGHT);
					editDescription.setPrefSize(TEXT_INPUT_WIDTH,EDIT_DESCRIPTION_HEIGHT);
				}
				editItemFields.getChildren().addAll(editName, editPriority, editDescription);
			}
			editItemFieldsBorder.getChildren().addAll(editItemFields);
		}
		HBox editItemMenu = new HBox(PADDING);
		{
			final int NUMBER_OF_BUTTONS = 2;
			final int BUTTON_WIDTH = (int)((WIDTH - (NUMBER_OF_BUTTONS + 1) * PADDING) * (1.0 / NUMBER_OF_BUTTONS)),
					BUTTON_HEIGHT = SECTION_HEIGHT - 2 * PADDING;
			final Pair<Integer,Integer> BUTTON_SIZE = new Pair<>(BUTTON_WIDTH,BUTTON_HEIGHT);
			editItemMenu.getStyleClass().add("paneBorder");
			Button saveNewItemButton = new Button("Save");
			{
				saveNewItemButton.setMinSize(BUTTON_SIZE.getFirst(), BUTTON_SIZE.getSecond());
				saveNewItemButton.setMaxSize(BUTTON_SIZE.getFirst(), BUTTON_SIZE.getSecond());
				saveNewItemButton.setPrefSize(BUTTON_SIZE.getFirst(), BUTTON_SIZE.getSecond());
				saveNewItemButton.getStyleClass().add("button");
				saveNewItemButton.setOnAction(
						(ActionEvent event) ->//TODO
						{
							String itemName = editName.getText(), itemDescription = editDescription.getText();
							Item.Priority itemPriority = prioritySelector.getValue();
							if(this.activeItem.isValid()){
								this.activeItem.set(new Database.PositionedItem(new Item(itemName, itemDescription, itemPriority),this.activeItem.get().getIndex()));
								this.database.editItem(this.activeItem.get());
								this.activeItem.set(this.database.getPositionedItem(this.activeItem.get().getIndex()));
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
				cancelItemAddition.getStyleClass().add("button");
				cancelItemAddition.setOnAction(
						(ActionEvent event) ->
						{
							this.rightDisplay = RightDisplay.ITEM_INFO;
							updateRightPane();
						}
				);
			}
			editItemMenu.getChildren().addAll(saveNewItemButton,cancelItemAddition);
		}
		this.editPane.getChildren().addAll(editItemFieldsBorder,editItemMenu);
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

		for(String iconSource: ICON_SOURCES) {
			this.mainStage.getIcons().add(new Image(iconSource));
		}
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
		Database.convertDataToTest();//TODO: for testing only

		this.mainStage = new Stage();//note: cannot edit this until after it has been set to the primaryStage

		this.database = new Database();
		this.database.fillList();

		this.rightDisplay = RightDisplay.ITEM_INFO;
		this.activeItem = (this.database.getItems().size() > 0) ? new Maybe<>(new Database.PositionedItem(this.database,0)) : new Maybe<>();

		this.rootPane = new HBox();
		this.rootPane.setId(ROOT_PANE_ID);

		this.sortMode = Analytics.SortMode.NONE;
		this.filterMode = Analytics.FilterMode.NONE;

		this.listPane = new VBox(PADDING);
		this.listPane.setId(LIST_PANE_ID);

		this.infoPane = new VBox();
		this.infoPane.setId(INFO_PANE_ID);

		this.addItemPane = new VBox();
		this.addItemPane.setId(ADD_ITEM_PANE_ID);

		this.editPane = new VBox();
		this.editPane.setId(EDIT_PANE_ID);
	}

	/**
	 * Used to launch the application with arguments
	 * @param args the arguments to launch the application which
	 */
	public static void main(String[] args){
		launch(args);
	}
}
