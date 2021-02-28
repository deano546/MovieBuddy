package com.example.moviebuddy.model;

public class GroupNight {

    private String id;
    private String MovieTitle;
    private String Date;
    private String GroupName;
    private String time;

    public GroupNight() {
    }

    public String getTime() {
        return time;
    }

    public GroupNight(String movieTitle, String date, String groupName, String time) {
        MovieTitle = movieTitle;
        Date = date;
        GroupName = groupName;
        this.time = time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public GroupNight(String movieTitle, String date, String groupName) {
        MovieTitle = movieTitle;
        Date = date;
        GroupName = groupName;
    }

    public GroupNight(String id, String movieTitle, String date, String groupName, String time) {
        this.id = id;
        MovieTitle = movieTitle;
        Date = date;
        GroupName = groupName;
        this.time = time;
    }

    @Override
    public String toString() {
        return "GroupNight{" +
                "MovieTitle='" + MovieTitle + '\'' +
                ", DateAndTime='" + Date + '\'' +
                ", GroupName='" + GroupName + '\'' +
                '}';
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getMovieTitle() {
        return MovieTitle;
    }

    public void setMovieTitle(String movieTitle) {
        MovieTitle = movieTitle;
    }

    public String getDate() {
        return Date;
    }

    public void setDate(String date) {
        Date = date;
    }

    public String getGroupName() {
        return GroupName;
    }

    public void setGroupName(String groupName) {
        GroupName = groupName;
    }
}
