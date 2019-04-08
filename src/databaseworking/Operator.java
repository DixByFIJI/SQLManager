/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package databaseworking;

import actors.Reader;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.print.attribute.PrintRequestAttributeSet;
import databaseworking.requests.Executable;

/**
 *
 * @author Username
 */
public abstract class Operator {
	public static List<String> getColumnNames(){
		String query = "SELECT * FROM " + ConnectingController.mysqlconnector.getTableName();
		List<String> list = null;
		ResultSet resultSet = Executable.execute(query);
		try {
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
	
	public static int getColumnCount(){
		String query = "SELECT * FROM " + ConnectingController.mysqlconnector.getTableName();
		int columnCount = 0;
		ResultSet resultSet = Executable.execute(query);
		try {
			resultSet.next();
			columnCount = resultSet.getMetaData().getColumnCount();
		} catch (SQLException ex) {
			ex.printStackTrace();
		}
		return columnCount;
	}
	
	public static void insert(List<ArrayList<String>> data){
		String query = "INSERT INTO " + ConnectingController.mysqlconnector.getTableName() + "(`"
			+ String.join("`, `", getColumnNames())
			+ "`) VALUES("
			+ String.join(", ", getColumnNames().stream().map(x -> "?").toArray(String[]::new)) + ");";
		
		for (ArrayList<String> rowData : data) {
			Executable.execute(query, rowData.stream().toArray(String[]::new));
		}
	}
	
	public static List<ArrayList<String>> selectAll(){
		String query = "SELECT * FROM " + ConnectingController.mysqlconnector.getTableName() + ";";
		List<ArrayList<String>> list = null;
		final List<String> columnNames = getColumnNames();
		ResultSet resultSet = Executable.execute(query);
		try {
			list = new ArrayList<ArrayList<String>>(){{
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
	
//	public static void delete(Reader person){
//		String query = "DELETE FROM " + ConnectingController.mysqlconnector.getTableName() + " WHERE `" + READER_ID + "` = ?;";
//		Executable.execute(query, String.valueOf(person.getId()));
//	}
//	
//	public static void delete(List<Reader> list){
//		list.forEach((temporary) -> {
//			String query = "DELETE FROM " + ConnectingController.mysqlconnector.getTableName() + " WHERE `" + READER_ID + "` = ?;";
//			Executable.execute(query, String.valueOf(temporary.getId()));
//		});
//	}
	
	public static void deleteAll(){
		String query = "TRUNCATE TABLE " + ConnectingController.mysqlconnector.getTableName() + ";";
		Executable.execute(query);
	}
}
