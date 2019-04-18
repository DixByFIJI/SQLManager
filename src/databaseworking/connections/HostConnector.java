/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package databaseworking.connections;

import com.mysql.cj.jdbc.MysqlDataSource;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.SQLSyntaxErrorException;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.FutureTask;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Username
 */
public class HostConnector {
	public String user;	
	private String pass;	
	public String host;	
	private int port;
	private final String config = "?serverTimezone=UTC&useSSL=false&characterEncoding=UTF8";	

	private Connection connection;
	
	public boolean connect() {
		boolean isConncted = false;
		FutureTask<Boolean> task = new FutureTask<>(new Callable<Boolean>(){
			@Override
			public Boolean call() throws Exception {
				String url = "jdbc:mysql://" + host + ":" + port + config;
				connection = DriverManager.getConnection(url, user, pass);
				return connection != null;
			}
		});
		
		Thread connectThread = new Thread(task);
			connectThread.start();
			try {
				isConncted = task.get(500, TimeUnit.MILLISECONDS);
			} catch (InterruptedException | ExecutionException ex) {
				ex.printStackTrace();
			} catch (TimeoutException ex) {
			Logger.getLogger(HostConnector.class.getName()).log(Level.SEVERE, null, ex);
		}
		return isConncted;
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
	
	public HostConnector setUser(String user) {
		this.user = user;
		return this;
	}

	public HostConnector setPass(String pass) {
		this.pass = pass;
		return this;
	}

	public HostConnector setHost(String host) {
		this.host = host;
		return this;
	}

	public HostConnector setPort(int port) {
		this.port = port;
		return this;
	}
	
	public Connection getConnection(){
		return connection;
	}
}
