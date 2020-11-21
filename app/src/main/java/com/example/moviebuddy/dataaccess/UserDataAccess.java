package com.example.moviebuddy.dataaccess;

import com.example.moviebuddy.model.Movie;
import com.example.moviebuddy.model.User;

public class UserDataAccess {

    User getUser(long id) {
        User u1 = new User();
        u1.setUsername("George");
        Movie m1 = new Movie();
        m1.setTitle("Kill Bill");
        u1.addtoWatchlist(m1);
        return u1;
    }

}
