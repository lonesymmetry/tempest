package control;

import desktopUI.MainDesktopApplication;
import javafx.application.Application;

/**
 *
 */
public class Main {
	private Database database;

	public void runApp(String[] args){
		Application.launch(MainDesktopApplication.class,args);
	}

	public static void main(String[] args){
		Main main = new Main();
		main.runApp(args);
	}

	public Main(){
		this.database = new Database();
		this.database.fillList();
	}
}
