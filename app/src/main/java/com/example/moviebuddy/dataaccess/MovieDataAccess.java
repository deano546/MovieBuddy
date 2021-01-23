package com.example.moviebuddy.dataaccess;

import com.example.moviebuddy.model.GroupNight;
import com.example.moviebuddy.model.Movie;

import java.util.ArrayList;
import java.util.List;

public class MovieDataAccess {


    //This is just faking a database for now, it allows me to populate the recycler views

    public List<GroupNight> getNight() {
        List<GroupNight> groupNightList = new ArrayList<>();

        GroupNight groupNight1 = new GroupNight("Dark Knight", "8th Oct 8.00pm", "OG BIS");
        GroupNight groupNight2 = new GroupNight("Dark Knights", "9th Oct 8.00pm", "OG BIS");

        groupNightList.add(groupNight1);
        groupNightList.add(groupNight2);

        return groupNightList;
    }


    public List<Movie> getMovies() {
        List<Movie> movieList = new ArrayList<>();

        Movie movie1 = new Movie("https://upload.wikimedia.org/wikipedia/en/thumb/c/c1/The_Matrix_Poster.jpg/220px-The_Matrix_Poster.jpg");
        Movie movie2 = new Movie("https://upload.wikimedia.org/wikipedia/en/thumb/9/98/John_Wick_TeaserPoster.jpg/220px-John_Wick_TeaserPoster.jpg");
        Movie movie3 = new Movie("https://upload.wikimedia.org/wikipedia/en/d/db/Spirited_Away_Japanese_poster.png");
        Movie movie4 = new Movie("https://upload.wikimedia.org/wikipedia/en/thumb/c/c1/The_Matrix_Poster.jpg/220px-The_Matrix_Poster.jpg");
        Movie movie5 = new Movie("https://upload.wikimedia.org/wikipedia/en/thumb/9/98/John_Wick_TeaserPoster.jpg/220px-John_Wick_TeaserPoster.jpg");
        Movie movie6 = new Movie("https://upload.wikimedia.org/wikipedia/en/d/db/Spirited_Away_Japanese_poster.png");

        movieList.add(movie1);
        movieList.add(movie2);
        movieList.add(movie3);
        movieList.add(movie4);
        movieList.add(movie5);
        movieList.add(movie6);








        return movieList;
    }


    public List<Movie> getPopularMovies() {
        List<Movie> movieList = new ArrayList<>();

        Movie movie1 = new Movie( 1,"Matriy","https://upload.wikimedia.org/wikipedia/en/thumb/c/c1/The_Matrix_Poster.jpg/220px-The_Matrix_Poster.jpg",1999);
        Movie movie2 = new Movie( 2,"John Wick", "https://upload.wikimedia.org/wikipedia/en/thumb/9/98/John_Wick_TeaserPoster.jpg/220px-John_Wick_TeaserPoster.jpg", 2010);
        Movie movie3 = new Movie( 3,"Spirited Away","https://upload.wikimedia.org/wikipedia/en/d/db/Spirited_Away_Japanese_poster.png",2005);
        Movie movie4 = new Movie( 4,"Matrix","https://upload.wikimedia.org/wikipedia/en/thumb/c/c1/The_Matrix_Poster.jpg/220px-The_Matrix_Poster.jpg",1999);
        Movie movie5 =  new Movie( 5,"John Wick", "https://upload.wikimedia.org/wikipedia/en/thumb/9/98/John_Wick_TeaserPoster.jpg/220px-John_Wick_TeaserPoster.jpg", 2010);
        Movie movie6 = new Movie( 6,"Spirited Away","https://upload.wikimedia.org/wikipedia/en/d/db/Spirited_Away_Japanese_poster.png",2005);

        movieList.add(movie1);
        movieList.add(movie2);
        movieList.add(movie3);
        movieList.add(movie4);
        movieList.add(movie5);
        movieList.add(movie6);








        return movieList;
    }



}
