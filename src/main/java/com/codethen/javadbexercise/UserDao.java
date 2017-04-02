package com.codethen.javadbexercise;

import java.util.List;

public interface UserDao {

    // Especifica los métodos del DaoComplex que puede llamar pero sin implementar.
    // Todos los métodos del interface son public y abstract.
    // Abstract porque están sin implementar.

    // Lo mejor es que las clases estén aisladas unas y otras, y si se tienen que relacionar o conocer se deben hacer
    // interfaces.


    /** finds all users */
    List<User> findAll();

    User findById(int id);
    void create(User user);
    void update(User user);
    void delete(int id);

}
