package com.example.moviebuddy.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import com.example.moviebuddy.R;
import com.example.moviebuddy.adapters.CreateGroupUserListRecyclerAdapter;
import com.example.moviebuddy.adapters.UserListRecyclerAdapter;
import com.example.moviebuddy.dataaccess.UserDataAccess;
import com.example.moviebuddy.model.User;

import java.util.ArrayList;
import java.util.List;

public class CreateGroupActivity extends AppCompatActivity {


    RecyclerView rvUserList;
    RecyclerView.Adapter mAdapter;
    RecyclerView.LayoutManager layoutManager;
    List<User> userList = new ArrayList<User>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_group);

        rvUserList = findViewById(R.id.rvCreateGroupList);
        rvUserList.setHasFixedSize(true);

        layoutManager = new LinearLayoutManager(CreateGroupActivity.this);
        rvUserList.setLayoutManager(layoutManager);

        UserDataAccess uda = new UserDataAccess();
        userList = uda.getUsers();

        mAdapter = new CreateGroupUserListRecyclerAdapter(userList,CreateGroupActivity.this);
        rvUserList.setAdapter(mAdapter);

    }
}