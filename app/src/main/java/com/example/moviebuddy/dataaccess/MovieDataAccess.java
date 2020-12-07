package com.example.moviebuddy.dataaccess;

import com.example.moviebuddy.model.Movie;

import java.util.ArrayList;
import java.util.List;

public class MovieDataAccess {

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

        Movie movie1 = new Movie((long) 1,"Matrix","https://upload.wikimedia.org/wikipedia/en/thumb/c/c1/The_Matrix_Poster.jpg/220px-The_Matrix_Poster.jpg",1999);
        Movie movie2 = new Movie((long) 2,"John Wick", "https://upload.wikimedia.org/wikipedia/en/thumb/9/98/John_Wick_TeaserPoster.jpg/220px-John_Wick_TeaserPoster.jpg", 2010);
        Movie movie3 = new Movie((long) 3,"Spirited Away","https://upload.wikimedia.org/wikipedia/en/d/db/Spirited_Away_Japanese_poster.png",2005);
        Movie movie4 = new Movie((long) 4,"Matrix","https://upload.wikimedia.org/wikipedia/en/thumb/c/c1/The_Matrix_Poster.jpg/220px-The_Matrix_Poster.jpg",1999);
        Movie movie5 =  new Movie((long) 5,"John Wick", "https://upload.wikimedia.org/wikipedia/en/thumb/9/98/John_Wick_TeaserPoster.jpg/220px-John_Wick_TeaserPoster.jpg", 2010);
        Movie movie6 = new Movie((long) 6,"Spirited Away","https://upload.wikimedia.org/wikipedia/en/d/db/Spirited_Away_Japanese_poster.png",2005);

        movieList.add(movie1);
        movieList.add(movie2);
        movieList.add(movie3);
        movieList.add(movie4);
        movieList.add(movie5);
        movieList.add(movie6);








        return movieList;
    }



}
