package com.codethen.javadbexercise;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseUtil {

    public static Connection getConnection() throws SQLException {

        String host = "localhost";
        String schema = "test";
        String user = "root";
        String pwd = "monsql";

        Connection conn = DriverManager.getConnection(
                "jdbc:mysql://" + host + "/" + schema + "?user=" + user + "&password=" + pwd);


        return conn;
    }

}
