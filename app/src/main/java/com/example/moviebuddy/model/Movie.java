package com.example.moviebuddy.model;

import java.util.List;

public class Movie {

    private int id;
    private String title;
    private String imageurl;
    private String runtime;
    private String overview;
    private int year;
    private String genre;

    public Movie() {
    }

    public Movie(int id, String title, String imageurl, String runtime, String overview, int year) {
        this.id = id;
        this.title = title;
        this.imageurl = imageurl;
        this.runtime = runtime;
        this.overview = overview;
        this.year = year;
    }

    public Movie(int id, String title, String imageurl, String runtime, String overview, int year, String genre) {
        this.id = id;
        this.title = title;
        this.imageurl = imageurl;
        this.runtime = runtime;
        this.overview = overview;
        this.year = year;
        this.genre = genre;
    }

    public String getGenre() {
        return genre;
    }

    public void setGenre(String genre) {
        this.genre = genre;
    }

    public Movie(String imageurl) {
        this.imageurl = imageurl;
    }

    @Override
    public String toString() {
        return "Movie{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", imageurl='" + imageurl + '\'' +
                ", runtime='" + runtime + '\'' +
                ", overview='" + overview + '\'' +
                ", year=" + year +
                ", genre=" + genre +
                '}';
    }

    public Movie(int id, String title, String imageurl, int year) {
        this.id = id;
        this.title = title;
        this.imageurl = imageurl;
        this.year = year;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getRuntime() {
        return runtime;
    }

    public void setRuntime(String runtime) {
        this.runtime = runtime;
    }

    public String getOverview() {
        return overview;
    }

    public void setOverview(String overview) {
        this.overview = overview;
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
