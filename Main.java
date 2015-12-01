package meloApp;
	
import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;


public class Main extends Application {
	@Override
	public void start(Stage primaryStage) {
		try {
			StackPane root = new StackPane();
			Scene scene = new Scene(root,400,400);
			VBox vbox = new VBox();
			vbox.setAlignment(Pos.CENTER);
			Label usrName = new Label("Username:");
			TextField usrNameText = new TextField();
			Label pwd = new Label("Password:");
			PasswordField password = new PasswordField();
			Button button = new Button("Sign in");
			button.setAlignment(Pos.BOTTOM_RIGHT);
			vbox.getChildren().addAll(usrName, usrNameText, pwd, password, button);
			root.getChildren().add(vbox);
			//scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
			primaryStage.setScene(scene);
			primaryStage.show();
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args) {
		launch(args);
	}
}
