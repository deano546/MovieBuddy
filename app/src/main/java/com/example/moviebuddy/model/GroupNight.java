package com.example.moviebuddy.model;

public class GroupNight {

    private String MovieTitle;
    private String DateAndTime;
    private String GroupName;

    public GroupNight() {
    }

    public GroupNight(String movieTitle, String dateAndTime, String groupName) {
        MovieTitle = movieTitle;
        DateAndTime = dateAndTime;
        GroupName = groupName;
    }

    @Override
    public String toString() {
        return "GroupNight{" +
                "MovieTitle='" + MovieTitle + '\'' +
                ", DateAndTime='" + DateAndTime + '\'' +
                ", GroupName='" + GroupName + '\'' +
                '}';
    }

    public String getMovieTitle() {
        return MovieTitle;
    }

    public void setMovieTitle(String movieTitle) {
        MovieTitle = movieTitle;
    }

    public String getDateAndTime() {
        return DateAndTime;
    }

    public void setDateAndTime(String dateAndTime) {
        DateAndTime = dateAndTime;
    }

    public String getGroupName() {
        return GroupName;
    }

    public void setGroupName(String groupName) {
        GroupName = groupName;
    }
}
