package com.example.moviebuddy.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.moviebuddy.R;
import com.example.moviebuddy.adapters.MovieListRecyclerAdapter;
import com.example.moviebuddy.dataaccess.JSONParser;
import com.example.moviebuddy.model.Movie;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;
import java.util.List;

public class MovieActivity extends AppCompatActivity {

    //Setting up recyclerview, mainly adapted from my IS4447 project
    RecyclerView rvMovieList;
    RecyclerView.Adapter mAdapter;
    RecyclerView.LayoutManager layoutManager;
    List<Movie> movieList = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie);
        Button btnSearch = findViewById(R.id.btnSearch);
        EditText etMovieSearch = findViewById(R.id.etMovieSearch);

        Log.d("Before", ":" + movieList.toString());

        //Displaying recyclerview, again adapted from IS4447 project
        rvMovieList
                = (RecyclerView)findViewById(
                R.id.rvMovieList);

        JSONParser jsonParser = new JSONParser();

        //Retrieves a list of popular movies from the movie database API
       jsonParser.getPopularMoviez(this, new JSONParser.VolleyResponseListener() {
            @Override
            public void onError(String message) {
                Toast.makeText(MovieActivity.this, "Error Retrieving", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onResponse(List<Movie> movieLists) {
                //Once list is retrieved, it is displayed in the recycler view
                movieList = movieLists;
                rvMovieList.setHasFixedSize(true);
                layoutManager = new LinearLayoutManager(MovieActivity.this);
                rvMovieList.setLayoutManager(layoutManager);
                mAdapter = new MovieListRecyclerAdapter(movieList,MovieActivity.this);
                rvMovieList.setAdapter(mAdapter);
            }
        });

        //Clicking this search button calls a search on the API
       btnSearch.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               if(etMovieSearch.getText().toString().trim().length() > 0) {
                   jsonParser.SearchMovies(MovieActivity.this, new JSONParser.SearchMovieResponseListener() {
                       @Override
                       public void onError(String message) {
                           Toast.makeText(MovieActivity.this, "Search error", Toast.LENGTH_SHORT).show();
                       }

                       @Override
                       public void onResponse(List<Movie> searchList) {
                           //Display retrieved search results in recycler
                           movieList = searchList;
                           Log.d("PrintingMovieList",movieList.toString());
                           Log.d("PrintingMovieList2",searchList.toString());
                           mAdapter.notifyDataSetChanged();
                           rvMovieList.setHasFixedSize(true);
                           layoutManager = new LinearLayoutManager(MovieActivity.this);
                           rvMovieList.setLayoutManager(layoutManager);
                           mAdapter = new MovieListRecyclerAdapter(movieList,MovieActivity.this);
                           rvMovieList.setAdapter(mAdapter);
                       }
                   },etMovieSearch.getText().toString());
               }
               else {
                   Toast.makeText(MovieActivity.this, "Please enter a search", Toast.LENGTH_SHORT).show();
               }

           }
       });



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