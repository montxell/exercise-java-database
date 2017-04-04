package com.codethen.javadbexercise.util;

import com.mysql.jdbc.jdbc2.optional.MysqlDataSource;

import javax.sql.DataSource;
import java.io.IOException;
import java.sql.*;
import java.util.Properties;

public class DatabaseUtil {

    public static Connection getConnection() throws Exception {

        Properties props = loadConnectionProperties();

        String host = props.getProperty("host");
        String schema = props.getProperty("schema");
        String user = props.getProperty("user");
        String pwd = props.getProperty("password");

        Connection conn = DriverManager.getConnection(
                "jdbc:mysql://" + host + "/" + schema + "?user=" + user + "&password=" + pwd);


        return conn;
    }


    public static DataSource getDataSource() throws Exception {

        Properties props = loadConnectionProperties();

        String host = props.getProperty("host");
        String schema = props.getProperty("schema");
        String user = props.getProperty("user");
        String pwd = props.getProperty("password");

        MysqlDataSource dataSource = new MysqlDataSource();
        dataSource.setURL("jdbc:mysql://" + host + "/" + schema);
        dataSource.setUser(user);
        dataSource.setPassword(pwd);

        return dataSource;
    }



    /** Loads connection properties from a file in the "resources" folder */
    private static Properties loadConnectionProperties() throws IOException {
        Properties props = new Properties();
        props.load(ClassLoader.getSystemResourceAsStream("database.properties"));
        return props;
    }



    /** Closes the resources if they are not null */
    public static void close(ResultSet rs, PreparedStatement stmt, Connection conn) {
        try {
            if (rs != null) {
                rs.close();
            }
            if (stmt != null) {
                stmt.close();
            }
            if (conn != null) {
                conn.close();
            }
        } catch (Exception e) {
            e.printStackTrace(); // will print the exception stack trace
        }
    }

}
