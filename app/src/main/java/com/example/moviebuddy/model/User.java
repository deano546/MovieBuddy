package com.example.moviebuddy.model;

import java.util.ArrayList;

public class User {

    private Long id;
    private String username;
    private ArrayList<Movie> watchlist;
    private ArrayList<User> friendlist;
    private ArrayList<Group> grouplist;

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

    public ArrayList<Movie> getWatchlist() {
        return watchlist;
    }

    public void setWatchlist(ArrayList<Movie> watchlist) {
        this.watchlist = watchlist;
    }

    public void addtoWatchlist(Movie movie) {

        watchlist.add(movie);
    }

    public ArrayList<User> getFriendlist() {
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
