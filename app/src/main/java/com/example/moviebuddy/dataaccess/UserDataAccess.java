package com.example.moviebuddy.dataaccess;

import com.example.moviebuddy.model.Movie;
import com.example.moviebuddy.model.User;

import java.util.ArrayList;
import java.util.List;

public class UserDataAccess {

    User getUser(long id) {
        User u1 = new User("George");
        Movie m1 = new Movie("Hello");
        m1.setTitle("Kill Bill");
        u1.addtoWatchlist(m1);
        return u1;
    }

    public List<User> getUsers() {
        List<User> userList = new ArrayList<>();

        User user1 = new User("GabyG");
        User user2 = new User("Conzuela");


        userList.add(user1);
        userList.add(user2);








        return userList;
    }


}
