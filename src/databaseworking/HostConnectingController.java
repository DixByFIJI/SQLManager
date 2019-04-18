/**
 * Sample Skeleton for 'FXMLConnectingWindow.fxml' Controller Class
 */

package databaseworking;

import databaseworking.connections.HostConnector;
import java.net.URL;
import java.sql.Connection;
import java.util.ResourceBundle;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class HostConnectingController {

	public static HostConnector hostConnector;
	public static Connection connection;
	
	private static boolean isConnected = false;
	
	@FXML // ResourceBundle that was given to the FXMLLoader
	private ResourceBundle resources;

	@FXML // URL location of the FXML file that was given to the FXMLLoader
	private URL location;

	@FXML // fx:id="apaneRoot"
	private AnchorPane apaneRoot; // Value injected by FXMLLoader

	@FXML // fx:id="vboxContainer"
	private VBox vboxContainer; // Value injected by FXMLLoader

	@FXML // fx:id="imgHost"
	private ImageView imgHost; // Value injected by FXMLLoader

	@FXML // fx:id="txtHost"
	private TextField txtHost; // Value injected by FXMLLoader

	@FXML // fx:id="imgPort"
	private ImageView imgPort; // Value injected by FXMLLoader

	@FXML // fx:id="txtPort"
	private TextField txtPort; // Value injected by FXMLLoader

	@FXML // fx:id="imgLogin"
	private ImageView imgLogin; // Value injected by FXMLLoader

	@FXML // fx:id="txtLogin"
	private TextField txtLogin; // Value injected by FXMLLoader

	@FXML // fx:id="imgPassword"
	private ImageView imgPassword; // Value injected by FXMLLoader

	@FXML // fx:id="txtPassword"
	private PasswordField txtPassword; // Value injected by FXMLLoader

	@FXML // fx:id="btnCheckConnection"
	private Button btnCheckConnection; // Value injected by FXMLLoader

	@FXML // fx:id="btnAccept"
	private Button btnAccept; // Value injected by FXMLLoader

	@FXML // This method is called by the FXMLLoader when initialization is complete
	void initialize() {
		
		ImageView imgConnected = new ImageView("databaseworking/sources/connected.png");
		imgConnected.setFitWidth(30);
		imgConnected.setFitHeight(30);
		
		ImageView imgDisconnected = new ImageView("databaseworking/sources/disconnected.png");
		imgDisconnected.setFitWidth(30);
		imgDisconnected.setFitHeight(30);
		
		ImageView imgCheckConnection = new ImageView("databaseworking/sources/execution.png");
		imgCheckConnection.setFitWidth(30);
		imgCheckConnection.setFitHeight(30);
		
		btnAccept.setGraphic(imgDisconnected);
		btnCheckConnection.setGraphic(imgCheckConnection);
		
		btnCheckConnection.setOnMouseClicked((event) -> {
			hostConnector = new HostConnector()
				.setHost(txtHost.getText())
				.setPort(Integer.parseInt(txtPort.getText()))
				.setUser(txtLogin.getText())
				.setPass(txtPassword.getText());
			
			if(hostConnector.connect()){
				isConnected = true;
				connection = hostConnector.getConnection();
				btnAccept.setDisable(false);
				btnAccept.setGraphic(imgConnected);
			} else {
				btnAccept.setDisable(true);
				btnAccept.setGraphic(imgDisconnected);
				Alert connectionAlert = new Alert(AlertType.ERROR);
				connectionAlert.setTitle("Connection error");
				connectionAlert.setHeaderText(null);
				connectionAlert.setContentText("Cannot connect to " + txtHost.getText());
				connectionAlert.showAndWait();
			}
		});
		
		btnAccept.setOnMouseClicked((event) -> {
				((Stage)((Node)event.getSource()).getScene().getWindow()).close();
		});
		
		Platform.runLater(new Runnable() {
			@Override
			public void run() {
				apaneRoot.getScene().getWindow().setOnCloseRequest((event) -> {
					if(!isConnected) {
						System.exit(0);
					}
				});
			}
		});
	}
}