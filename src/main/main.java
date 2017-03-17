package main;

public class main {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		JDBCclass test = new JDBCclass("jdbc", "192.168.56.2", Constants.JDBC_DEFAULT_PORT);
		test.loadDataIntoHDFS("/user/visDiscovery/temp.parq/", "foo", true);

	}

}
