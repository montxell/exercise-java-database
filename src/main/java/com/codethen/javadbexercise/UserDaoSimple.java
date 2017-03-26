package com.codethen.javadbexercise;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;

public class UserDaoSimple extends GenericDao<User>{

    public UserDaoSimple() {
        super("users"); // Se le pasa el nombre de la tabla de la clase superior que es la de GenericDao
    }


    @Override
    protected User getObject(ResultSet rs) throws SQLException {

        User user = new User();
        user.setId(rs.getInt("id"));
        user.setUsername(rs.getString("username"));
        user.setName(rs.getString("name"));
        user.setEmail(rs.getString("email"));

        return user;

    }

    @Override
    protected List<String> getColumnNames() {
        return Arrays.asList("username", "name", "email");
    }

    @Override
    protected void setValues(User user, PreparedStatement stmt, boolean needsId) throws SQLException {
        stmt.setString(1, user.getUsername());
        stmt.setString(2, user.getName());
        stmt.setString(3, user.getEmail());

        if (needsId) {
            stmt.setInt(4, user.getId());
        }
    }
}
