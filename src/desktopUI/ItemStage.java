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
	private static final String STYLESHEET_SOURCE = "/res/ItemStageStylesheet.css";
	private final TilePane layout;

	public ItemStage(){
		{//initialize layout
			this.layout = new TilePane();
			this.layout.setPrefSize(StageConstants.DEFAULT_SIZE.width,StageConstants.DEFAULT_SIZE.height);
		}
		{//populate stage
			Button button = new Button("Click Me");
			button.setOnAction((ActionEvent event) -> System.out.println("Test"));
			this.layout.getChildren().addAll(button);
		}
		this.setScene(new Scene(this.layout,StageConstants.DEFAULT_SIZE.width,StageConstants.DEFAULT_SIZE.height));
		this.getScene().getStylesheets().add(STYLESHEET_SOURCE);
	}

}
