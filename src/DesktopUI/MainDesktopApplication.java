package desktopUI;

import javafx.application.Application;
import javafx.stage.Stage;

/**
 *
 */
public class MainDesktopApplication extends Application{
	@Override
	public void start(Stage primaryStage) throws Exception {
		primaryStage = new ItemStage();
		primaryStage.show();
	}
}
