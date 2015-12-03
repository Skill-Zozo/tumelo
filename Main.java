package meloApp;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.LinkedList;

import javafx.application.Application;
import javafx.event.ActionEvent;
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
import javafx.scene.shape.Circle;
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
			gridpane.add(fblogin, 1, 1);
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
					boolean valid = authenticate(usrNameText.getText(),
							password.getText());
					if (valid) {
						locationScene(stage);
					}
				}

			});
			gridpane.add(signinBtn, 3, 7);
			root.setCenter(gridpane);
			Scene scene = new Scene(root, 300, 200);
			scene.getStylesheets().add(
					getClass().getResource("application.css").toExternalForm());
			primaryStage.setScene(scene);
			primaryStage.show();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private boolean authenticate(String usrname, String pwd) {
		if (usrname.isEmpty())
			return false;
		try {
			String entry = usrname + "," + pwd;
			FileReader fr = new FileReader("users.txt");
			BufferedReader br = new BufferedReader(fr);
			for (String line = br.readLine(); line != null; line = br
					.readLine()) {

				if (entry.equals(entry)) {
					br.close();
					buildUserProfile(usrname);
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

	private void buildUserProfile(String usrname) {
		try {
			user = new Profile(usrname);
			FileReader fr = new FileReader("db.txt");
			BufferedReader br = new BufferedReader(fr);
			for(String line = br.readLine(); line != null; line = br.readLine()) {
				String[] dets = line.split(",");
				if(dets[0].equalsIgnoreCase(usrname)) {
					Location loc = new Location(user, dets[1]);
					Room room = new Room(dets[2], loc);
					Device device = new Device(dets[3].trim(), room);
					user.addLocation(loc);
					loc.addRoom(room);
					room.addDevice(device);
				}
			}
			br.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}

	private HBox setupDevButton(Device dev) {
		HBox hbox = new HBox();
		hbox.setAlignment(Pos.CENTER);
		hbox.setStyle("-fx-background-color: #4f4f4f; -fx-padding: 5; -fx-spacing: 10;");
		hbox.setSpacing(5);
		Label name = new Label(dev.getNameofDevice());
		name.setStyle("-fx-background-color: #7f7f4f; -fx-font: 15pt Verdana");
		final Button on = new Button("ON");
		final Button off = new Button("OFF");
		on.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent event) {
				on.setDisable(true);
				off.setDisable(false);
			}
		});
		off.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent event) {
				off.setDisable(true);
				on.setDisable(false);
			}
		});
		hbox.getChildren().addAll(name, on, off);
		return hbox;
	}
	
	private void deviceScene(Stage stage, final Room room) {
		final VBox vbox = new VBox();
		vbox.setSpacing(15);
		vbox.setAlignment(Pos.CENTER);
		LinkedList<Device> devices = room.getDevices();
		for (int i = 0; i < devices.size(); i++) {
			Device device = devices.get(i);
			HBox hbox = setupDevButton(device);
			vbox.getChildren().add(hbox);
		}
		StackPane root = new StackPane();
		root.getChildren().add(vbox);
		Scene scene = new Scene(root, 400, 400);
		final Button addDev = new Button("+");
		addDev.setShape(new Circle(3));
		addDev.setOnMouseClicked(new EventHandler<Event>() {

			@Override
			public void handle(Event event) {
				final TextField name = new TextField();
				name.setPromptText("Name of device");
				final Button ok = new Button("Done");
				final HBox hbox = new HBox();
				hbox.getChildren().addAll(name, ok);
				hbox.setAlignment(Pos.CENTER);
				hbox.setStyle("-fx-background-color: #4f4f4f; -fx-padding: 15; -fx-spacing: 10;");
				vbox.getChildren().add(hbox);
				ok.setOnMouseClicked(new EventHandler<Event>() {

					@Override
					public void handle(Event event) {
						if (!name.getText().isEmpty()) {
							Device device = new Device(name.getText(), room);
							room.addDevice(device);
							vbox.getChildren().removeAll(hbox, addDev);
							HBox hbox = setupDevButton(device);
							vbox.getChildren().addAll(hbox, addDev);
							String entry = getEntry(device);
							write(entry, "db.txt");
							
						}
					}
				});

			}
		});
		vbox.getChildren().add(addDev);
		stage.setScene(scene);
	}

	private String getEntry(Device device) {
		Room room = device.getParentRoom();
		Location location = room.getPlace();
		Profile usr = location.getOwner();
		return usr.getName() + "," + location.getName() + "," + room.getName()
				+ "," + device.getNameofDevice();
	}

	private void roomScene(final Stage stage, final Location location) {
		final VBox vbox = new VBox();
		vbox.setAlignment(Pos.CENTER);
		vbox.setSpacing(10);
		if (user.hasLocations()) {
			LinkedList<Room> rooms = location.getRooms();
			for (int i = 0; i < rooms.size(); i++) {
				final Room room = rooms.get(i);
				Button roomBtn = new Button(room.getName());
				roomBtn.setOnMouseClicked(new EventHandler<Event>() {

					@Override
					public void handle(Event event) {
						deviceScene(stage, room);
					}
				});
				vbox.getChildren().add(roomBtn);
			}
		}
		final Button addloc = new Button("+");
		addloc.setShape(new Circle(3));
		vbox.getChildren().add(addloc);
		StackPane root = new StackPane();
		root.getChildren().add(vbox);
		Scene scene = new Scene(root, 400, 400);
		addloc.setOnMouseClicked(new EventHandler<Event>() {

			@Override
			public void handle(Event event) {
				final TextField name = new TextField();
				name.setPromptText("Name of room");
				final Button ok = new Button("Done");
				final HBox hbox = new HBox();
				hbox.getChildren().addAll(name, ok);
				hbox.setAlignment(Pos.CENTER);
				hbox.setStyle("-fx-background-color: #4f4f4f; -fx-padding: 15; -fx-spacing: 10;");
				vbox.getChildren().add(hbox);
				ok.setOnMouseClicked(new EventHandler<Event>() {

					@Override
					public void handle(Event event) {
						if (!name.getText().isEmpty()) {
							final Room room = new Room(name.getText(), location);
							location.addRoom(room);
							vbox.getChildren().removeAll(hbox, addloc);
							Button roomBtn = new Button(name.getText());
							vbox.getChildren().addAll(roomBtn, addloc);
							roomBtn.setOnMouseClicked(new EventHandler<Event>() {

								@Override
								public void handle(Event event) {
									deviceScene(stage, room);
								}
							});
						}
					}
				});

			}
		});
		stage.setScene(scene);
		stage.show();
	}

	private void locationScene(final Stage stage) {
		final VBox vbox = new VBox();
		vbox.setAlignment(Pos.CENTER);
		vbox.setSpacing(10);
		if (user.hasLocations()) {
			final LinkedList<Location> locs = user.getLocations();
			for (int i = 0; i < locs.size(); i++) {
				final Location l = locs.get(i);
				Button loc = new Button(l.getName());
				loc.setOnMouseClicked(new EventHandler<Event>() {

					@Override
					public void handle(Event event) {
						roomScene(stage, l);
					}
				});
				vbox.getChildren().add(loc);
			}
		}
		final Button addloc = new Button("+");
		addloc.setShape(new Circle(3));
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
						if (!name.getText().isEmpty()) {
							final Location loc = new Location(user, name.getText());
							user.addLocation(loc);
							vbox.getChildren().removeAll(hbox, addloc);
							Button locBtn = new Button(name.getText());
							vbox.getChildren().addAll(locBtn, addloc);
							locBtn.setOnMouseClicked(new EventHandler<Event>() {

								@Override
								public void handle(Event event) {
									roomScene(stage, loc);
								}
							});
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
			bw.newLine();
			bw.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void main(String[] args) {
		launch(args);
	}
}
