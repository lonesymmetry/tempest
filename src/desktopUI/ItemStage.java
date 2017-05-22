package desktopUI;

import javafx.event.ActionEvent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.TilePane;
import javafx.stage.Stage;
import util.Graphics;

import java.awt.Dimension;

/**
 * The expanded display view of a To-Do Item that contains all its useful information
 *
 * @author Logan Traffas
 */
public class ItemStage extends Stage{
	private static final int EXPERIMENTAL_HORIZONTAL_INSETS = 0;//from testing with Windows 10
	private static final int EXPERIMENTAL_VERTICAL_INSETS = 31;//from testing with Windows 10
	private static final Dimension DEFAULT_SIZE = new Dimension(
			1366 - EXPERIMENTAL_HORIZONTAL_INSETS,
			768 - EXPERIMENTAL_VERTICAL_INSETS - Graphics.TASKBAR_HEIGHT
	);

	private static final String STYLESHEET_SOURCE = "/res/ItemSceneStylesheet.css";
	private final TilePane layout;

	public ItemStage(){
		{//initialize layout
			this.layout = new TilePane();
			this.layout.setPrefSize(DEFAULT_SIZE.width,DEFAULT_SIZE.height);
		}
		{//populate stage
			Button button = new Button("Click Me");
			button.setOnAction((ActionEvent event) -> System.out.println("Test"));
			this.layout.getChildren().addAll(button);
		}
		this.setScene(new Scene(this.layout,DEFAULT_SIZE.width,DEFAULT_SIZE.height));
		this.getScene().getStylesheets().add(STYLESHEET_SOURCE);
	}

}
