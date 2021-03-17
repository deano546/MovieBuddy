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
import android.widget.ImageView;

import com.example.moviebuddy.R;
import com.example.moviebuddy.adapters.GroupListRecyclerAdapter;
import com.example.moviebuddy.dataaccess.JSONParser;
import com.example.moviebuddy.model.GroupNight;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class GroupActivity extends AppCompatActivity {


    //Declarations
    RecyclerView rvGroupList;
    RecyclerView.Adapter mAdapter;
    RecyclerView.LayoutManager layoutManager;
    List<GroupNight> groupList1 = new ArrayList<>();
    FirebaseAuth auth;
    FirebaseFirestore fStore;
    String SQLID, movieid, movietitle, moviegenre;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group);

        auth = FirebaseAuth.getInstance();
        fStore = FirebaseFirestore.getInstance();
        String ID = auth.getCurrentUser().getUid();

        Bundle extras = getIntent().getExtras();

        if (extras != null) {
            movieid = String.valueOf(extras.getInt("MOVIEID"));
            movietitle = extras.getString("MOVIETITLE");
            moviegenre = extras.getString("GENRE");
        }


        //Assignments
        Button btnFriendList = findViewById(R.id.btnFriendList);
        Button btnManageGroups = findViewById(R.id.btnViewGroups);
        ImageView imgAddGroup = findViewById(R.id.imgAddGroup);
        rvGroupList = findViewById(R.id.rvGroupList);
        JSONParser jsonParser = new JSONParser();

        DocumentReference docRef = fStore.collection("Users").document(ID);
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull @NotNull Task<DocumentSnapshot> task) {
                if(task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if(document.exists()) {
                        SQLID = document.get("id").toString();

                        jsonParser.getUltramovienightbyuserid(GroupActivity.this, new JSONParser.getUltraGroupResponseListener() {
                            @Override
                            public void onError(String message) {

                            }

                            @Override
                            public void onResponse(List<GroupNight> groupList) {
                                Log.d("SUPERMARIORPG",groupList.toString());
                                groupList1 = groupList;
                                setUpRecycler();

                            }
                        },SQLID);

                    }
                }
            }
        });

        btnManageGroups.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(GroupActivity.this,ManageGroupsActivity.class);
                startActivity(intent);
            }
        });



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

    private void setUpRecycler() {
        rvGroupList.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(GroupActivity.this);
        rvGroupList.setLayoutManager(layoutManager);
        mAdapter = new GroupListRecyclerAdapter(groupList1, GroupActivity.this,movieid,movietitle,moviegenre);
        Log.d("UHOHLISTINACTIVITY",groupList1.toString());
        rvGroupList.setAdapter(mAdapter);
    }
}