package com.codethen.javadbexercise.dao;

import com.codethen.javadbexercise.model.User;

/**
 * This DAO is much simpler than {@link UserDaoComplex} because it relies on {@link GenericDao}.
 */

public class UserDaoSimple extends GenericDao<User> implements UserDao {

    public UserDaoSimple() {
        super("users", User.class); // Se le pasa el nombre de la tabla de la clase superior que es la de GenericDao
    }                                           // Va a extraer los métodos y propiedades de User, la T es User

/* SE QUITA PORQUE SE HA PASADO AL GENERICDAO

    @Override
    protected User getObject(ResultSet rs) throws SQLException {

        // create instance of the model class
        User user = new User();

        //  no importa esto
        //Class<? extends User> c1 = user.getClass(); // Quieres saber la clase, a partir de un objeto.

        //Class<User> c2 =  User.class; // Este ya se sabe el tipo


        // sets values from rs to the instance
        user.setId(rs.getInt("id"));
        user.setUsername(rs.getString("username"));
        user.setName(rs.getString("name"));
        user.setEmail(rs.getString("email"));

        // return instance
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
*/
}
