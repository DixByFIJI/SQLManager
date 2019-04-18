/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package databaseworking.requests;

import databaseworking.connections.MySQLConnector;
import databaseworking.HostConnectingController;
import databaseworking.MainController;
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
public class Executable {
	
	private Connection connection;

	public Executable(Connection connection) {
		this.connection = connection;
	}
	
	/**
	 * Executes the specified query on the established connection
	 * @param query value which will be executed
	 * @return ResultSet reference that is result of the query executing
	 */
	
	public ResultSet execute(String query) throws SQLException{
//		ResultSet resultSet = null;
//		PreparedStatement preparedStatement;
		PreparedStatement preparedStatement = connection.prepareStatement(query);
		preparedStatement.execute();
		ResultSet resultSet = preparedStatement.getResultSet();
		return resultSet;
	}
	
	/**
	 * Executes the specified query with some prepared parameters on the established connection
	 * @param query value which will be executed
	 * @param values prepared parameters for executing
	 * @return ResultSet reference that is result of the query executing
	 */
	
	public ResultSet execute(String query, String ... values){
		ResultSet resultSet = null;

		
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
	
	public void setConnection(Connection connection) {
		this.connection = connection;
	}
}
