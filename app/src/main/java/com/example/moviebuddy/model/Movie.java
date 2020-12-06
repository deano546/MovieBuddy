package com.example.moviebuddy.model;

public class Movie {

    private Long id;
    private String title;
    private String imageurl;

    public Movie(Long id, String title, String imageurl) {
        this.id = id;
        this.title = title;
        this.imageurl = imageurl;
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

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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
}
