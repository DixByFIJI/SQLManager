/**
 * Sample Skeleton for 'FXMLConnectingWindow.fxml' Controller Class
 */

package databaseworking;

import databaseworking.connections.MySQLConnector;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class ConnectingController {

	public static MySQLConnector mysqlconnector;
	
	@FXML // ResourceBundle that was given to the FXMLLoader
	private ResourceBundle resources;

	@FXML // URL location of the FXML file that was given to the FXMLLoader
	private URL location;

	@FXML // fx:id="txtHost"
	private TextField txtHost; // Value injected by FXMLLoader

	@FXML // fx:id="txtPort"
	private TextField txtPort; // Value injected by FXMLLoader

	@FXML // fx:id="txtLogin"
	private TextField txtLogin; // Value injected by FXMLLoader

	@FXML // fx:id="txtPassword"
	private PasswordField txtPassword; // Value injected by FXMLLoader

	@FXML // fx:id="txtDatabase"
	private TextField txtDatabase; // Value injected by FXMLLoader

	@FXML // fx:id="txtTable"
	private TextField txtTable; // Value injected by FXMLLoader

	@FXML // fx:id="btnCheckConnection"
	private Button btnCheckConnection; // Value injected by FXMLLoader

	@FXML // fx:id="btnAccept"
	private Button btnAccept; // Value injected by FXMLLoader

	@FXML // This method is called by the FXMLLoader when initialization is complete
	void initialize() {
		btnCheckConnection.setOnMouseClicked((event) -> {
			mysqlconnector = new MySQLConnector()
				.setHost(txtHost.getText())
				.setPort(Integer.parseInt(txtPort.getText()))
				.setUser(txtLogin.getText())
				.setPass(txtPassword.getText())
				.setDatabaseName(txtDatabase.getText())
				.setTableName(txtTable.getText());
			
			if(mysqlconnector.connect()){
				btnAccept.setDisable(false);
			} else {
				btnAccept.setDisable(true);
			}
		});
		
		btnAccept.setOnMouseClicked((event) -> {
			try {
				Stage mainStage = new Stage();
				Parent root = FXMLLoader.load(getClass().getResource("FXMLDocument.fxml"));
				Scene scene = new Scene(root);
				scene.getStylesheets().add(getClass().getResource("styles/style.css").toExternalForm());
				mainStage.setScene(scene);
				mainStage.show();
				
				((Stage)((Node)event.getSource()).getScene().getWindow()).close();
			} catch (IOException ex) {
				Logger.getLogger(ConnectingController.class.getName()).log(Level.SEVERE, null, ex);
			}
		});
	}
}