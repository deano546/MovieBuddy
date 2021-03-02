package com.example.moviebuddy.model;

import java.util.List;

public class GroupsRatings {

    private String rating;
    //private String userid;
    //private String movieid;
    private String genre;

    public GroupsRatings() {
    }

    public GroupsRatings(String rating, String genre) {
        this.rating = rating;
        this.genre = genre;
    }

    public String getGenre() {
        return genre;
    }

    public void setGenre(String genre) {
        this.genre = genre;
    }

    public String getRating() {
        return rating;
    }

    public void setRating(String rating) {
        this.rating = rating;
    }

    @Override
    public String toString() {
        return "GroupsRatings{" +
                "rating='" + rating + '\'' +
                ", genres=" + genre +
                '}';
    }
}
