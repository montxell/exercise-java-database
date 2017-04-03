package com.codethen.javadbexercise;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;

import javax.sql.DataSource;
import java.util.List;

public class UserDaoSpring implements UserDao {

    private JdbcTemplate template;


    public UserDaoSpring(DataSource dataSource) {  // Se llama a un DataSource pero no tienes que saber cuál, en este caso es mysql

        template = new JdbcTemplate(dataSource);  // el template necesita un dataSource para crear las conexiones

    }



    @Override
    public List<User> findAll() {

        // TODO: template.queryForList

    }



    @Override
    public User findById(int id) {

        String sql = "select * from users where id = ?";

        Object[] args = {1, id};

        // Esto es una interface y no puede quedarse solamente como RowMapper<User> rowMapper = new RowMapper public User mapRow(ResultSet rs, int rowNum) throws SQLException {
        RowMapper<User> rowMapper = (rs, rowNum) -> {

            User user = new User();
            user.setId(rs.getInt("id"));
            user.setUsername(rs.getString("username"));
            user.setName(rs.getString("name"));
            user.setEmail(rs.getString("email"));
            return user;

        };

        User user = template.queryForObject(sql, args, rowMapper);  // Abre conexión, ejecuta la sentencia y cierra conexión

        return user;
    }

    @Override
    public void create(User user) {

        template.update(
                "insert into users (username, name, email) values (?, ?, ?)",
                user.getUsername(), user.getName(), user.getEmail());  // El update(String sql, Object... args) Los puntos suspensivos indica que puedes poner varios objetos separados por comas
                                                                        // En este caso es user.getUsername(), user.getName(), user.getEmail()

    }

    @Override
    public void update(User user) {

        // TODO

    }

    @Override
    public void delete(int id) {

        template.update("delete from users where id = ?", id);
    }
}
