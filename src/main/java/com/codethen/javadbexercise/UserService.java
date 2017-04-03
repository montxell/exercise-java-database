package com.codethen.javadbexercise;

import java.util.List;

public class UserService {

    private UserDao userDao;   // No sabe UserService la clase de userDao, solo que viene del interface UserDao
    // TODO: private MailService mailService;



    public UserService(UserDao userDao) {
        this.userDao = userDao;
    }



    public void create(User user) {

        userDao.create(user);
        // TODO: mailService.sendWelcomeEmail(user.getEmail());

    }


    public List<User> findAll() {
        return userDao.findAll();
    }



}
