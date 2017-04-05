package com.codethen.javadbexercise.dao;


import com.codethen.javadbexercise.util.DatabaseUtil;
import com.codethen.javadbexercise.model.User;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/** Responsible of talking with the database to read/write users */
public class UserDaoComplex implements UserDao {

    // If interface is implemented (UserDao) we have to write Override.

    @Override
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
            throw new RuntimeException("error finding users", e);
        } finally {
            DatabaseUtil.close(rs, stmt, conn);
        }
    }


    @Override
    public User findById(int id) {

        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;


        try {

            final User result;

            conn = DatabaseUtil.getConnection();

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


        // boilerplate (exception handling and closing resources)
        } catch(Exception e) {
            throw new RuntimeException("error finding user", e);
        } finally {
            DatabaseUtil.close(rs, stmt, conn);
        }
    }


    @Override
    public void create(User user) {

        Connection conn = null;
        PreparedStatement stmt = null;

        try {

            conn = DatabaseUtil.getConnection();

            // prepare and execute update
            stmt = conn.prepareStatement("insert into users (username, name, email) values (?, ?, ?)");
            stmt.setString(1, user.getUsername());
            stmt.setString(2, user.getName());
            stmt.setString(3, user.getEmail());

            stmt.executeUpdate();


        } catch(Exception e) {
            throw new RuntimeException("error creating user", e);
        } finally {
            DatabaseUtil.close(null, stmt, conn);
        }
    }


    @Override
    public void update(User user) {

        Connection conn = null;
        PreparedStatement stmt = null;

        try {

            conn = DatabaseUtil.getConnection();

            stmt = conn.prepareStatement("update users set username = ?, name = ?, email = ? where id = ?");
            stmt.setString(1, user.getUsername());
            stmt.setString(2, user.getName());
            stmt.setString(3, user.getEmail());
            stmt.setInt(4, user.getId());

            stmt.executeUpdate();


        } catch(Exception e) {
            throw new RuntimeException("error updating user", e);
        } finally {
            DatabaseUtil.close(null, stmt, conn);
        }
    }


    @Override
    public void delete(int id) {

        Connection conn = null;
        PreparedStatement stmt = null;

        try {

            conn = DatabaseUtil.getConnection();

            stmt = conn.prepareStatement("delete from users where id = ?");
            stmt.setInt(1, id);

            stmt.executeUpdate();


        } catch(Exception e) {
            throw new RuntimeException("error deleting user", e);
        } finally {
            DatabaseUtil.close(null, stmt, conn);
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

}
