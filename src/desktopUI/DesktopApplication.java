package desktopUI;

import javafx.application.Application;
import javafx.stage.Stage;

/**
 * Runs the desktop application that displays the Items and provides for their management
 *
 * @author Logan Traffas
 */
public class DesktopApplication extends Application{
	@Override
	public void start(Stage primaryStage) throws Exception {
		primaryStage = new MainStage();
		primaryStage.show();
	}
}
