/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package databaseworking.connections;

import com.mysql.cj.jdbc.MysqlDataSource;
import com.mysql.cj.xdevapi.Result;
import java.sql.Connection;
import java.sql.SQLException;
import databaseworking.requests.Executable;
import java.sql.ResultSet;
import java.sql.SQLSyntaxErrorException;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.FutureTask;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Username
 */
public class MySQLConnector {
	private String user = "root";	
	private String pass = "qwe123";	
	private String host = "127.0.0.1";	
	private int port = 3306;	
	private String databaseName = "db_working";
	private String tableName = "readers";
	private String charset = "cp1251";
	private String timeZone = "UTC";
	
	private Connection connection;

	/**
	 * Make connection to remote MySQL database
	 * @return @code true if connection is successful;
	 */
	public void setTimeZone(String timeZone) {
		this.timeZone = timeZone;
	}

	public boolean connect() {
		boolean isConncted = true;
		FutureTask<Boolean> task = new FutureTask<>(new Callable<Boolean>(){
			@Override
			public Boolean call() throws Exception {
				MysqlDataSource mysqlDataSource = new MysqlDataSource();
		
				mysqlDataSource.setUser(user);
				mysqlDataSource.setPassword(pass);
				mysqlDataSource.setDatabaseName(databaseName);
				mysqlDataSource.setPort(port);
				try {
					mysqlDataSource.setServerTimezone(timeZone);
					mysqlDataSource.setCharacterEncoding(charset);

					connection = mysqlDataSource.getConnection();
				} catch (SQLSyntaxErrorException ex){
					return false;
				} catch (SQLException sqlEx) {
					return false;
				}
				return true;
			}
		});
		
		Thread connectThread = new Thread(task);
			connectThread.start();
			try {
				isConncted = task.get();
			} catch (InterruptedException | ExecutionException ex) {
				ex.printStackTrace();
			}
		return isConncted;
	}
	
	public MySQLConnector setUser(String user) {
		this.user = user;
		return this;
	}

	public MySQLConnector setPass(String pass) {
		this.pass = pass;
		return this;
	}

	public MySQLConnector setHost(String host) {
		this.host = host;
		return this;
	}

	public MySQLConnector setPort(int port) {
		this.port = port;
		return this;
	}

	public MySQLConnector setDatabaseName(String databaseName) {
		this.databaseName = databaseName;
		return this;
	}
	
	public MySQLConnector setTableName(String tableName) {
		this.tableName = tableName;
		return this;
	}

	public MySQLConnector setCharset(String charset) {
		this.charset = charset;
		return this;
	}

	public String getUser() {
		return user;
	}

	public String getPass() {
		return pass;
	}

	public String getHost() {
		return host;
	}

	public int getPort() {
		return port;
	}

	public String getDatabaseName() {
		return databaseName;
	}

	public String getTableName() {
		return tableName;
	}

	public String getCharset() {
		return charset;
	}

	public String getTimeZone() {
		return timeZone;
	}
	
	public Connection getConnection(){
		return connection;
	}
}
