package com.codethen.javadbexercise.dao;

import com.codethen.javadbexercise.model.User;

import java.util.List;

public interface UserDao {

    // Especifica los métodos del DaoComplex que puede llamar pero sin implementar

    List<User> findAll();
    User findById(int id);
    void create(User user);
    void update(User user);
    void delete(int id);

}
