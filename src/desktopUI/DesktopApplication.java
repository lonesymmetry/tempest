package desktopUI;

import control.Database;
import javafx.application.Application;
import javafx.stage.Stage;

/**
 * Runs the desktop application that displays the Items and provides for their management
 *
 * @author Logan Traffas
 */
public class DesktopApplication extends Application{
	private Database database;

	@Override
	public void start(Stage primaryStage) throws Exception {
		initialize();
		primaryStage = new MainStage(database);
		primaryStage.show();
	}

	private void initialize(){
		{
			//TODO: for testing only
			Database.testWrite();
		}
		database = new Database();
		database.fillList();
	}

	public static void main(String[] args){
		launch(args);
	}
}
