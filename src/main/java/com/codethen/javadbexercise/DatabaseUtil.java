package com.codethen.javadbexercise;

import java.sql.*;

public class DatabaseUtil {

    public static Connection getConnection() throws SQLException {

        String host = "localhost";
        String schema = "test";
        String user = "root";
        String pwd = "moncode83";

        Connection conn = DriverManager.getConnection(
                "jdbc:mysql://" + host + "/" + schema + "?user=" + user + "&password=" + pwd);


        return conn;
    }


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
