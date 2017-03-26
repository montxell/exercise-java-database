package com.codethen.javadbexercise;

import java.util.List;

public class MainTestDao {

    public static void main(String[] args) {

        UserDao userDao = new UserDao();


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
