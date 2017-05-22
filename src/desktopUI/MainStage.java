package desktopUI;

import javafx.event.ActionEvent;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.stage.Stage;

/**
 *
 *
 * @author Logan Traffas
 */
public class MainStage extends Stage{

	private static final String STYLESHEET_SOURCE = "/res/MainStageStylesheet.css";
	private final HBox rootPane;
	private final VBox listPane;
	private final VBox infoPane;
	private final Pane addItemPane;

	public MainStage(){
		{//initialize layout
			this.rootPane = new HBox(StageConstants.PADDING);
			this.rootPane.setPrefSize(StageConstants.DEFAULT_SIZE.width,StageConstants.DEFAULT_SIZE.height);
		}
		{
			final double WIDTH_PERCENT = .33;
			this.listPane = new VBox();
			this.listPane.setPrefSize(StageConstants.DEFAULT_SIZE.width * WIDTH_PERCENT, StageConstants.DEFAULT_SIZE.height - 2 * StageConstants.PADDING);
			this.listPane.setBorder(new Border(new BorderStroke(Color.BLACK, BorderStrokeStyle.SOLID, CornerRadii.EMPTY, BorderStroke.DEFAULT_WIDTHS)));
		}
		{
			final double WIDTH_PERCENT = .66;
			this.infoPane = new VBox();
			this.infoPane.setPrefSize(StageConstants.DEFAULT_SIZE.width * WIDTH_PERCENT, StageConstants.DEFAULT_SIZE.height - 2 * StageConstants.PADDING);
			this.infoPane.setBorder(new Border(new BorderStroke(Color.BLACK, BorderStrokeStyle.SOLID, CornerRadii.EMPTY, BorderStroke.DEFAULT_WIDTHS)));
		}
		{
			this.addItemPane = new Pane();
		}

		this.rootPane.getChildren().addAll(this.listPane,this.infoPane);//ordered from left to right

		{//populate stage
			Button button = new Button("Click Me");
			button.setOnAction((ActionEvent event) -> System.out.println("Test"));
			//this.rootPane.getChildren().addAll(button);
		}

		this.setScene(new Scene(this.rootPane,StageConstants.DEFAULT_SIZE.width,StageConstants.DEFAULT_SIZE.height));
		this.getScene().getStylesheets().add(STYLESHEET_SOURCE);
	}
}
