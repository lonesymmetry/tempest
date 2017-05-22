package desktopUI;

import javafx.application.Application;
import javafx.stage.Stage;

/**
 * The primary stage for the desktop application that displays the Items in a list and provides for their management
 *
 * @author Logan Traffas
 */
public class MainDesktopApplication extends Application{
	@Override
	public void start(Stage primaryStage) throws Exception {
		primaryStage = new ItemStage();
		primaryStage.show();
	}
}
