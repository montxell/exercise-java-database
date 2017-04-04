package com.codethen.javadbexercise.dao;

import com.codethen.javadbexercise.model.User;
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

        String sql = "select * from users";

        //RowMapper<User> rowMapper = getUserRowMapper();  --> CREADO CAMPO DE INSTANCIA ABAJO rowMapper

        List<User> userList = template.query(sql, rowMapper);  // Abre conexión, ejecuta la sentencia y cierra conexión

        return userList;

    }

    /*
        Si introduzco el CAMPO DE INSTANCIA DE MÁS ABAJO private static RowMapper<User>, tengo
        que quitar RowMapper<User> rowMapper = getUserRowMapper();

     */


    /*
    // Podría hacer una clase MyRowMapper que implemente RowMapper que es una interface

    class MyRowMapper implements RowMapper<User> {
    @Override
    public User mapRow(ResultSet rs, int rowNum) throws SQLException {
    User user = new User();
				user.setId( rs.getInt("id") );
				user.setUsername( rs.getString("username") );
				user.setName( rs.getString("name") );
				user.setEmail( rs.getString("email") );
				return user;
    */


    @Override
    public User findById(int id) {

        String sql = "select * from users where id = ?";

        Object[] args = {1, id};

        // Esto es una interface y no puede quedarse solamente como RowMapper<User> rowMapper = new RowMapper.....

        /*
        RowMapper<User> rowMapper = new RowMapper<>() {
			@Override
			public User mapRow(ResultSet rs, int rowNum) throws SQLException {
                User user = new User();
				user.setId( rs.getInt("id") );
				user.setUsername( rs.getString("username") );
				user.setName( rs.getString("name") );
				user.setEmail( rs.getString("email") );
				return user;
			}

			// LAMBDA. Se puede hacer lambda cuando la interface solamente tiene un método. No se puede aplicar cuando hay más métodos
			// RowMapper si vas a documentación sabe que tiene como parámetros rs y rowNum.

			RowMapper<User> rowMapper = (rs, rowNum) -> {

            User user = new User();
            user.setId(rs.getInt("id"));
            user.setUsername(rs.getString("username"));
            user.setName(rs.getString("name"));
            user.setEmail(rs.getString("email"));
            return user;

        };
         */


        // RowMapper<User> rowMapper = getUserRowMapper(); --> CREADO CAMPO DE INSTANCIA abajo rowMapper

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

        template.update("update users set username = ?, name = ?, email = ? where id = ?",
                user.getUsername(), user.getName(), user.getEmail(), user.getId());

    }



    @Override
    public void delete(int id) {

        template.update("delete from users where id = ?", id);
    }




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


    /* ALTERNATIVA: CREAR UN CAMPO DE INSTANCIA (PROPIEDAD)

    Esto se podría poner en las propiedades quitando 'static', pero como actúa como un método se pone al final de la clase como otro método.

    Si pones static se crea solamente una vez, se asigna una vez en toda la vida del programa.
    STATIC se puede llamar sin instanciar la clase, puedes poner el nombre de la clase.nombre del método.
    Es como una función global que puedes llamar cuando quieres por ejemplo: DatabaseUtil.getConnection;
    No se hace una nueva instancia (new blabla();=) y la llamas.
    Como no existe una instancia no puedes llamar a las propiedades, si tienes que acceder a ellas entonces no puede
    ser static.
    */

    private static RowMapper<User> rowMapper = (rs, rowNum) -> {

        User user = new User();
        user.setId(rs.getInt("id"));
        user.setUsername(rs.getString("username"));
        user.setName(rs.getString("name"));
        user.setEmail(rs.getString("email"));
        return user;

    };


    /*

    Se puede utilizar Java Beans, cambiamos el RowMapper a:

    private static RowMapper<User> rowMapper = new BeanPropertyRowMapper<>(User.class);

    */
}
