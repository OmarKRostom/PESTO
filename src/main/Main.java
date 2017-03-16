package main;

import java.sql.SQLException;
import java.sql.PreparedStatement;
import java.util.ArrayList;
import java.util.LinkedHashMap;

public class Main {

	public static void main(String[] args) {
		JDBCclass test = new JDBCclass("jdbc", "192.168.56.2", Constants.JDBC_DEFAULT_PORT);
	}

}
