package com.example.moviebuddy.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.Toast;

import com.example.moviebuddy.R;
import com.example.moviebuddy.adapters.WatchListRecyclerAdapter;
import com.example.moviebuddy.dataaccess.JSONParser;
import com.example.moviebuddy.model.Movie;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;
import java.util.List;



public class WatchListActivity extends AppCompatActivity {



    //Setting up recyclerview, mainly adapted from my IS4447 project
    RecyclerView rvMovieList;
    RecyclerView.Adapter mAdapter;
    RecyclerView.LayoutManager layoutManager;
    List<Movie> movieList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_watch_list);

        rvMovieList = findViewById(R.id.rvWatchlist);

        JSONParser jsonParser = new JSONParser();

        //This gets the watchlist of the current user by using their ID
        jsonParser.getWatchlistbyID(WatchListActivity.this, new JSONParser.WatchListResponseListener() {
            @Override
            public void onError(String message) {
                Log.d("TAG",message);

            }

            @Override
            public void onResponse(List<Movie> movies) {
                //Once list is retrieved, it is displayed in the recycler
                movieList = movies;
                updateRecycler();
            }
        },1);


        //Declare bottom nav, and set correct option as selected, adapted from https://stackoverflow.com/questions/40202294/set-selected-item-in-android-bottomnavigationview
        BottomNavigationView bottomNavigationView;
        bottomNavigationView = (BottomNavigationView) findViewById(R.id.bottom_navigation);
        bottomNavigationView.setSelectedItemId(R.id.page_2);

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
                        break;
                    case R.id.page_3:
                        Intent startGroupIntent = new Intent(getApplicationContext(), GroupActivity.class);
                        startActivity(startGroupIntent);
                        break;
                    case R.id.page_4:
                        Intent startMovieIntent = new Intent(getApplicationContext(), MovieActivity.class);
                        startActivity(startMovieIntent);
                        break;
                    case R.id.page_5:
                        Intent startNotifyIntent = new Intent(getApplicationContext(), NotifyActivity.class);
                        startActivity(startNotifyIntent);}
                return true;
            }
        });







    }

    public void updateRecycler() {
        rvMovieList.setHasFixedSize(true);

        layoutManager = new LinearLayoutManager(WatchListActivity.this);
        rvMovieList.setLayoutManager(layoutManager);

        mAdapter = new WatchListRecyclerAdapter(movieList, WatchListActivity.this);
        rvMovieList.setAdapter(mAdapter);
    }

}