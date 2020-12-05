package com.example.moviebuddy.service;


import com.example.moviebuddy.model.Movie;
import com.example.moviebuddy.model.User;

import java.util.ArrayList;

public class MovieService {

public ArrayList<Movie> getRecommendedMovies(User user) {

    ArrayList<Movie> recommendlist = null;

    Movie movie = new Movie("Test1");
    movie.setTitle("Spirited Away");
    recommendlist.add(movie);

    return recommendlist;

    }


    public ArrayList<Movie> getUpcomingMovies() {
         Movie m1 = new Movie("Test2");

        ArrayList<Movie> upcominglist = null;

        upcominglist.add(m1);

        return upcominglist;


    }


}
