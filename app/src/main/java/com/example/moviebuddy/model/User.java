package com.example.moviebuddy.model;

import java.util.ArrayList;
import java.util.List;

public class User {

    private Long id;
    private String username;
    private List<Movie> watchlist;
    private List<User> friendlist;
    private ArrayList<Group> grouplist;

    public User(String username) {
        this.username = username;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public List<Movie> getWatchlist() {
        return watchlist;
    }

    public void setWatchlist(ArrayList<Movie> watchlist) {
        this.watchlist = watchlist;
    }

    public void addtoWatchlist(Movie movie) {

        watchlist.add(movie);
    }

    public List<User> getFriendlist() {
        return friendlist;
    }

    public void setFriendlist(ArrayList<User> friendlist) {
        this.friendlist = friendlist;
    }

    public void addtoFriendlist(User user) {

        friendlist.add(user);
    }

    public ArrayList<Group> getGrouplist() {
        return grouplist;
    }

    public void setGrouplist(ArrayList<Group> grouplist) {
        this.grouplist = grouplist;
    }
}
