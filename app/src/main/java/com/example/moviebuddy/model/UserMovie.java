package com.example.moviebuddy.model;

public class UserMovie {

    private int rating;
    private String onWatchlist;
    private boolean known;

    public UserMovie() {
    }

    public UserMovie(int rating, String onWatchlist) {
        this.rating = rating;
        this.onWatchlist = onWatchlist;
    }

    public boolean isKnown() {
        return known;
    }

    public UserMovie(int rating, String onWatchlist, boolean known) {
        this.rating = rating;
        this.onWatchlist = onWatchlist;
        this.known = known;
    }

    public void setKnown(boolean known) {
        this.known = known;
    }

    @Override
    public String toString() {
        return "UserMovie{" +
                "rating=" + rating +
                ", onWatchlist='" + onWatchlist + '\'' +
                '}';
    }

    public int getRating() {
        return rating;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }

    public String getOnWatchlist() {
        return onWatchlist;
    }

    public void setOnWatchlist(String onWatchlist) {
        this.onWatchlist = onWatchlist;
    }
}
