package com.example.moviebuddy.model;

import java.util.ArrayList;
import java.util.List;

public class Group {

    private Long id;
    private String groupname;
    private List<User> members;

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

    public List<User> getMembers() {
        return members;
    }

    public void setMembers(ArrayList<User> members) {
        this.members = members;
    }

    public void addMember(User user) {
        members.add(user);
    }
}
