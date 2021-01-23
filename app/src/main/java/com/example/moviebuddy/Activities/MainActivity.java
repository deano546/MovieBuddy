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
import android.widget.Toast;

import com.example.moviebuddy.R;
import com.example.moviebuddy.adapters.UpcomingNightRecyclerAdapter;
import com.example.moviebuddy.adapters.UpcomingRecyclerAdapter;
import com.example.moviebuddy.dataaccess.JSONParser;
import com.example.moviebuddy.dataaccess.MovieDataAccess;
import com.example.moviebuddy.model.GroupNight;
import com.example.moviebuddy.model.Movie;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.List;

public class MainActivity extends AppCompatActivity {



    //Code for setting up horizontal recycler view adapted from https://www.geeksforgeeks.org/android-horizontal-recyclerview-with-examples/

    // Recycler View object
    RecyclerView recyclerView;
    RecyclerView grouprecycler;

    // Array list for recycler view data source
    List<Movie> source;
    List<GroupNight> nightsource;

    // Layout Manager
    RecyclerView.LayoutManager RecyclerViewLayoutManager;
    RecyclerView.LayoutManager NightRecyclerManager;

    // adapter class object
    RecyclerView.Adapter adapter;
    RecyclerView.Adapter nightadapter;

    // Linear Layout Manager
    LinearLayoutManager HorizontalLayout;
    LinearLayoutManager NightHorizontalLayout;

    View ChildView;
    int RecyclerViewItemPosition;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        grouprecycler = findViewById(R.id.rvGroupNight);
        NightRecyclerManager = new LinearLayoutManager(getApplicationContext());
        grouprecycler.setLayoutManager(NightRecyclerManager);

        MovieDataAccess dataAccess = new MovieDataAccess();
        nightsource = dataAccess.getNight();
        nightadapter = new UpcomingNightRecyclerAdapter(nightsource,MainActivity.this);
        NightHorizontalLayout = new LinearLayoutManager(
                MainActivity.this,
                LinearLayoutManager.HORIZONTAL,
                false);
        grouprecycler.setLayoutManager(NightHorizontalLayout);
        grouprecycler.setAdapter(nightadapter);



        // initialisation with id's
        recyclerView
                = (RecyclerView)findViewById(
                R.id.rvMovie);
        RecyclerViewLayoutManager
                = new LinearLayoutManager(
                getApplicationContext());

        // Set LayoutManager on Recycler View
        recyclerView.setLayoutManager(
                RecyclerViewLayoutManager);

        // Adding items to RecyclerView.

        JSONParser jsonParser = new JSONParser();
        jsonParser.getCurrentMovies(MainActivity.this, new JSONParser.CurrentMovieResponseListener() {
            @Override
            public void onError(String message) {
                Toast.makeText(MainActivity.this, "Error", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onResponse(List<Movie> movieLists) {
            source = movieLists;

                Log.d("TAGG",source.toString());

                // calling constructor of adapter
                // with source list as a parameter
                adapter = new UpcomingRecyclerAdapter(source,MainActivity.this);

                // Set Horizontal Layout Manager
                // for Recycler view
                HorizontalLayout
                        = new LinearLayoutManager(
                        MainActivity.this,
                        LinearLayoutManager.HORIZONTAL,
                        false);
                recyclerView.setLayoutManager(HorizontalLayout);

                // Set adapter on recycler view
                recyclerView.setAdapter(adapter);

            }
        });




        //Declare bottom nav, and set correct option as selected, adapted from https://stackoverflow.com/questions/40202294/set-selected-item-in-android-bottomnavigationview
        BottomNavigationView bottomNavigationView = (BottomNavigationView) findViewById(R.id.bottom_navigation);
        bottomNavigationView.setSelectedItemId(R.id.page_1);


        //Setting up navigation to correct activity, adapted from https://suragch.medium.com/how-to-add-a-bottom-navigation-bar-in-android-958ed728ef6c
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.page_1:
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
                        Intent startMovieIntent = new Intent(getApplicationContext(), MovieActivity.class);
                        startActivity(startMovieIntent);
                        break;
                    case R.id.page_5:
                        Intent startNotifyIntent = new Intent(getApplicationContext(), NotifyActivity.class);
                        startActivity(startNotifyIntent);
                }
                return true;
            }
        });



    }


}