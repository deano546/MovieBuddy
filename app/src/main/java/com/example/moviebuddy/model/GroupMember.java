package com.example.moviebuddy.model;

public class GroupMember {
    private String id;
    private String username;
    private String approval;

    public GroupMember(String id, String username, String approval) {
        this.id = id;
        this.username = username;
        this.approval = approval;
    }

    @Override
    public String toString() {
        return "GroupMember{" +
                "id='" + id + '\'' +
                ", username='" + username + '\'' +
                ", approval='" + approval + '\'' +
                '}';
    }

    public GroupMember() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getApproval() {
        return approval;
    }

    public void setApproval(String approval) {
        this.approval = approval;
    }
}
