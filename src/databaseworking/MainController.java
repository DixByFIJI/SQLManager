/**
 * Sample Skeleton for 'FXMLDocument.fxml' Controller Class
 */

package databaseworking;

import static databaseworking.Operator.getColumnNames;
import databaseworking.connections.MySQLConnector;
import databaseworking.entities.TreeDatabaseItem;
import databaseworking.entities.TreeEntityItem;
import databaseworking.entities.TreeTableItem;
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
import java.sql.Connection;
import java.util.Arrays;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.geometry.Orientation;
import javafx.scene.control.Alert;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Control;
import javafx.scene.control.MultipleSelectionModel;
import javafx.scene.control.Separator;
import javafx.scene.control.TextArea;
import javafx.scene.control.Tooltip;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import javafx.scene.effect.BoxBlur;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.ContextMenuEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.stage.StageStyle;

//import org.apache.commons.lang.SerializationUtils;

public class MainController {
	
	HBox selectedRow;
	Pane selectedField;
	
	TreeTableItem selectedTable;
	TreeDatabaseItem selectedDatabase;
	TreeEntityItem selectedEntity;
	
	MySQLConnector mysqlconnector;
	public static Connection connection;
	
	public static Map<String, HashSet<String>> charsetsMap = new LinkedHashMap<String, HashSet<String>>(){{
		put(
			"utf8", 
			new HashSet<String>(){{
				add("utf8_general_ci");
				add("utf8_unicode_ci");
			}}
		);
		
		put(
			"cp1251", 
			new HashSet<String>(){{
				add("cp1251_general_ci");
				add("cp1251_ukrainian_ci");
			}}
		);
	}};
	
	public enum dataTypes {
		INT, VARCHAR, TEXT, DATE
	}
	
	public enum tableTypes {
		InnoDB, MyISAM, CSV, MEMORY
	}
	
	@FXML // ResourceBundle that was given to the FXMLLoader
	private ResourceBundle resources;

	@FXML // URL location of the FXML file that was given to the FXMLLoader
	private URL location;

	@FXML // fx:id="paneRoot"
	private AnchorPane paneRoot; // Value injected by FXMLLoader

	@FXML // fx:id="vboxTableConfiguration"
	private VBox vboxTableConfiguration; // Value injected by FXMLLoader

	@FXML // fx:id="scrlContainer"
	private ScrollPane scrlContainer; // Value injected by FXMLLoader

	@FXML // fx:id="vboxTable"
	private VBox vboxTable; // Value injected by FXMLLoader

	@FXML // fx:id="hboxTableConfigurationActions"
	private HBox hboxTableConfigurationActions; // Value injected by FXMLLoader

	@FXML // fx:id="imgAddRow"
	private ImageView imgAddRow; // Value injected by FXMLLoader

	@FXML // fx:id="imgRemoveRow"
	private ImageView imgRemoveRow; // Value injected by FXMLLoader

	@FXML // fx:id="imgConfirm"
	private ImageView imgConfirm; // Value injected by FXMLLoader

	@FXML // fx:id="imgRefresh"
	private ImageView imgRefresh; // Value injected by FXMLLoader

	@FXML // fx:id="hboxToolBar"
	private HBox hboxToolBar; // Value injected by FXMLLoader

	@FXML // fx:id="imgConnection"
	private ImageView imgConnection; // Value injected by FXMLLoader

	@FXML // fx:id="imgAddDatabase"
	private ImageView imgAddDatabase; // Value injected by FXMLLoader

	@FXML // fx:id="treeDatabases"
	private TreeView treeDatabases; // Value injected by FXMLLoader

	@FXML // fx:id="paneDatabaseCreating"
	private Pane paneDatabaseCreating; // Value injected by FXMLLoader

	@FXML // fx:id="paneDatabaseConfig"
	private Pane paneDatabaseConfig; // Value injected by FXMLLoader

	@FXML // fx:id="txtDatabaseName"
	private TextField txtDatabaseName; // Value injected by FXMLLoader

	@FXML // fx:id="cmbCharset"
	private ComboBox<String> cmbDatabaseCharset; // Value injected by FXMLLoader

	@FXML // fx:id="txtManualCharset"
	private TextField txtDatabaseManualCharset; // Value injected by FXMLLoader

	@FXML // fx:id="hboxDatabaseActions"
	private HBox hboxDatabaseActions; // Value injected by FXMLLoader

	@FXML // fx:id="imgCreateDatabase"
	private ImageView imgCreateDatabase; // Value injected by FXMLLoader

	@FXML // fx:id="vboxTableCreating"
	private VBox vboxTableCreating; // Value injected by FXMLLoader

	@FXML // fx:id="paneTableFieldsHeader"
	private Pane paneTableFieldsHeader; // Value injected by FXMLLoader

	@FXML // fx:id="scrlCreatingTable"
	private ScrollPane scrlCreatingTable; // Value injected by FXMLLoader

	@FXML // fx:id="vboxTableFields"
	private VBox vboxTableFields; // Value injected by FXMLLoader

	@FXML // fx:id="hboxCreatingTableMetaNameComment"
	private HBox hboxCreatingTableMetaNameComment; // Value injected by FXMLLoader

	@FXML // fx:id="txtTableName"
	private TextField txtTableName; // Value injected by FXMLLoader

	@FXML // fx:id="txtTableComment"
	private TextField txtTableComment; // Value injected by FXMLLoader

	@FXML // fx:id="hboxCreatingTableMetaCharsetType"
	private HBox hboxCreatingTableMetaCharsetType; // Value injected by FXMLLoader

	@FXML // fx:id="cmbTableCharset"
	private ComboBox<String> cmbTableCharset; // Value injected by FXMLLoader

	@FXML // fx:id="txtTableManualCharset"
	private TextField txtTableManualCharset; // Value injected by FXMLLoader

	@FXML // fx:id="cmbTableType"
	private ComboBox<String> cmbTableType; // Value injected by FXMLLoader

	@FXML // fx:id="txtTableManualType"
	private TextField txtTableManualType; // Value injected by FXMLLoader

	@FXML // fx:id="hboxTableCreatingActions"
	private HBox hboxTableCreatingActions; // Value injected by FXMLLoader

	@FXML // fx:id="imgAddField"
	private ImageView imgAddField; // Value injected by FXMLLoader

	@FXML // fx:id="imgRemoveField"
	private ImageView imgRemoveField; // Value injected by FXMLLoader

	@FXML // fx:id="imgCreateTable"
	private ImageView imgCreateTable; // Value injected by FXMLLoader

	@FXML // fx:id="paneSQLConfiguration"
	private Pane paneSQLConfiguration; // Value injected by FXMLLoader

	@FXML // fx:id="paneSQLInput"
	private Pane paneSQLInput; // Value injected by FXMLLoader

	@FXML // fx:id="imgExecuteQuery"
	private ImageView imgExecuteQuery; // Value injected by FXMLLoader

	@FXML // fx:id="txaSQLQuery"
	private TextArea txaSQLQuery; // Value injected by FXMLLoader

	@FXML // fx:id="scrlSQLResults"
	private ScrollPane scrlSQLResults; // Value injected by FXMLLoader

	@FXML // fx:id="vboxSQLResults"
	private VBox vboxSQLResults; // Value injected by FXMLLoader

	@FXML // fx:id="lblSQLStatus"
	private Label lblSQLStatus; // Value injected by FXMLLoader

	@FXML // fx:id="imgSQLConfiguration"
	private ImageView imgSQLConfiguration; // Value injected by FXMLLoader

	@FXML // This method is called by the FXMLLoader when initialization is complete
	void initialize() {
		
		Tooltip.install(imgConnection, new Tooltip("Connection settings"));
		Tooltip.install(imgAddDatabase, new Tooltip("Add database"));
		Tooltip.install(imgSQLConfiguration, new Tooltip("SQL"));
		
		Tooltip.install(imgAddRow, new Tooltip("Add row"));
		Tooltip.install(imgRemoveRow, new Tooltip("Remove selected row"));
		Tooltip.install(imgConfirm, new Tooltip("Save changes"));
		Tooltip.install(imgRefresh, new Tooltip("Refresh table"));
		
		Tooltip.install(imgAddField, new Tooltip("Add field"));
		Tooltip.install(imgRemoveField, new Tooltip("Remove field"));
		Tooltip.install(imgCreateTable, new Tooltip("Create table"));
		
		Tooltip.install(imgExecuteQuery, new Tooltip("Execute query"));
		Tooltip.install(imgCreateDatabase, new Tooltip("Create database"));
		
		ImageView imgRemoveDatabase = new ImageView("databaseworking/sources/database_remove.png");
		imgRemoveDatabase.setFitHeight(50);
		imgRemoveDatabase.setFitWidth(50);
		Tooltip.install(imgRemoveDatabase, new Tooltip("Remove database"));

		ImageView imgRemoveTable = new ImageView("databaseworking/sources/table_remove.png");
		imgRemoveTable.setFitHeight(50);
		imgRemoveTable.setFitWidth(50);
		Tooltip.install(imgRemoveTable, new Tooltip("Remove table"));
		
		ImageView imgAddTable = new ImageView("databaseworking/sources/table.png");
		imgAddTable.setFitHeight(50);
		imgAddTable.setFitWidth(50);
		Tooltip.install(imgAddTable, new Tooltip("Add table"));
		
		Platform.runLater(new Runnable() {
			@Override
			public void run() {
				openSettingsWindow();
			}
		});
		
		imgAddRow.setOnMouseClicked((event) -> {
			addNewRow(selectedTable.getName());
		});
		
		imgRemoveRow.setOnMouseClicked((event) -> {
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
			
			Operator.deleteAll(selectedTable.getName());
			Operator.insert(selectedTable.getName(), list);
			loadDataToTable(selectedTable.getName());
		});
		
		imgRefresh.setOnMouseClicked((event) -> {
			loadDataToTable(selectedTable.getName());
		});
		
		imgConnection.setOnMouseClicked((event) -> {
			openSettingsWindow();
		});
		
		treeDatabases.setOnMouseClicked((event) -> {
			Object selectedItem = treeDatabases.getSelectionModel().getSelectedItem();
			
			if(selectedItem != null) {
				if(selectedItem.getClass() == TreeDatabaseItem.class){
					selectedDatabase = ((TreeDatabaseItem)selectedItem);
					lblSQLStatus.setText("SQL query (s) to database: " + selectedDatabase.getName());
					setTablesConnection(selectedDatabase.getName());
					if(!hboxToolBar.getChildren().contains(imgRemoveDatabase)) {
						hboxToolBar.getChildren().add(3, imgRemoveDatabase);
					}
					if(!hboxToolBar.getChildren().contains(imgAddTable)) {
						hboxToolBar.getChildren().add(4, imgAddTable);
					}
					
					if(hboxToolBar.getChildren().contains(imgRemoveTable)){
						hboxToolBar.getChildren().remove(imgRemoveTable);
					}
				} else if(selectedItem.getClass() == TreeTableItem.class){
					hideAllPanes();
					vboxTableConfiguration.setVisible(true);
					selectedTable = ((TreeTableItem)selectedItem);
					lblSQLStatus.setText("SQL query (s) to table: " + selectedTable.getName());
					String selectedTableDatabase = ((TreeDatabaseItem)selectedTable.getParent()).getName();
					if(setTablesConnection(selectedTableDatabase)){
						loadDataToTable(selectedTable.getName());
						
						if(!hboxToolBar.getChildren().contains(imgRemoveTable)){
							hboxToolBar.getChildren().add(5, imgRemoveTable);
						}
						
						if(hboxToolBar.getChildren().contains(imgRemoveDatabase)){
							hboxToolBar.getChildren().remove(imgRemoveDatabase);
						}
					} else {
						Alert errorAlert = new Alert(Alert.AlertType.ERROR);
						errorAlert.setTitle("Error");
						errorAlert.setHeaderText(null);
						errorAlert.setContentText("Database connection error");
						errorAlert.showAndWait();
					}
				} else {
					List<ImageView> imgList = new LinkedList<ImageView>(){{
						add(imgAddTable);
						add(imgRemoveTable);
						add(imgRemoveDatabase);
					}};
					if(hboxToolBar.getChildren().containsAll(imgList)){
						hboxToolBar.getChildren().removeAll(imgList);
					}
					lblSQLStatus.setText("SQL query (s) on server \"MySQL\"");
				}
			}
		});
		
		imgAddDatabase.setOnMouseClicked((event) -> {
			hideAllPanes();
			txtDatabaseName.clear();
			paneDatabaseCreating.setVisible(true);
			
			for (Entry<String, HashSet<String>> entry : charsetsMap.entrySet()) {
				cmbDatabaseCharset.getItems().addAll(entry.getValue());
			}
			cmbDatabaseCharset.setValue(cmbDatabaseCharset.getItems().get(0));
		});
		
		imgCreateDatabase.setOnMouseClicked((event) -> {
			
			if(!txtDatabaseName.getText().isEmpty()){
				Operator.createDatabase(
					txtDatabaseName.getText(), 
					txtDatabaseManualCharset.getText().isEmpty() ? 
						cmbDatabaseCharset.getValue() :
						txtDatabaseManualCharset.getText()
				);
				loadDatabasesTables();
				txtDatabaseName.clear();
			}
		});
		
		imgRemoveDatabase.setOnMouseClicked((event) -> {
			Operator.removeDatabase(selectedDatabase.getName());
			loadDatabasesTables();
		});
		
		imgAddTable.setOnMouseClicked((event) -> {
			hideAllPanes();
			
			txtTableName.clear();
			txtTableManualCharset.clear();
			txtTableManualType.clear();
			txtTableComment.clear();
			
			vboxTableCreating.setVisible(true);
			cmbTableType.getItems().addAll(Arrays.stream(tableTypes.values()).map(tableTypes::name).collect(Collectors.toList()));
			cmbTableType.setValue(cmbTableType.getItems().get(0));
			
			for (Entry<String, HashSet<String>> entry : charsetsMap.entrySet()) {
				cmbTableCharset.getItems().addAll(entry.getValue());
			}
			cmbTableCharset.setValue(cmbTableCharset.getItems().get(0));
			
			vboxTableFields.getChildren().clear();
			addNewField();
		});
		
		imgRemoveTable.setOnMouseClicked((event) -> {
			Operator.removeTable(selectedTable.getName());
			loadDatabasesTables();
			hideAllPanes();
		});
		
		imgAddField.setOnMouseClicked((event) -> {
			addNewField();
		});
		
		imgCreateTable.setOnMouseClicked((event) -> {
			String name = txtTableName.getText();
			String comment = txtTableComment.getText();
			final String collate =  
				txtTableManualCharset.getText().isEmpty() ? 
				cmbTableCharset.getValue() :
				txtTableManualCharset.getText();
			final String type = 
				txtTableManualType.getText().isEmpty() ? 
				cmbTableType.getValue() :
				txtTableManualType.getText();
			
			if(!name.isEmpty()) {
				ObservableList<Node> rows = vboxTableFields.getChildren();
				List<ArrayList<String>> list = new ArrayList<ArrayList<String>>(){{
					for (Node row : rows) {
						add(new ArrayList<String>(){{
							String type = null;
							for (Node cell : ((Pane)row).getChildren()) {
								if(cell.getClass() == ComboBox.class) {
									type = ((ComboBox<String>)cell).getValue();
								} else if(cell.getClass() == TextField.class){
									TextField field = ((TextField)cell);
									if(field.getId().equals("length")) {
										add(field.getText().isEmpty() ? type : type + "(" + field.getText() + ")");
									} else {
										add(field.getText());
									}
								}	else if(cell.getClass() == CheckBox.class) {
									CheckBox item = ((CheckBox)cell);
									switch(cell.getId()) {
										case "null": {
											if(!item.isSelected()) {
												add("NOT NULL");
											}
											break;
										}

										case "primary": {
											if(item.isSelected()) {
												add("PRIMARY KEY");
											}
											break;
										}

										case "auto_inc": {
											if(item.isSelected()) {
												add("AUTO_INCREMENT");
											}
											break;
										}
									}
								}
							}
						}});
					}
				}};

				Operator.createTalbe(name, collate, type, comment, list);

				txtTableName.clear();
				txtTableManualCharset.clear();
				txtTableManualType.clear();

				loadDatabasesTables();
				hideAllPanes();
				loadDataToTable(name);
				vboxTableConfiguration.setVisible(true);
			} else {
				Alert errorAlert = new Alert(Alert.AlertType.ERROR);
				errorAlert.setTitle("Error");
				errorAlert.setHeaderText(null);
				errorAlert.setContentText("The table must have a name...");
				errorAlert.showAndWait();
			}
		});
		
		imgRemoveField.setOnMouseClicked((event) -> {
			if(selectedField != null) {
				vboxTableFields.getChildren().remove(selectedField);
			}
		});
		
		imgExecuteQuery.setOnMouseClicked((event) -> {
			Connection localConnection = null;
			Object selectedItem = treeDatabases.getSelectionModel().getSelectedItem();
			
			if(!txaSQLQuery.getText().isEmpty()) {
				if(selectedItem != null) {
					if(selectedItem.getClass() == TreeDatabaseItem.class){
						setTablesConnection(selectedDatabase.getName());
						localConnection = MainController.connection;
					} else if(selectedItem.getClass() == TreeTableItem.class){
						String selectedTableDatabase = ((TreeDatabaseItem)selectedTable.getParent()).getName();
						setTablesConnection(selectedTableDatabase);
						localConnection = MainController.connection;
					} else {
						localConnection = HostConnectingController.connection;
					}
				} else {
					localConnection = HostConnectingController.connection;
				}
				
				Executable executor = new Executable(localConnection);
				ResultSet resultSet = null;
				try {
					resultSet = executor.execute(txaSQLQuery.getText());
				} catch (SQLException ex) {
					Alert sqlAlert = new Alert(Alert.AlertType.ERROR);
					sqlAlert.setTitle("SQL Error");
					VBox alertContent = new VBox();
					Label label = new Label("Description:");
					TextArea errorDescription = new TextArea(ex.getLocalizedMessage());
					alertContent.getChildren().addAll(label, errorDescription);
					sqlAlert.getDialogPane().setContent(alertContent);
					sqlAlert.setHeaderText(null);
					sqlAlert.showAndWait();
				}
				
				List<String> columnNames = null;
				
				try {
					resultSet.next();
					columnNames = new LinkedList<String>();
					for (int i = 1; i <= resultSet.getMetaData().getColumnCount(); i++) {
						columnNames.add(resultSet.getMetaData().getColumnLabel(i));
					}
				} catch (SQLException ex) {
					ex.printStackTrace();
				}
				
				List<ArrayList<String>> data = null;
				
				try {
					data = new ArrayList<ArrayList<String>>();
					do {
						ArrayList<String> row = new ArrayList<String>();
						for (String column : columnNames) {
							row.add(resultSet.getString(column));
						}
						data.add(row);
					}
					while(resultSet.next());
				} catch (SQLException ex) {
					ex.printStackTrace();
				}
				
				System.out.println(data);
				
				int columnCount = columnNames.size();
				double cellWidth = columnCount < 3 ? 
								(scrlSQLResults.getPrefWidth() - 16 - 10 - ((columnCount - 1) * 5)) / columnCount :
								(scrlSQLResults.getPrefWidth() - 16 - 10 - ((columnCount - 1) * 5)) / 4;
				HBox header = new HBox();
				vboxSQLResults.getChildren().clear();
				for (int i = 0; i < columnNames.size(); i++) {
					Label labelCell = new Label(columnNames.get(i));
					labelCell.prefWidthProperty().bind(vboxSQLResults.widthProperty());
					labelCell.setMinWidth(cellWidth);
					labelCell.setAlignment(Pos.CENTER);
					labelCell.getStyleClass().add("labelCell");
					header.getChildren().add(labelCell);
				}
				header.setPadding(new Insets(5, 0, 0, 10));
				header.getStyleClass().add("header");
				vboxSQLResults.getChildren().add(header);

				for (ArrayList<String> rowData : data) {
					HBox row = new HBox(2);
					for (String cellString : rowData) {
						TextField cell = new TextField(cellString);
						cell.prefWidthProperty().bind(vboxTable.widthProperty());
						cell.setMinWidth(cellWidth);
						cell.setEditable(false);
						cell.getStyleClass().add("tableCell");
						row.getChildren().addAll(cell);
					}
					row.setPadding(new Insets(0, 0, 0, 10));
					row.getStyleClass().add("focusedRow");
					vboxSQLResults.getChildren().add(row);
				}
			}
		});
		
		imgSQLConfiguration.setOnMouseClicked((event) -> {
			hideAllPanes();
			vboxSQLResults.getChildren().clear();
			txaSQLQuery.clear();
			paneSQLConfiguration.setVisible(true);
		});
	}
	
	/**
	 * Loads databases and tables list to tree-view bar
	 */
	
	public void loadDatabasesTables() {
		Image serverIcon = new Image(getClass().getResourceAsStream("sources/server.png"));
		TreeEntityItem root = new TreeEntityItem(HostConnectingController.hostConnector.host, new ImageView(serverIcon));
		root.setExpanded(true);
		treeDatabases.setRoot(root);
		for (String database : Operator.getDatabases()) {
			ImageView imgDatabase = new ImageView(new Image(getClass().getResourceAsStream("sources/settings_database.png")));
			imgDatabase.setFitHeight(20);
			imgDatabase.setFitWidth(20);
			TreeDatabaseItem databaseCell = new TreeDatabaseItem(database, imgDatabase);
			setTablesConnection(database);
			for (String table : Operator.getTables(database)) {
				ImageView imgTable = new ImageView(new Image(getClass().getResourceAsStream("sources/settings_table.png")));
				imgTable.setFitHeight(20);
				imgTable.setFitWidth(20);
				TreeTableItem tableCell = new TreeTableItem(table, imgTable);
				databaseCell.getChildren().add(tableCell);
			}
			root.getChildren().add(databaseCell);
		}
	}
	
	/**
	 * Loads data from specified remote table to work-space table
	 * @param table the name of remote table from which data is loading
	 */
	
	public void loadDataToTable(String table){
		List<String> columnNames = Operator.getColumnNames(table);
		int columnCount = Operator.getColumnCount(table);
		double cellWidth = columnCount < 3 ? 
						(scrlContainer.getPrefWidth() - 16 - 10 - ((columnCount - 1) * 5)) / columnCount :
						(scrlContainer.getPrefWidth() - 16 - 10 - ((columnCount - 1) * 5)) / 4;
		HBox header = new HBox();
		vboxTable.getChildren().clear();
		for (int i = 0; i < columnNames.size(); i++) {
			Label labelCell = new Label(columnNames.get(i));
			labelCell.prefWidthProperty().bind(vboxTable.widthProperty());
			labelCell.setMinWidth(cellWidth);
			labelCell.setAlignment(Pos.CENTER);
			labelCell.getStyleClass().add("labelCell");
			header.getChildren().add(labelCell);
		}
		header.setPadding(new Insets(5, 0, 0, 10));
		header.getStyleClass().add("header");
		vboxTable.getChildren().add(header);
		
		List<ArrayList<String>> data = Operator.selectAll(table);

		for (ArrayList<String> rowData : data) {
			HBox row = new HBox(2);
			for (String cellString : rowData) {
				TextField cell = new TextField(cellString);
				cell.prefWidthProperty().bind(vboxTable.widthProperty());
				cell.setMinWidth(cellWidth);
				cell.setOnMouseClicked(rowSelecting);
				cell.getStyleClass().add("tableCell");
				row.getChildren().addAll(cell);
			}
			row.setPadding(new Insets(0, 0, 0, 10));
			row.setOnMouseClicked(rowSelecting);
			row.getStyleClass().add("focusedRow");
			vboxTable.getChildren().add(row);
		}
	}
	
	/**
	 * Adds a new row to the specified table
	 * @param table the name of the table to which new row will be added
	 */
	
	public void addNewRow(String table){
		int columnCount = Operator.getColumnCount(table);
		HBox row = new HBox(5);
		double cellWidth = columnCount < 3 ? 
			(scrlContainer.getPrefWidth() - 16 - 10 - ((columnCount - 1) * 5)) / columnCount :
			(scrlContainer.getPrefWidth() - 16 - 10 - ((columnCount - 1) * 5)) / 4;
		for (int i = 0; i < columnCount; i++) {
			TextField cell = new TextField();
			cell.prefWidthProperty().bind(vboxTable.widthProperty());
			cell.setMinWidth(cellWidth);
			cell.setOnMouseClicked(rowSelecting);
			cell.getStyleClass().add("tableCell");
			row.getChildren().addAll(cell);
		}
		row.setAlignment(Pos.TOP_LEFT);
		row.setOnMouseClicked(rowSelecting);
		row.getStyleClass().add("focusedRow");
		row.setPadding(new Insets(0, 0, 0, 10));
		vboxTable.getChildren().add(row);
	}
	
	/**
	 * Adds new field to work-space table
	 */
	
	public void addNewField(){
		Pane newField = new Pane();
		
		Pane pointer = new Pane();
		pointer.setPrefHeight(25);
		pointer.setPrefWidth(5);
		pointer.setLayoutX(0);
		pointer.setId("pointer");
		pointer.setOnMouseClicked(fieldSelecting);
		
		TextField name = new TextField();
		name.setLayoutX(10);
		name.setPrefWidth(164);
		name.setId("name");
		name.setOnMouseClicked(fieldSelecting);

		ComboBox<String> type = new ComboBox<String>();
		type.setLayoutX(191);
		type.setPrefWidth(150);
		type.getItems().addAll(Arrays.stream(dataTypes.values()).map(dataTypes::name).collect(Collectors.toList()));
		type.setId("type");
		type.setOnMouseClicked(fieldSelecting);
		
		TextField length = new TextField();
		length.setLayoutX(361);
		length.setPrefWidth(67);
		length.setId("length");
		length.setOnMouseClicked(fieldSelecting);
		
		CheckBox nullValue = new CheckBox();
		nullValue.setLayoutX(446);
		nullValue.setLayoutY(3);
		nullValue.setPrefWidth(16);
		nullValue.setId("null");
		nullValue.setOnMouseClicked(fieldSelecting);

		CheckBox primaryKey = new CheckBox();
		primaryKey.setLayoutX(514);
		primaryKey.setLayoutY(3);
		primaryKey.setPrefWidth(16);
		primaryKey.setId("primary");
		primaryKey.setOnMouseClicked(fieldSelecting);

		CheckBox autoIncrement = new CheckBox();
		autoIncrement.setLayoutX(574);
		autoIncrement.setLayoutY(3);
		autoIncrement.setPrefWidth(16);
		autoIncrement.setId("auto_inc");
		autoIncrement.setOnMouseClicked(fieldSelecting);
		
		newField.getChildren().addAll(pointer, name, type, length, nullValue, primaryKey, autoIncrement);
		newField.setMinHeight(25);
		newField.getStyleClass().add("fieldRow");
		newField.setOnMouseEntered((event) -> {
			pointer.getStyleClass().add("tableFieldPointerFocused");
		});
		newField.setOnMouseExited((event) -> {
			pointer.getStyleClass().remove("tableFieldPointerFocused");
		});
		newField.setOnMouseClicked(fieldSelecting);
		vboxTableFields.getChildren().add(newField);
	}
	
	/**
	 * Opens the connection settings window
	 */
	
	public void openSettingsWindow(){
		try {
			Stage settingsStage = new Stage();
			Parent root = FXMLLoader.load(getClass().getResource("HostConnecting.fxml"));
			Scene scene = new Scene(root);
			scene.getStylesheets().add(getClass().getResource("styles/connecting_form_style.css").toExternalForm());
			settingsStage.setScene(scene);
			settingsStage.initModality(Modality.WINDOW_MODAL);
			settingsStage.initOwner(paneRoot.getScene().getWindow());
			settingsStage.setResizable(false);
			settingsStage.setTitle("Connection");
			settingsStage.getIcons().add(new Image("databaseworking/sources/connectionIcon.png"));
			paneRoot.setEffect(new BoxBlur(7, 7, 7));
			settingsStage.showAndWait();
			SQLManager.getStage().setTitle(HostConnectingController.hostConnector.host + " - SQLManager");
			paneRoot.setEffect(null);
			hideAllPanes();
			loadDatabasesTables();
			paneSQLConfiguration.setVisible(true);
		} catch (IOException ex) {
			Logger.getLogger(HostConnectingController.class.getName()).log(Level.SEVERE, null, ex);
		}
	}
	
	/**
	 * Hides all displayed panels
	 */
	
	public void hideAllPanes(){
		vboxTableConfiguration.setVisible(false);
		paneDatabaseCreating.setVisible(false);
		vboxTableCreating.setVisible(false);
		paneSQLConfiguration.setVisible(false);
	}
	
	/**
	 * Sets connection to specified database
	 * @param database name of database to which connection will be established
	 * @return {@code true} if connection was established
	 */
	
	public boolean setTablesConnection(String database){
		boolean isConnected = false;
		mysqlconnector = new MySQLConnector()
			.setHost(HostConnectingController.hostConnector.getHost())
			.setPort(HostConnectingController.hostConnector.getPort())
			.setUser(HostConnectingController.hostConnector.getUser())
			.setPass(HostConnectingController.hostConnector.getPass())
			.setDatabaseName(database);
		
		if(mysqlconnector.connect()){
			connection = mysqlconnector.getConnection();
			isConnected = true;
		}
		return isConnected;
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
		}
	};
	
	EventHandler<MouseEvent> fieldSelecting = new EventHandler<MouseEvent>(){
		@Override
		public void handle(MouseEvent event) {
			Object source = event.getSource();
			Node pointer = null;
			if(selectedField != null){
				pointer = selectedField.getChildren().stream()
					.filter(node -> node.getId().equals("pointer"))
					.findFirst()
					.get();
				pointer.getStyleClass().remove("tableFieldPointer");
			}

			if(source.getClass() != Pane.class){
				selectedField = ((Pane)(((Node)source).getParent()));
			} else {
				selectedField = ((Pane)source);
			}
			
			pointer = selectedField.getChildren().stream()
					.filter(node -> node.getId().equals("pointer"))
					.findFirst()
					.get();
			pointer.getStyleClass().add("tableFieldPointer");
		}
	};
}
