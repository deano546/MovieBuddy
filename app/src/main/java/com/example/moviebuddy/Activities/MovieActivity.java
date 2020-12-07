package com.example.moviebuddy.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.moviebuddy.R;
import com.example.moviebuddy.adapters.MovieListRecyclerAdapter;
import com.example.moviebuddy.dataaccess.MovieDataAccess;
import com.example.moviebuddy.model.Movie;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;
import java.util.List;

public class MovieActivity extends AppCompatActivity {


    RecyclerView rvMovieList;
    RecyclerView.Adapter mAdapter;
    RecyclerView.LayoutManager layoutManager;
    List<Movie> movieList = new ArrayList<Movie>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie);


        rvMovieList
                = (RecyclerView)findViewById(
                R.id.rvMovieList);
        MovieDataAccess moviedao = new MovieDataAccess();
        movieList = moviedao.getPopularMovies();
        rvMovieList.setHasFixedSize(true);

        layoutManager = new LinearLayoutManager(this);
        rvMovieList.setLayoutManager(layoutManager);

        mAdapter = new MovieListRecyclerAdapter(movieList,this);
        rvMovieList.setAdapter(mAdapter);


        //Declare bottom nav, and set correct option as selected, adapted from https://stackoverflow.com/questions/40202294/set-selected-item-in-android-bottomnavigationview
        BottomNavigationView bottomNavigationView = (BottomNavigationView) findViewById(R.id.bottom_navigation);
        bottomNavigationView.setSelectedItemId(R.id.page_4);


        //Setting up navigation to correct activity, adapted from https://suragch.medium.com/how-to-add-a-bottom-navigation-bar-in-android-958ed728ef6c
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.page_1:
                        Intent startMainIntent = new Intent(getApplicationContext(), MainActivity.class);
                        startActivity(startMainIntent);
                        break;
                    case R.id.page_2:
                        Intent startWatchlistIntent = new Intent(getApplicationContext(), WatchListActivity.class);
                        startActivity(startWatchlistIntent);
                        break;
                    case R.id.page_3:
                        Intent startGroupIntent = new Intent(getApplicationContext(), GroupActivity.class);
                        startActivity(startGroupIntent);
                        break;
                    case R.id.page_4:
                        break;
                    case R.id.page_5:
                        Intent startNotifyIntent = new Intent(getApplicationContext(), NotifyActivity.class);
                        startActivity(startNotifyIntent);}
                return true;
            }
        });



    }
}