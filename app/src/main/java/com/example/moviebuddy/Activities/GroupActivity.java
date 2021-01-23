package com.example.moviebuddy.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.example.moviebuddy.R;
import com.example.moviebuddy.adapters.GroupListRecyclerAdapter;
import com.example.moviebuddy.dataaccess.MovieDataAccess;
import com.example.moviebuddy.model.GroupNight;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;
import java.util.List;

public class GroupActivity extends AppCompatActivity {



    RecyclerView rvGroupList;
    RecyclerView.Adapter mAdapter;
    RecyclerView.LayoutManager layoutManager;
    List<GroupNight> groupList = new ArrayList<GroupNight>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group);


        Button btnFriendList = findViewById(R.id.btnFriendList);
        ImageView imgAddGroup = findViewById(R.id.imgAddGroup);
        rvGroupList = findViewById(R.id.rvGroupList);

        rvGroupList.setHasFixedSize(true);

        layoutManager = new LinearLayoutManager(GroupActivity.this);
        rvGroupList.setLayoutManager(layoutManager);


        MovieDataAccess dataAccess = new MovieDataAccess();
        groupList = dataAccess.getNight();
        mAdapter = new GroupListRecyclerAdapter(groupList, GroupActivity.this);
        rvGroupList.setAdapter(mAdapter);

        //launch create group activity
        imgAddGroup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(GroupActivity.this, CreateGroupActivity.class);
                startActivity(intent);
            }
        });

        //launch friend list activity
        btnFriendList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(GroupActivity.this, FriendListActivity.class);
                startActivity(intent);
            }
        });

        //Declare bottom nav, and set correct option as selected, adapted from https://stackoverflow.com/questions/40202294/set-selected-item-in-android-bottomnavigationview
        BottomNavigationView bottomNavigationView = (BottomNavigationView) findViewById(R.id.bottom_navigation);
        bottomNavigationView.setSelectedItemId(R.id.page_3);


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
}