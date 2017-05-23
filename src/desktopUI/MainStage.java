package desktopUI;

import control.Database;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.layout.HBox;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;

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
			this.rootPane.setPrefSize(StageConstants.DEFAULT_SIZE.width,StageConstants.DEFAULT_SIZE.height);
		}
		{
			final double WIDTH_PERCENT = .33;
			this.listPane = new VBox();
			this.listPane.setPrefWidth(StageConstants.DEFAULT_SIZE.width * WIDTH_PERCENT);
			this.listPane.getStyleClass().add("listPane");
			{
				final int MENU_HEIGHT = 150;
				ScrollPane itemList = new ScrollPane();//TODO: figure out what's going on with the scrollbar position
				HBox itemListMenu = new HBox();
				{//TODO
					itemListMenu.setPadding(new Insets(StageConstants.PADDING, StageConstants.PADDING, StageConstants.PADDING, StageConstants.PADDING));
					itemListMenu.setPrefSize(500,MENU_HEIGHT);

					Label temp = new Label("Menu");
					itemListMenu.getChildren().addAll(temp);
				}
				{
					final boolean PANNABLE = true;
					itemList.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
					itemList.setPannable(PANNABLE);
					itemList.getStyleClass().add("itemList");

					itemList.setPrefWidth(StageConstants.DEFAULT_SIZE.width * WIDTH_PERCENT);
					itemList.setPrefViewportWidth(StageConstants.DEFAULT_SIZE.width * WIDTH_PERCENT - (itemList.getViewportBounds().getWidth() + 20.5));
					{
						VBox items = new VBox(StageConstants.PADDING);
						items.setPadding(new Insets(StageConstants.PADDING, StageConstants.PADDING, StageConstants.PADDING, StageConstants.PADDING));
						for(int i = 0; i < database.getItems().size(); i++){
							final int HEIGHT = 75;
							StackPane itemName = new StackPane();

							Rectangle background = new Rectangle(itemList.getPrefViewportWidth() - (items.getPadding().getLeft() + items.getPadding().getRight()), HEIGHT);
							background.getStyleClass().add("itemName");

							Label name = new Label(database.getItems().get(i).getDisplayName());
							name.getStyleClass().add("itemName");

							itemName.getChildren().addAll(background,name);
							items.getChildren().add(itemName);
						}
						itemList.setContent(items);
					}
				}
				this.listPane.getChildren().addAll(itemListMenu,itemList);
			}
		}
		{//TODO
			final double WIDTH_PERCENT = .67;
			{
				this.infoPane = new VBox();
				this.infoPane.setPrefWidth(StageConstants.DEFAULT_SIZE.width * WIDTH_PERCENT);
				this.infoPane.getStyleClass().add("infoPane");
			}
			{
				this.addItemPane = new VBox();
				this.addItemPane.setPrefWidth(StageConstants.DEFAULT_SIZE.width * WIDTH_PERCENT);
				this.addItemPane.getStyleClass().add("addItemPane");
			}
		}

		this.rootPane.getChildren().addAll(this.listPane,this.infoPane);//ordered from left to right
		this.setScene(new Scene(this.rootPane,StageConstants.DEFAULT_SIZE.width,StageConstants.DEFAULT_SIZE.height));
		this.getScene().getStylesheets().add(STYLESHEET_SOURCE);
	}
}
