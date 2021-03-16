package com.example.moviebuddy.model;

public class GroupNight {

    private String id;
    private String MovieTitle;
    private String Date;
    private String GroupName;
    private String groupid;
    private String time;
    private String approval;
    private String movieid;
    private String creatorid;

    public GroupNight(String id, String movieTitle, String date, String groupName, String groupid, String time, String approval, String movieid) {
        this.id = id;
        MovieTitle = movieTitle;
        Date = date;
        GroupName = groupName;
        this.groupid = groupid;
        this.time = time;
        this.approval = approval;
        this.movieid = movieid;
    }

    public GroupNight(String id, String movieTitle, String date, String groupName, String groupid, String time, String approval, String movieid, String creatorid) {
        this.id = id;
        MovieTitle = movieTitle;
        Date = date;
        GroupName = groupName;
        this.groupid = groupid;
        this.time = time;
        this.approval = approval;
        this.movieid = movieid;
        this.creatorid = creatorid;
    }

    public String getCreatorid() {
        return creatorid;
    }

    public void setCreatorid(String creatorid) {
        this.creatorid = creatorid;
    }

    public String getGroupid() {
        return groupid;
    }

    public void setGroupid(String groupid) {
        this.groupid = groupid;
    }

    public String getMovieid() {
        return movieid;
    }

    public void setMovieid(String movieid) {
        this.movieid = movieid;
    }

    public GroupNight(String id, String movieTitle, String date, String groupName, String time, String approval, String movieid) {
        this.id = id;
        MovieTitle = movieTitle;
        Date = date;
        GroupName = groupName;
        this.time = time;
        this.approval = approval;
        this.movieid = movieid;
    }

    public GroupNight() {
    }

    public String getApproval() {
        return approval;
    }

    public void setApproval(String approval) {
        this.approval = approval;
    }

    public GroupNight(String id, String movieTitle, String date, String groupName, String time, String approval) {
        this.id = id;
        MovieTitle = movieTitle;
        Date = date;
        GroupName = groupName;
        this.time = time;
        this.approval = approval;
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
                "id='" + id + '\'' +
                ", MovieTitle='" + MovieTitle + '\'' +
                ", Date='" + Date + '\'' +
                ", GroupName='" + GroupName + '\'' +
                ", groupid='" + groupid + '\'' +
                ", time='" + time + '\'' +
                ", approval='" + approval + '\'' +
                ", movieid='" + movieid + '\'' +
                ", creatorid='" + creatorid + '\'' +
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
