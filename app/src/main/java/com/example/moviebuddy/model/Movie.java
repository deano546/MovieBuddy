package com.example.moviebuddy.model;

public class Movie {

    private int id;
    private String title;
    private String imageurl;
    private int year;

    public Movie(int id, String title, String imageurl) {
        this.id = id;
        this.title = title;
        this.imageurl = imageurl;
    }

    public Movie(int id, String title, String imageurl, int year) {
        this.id = id;
        this.title = title;
        this.imageurl = imageurl;
        this.year = year;
    }

    public Movie(String imageurl) {
        this.imageurl = imageurl;
    }

    @Override
    public String toString() {
        return "Movie{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", poster='" + imageurl + '\'' +
                '}';
    }



    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getImageurl() {
        return imageurl;
    }

    public void setImageurl(String imageurl) {
        this.imageurl = imageurl;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }
}
