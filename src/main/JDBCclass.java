package main;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;	
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.sql.Connection;

public class JDBCclass implements JDBCInterface {
	/**
	 * @author omarkrostom
	 */
	public String ip_address;
	public String port_number;
	public String driver;
	public Connection conn = null;
	public Statement stmt = null;
	public PreparedStatement preparedStmt = null;
	public ResultSet results;
	
	/**
	 * @param driver : JDBC or HIVE
	 * @param ip_address : IP Address of the impala host
	 * @param port_number : Port Number, default is 21050
	 */
	public JDBCclass(String driver, String ip_address, String port_number) {
		this.ip_address = ip_address;
		this.port_number = port_number;
		this.driver = driver;	
		
		conn = JDBCconnector.getInstance(driver, ip_address, port_number).getConnectionObject();
	}

	/**
	 * This method for creating tables in impala, passing arguments as follows :
	 * @return boolean
	 */
	@Override
	public boolean createTable(String table_name, LinkedHashMap<String, String> table_data, String store_type) {
		// TODO Auto-generated method stub
		String queryStr = "";
		String paramsStr = "";
		Iterator it = table_data.entrySet().iterator();
	    while (it.hasNext()) {
	        Map.Entry pair = (Map.Entry)it.next();
	        paramsStr += pair.getKey().toString() + " " + pair.getValue().toString();
	        if (table_data.size()>1) {
	        	paramsStr += ", ";
	        }
	        it.remove();
	    }
	    queryStr = "CREATE TABLE IF NOT EXISTS " + table_name + " (" + paramsStr + ") STORED AS " + store_type + ";";
	    try {
			stmt = conn.createStatement();
			stmt.execute(queryStr);
			return true;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			System.out.println(e.getMessage());
		}
		return false;
	}

	/**
	 * This method for dropping table from impala, takes only one argument, the table name.
	 * @return boolean
	 */
	@Override
	public boolean dropTableFromImpala(String table_name) {
		// TODO Auto-generated method stub
		String queryStr = "DROP TABLE " + table_name + ";";
		try	{
			stmt = conn.createStatement();
			stmt.execute(queryStr);
			return true;
		} catch (SQLException e) {
			System.out.println(e.getMessage());
		}
		return false;
	}

	/**
	 * This method for dropping database from impala, takes only one argument, the database name.
	 * @return boolean
	 */
	@Override
	public boolean dropDatabaseFromImpala(String database_name) {
		// TODO Auto-generated method stub
		String queryStr = "DROP DATABASE " + database_name + ";";
		try	{
			stmt = conn.createStatement();
			stmt.execute(queryStr);
			return true;
		} catch (SQLException e) {
			System.out.println(e.getMessage());
		}
		return false;
	}

	/**
	 * This methods selects from a table in impala, based on the given query, if any of the offered parameters are not used, just make them null.
	 * @return Method returns a ResultSet Object of the selected query.
	 */
	@Override
	public ResultSet selectFromTable(String table_name, String[] select_params, 
			String case_param, LinkedHashMap case_when, String else_param, String case_flag,
			String where_params, String[] order_params, String[] group_params) {
		// TODO Auto-generated method stub
		String queryStr = "SELECT ";
		String select_paramsStr = Arrays.toString(select_params).replaceAll(" |\\[|\\]", "") + "\n";
		queryStr += select_paramsStr;
		if(case_when!=null) {
			queryStr += ",case " + case_param + "\n";
			Iterator it = case_when.entrySet().iterator();
		    while (it.hasNext()) {
		        Map.Entry pair = (Map.Entry)it.next();
		        queryStr += "when '" + pair.getKey().toString() + "' then '" + pair.getValue().toString() +"'\n";
		        it.remove();
		    }
		    queryStr += "else '" + else_param + "'\n";
		    queryStr += "end";
		    if(case_flag!=null) {
		    	queryStr += " as " + case_flag + "\n";
		    }
		}
	    queryStr += "from " + table_name + "\n";
	    if(where_params != null) {
	    	queryStr += where_params + " \n";
	    }
	    if(order_params != null) {
	    	queryStr += " ORDER BY ";
	    	queryStr += Arrays.toString(order_params).replaceAll(" |\\[|\\]", "") + "\n";
	    }
	    if(group_params != null) {
	    	queryStr += "GROUP BY ";
	    	queryStr += Arrays.toString(group_params).replaceAll(" |\\[|\\]", "");
	    }
	    queryStr += ";";
	    System.out.println(queryStr);
		try {
			this.stmt = conn.createStatement();
			this.results = stmt.executeQuery(queryStr);
			ResultSetMetaData rsmd = this.results.getMetaData();
			int columnsNumber = rsmd.getColumnCount();
			while (this.results.next()) {
				for (int i = 1; i <= columnsNumber; i++) {
		           if (i > 1) System.out.print(",  ");
		           String columnValue = this.results.getString(i);
		           System.out.print(columnValue + " " + rsmd.getColumnName(i));
		       }
			   System.out.println("");
	       }
		} catch (SQLException e) {
			System.out.println("An error occured while executing query :" + e.getMessage());
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * This method returns a preparedStatement based on your connection for you insert query,
	 * this features a strong anti SQL Injection, by just writing ? instead of your values,
	 * then adding them using setString, setArray, setInt ... etc, add mapping the index of
	 * each of these with the correct position.
	 * @return This method returns a PreparedStatement, further information read this : https://goo.gl/RjpIHM
	 */
	@Override
	public PreparedStatement getPreparedStmtForInsert(String table_name, int no_of_params, int no_of_rows) {
		// TODO Auto-generated method stub
		String queryStr = "INSERT INTO " + table_name + " VALUES ";
		for(int i=0; i<no_of_rows ; i++) {
			queryStr += "( ";
			for(int j=0; j<no_of_params; j++) {
				queryStr += "?";
				if(j<(no_of_params)-1) {
					queryStr += ", ";
				}
			}
			queryStr += " )";
			if(i<no_of_rows-1) {
				queryStr += ", ";
			}
		}
		try {
			this.preparedStmt = this.conn.prepareStatement(queryStr);
			return this.preparedStmt;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			System.out.println("PreparedStatement could not be executed successfully, returning null !");
		}
		return null;
	}

	/**
	 * This method returns a preparedStatement based on your connection for you custom query,
	 * this features a strong anti SQL Injection, by just writing ? instead of your values,
	 * then adding them using setString, setArray, setInt ... etc, add mapping the index of
	 * each of these with the correct position.
	 * @return PreparedStatement
	 */
	@Override
	public PreparedStatement getPreparedStmtForCustomQuery(String query) {
		// TODO Auto-generated method stub
		try {
			this.preparedStmt = this.conn.prepareStatement(query);
			return this.preparedStmt;
		} catch (SQLException e) {
			System.out.println("Error casting into the prepared statement !" + e.getMessage());
		}
		return null;
	}

	/**
	 * This method for loading data into impala, one of the most brilliant methods in impala,
	 * is to load avro or parquet file directly into impala machine, you only need
	 * the file path and the table name for this, and whether you will overwrite existing data or not.
	 * @return boolean
	 */
	@Override
	public boolean loadDataIntoHDFS(String hdfs_directory, String table_name, boolean overwrite) {
		// TODO Auto-generated method stub
		String queryStr = "LOAD DATA INPATH '" + hdfs_directory + "' ";
		if(overwrite) {
			queryStr += " OVERWRITE ";
		}
		queryStr += " INTO TABLE " + table_name + " ;";
		try {
			PreparedStatement prepStmt = conn.prepareStatement(queryStr);
			prepStmt.execute();
			return true;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			System.out.println("An error occured while executing sql : " + e.getMessage());
			e.printStackTrace();
		}
		return false;
	}
	
}
