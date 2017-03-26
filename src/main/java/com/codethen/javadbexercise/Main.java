package com.codethen.javadbexercise;

import java.sql.*;


public class Main {

    public static void main(String[] args) throws Exception {

        Class.forName("com.mysql.jdbc.Driver").newInstance();

        String host = "localhost";
        String schema = "test";
        String user = "root";
        String pwd = "moncode83";

        Connection conn = DriverManager.getConnection(
                "jdbc:mysql://" + host + "/" + schema + "?user=" + user + "&password=" + pwd);



        String userToFind = "montse";

        // danger of sql-injection
        //Statement stmt = conn.createStatement();
        //ResultSet rs = stmt.executeQuery("select * from users where username = '" + userToFind + "'");


        // prepared statements can't be attacked with the sql-injection
        PreparedStatement stmt = conn.prepareStatement("select * from users where username = ?");
        stmt.setString(1, userToFind);
        ResultSet rs = stmt.executeQuery();


        while (rs.next()) {

            int id = rs.getInt("id");
            String username = rs.getString("username");
            String name = rs.getString("name");
            String email = rs.getString("email");

            System.out.println(id + ", " + username + ", " + name + ", " + email);
        }

        rs.close();
        stmt.close();
        conn.close();
    }
}
