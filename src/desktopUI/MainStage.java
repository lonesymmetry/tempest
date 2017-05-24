package desktopUI;

import control.Database;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.layout.HBox;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;

import java.awt.*;

/**
 *
 *
 * @author Logan Traffas
 */
public class MainStage extends Stage{
	private static final String STYLESHEET_SOURCE = "/res/MainStageStylesheet.css";
	private static final boolean RESIZABLE = false;
	private final HBox rootPane;
	private final VBox listPane;
	private final VBox infoPane;
	private final VBox addItemPane;

	public MainStage(final Database database){
		setResizable(RESIZABLE);

		{//initialize layout
			this.rootPane = new HBox();
			//this.rootPane.setPadding(StageConstants.PADDING_INSETS);//unnecessary
			this.rootPane.setMaxSize(StageConstants.DEFAULT_SIZE.width,StageConstants.DEFAULT_SIZE.height);
			this.rootPane.getStyleClass().add("rootPane");
		}
		{//initialize listPane
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
					final double ITEM_LIST_MENU_HEIGHT = 50;//TODO: make same height as Rectangles that contain the Item displayNames?
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
							final int HEIGHT = 75;
							for(int i = 0; i < database.getItems().size(); i++){
								StackPane itemName = new StackPane();

								Rectangle background = new Rectangle(itemList.getPrefViewportWidth() - (4 * StageConstants.PADDING + util.Graphics.SCROLL_BAR_WIDTH), HEIGHT);//note: width subtracts three padding widths because there is the padding around the rectangle and the padding around the entire ScrollPane
								background.getStyleClass().add("itemName");

								Label name = new Label(database.getItems().get(i).getDisplayName());
								name.getStyleClass().add("itemName");

								itemName.getChildren().addAll(background, name);
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
			{
				this.infoPane = new VBox();
				this.infoPane.setMaxWidth(StageConstants.DEFAULT_SIZE.width * WIDTH_PERCENT);
				this.infoPane.setPrefWidth(this.infoPane.getMaxWidth());
				this.infoPane.getStyleClass().add("infoPane");
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
