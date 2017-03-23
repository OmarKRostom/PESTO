package main;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

public class JDBCconnector {
	public String ip_address;
	public String port_number;
	private static JDBCconnector instance = null;
	private String driver;
	public Connection conn = null;
	
	protected JDBCconnector(String driver, String ip_address, String port_number) {
		this.ip_address = ip_address;
		this.port_number = port_number;
		this.driver = driver;

        //SETTING DRIVER TO JDBC;
        try {
        	if(driver.equalsIgnoreCase("jdbc")) {
                Class.forName(Constants.JDBC_DRIVER);
        	} else {
        		//LOAD HVIE DRIVER
        	}
        	
            this.conn = DriverManager.getConnection(Constants.JDBC_URL + this.ip_address + ":" + this.port_number + ";" + Constants.NO_AUTH);
        } catch (ClassNotFoundException e) {
            System.out.println("JDBC DRIVER NOT FOUND");
        } catch (SQLException ex) {
            System.out.println("AN ERROR OCCURED WHILE CONNECTING TO IMPALA");
        } finally {
        	try {
				this.conn.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				System.out.println("ERROR CLOSING CONNECTION");
				e.printStackTrace();
			}
        }
	}
	
	public static JDBCconnector getInstance(String driver, String ip_address, String port_number) {
		if(instance == null) {
			instance = new JDBCconnector(driver, ip_address, port_number);
		}
		return instance;
	}
	
	public Connection getConnectionObject() {
		return this.conn;
	}
}
