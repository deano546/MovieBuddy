package com.example.moviebuddy.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import com.example.moviebuddy.R;
import com.example.moviebuddy.adapters.MovieListRecyclerAdapter;
import com.example.moviebuddy.adapters.UserListRecyclerAdapter;
import com.example.moviebuddy.dataaccess.UserDataAccess;
import com.example.moviebuddy.model.Movie;
import com.example.moviebuddy.model.User;

import java.util.ArrayList;
import java.util.List;

public class FriendListActivity extends AppCompatActivity {


    RecyclerView rvUserList;
    RecyclerView.Adapter mAdapter;
    RecyclerView.LayoutManager layoutManager;
    List<User> userList = new ArrayList<User>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friend_list);


        rvUserList = findViewById(R.id.rvUsers);
        rvUserList.setHasFixedSize(true);

        layoutManager = new LinearLayoutManager(FriendListActivity.this);
        rvUserList.setLayoutManager(layoutManager);

        UserDataAccess uda = new UserDataAccess();
        userList = uda.getUsers();

        mAdapter = new UserListRecyclerAdapter(userList,FriendListActivity.this);
        rvUserList.setAdapter(mAdapter);
    }


    }
