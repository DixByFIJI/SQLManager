/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package databaseworking.requests;

import databaseworking.connections.MySQLConnector;
import databaseworking.ConnectingController;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Username
 */
public abstract class Executable {
	
	public static ResultSet execute(String query){
		ResultSet resultSet = null;
		Connection connection = ConnectingController.mysqlconnector.getConnection();
		
		PreparedStatement preparedStatement;
		try {
			preparedStatement = connection.prepareStatement(query);
			preparedStatement.execute();
			resultSet = preparedStatement.getResultSet();
		} catch (SQLException ex) {
			Logger.getLogger(Executable.class.getName()).log(Level.SEVERE, null, ex);
		}
		return resultSet;
	}
	
	public static ResultSet execute(String query, String ... values){
		ResultSet resultSet = null;
		Connection connection = ConnectingController.mysqlconnector.getConnection();
		
		PreparedStatement preparedStatement;
		try {
			preparedStatement = connection.prepareStatement(query);
			for (int i = 0; i < values.length; i++) {
				preparedStatement.setString(i + 1, values[i]);
			}
			preparedStatement.execute();
			resultSet = preparedStatement.getResultSet();
		} catch (SQLException ex) {
			Logger.getLogger(Executable.class.getName()).log(Level.SEVERE, null, ex);
		}
		
		return resultSet;
	}
}
