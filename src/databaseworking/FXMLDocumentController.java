/**
 * Sample Skeleton for 'FXMLDocument.fxml' Controller Class
 */

package databaseworking;

import actors.Reader;
import static databaseworking.Operator.getColumnNames;
import java.io.IOException;
import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import javafx.beans.value.ObservableValue;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.control.cell.TextFieldTreeTableCell;
import javafx.util.StringConverter;
import javafx.scene.control.TableColumn.CellDataFeatures;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.converter.IntegerStringConverter;
import javafx.util.Callback;
import databaseworking.requests.Executable;
import javafx.geometry.Orientation;
import javafx.scene.control.Separator;
import javafx.scene.image.ImageView;

public class FXMLDocumentController {
	
	HBox selectedRow;
	
	@FXML // ResourceBundle that was given to the FXMLLoader
	private ResourceBundle resources;

	@FXML // URL location of the FXML file that was given to the FXMLLoader
	private URL location;

	@FXML // fx:id="scrlContainer"
	private ScrollPane scrlContainer; // Value injected by FXMLLoader

	@FXML // fx:id="vboxTable"
	private VBox vboxTable; // Value injected by FXMLLoader

	@FXML // fx:id="hboxActions"
	private HBox hboxActions; // Value injected by FXMLLoader

	@FXML // fx:id="imgAdd"
	private ImageView imgAdd; // Value injected by FXMLLoader

	@FXML // fx:id="imgRemove"
	private ImageView imgRemove; // Value injected by FXMLLoader

	@FXML // fx:id="imgConfirm"
	private ImageView imgConfirm; // Value injected by FXMLLoader

	@FXML // fx:id="imgConnection"
	private ImageView imgConnection; // Value injected by FXMLLoader
	
	@FXML // fx:id="imgRefresh"
	private ImageView imgRefresh; // Value injected by FXMLLoader

	@FXML // fx:id="imgAddDatabase"
	private ImageView imgAddDatabase; // Value injected by FXMLLoader

	@FXML // fx:id="imgRemoveDatabase"
	private ImageView imgRemoveDatabase; // Value injected by FXMLLoader

	@FXML // fx:id="imgAddTable"
	private ImageView imgAddTable; // Value injected by FXMLLoader

	@FXML // fx:id="imgRemoveTable"
	private ImageView imgRemoveTable; // Value injected by FXMLLoader

	@FXML // This method is called by the FXMLLoader when initialization is complete
	void initialize() {
		loadData();
		
		imgAdd.setOnMouseClicked((event) -> {
			addNewRow();
		});
		
		imgRemove.setOnMouseClicked((event) -> {
			vboxTable.getChildren().remove(selectedRow);
		});
	
		imgConfirm.setOnMouseClicked((event) -> {
			ObservableList<Node> rows = vboxTable.getChildren();
			rows.remove(0);
			List<ArrayList<String>> list = new ArrayList<ArrayList<String>>(){{
				for (Node row : rows) {
					add(new ArrayList<String>(){{
						for (Node cell : ((HBox)row).getChildren()) {
							if(cell.getClass() == TextField.class){
								if(!((TextField)cell).getText().isEmpty()){
									add(((TextField)cell).getText());
								}
							}
						}
					}});
				}
			}};
			
			Operator.deleteAll();
			Operator.insert(list);
			loadData();
		});
		
		imgConnection.setOnMouseClicked((event) -> {
			try {
				Stage settingsStage = new Stage();
				Parent root = FXMLLoader.load(getClass().getResource("Connecting.fxml"));
				Scene scene = new Scene(root);
				settingsStage.setScene(scene);
//				settingsStage.initModality(Modality.WINDOW_MODAL);
//				settingsStage.initOwner(((Node)event.getSource()).getScene().getWindow());
				settingsStage.setResizable(false);
				settingsStage.show();
				((Stage)((Node)event.getSource()).getScene().getWindow()).close();
			} catch (IOException ex) {
				Logger.getLogger(ConnectingController.class.getName()).log(Level.SEVERE, null, ex);
			}
		});
	}
	
	public void loadData(){
		vboxTable.getChildren().clear();
		HBox header = new HBox(
			Operator.getColumnCount() < 3 ? 
				(scrlContainer.getPrefWidth() - 10) / Operator.getColumnCount() / 2 :
				(scrlContainer.getPrefWidth() - 10) / 6
		);
		for (String columnName : Operator.getColumnNames()) {
			header.getChildren().add(new Label(columnName));
		}
		header.setAlignment(Pos.CENTER);
		header.setPadding(new Insets(0, 10, 0, 10));
		header.getStyleClass().add("header");
		vboxTable.getChildren().add(header);
		
		List<ArrayList<String>> data = Operator.selectAll();
		for (ArrayList<String> rowData : data) {
			HBox row = new HBox(0);
			for (String cellString : rowData) {
				TextField cell = new TextField(cellString);
				cell.setPrefWidth((scrlContainer.getPrefWidth() - (Operator.getColumnCount() + 2) * 10) / Operator.getColumnCount());
				cell.setOnMouseClicked(rowSelecting);
				cell.getStyleClass().add("tableCell");
				row.getChildren().addAll(cell, new Separator(Orientation.VERTICAL));
			}
			row.setAlignment(Pos.TOP_LEFT);
			row.setPadding(new Insets(0, 10, 0, 10));
			row.setOnMouseClicked(rowSelecting);
			row.getStyleClass().add("focusedRow");
			vboxTable.getChildren().add(row);
		}
	}
	
	public void addNewRow(){
		HBox row = new HBox(5);
		TextField numCell = new TextField(String.valueOf(vboxTable.getChildren().size()));
		numCell.setPrefWidth((scrlContainer.getPrefWidth() - (Operator.getColumnCount() + 2) * 10) / Operator.getColumnCount());
		numCell.getStyleClass().add("tableCell");
		row.getChildren().add(numCell);
		for (int i = 1; i < Operator.getColumnCount(); i++) {
			TextField cell = new TextField();
			cell.setMinWidth((scrlContainer.getPrefWidth() - (Operator.getColumnCount() + 2) * 10) / Operator.getColumnCount());
			cell.setOnMouseClicked(rowSelecting);
			cell.getStyleClass().add("tableCell");
			row.getChildren().add(cell);
		}
		row.setAlignment(Pos.TOP_LEFT);
		row.setOnMouseClicked(rowSelecting);
		row.getStyleClass().add("focusedRow");
		row.setPadding(new Insets(0, 10, 0, 10));
		vboxTable.getChildren().add(row);
	}
	
	EventHandler<MouseEvent> rowSelecting = new EventHandler<MouseEvent>(){
		@Override
		public void handle(MouseEvent event) {
			if(selectedRow != null){
				selectedRow.getStyleClass().remove("selectedRow");
			}
			Object source = event.getSource();
			if(source.getClass() == TextField.class){
				selectedRow = (HBox)((TextField)event.getSource()).getParent();
			} else {
				selectedRow = (HBox)event.getSource();
			}
			selectedRow.getStyleClass().add("selectedRow");
			System.out.println("row is selected");
		}
	};
}
