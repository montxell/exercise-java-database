package com.codethen.javadbexercise.dao;

import com.codethen.javadbexercise.model.User;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;

import javax.sql.DataSource;
import java.util.List;

public class UserDaoSpring implements UserDao {

    private JdbcTemplate template;


    public UserDaoSpring(DataSource dataSource) {

        template = new JdbcTemplate(dataSource);

    }



    @Override
    public List<User> findAll() {

        String sql = "select * from users";

        //RowMapper<User> rowMapper = getUserRowMapper();  --> CREADO CAMPO DE INSTANCIA rowMapper

        List<User> userList = template.query(sql, rowMapper);

        return userList;

    }



    @Override
    public User findById(int id) {

        String sql = "select * from users where id = ?";

        Object[] args = {1, id};

        // RowMapper<User> rowMapper = getUserRowMapper(); --> CREADO CAMPO DE INSTANCIA rowMapper

        User user = template.queryForObject(sql, args, rowMapper);

        return user;
    }



    @Override
    public void create(User user) {

        template.update(
                "insert into users (username, name, email) values (?, ?, ?)",
                user.getUsername(), user.getName(), user.getEmail());
    }



    @Override
    public void update(User user) {

        template.update("update users set username = ?, name = ?, email = ? where id = ?",
                user.getUsername(), user.getName(), user.getEmail(), user.getId());

    }



    @Override
    public void delete(int id) {

        template.update("delete from users where id = ?", id);
    }



    // CREAR UN CAMPO DE INSTANCIA (PROPIEDAD)

    private static RowMapper<User> rowMapper = (rs, rowNum) -> {

        User user = new User();
        user.setId(rs.getInt("id"));
        user.setUsername(rs.getString("username"));
        user.setName(rs.getString("name"));
        user.setEmail(rs.getString("email"));
        return user;

    };



    /*

    **** JAVA BEANS ****

    Se puede utilizar Java Beans, cambiamos el RowMapper a:

    private static RowMapper<User> rowMapper = new BeanPropertyRowMapper<>(User.class);

    */



    // ALTERNATIVA AL CAMPO DE INSTANCIA: MÉTODO SIMPLIFICADO CON LAMBDA

    /*
    private RowMapper<User> getUserRowMapper() {

        return (rs, rowNum) -> {

            User user = new User();
            user.setId(rs.getInt("id"));
            user.setUsername(rs.getString("username"));
            user.setName(rs.getString("name"));
            user.setEmail(rs.getString("email"));
            return user;

        };
    }
    */



}
