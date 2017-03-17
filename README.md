<h1 align="center">PESTO</h1>
<h6 align="center">A query builder for cloudera impala-shell</h6>
<h6 align="center">Project in progress, and might contain issues</h6>

# Description
This is a tool, intended as a first place to be placed into a java web server application, a tool to be released Q2 
by a group of students of Alexandria University, Faculty of Engineering, Computer Department, aims to recommend visualizations based on data stored on a distributed cluster managed by Cloudera Impala (CDH).

The tool is quite handy as it makes connecting to a impala server as easy as writing one single line to connect to impala server, helping you out of the nights of pain trying different methodologies to connect to an impala server, diving through the internet for proper jar files and the methods of connecting, here we help you to easily connect and crash into impala.

# Instructions

1. First you need to download the jar files from https://goo.gl/gKqM10 don't bother yourself with the operating system requirements, choose any, enter your name and stuff, and download the zip.

2. After download and extracting you will find files with pdf documentations, don't bother yourself, extract the zip file with **Cloudera_ImpalaJDBC4_X.XX.XX** as we currently support these drivers only.

3. Import the jar files included into your project, **this is nessacry for the project to work** by clicking the following if using eclipse :
```
RightClick on project name > Properties > Java Build Path > Add External Jars > Select CDH Jars > Ok
```

4. Import the jar file included From the project repo, in **dist** folder, the same way as the CDH jars.

5. Now you are good to go, next is methods break down, and how could you use them.

# Methods Included

- To create the java object, all you have to do is :
```java
JDBCclass jdbcObj = new JDBCclass(DRIVER_NAME, SERVER_NAME, Constants.JDBC_DEFAULT_PORT);
```
- Replace DRIVER_NAME with "jdbc" or "hive" note that we currently support jdbc at this stage, hive to be supported sooner than later, replace SERVER_NAME with your IP Address of the machine, or the name server if it has one. It is strongly advised not to change the Impala Default Port, but if you have changed it, the change Constants.JDBC_DEFAULT_PORT into your changed port.

- Exploring jdbcObj you would find the following :

| Method        | Parameters           | Return  | Description |
| ------------- |:-------------:| :-----:| :-------:|
| createTable      | (String table_name, LinkedHashMap<String, String> table_data, String store_type) | boolean | Method to create a table from the given parameters, table_data takes in each coloumn name and type (KEY is TYPE, VALUE is NAME) |
| dropTableFromImpala      | (String table_name) | boolean | Method to drop a table from impala |
| dropDatabaseFromImpala | (String database_name) | boolean | Method to drop a database from impala |
| getPreparedStmtForInsert | (String table_name, int no_of_params, int no_of_rows) | PreparedStatement | Method to return a prepared statement, with ? for sql injection already in right places, all you have to do for example if you have to insert 5 rows, and each row has 3 parameters, then you would set no_of_params 5 and no_of_rows 3 and let the magic works |
| selectFromTable | (String table_name, String[] select_params, String case_param, LinkedHashMap case_when, String else_param, String case_flag, String where_params, String[] order_params, String[] group_params) | ResultSet | Method for select, table_name takes in table name, select_params takes in what would you select * for all, if you would like to add a case when statement, set the case_param to whatever you would like to case, set the values of hashmap case_when as When'x' then 'y' as (KEY is X, VALUE is Y), set the else_param, set the case_flag if you would like to assign an alias for your result, else you would assign it as null, enter the where as a string, and the order by params as well as the group by params | 
| getPreparedStmtForCustomQuery | (String query) | PreparedStatement | This method returns a prepared statement object for your custom query, you would insert the string with ? and then the variables would be set via java built in methods, setString, setInt ... etc as your table is set |
| loadDataIntoHDFS | (String hdfs_directory, String table_name, boolean overwrite) | boolean | Method to load a parquet or avro file saved in an hdfs directory, into your desired table, with the ability to overwrite existing data with setting overwrite boolean true |

# Credits 
All credits go to cloudera impala for providing an excellent community as well as a perfect documentation for cloudera impala.

# Links
[![alt text][1.1]][1]
[![alt text][1.2]][2]
[![alt text][1.3]][3]


[1.1]: http://i.imgur.com/P3YfQoD.png
[1.2]: http://i.imgur.com/tXSoThF.png
[1.3]: http://i.imgur.com/yCsTjba.png

[1]: https://www.facebook.com/Omar.K.Rostom
[2]: https://twitter.com/OmarKRostom
[3]: https://plus.google.com/u/0/115355957661112695248


# Email
<a href="mailto:o.rostom.93@gmail.com" target="_top">Send Mail (o.rostom.93@gmail.com)</a>

