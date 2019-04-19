/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package databaseworking;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import databaseworking.requests.Executable;
import java.sql.PreparedStatement;
import java.util.HashSet;
import java.util.Map;
import java.util.Map.Entry;

/**
 *
 * @author Username
 */
public abstract class Operator {
	
	/**
	 * Creates a database on the server by name and collate
	 * @param name the name of database which will be created
	 * @param collate collate of created database
	 */
	
	public static void createDatabase(String name, String collate){
		String charset = null;
		Executable executor = new Executable(HostConnectingController.connection);
		
		for (Map.Entry<String, HashSet<String>> entry : MainController.charsetsMap.entrySet()) {
			if(entry.getValue().contains(collate)){
				charset = entry.getKey();
			}
		}
		
		final String addQuery = "CREATE DATABASE " + name + ";";
		final String charsetQuery = "ALTER DATABASE " + name + " CHARACTER SET " + charset + " COLLATE " + collate + ";";
		
		try {
			executor.execute(addQuery);
			executor.execute(charsetQuery);
		} catch (SQLException ex) {
			Logger.getLogger(Operator.class.getName()).log(Level.SEVERE, null, ex);
		}
	}
	
	/**
	 * Removes specified database from server by name
	 * @param name name of removed database
	 */
	
	public static void removeDatabase(String name){
		final String query = "DROP DATABASE " + name + ";";
		Executable executor = new Executable(HostConnectingController.connection);
		
		try {
			executor.execute(query);
		} catch (SQLException ex) {
			Logger.getLogger(Operator.class.getName()).log(Level.SEVERE, null, ex);
		}
	}
	
	/**
	 * Gets list of exists database from server
	 * @return list of database names
	 */
	
	public static List<String> getDatabases(){
		List<String> list = null;
		final String query = "SHOW DATABASES;";
		ResultSet resultSet;
		Executable executor = new Executable(HostConnectingController.connection);
		try {
			resultSet = executor.execute(query);
			
			list = new LinkedList<String>(){{
				while (resultSet.next()) {					
					add(resultSet.getString(1));
				}
			}};
		} catch (SQLException ex) {
			Logger.getLogger(Executable.class.getName()).log(Level.SEVERE, null, ex);
		}
		return list;
	}
	
	/**
	 * Creates table in current database on server
	 * @param name name of created table
	 * @param collate collate of created table
	 * @param type type of created table
	 * @param comment comment for created table
	 * @param fields list of fields to be added to created table
	 */
	
	public static void createTalbe(String name, String collate, String type, String comment, List<ArrayList<String>> fields){
		String primaryKey = null;
		String charset = null;
		
		for (ArrayList<String> rowFields : fields) {
			if(rowFields.contains("PRIMARY KEY")) {
				primaryKey = "PRIMARY KEY(" + rowFields.get(0) + ")";
				rowFields.remove("PRIMARY KEY");
			}
		}
		
		for (Map.Entry<String, HashSet<String>> entry : MainController.charsetsMap.entrySet()) {
			if(entry.getValue().contains(collate)){
				charset = entry.getKey();
			}
		}
		
		final String query = "CREATE TABLE " + name + "("
			.concat(String.join(", ", 
				fields
					.stream()
					.map(field -> String.join(" ", 
						field
							.stream()
							.toArray(String[]::new)))
					.toArray(String[]::new)))
			.concat(primaryKey == null ? "" : ", " + primaryKey + ") ENGINE " + type + " ")
			.concat(collate.isEmpty() ? "" : "CHARSET " + charset + " COLLATE " + collate + " ")
			.concat(comment.isEmpty() ? "" : "COMMENT '" + comment + "'")
			.concat(";"); 
		Executable executor = new Executable(MainController.connection);
		try {
			executor.execute(query);
		} catch (SQLException ex) {
			Logger.getLogger(Operator.class.getName()).log(Level.SEVERE, null, ex);
		}
	}
	
	/**
	 * Removes specified table from current database by name
	 * @param name name of removed table
	 */
	
	public static void removeTable(String name){
		final String query = "DROP TABLE " + name + ";";
		Executable executor = new Executable(MainController.connection);
		
		try {
			executor.execute(query);
		} catch (SQLException ex) {
			Logger.getLogger(Operator.class.getName()).log(Level.SEVERE, null, ex);
		}
	}
	
	/**
	 * Gets list of tables from specified database
	 * @param database name of database from which tables will be obtained
	 * @return list of table names
	 */
	
	public static List<String> getTables(String database){
		List<String> list = null;
		final String query = "SHOW TABLES FROM " + database + ";";
		Executable executor = new Executable(HostConnectingController.connection);
		
		try {
			ResultSet resultSet = executor.execute(query);
			list = new LinkedList<String>(){{
				while (resultSet.next()) {				
					add(resultSet.getString(1));
				}
			}};
		} catch (SQLException ex) {
			Logger.getLogger(Operator.class.getName()).log(Level.SEVERE, null, ex);
		}
		return list;
	}
	
	/**
	 * Gets list of column names from specified table
	 * @param table name of table from which column names will be obtained
	 * @return list of column names
	 */
	
	public static List<String> getColumnNames(String table){
		final String query = "SELECT * FROM " + table;
		List<String> list = null;
		
		Executable executor = new Executable(MainController.connection);
		try {
			ResultSet resultSet = executor.execute(query);
			resultSet.next();
			list = new LinkedList<String>(){{
				for (int i = 1; i <= resultSet.getMetaData().getColumnCount(); i++) {
					add(resultSet.getMetaData().getColumnLabel(i));
				}
			}};
		} catch (SQLException ex) {
			ex.printStackTrace();
		}
		return list;
	}
	
	/**
	 * Gets count of columns of specified table
	 * @param table
	 * @return count of columns
	 */
	
	public static int getColumnCount(String table){
		final String query = "SELECT * FROM " + table;
		int columnCount = 0;
		
		Executable executor = new Executable(MainController.connection);
		try {
			ResultSet resultSet = executor.execute(query);
			resultSet.next();
			columnCount = resultSet.getMetaData().getColumnCount();
		} catch (SQLException ex) {
			ex.printStackTrace();
		}
		return columnCount;
	}
	
	/**
	 * Inserts data into specified table
	 * @param table name of table into which data will be inserted
	 * @param data list of rows of data
	 */
	
	public static void insert(String table, List<ArrayList<String>> data){
		List<String> columnNames = getColumnNames(table);
		final String query = "INSERT INTO " + table + "(`"
			+ String.join("`, `", columnNames)
			+ "`) VALUES("
			+ String.join(", ", columnNames.stream().map(x -> "?").toArray(String[]::new)) + ");";
		
		Executable executor = new Executable(MainController.connection);
		
		try {
			for (ArrayList<String> rowData : data) {
				executor.execute(query, rowData.stream().toArray(String[]::new));
			}
		} catch (SQLException ex) {
			ex.printStackTrace();
		}
	}
	
	/**
	 * Gets data from specified table
	 * @param table name of table from which data will be obtained
	 * @return list of rows of data
	 */
	
	public static List<ArrayList<String>> selectAll(String table){
		String query = "SELECT * FROM " + table + ";";
		List<ArrayList<String>> list = null;
		final List<String> columnNames = getColumnNames(table);
		
		Executable executor = new Executable(MainController.connection);
		try {
			list = new ArrayList<ArrayList<String>>(){{
				ResultSet resultSet = executor.execute(query);
				while(resultSet.next()){
					add(new ArrayList<String>(){{
						for (String column : columnNames) {
							add(resultSet.getString(column));
						}
					}});
				}
			}};
		} catch (SQLException ex) {
			ex.printStackTrace();
		}
		return list;
	}
	
	/**
	 * Removes all records from specified table
	 * @param table name of table which will be truncated
	 */
	
	public static void deleteAll(String table){
		final String query = "TRUNCATE TABLE " + table + ";";
		
		Executable executor = new Executable(MainController.connection);
		try {
			executor.execute(query);
		} catch (SQLException ex) {
			Logger.getLogger(Operator.class.getName()).log(Level.SEVERE, null, ex);
		}
	}
}
