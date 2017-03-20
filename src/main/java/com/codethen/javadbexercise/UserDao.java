package com.codethen.javadbexercise;


import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/** Responsible of talking with the database to read/write users */
public class UserDao {


    public List<User> findAll() {

        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;

        try {

            // get connection
            conn = DatabaseUtil.getConnection();

            // prepare and execute query
            stmt = conn.prepareStatement("select * from users");
            rs = stmt.executeQuery();

            List<User> users = new ArrayList<User>();

            while (rs.next()) {

                User user = getUser(rs);
                users.add(user);

            }

            return users;

        } catch(Exception e) {
            throw new RuntimeException("error finding user", e);
        } finally {
            firstClose(rs, stmt, conn);
        }
    }



    public User findById(int id) {

        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;


        try {

            final User result;

            // get connection
            conn = DatabaseUtil.getConnection();

            // prepare and execute query
            stmt = conn.prepareStatement("select * from users where id = ?");
            stmt.setInt(1, id);
            rs = stmt.executeQuery();


            if (rs.next()) {

                User user = getUser(rs);

                result = user;

            } else {

                result = null;
            }

            return result;


        // next code is boilerplate (exception handling and closing resources)

        } catch(Exception e) {
            throw new RuntimeException("error finding user", e);
        } finally {
            firstClose(rs, stmt, conn);
        }
    }



    private User getUser(ResultSet rs) throws SQLException {
        int id = rs.getInt("id");
        String username = rs.getString("username");
        String name = rs.getString("name");
        String email = rs.getString("email");

        User user = new User();
        user.setId(id);
        user.setUsername(username);
        user.setName(name);
        user.setEmail(email);
        return user;
    }



    private void firstClose(ResultSet rs, PreparedStatement stmt, Connection conn) {
        try {
            rs.close();
            stmt.close();
            conn.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }



    public void create(User user) {

        Connection conn = null;
        PreparedStatement stmt = null;

        try {

            // get connection
            conn = DatabaseUtil.getConnection();

            // prepare and execute update
            stmt = conn.prepareStatement("insert into users (username, email) values (?, ?)");
            stmt.setString(1, user.getUsername());
            stmt.setString(2, user.getEmail());

            stmt.executeUpdate();


        } catch(Exception e) {
            throw new RuntimeException("error creating user", e);
        } finally {
            secondClose(stmt, conn);
        }
    }



    public void update(User user) {

        Connection conn = null;
        PreparedStatement stmt = null;

        try {

            // get connection
            conn = DatabaseUtil.getConnection();

            // prepare and execute update
            stmt = conn.prepareStatement("update users set name = ? where id = ?");
            stmt.setString(1, user.getName());
            stmt.setInt(2, user.getId());

            stmt.executeUpdate();


        } catch(Exception e) {
            throw new RuntimeException("error updating user", e);
        } finally {
            secondClose(stmt, conn);
        }
    }



    private void secondClose(PreparedStatement stmt, Connection conn) {
        try {
            stmt.close();
            conn.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
