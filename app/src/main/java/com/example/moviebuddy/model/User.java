package com.example.moviebuddy.model;

import java.lang.reflect.Member;
import java.util.ArrayList;
import java.util.List;

public class User {

    private int id;
    private String username;
    private List<Movie> watchlist;
    private List<User> friendlist;
    private ArrayList<Group> grouplist;

    public User(String username) {
        this.username = username;
    }

    public User() {
    }

    public User(int id, String username) {
        this.id = id;
        this.username = username;
    }


    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", username='" + username + '\'' +
                '}';
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
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

    //Overriding equals method so I can compare User objects via ID
    //https://stackoverflow.com/questions/17003427/is-it-proper-for-equals-to-depend-only-on-an-id
    @Override
    public boolean equals(Object ob) {
        if (ob == null) {
            return false;
        }
        if (this == ob) {
            return true;
        }
        if (ob instanceof User) {
            User other = (User) ob;
            return this.id == (other.getId());
        }
        return false;
    }

    public void setGrouplist(ArrayList<Group> grouplist) {
        this.grouplist = grouplist;
    }
}
