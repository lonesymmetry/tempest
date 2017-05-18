package JavaFXTesting;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import javafx.scene.control.Button;

import java.awt.Dimension;

/**
 *
 */
public class Main extends Application{
	private static final Dimension size = new Dimension(450,300);
	private Button button;

	@Override
	public void start(Stage stage) throws Exception {
		stage.setTitle("Tempest");

		button = new Button("Click Me");
		button.setOnAction((ActionEvent event) -> System.out.println("Clicked!"));

		StackPane layout = new StackPane();
		layout.getChildren().addAll(button);

		Scene scene = new Scene(layout, size.width, size.height);
		scene.getStylesheets().add(this.getClass().getResource("stylesheet.css").toExternalForm());

		stage.setScene(scene);
		stage.show();
	}

	public static void main(String[] args){
		launch(args);
	}

}
