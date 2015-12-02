package meloApp;
	
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.LinkedList;

import javafx.application.Application;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;


public class Main extends Application {
	
	Profile user;
	
	@Override
	public void start(Stage primaryStage) {
		try {
			final Stage stage = primaryStage;
			BorderPane root = new BorderPane();
			GridPane gridpane = new GridPane();
			gridpane.setPadding(new Insets(5));
			gridpane.setHgap(10);
			gridpane.setVgap(5);
			Label signin = new Label("Sign in:");
			gridpane.add(signin, 0, 1);
			Button glogin = new Button("Google");
			gridpane.add(glogin, 2, 1);
			Button fblogin = new Button("Facebook");
			gridpane.add(fblogin, 1,1);
			Label usrName = new Label("Username:");
			gridpane.add(usrName, 0, 2);
			final TextField usrNameText = new TextField();
			gridpane.add(usrNameText, 0, 3, 5, 1);
			Label pwd = new Label("Password:");
			gridpane.add(pwd, 0, 4);
			final PasswordField password = new PasswordField();
			gridpane.add(password, 0, 5, 5, 1);
			Button signinBtn = new Button("Sign in");
			signinBtn.setOnMouseClicked(new EventHandler<Event>() {

				@Override
				public void handle(Event event) {
					boolean valid = authenticate(usrNameText.getText(), password.getText());
					if(valid) {
						user = new Profile(usrNameText.getText());
						locationScene(stage);					
					}
				}

				
			});
			gridpane.add(signinBtn, 3, 7);
			root.setCenter(gridpane);
			Scene scene = new Scene(root,300,200);
			scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
			primaryStage.setScene(scene);
			primaryStage.show();
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	private boolean authenticate(String usrname, String pwd) {
		if(usrname.isEmpty()) return false;
		try{
			String entry = usrname + "," + pwd;
			FileReader fr = new FileReader("users.txt");
			BufferedReader br = new BufferedReader(fr);
			for(String line = br.readLine(); line != null; line = br.readLine()) {
				
				if(entry.equals(entry)) {
					br.close();
					return true;
				}
			}
			br.close();
			write(entry, "users.txt");
			return true;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}
	
	private void locationScene(Stage stage) {
		final VBox vbox = new VBox();
		vbox.setAlignment(Pos.CENTER);
		vbox.setSpacing(10);
		if(user.hasLocations()) {
			LinkedList<Location> locs = user.getLocations();
			for(int i = 0; i < locs.size(); i++) {
				Button loc = new Button(locs.get(i).getNameOfPlace());
				loc.setOnMouseClicked(new EventHandler<Event>() {

					@Override
					public void handle(Event event) {
						// TODO Auto-generated method stub
						
					}
				});
				vbox.getChildren().add(loc);
			}
		} 
		final Button addloc = new Button("+");
		vbox.getChildren().add(addloc);
		StackPane root = new StackPane();
		root.getChildren().add(vbox);
		Scene scene = new Scene(root, 400, 400);
		addloc.setOnMouseClicked(new EventHandler<Event>() {

			@Override
			public void handle(Event event) {
				final TextField name = new TextField();
				name.setPromptText("Name of location");
				final Button ok = new Button("Done");
				final HBox hbox = new HBox();
				hbox.getChildren().addAll(name, ok);
				hbox.setAlignment(Pos.CENTER);
				hbox.setStyle("-fx-background-color: #4f4f4f; -fx-padding: 15; -fx-spacing: 10;");
				vbox.getChildren().add(hbox);
				ok.setOnMouseClicked(new EventHandler<Event>() {

					@Override
					public void handle(Event event) {
						if(!name.getText().isEmpty()) {
							Location loc = new Location(user, name.getText()); 
							user.addLocation(loc);
							vbox.getChildren().removeAll(hbox, addloc);
							Button locBtn = new Button(name.getText());
							write(user.getName() + " ,"  + loc.getNameOfPlace(), "db.txt");
							vbox.getChildren().addAll(locBtn, addloc);
						}
					}
				});
				
			}
		});
		stage.setScene(scene);
	}
	
	public void write(String s, String file) {
		try {
			FileWriter fw = new FileWriter(file, true);
			BufferedWriter bw = new BufferedWriter(fw);
			bw.write(s);
			bw.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
 	}
	
	public static void main(String[] args) {
		launch(args);
	}
}
