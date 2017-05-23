package control;

import desktopUI.DesktopApplication;
import javafx.application.Application;

/**
 *
 */
public class Main {
	public void runApp(String[] args){
		Application.launch(DesktopApplication.class,args);
	}

	public static void main(String[] args){
		Main main = new Main();
		main.runApp(args);
	}
}
