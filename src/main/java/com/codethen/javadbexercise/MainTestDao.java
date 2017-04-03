package com.codethen.javadbexercise;

import com.mysql.jdbc.jdbc2.optional.MysqlDataSource;

import java.util.List;

public class MainTestDao {

    public static void main(String[] args) {

        //UserDaoComplex userDao = new UserDaoComplex();

        // Se configura el DataSource del tipo de Mysql, pero después se aplica en UserDaoSpring y éste no tiene por qué saber que tipo de DataSource
        MysqlDataSource dataSource = new MysqlDataSource();
        dataSource.setURL("jdbc:mysql://localhost/test");  // servidor: localhost   schema: test
        dataSource.setUser("root");
        dataSource.setPassword("moncode83");


        UserDao userDao = new UserDaoSpring(dataSource);


        User user = userDao.findById(2);

        System.out.println(user.getUsername());



        List<User> users = userDao.findAll();

        for (User u : users) {
            System.out.println(u);
        }


        User newUser = new User();

        newUser.setUsername("montxell");
        newUser.setEmail("mon@gmail.com");

        userDao.create(newUser);


        newUser.setId(9);
        newUser.setName("Montse");
        userDao.update(newUser);


        users = userDao.findAll();

        for (User u : users) {
            System.out.println(u);
        }
    }
}
