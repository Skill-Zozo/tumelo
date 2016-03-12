import java.io.BufferedWriter;
import java.io.FileWriter;
import java.util.LinkedList;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.geometry.VPos;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.RowConstraints;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.text.Font;
import javafx.stage.Stage;

public class Main extends Application {

	private LinkedList<Scene> prevScenes = new LinkedList<>();
	private Profile user;
	private static Connector conn;
	private String pwrd = "";

	@Override
	public void start(Stage primaryStage) {
		try {
			final Stage stage = primaryStage;
			BorderPane root = new BorderPane();
			GridPane gridpane = new GridPane();
			gridpane.setPadding(new Insets(5));
			gridpane.setHgap(10);
			gridpane.setVgap(5);
			Label usrName = new Label("Username:");
			gridpane.add(usrName, 0, 2);
			final TextField usrNameText = new TextField();
			gridpane.add(usrNameText, 0, 3, 18, 1);
			Label pwd = new Label("Password:");
			gridpane.add(pwd, 0, 4);
			final PasswordField password = new PasswordField();
			gridpane.add(password, 0, 5, 18, 1);
			Button signinBtn = new Button("Sign in");
			gridpane.add(signinBtn, 6, 8);
			final Scene scene = new Scene(root, 300, 200);
			scene.getStylesheets().add(
					getClass().getResource("application.css").toExternalForm());
			signinBtn.setOnMouseClicked(new EventHandler<Event>() {

				@Override
				public void handle(Event event) {
					String status = conn.login(usrNameText.getText(), 
							password.getText());
					if(status.contains("ok")) {
						user = conn.getUser(usrNameText.getText(), 
							password.getText());
						pwrd = password.getText();
						prevScenes.add(scene);
						locationScene(stage);
					} else {
						status.replace("-", " ");
						Label info = new Label(status.replace("failed_", ""));
						info.textFillProperty();
						gridpane.add(info, 0, 6, 5, 1);
						gridpane.getChildren().remove(signinBtn);
						gridpane.add(signinBtn, 6, 8, 5, 1);
					}
				}

			});
			stage.setTitle("Sign in");
			root.setCenter(gridpane);
			primaryStage.setScene(scene);
			primaryStage.show();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	private HBox setupDevButton(Device dev) {
		HBox hbox = new HBox();
		hbox.setAlignment(Pos.CENTER);
		hbox.setStyle("-fx-background-color: #4f4f4f; -fx-padding: 5; -fx-spacing: 10;");
		hbox.setSpacing(5);
		Label name = new Label(dev.getNameofDevice());
		name.setStyle("-fx-background-color: #4f4f4f; -fx-font: 15pt Verdana");
		name.setTextFill(Color.WHITE);
		final Button close = new Button();
		Image icon = new Image(Main.class.getResourceAsStream("defualt.png"));
		close.setStyle("-fx-background-color: #4f4f4f; -fx-font: 15pt Verdana");
		close.setGraphic(new ImageView(icon));
		close.setShape(new Circle(2));
		close.setOnMouseEntered(new EventHandler<Event>() {

			@Override
			public void handle(Event event) {
				Image icon = new Image(Main.class.getResourceAsStream("delete.png"));
				close.setGraphic(new ImageView(icon));
			}
			
		});
		
		close.setOnMouseExited(new EventHandler<Event>() {

			@Override
			public void handle(Event event) {
				Image icon = new Image(Main.class.getResourceAsStream("defualt.png"));
				close.setGraphic(new ImageView(icon));
			}
			
		});
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
		hbox.getChildren().addAll(name, on, off, close);
		return hbox;
	}

	private String getEntry(Device device) {
		Room room = device.getParentRoom();
		Location location = room.getPlace();
		Profile usr = location.getOwner();
		return usr.getName() + "," + location.getName() + "," + room.getName()
				+ "," + device.getNameofDevice();
	}

	private Tab getDevicesTab(Stage stage, Room room) {
		Tab tab = new Tab(room.getName());
		BorderPane grid = getRoomContent(stage, room);
		tab.setContent(grid);
		return tab;
	}
	
	private BorderPane getRoomContent(Stage stage, Room room) {
		BorderPane pane = new BorderPane();
		Label roomLabel = new Label("Devices in " + room.getName());
		roomLabel.setAlignment(Pos.CENTER);
		roomLabel.setFont(Font.font("sans-serif", 26));
		final VBox vbox = new VBox();
		vbox.setSpacing(15);
		vbox.setAlignment(Pos.CENTER);
		LinkedList<Device> devices = room.getDevices();
		for (int i = 0; i < devices.size(); i++) {
			Device device = devices.get(i);
			HBox hbox = setupDevButton(device);
			vbox.getChildren().add(hbox);
		}
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
							conn.addDeviceTo(user.getName(), pwrd, device);
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
		pane.setTop(roomLabel);
		pane.setCenter(vbox);
		pane.setBottom(backButton(stage));
		return pane;
	}

	private void setupEmptyTab(final Stage stage, Tab tab,TabPane pane ,Location location) {
		final TextField name = new TextField();
		name.setPromptText("Name of room");
		final Button ok = new Button("Done");
		final HBox hbox = new HBox();
		tab.setContent(hbox);
		hbox.getChildren().addAll(name, ok);
		hbox.setAlignment(Pos.CENTER);
		hbox.setStyle("-fx-background-color: #4f4f4f; -fx-padding: 15; -fx-spacing: 10;");
		ok.setOnMouseClicked(new EventHandler<Event>() {

			@Override
			public void handle(Event event) {
				if (!name.getText().isEmpty()) {
					final Room room = new Room(name.getText(), location);
					location.addRoom(room);
					Tab newAddRoom = new Tab("+");
					newAddRoom.setClosable(false);
					pane.getTabs().add(newAddRoom);
					tab.setContent(getRoomContent(stage, room));
					tab.setText(room.getName());
					setupEmptyTab(stage, newAddRoom, pane,location);
				}
			}
		});
	}
	
	private void roomScene(final Stage stage, final Location location) {
		stage.setTitle(location.getName());
		final Button addloc = new Button("+");
		addloc.setShape(new Circle(3));
		Group root = new Group();
		TabPane tabpane = new TabPane();
		final Scene scene = new Scene(root, 400, 400);
		BorderPane borderpane = new BorderPane();
		for(int i = 0; i < location.getRooms().size(); i++) {
			Room room  = location.getRooms().get(i);
			tabpane.getTabs().add(getDevicesTab(stage, room));
		}
		Tab addRoom = new Tab("+");
		addRoom.setClosable(false);
		tabpane.getTabs().add(addRoom);
		setupEmptyTab(stage, addRoom, tabpane,location);
		borderpane.prefHeightProperty().bind(scene.heightProperty());
		borderpane.prefWidthProperty().bind(scene.widthProperty());
		borderpane.setCenter(tabpane);
		root.getChildren().add(borderpane);
		stage.setScene(scene);
		stage.show();
	}

	private void setupGrid(GridPane gridpane) {
		RowConstraints rw1 = new RowConstraints();
		RowConstraints rw2 = new RowConstraints();
		ColumnConstraints cc  = new ColumnConstraints();
		cc.setPercentWidth(100);
		cc.setHalignment(HPos.CENTER);
		gridpane.getColumnConstraints().add(cc);
		rw1.setPercentHeight(5);
		rw1.setValignment(VPos.CENTER);
		rw2.setPercentHeight(85);
		gridpane.getRowConstraints().addAll(rw1, rw2);
	}

	private void locationScene(final Stage stage) {
		stage.setTitle(user.getName());
		Label locationLabel = new Label("Your locations");
		locationLabel.setFont(Font.font("san-serif", 26));
		locationLabel.underlineProperty();
		final VBox vbox = new VBox();
		vbox.setAlignment(Pos.CENTER);
		vbox.setSpacing(10);
		final Button addloc = new Button("+");
		addloc.setShape(new Circle(3));
		GridPane root = new GridPane();
		final Scene scene = new Scene(root, 400, 400);
		if (user.hasLocations()) {
			final LinkedList<Location> locs = user.getLocations();
			for (int i = 0; i < locs.size(); i++) {
				final Location l = locs.get(i);
				Button loc = new Button(l.getName());
				loc.setOnMouseClicked(new EventHandler<Event>() {

					@Override
					public void handle(Event event) {
						prevScenes.add(scene);
						roomScene(stage, l);
					}
				});
				vbox.getChildren().add(loc);
			}
		}
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
									prevScenes.add(scene);
									roomScene(stage, loc);
								}
							});
						}
					}
				});

			}
		});
		setupGrid(root);
		vbox.getChildren().add(addloc);
		root.add(locationLabel, 0, 0);
		root.add(vbox, 0, 1);
		root.add(backButton(stage), 0, 2);
		stage.setScene(scene);
	}

	private void goBack(Stage stage) {
		if(!prevScenes.isEmpty()) {
			stage.setScene(prevScenes.pollLast());
		}
	}
	
	private Button backButton(Stage stage) {
		Button back = new Button("Back");
		back.setOnAction((ActionEvent e) -> goBack(stage));
		back.setMaxWidth(Double.MAX_VALUE);
		return back;
	}
	
	public void write(String s, String file) {
		try {
			FileWriter fw = new FileWriter(file, true);
			BufferedWriter bw = new BufferedWriter(fw);
			bw.newLine();
			bw.write(s);
			bw.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void main(String[] args) {
		conn = new Connector();
		launch(args);
	}
}
