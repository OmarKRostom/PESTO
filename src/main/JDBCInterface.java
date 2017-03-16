package main;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.LinkedHashMap;

public interface JDBCInterface {
	public boolean createTable(String table_name, LinkedHashMap<String, String> table_data, String store_type);
    public boolean dropTableFromImpala(String table_name);
    public boolean dropDatabaseFromImpala(String database_name);
    public PreparedStatement getPreparedStmtForInsert(String table_name, int no_of_params, int no_of_rows);
    public ResultSet selectFromTable(String table_name, String[] select_params, String case_param, LinkedHashMap case_when, String else_param, String case_flag, String where_params, String[] order_params, String[] group_params);
    public PreparedStatement getPreparedStmtForCustomQuery(String query);
    public boolean executeprepStmt(PreparedStatement prepStmt);
}
