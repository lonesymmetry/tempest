package DesktopUI;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import javafx.scene.control.Button;

import java.awt.Dimension;

/**
 *
 */
public class Main extends Application{
	private static final Dimension SIZE = new Dimension(450,300);
	private Stage stage;
	private Scene scene1;
	private Scene scene2;

	@Override
	public void start(Stage primaryStage) throws Exception {
		this.stage = primaryStage;
		stage.setTitle("Tempest");
		{
			Label label = new Label("Text");
			StackPane layout = new StackPane();
			layout.getChildren().addAll(label);
			this.scene2 = new Scene(layout, SIZE.width, SIZE.height);
		}
		{
			Button button = new Button("Switch Scene");
			button.setOnAction((ActionEvent event) -> this.stage.setScene(this.scene2));
			StackPane layout = new StackPane();
			layout.getChildren().addAll(button);

			this.scene1 = new Scene(layout, SIZE.width, SIZE.height);
			this.stage.setScene(this.scene1);
		}
		stage.getScene().getStylesheets().add("/res/ItemSceneStylesheet.css");
		stage.show();
	}

	public static void main(String[] args){
		launch(args);
	}
}
