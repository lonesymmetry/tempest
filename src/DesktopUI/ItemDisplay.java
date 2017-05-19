package DesktopUI;

import javafx.scene.Scene;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

import java.awt.Dimension;

/**
 *
 */
public class ItemDisplay extends Stage{
	//TODO: check out http://stackoverflow.com/questions/36632119/how-to-run-one-of-multiple-distinct-stages-at-application-startup-with-javafx
	private static final Dimension SCENE_SIZE = new Dimension(450,300);
	private static final String STYLESHEET_SOURCE = "/res/ItemSceneStylesheet.css";
	private StackPane layout;
	private Scene scene;

	public ItemDisplay(final Stage STAGE){
		this.layout = new StackPane();
		this.scene = new Scene(this.layout, SCENE_SIZE .width, SCENE_SIZE .height);
		this.scene.getStylesheets().add(STYLESHEET_SOURCE);
	}

}
