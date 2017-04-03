package com.codethen.javadbexercise;

import org.junit.Assert;
import org.junit.Test;

import java.util.List;

public class UserServiceTest {

    @Test
    public void simpleTest() {

        /*
        UserDaoSimple userDao = new UserDaoSimple();

        UserService userService = new UserService(userDao);
        */

        // Inline:
        UserService userService = new UserService(new UserDaoSimple());

        // Create users into DB
        User user = new User();
        user.setUsername("u1");
        user.setEmail("u1@test.com");

        userService.create(user);

        User user2 = new User();
        user2.setUsername("u2");
        user2.setEmail("u2@test.com");

        userService.create(user2);


        // Get users from DB

        // List<User> users = userDao.findAll();
        List<User> users = userService.findAll();

        Assert.assertEquals(2, users.size());

        Assert.assertEquals("u1", users.get(0).getUsername());
        Assert.assertEquals("u1@test.com", users.get(0).getEmail());
        Assert.assertEquals("u2", users.get(1).getUsername());
        Assert.assertEquals("u2@test.com", users.get(1).getEmail());


        // remove created users

        for (User u : users) {
            new UserDaoComplex().delete(u.getId());
        }
    }
}
