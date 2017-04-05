package com.codethen.javadbexercise.service;

import com.codethen.javadbexercise.model.User;
import com.codethen.javadbexercise.dao.UserDao;

import java.util.List;
import java.util.stream.Collectors;

public class UserService {

    private UserDao userDao;
    // private MailService mailService;



    public UserService(UserDao userDao) {
        this.userDao = userDao;
    }



    public void create(User user) {

        userDao.create(user);
        // mailService.sendWelcomeEmail(user.getEmail());

    }


    public List<User> findAll() {
        return userDao.findAll();
    }



    public List<User> findByEmailDomain(String domain) {

        // This method could be done by the DAO but for learning purposes
        // let's suppose we have to code the logic here (so we can test it)


        // return userDao.findByEmailDomain(domain);  --> Suponemos que la base de datos no puede hacer esta búsqueda,
        //                                                solamente nos da todos los usuarios (findAll()).


        List<User> all = findAll();  // En esta clase ya está el método de userDao.findAll().


        /*  OPCIÓN 1:

        List <User> result = new ArrayList<>();

        for(User user : all) {
            if (user.getEmail().endsWith(domain)) {  // endsWith
                result.add(user);
            }
        }

        return result;

        */


        // OPCIÓN 2: STREAM

        return all.stream()
                .filter(u -> u.getEmail().endsWith(domain))
                .collect(Collectors.toList());
    }

}
