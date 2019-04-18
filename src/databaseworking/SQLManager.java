/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package databaseworking;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

/**
 *
 * @author Username
 */
public class SQLManager extends Application {
	
	private static Stage stage;
	
	@Override
	public void start(Stage stage) throws Exception {
		this.setStage(stage);
		Parent root = FXMLLoader.load(getClass().getResource("MainController.fxml"));
		Scene scene = new Scene(root);
		scene.getStylesheets().add(getClass().getResource("styles/main_form_style.css").toExternalForm());
		stage.setScene(scene);
		stage.setTitle("SQLManager");
		stage.getIcons().addAll(new Image("databaseworking/sources/logo.png"), new Image("databaseworking/sources/logo32.png"));
		stage.setResizable(false);
		stage.show();
	}

	/**
	 * @param args the command line arguments
	 */
	public static void main(String[] args) {
		launch(args);
	}
	
	public void setStage(Stage stage) {
		SQLManager.stage = stage;
	}

	public static Stage getStage() {
		return stage;
	}
}
