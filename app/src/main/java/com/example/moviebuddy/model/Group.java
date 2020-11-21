package com.example.moviebuddy.model;

import java.util.ArrayList;

public class Group {

    private Long id;
    private String groupname;
    private ArrayList <User> members;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getGroupname() {
        return groupname;
    }

    public void setGroupname(String groupname) {
        this.groupname = groupname;
    }

    public ArrayList<User> getMembers() {
        return members;
    }

    public void setMembers(ArrayList<User> members) {
        this.members = members;
    }

    public void addMember(User user) {
        members.add(user);
    }
}
