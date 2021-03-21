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
import android.widget.TextView;

import com.example.moviebuddy.R;
import com.example.moviebuddy.adapters.FriendRequestListRecyclerAdapter;
import com.example.moviebuddy.adapters.GroupListRecyclerAdapter;
import com.example.moviebuddy.adapters.UnApprovedGroupNightRecyclerAdapter;
import com.example.moviebuddy.dataaccess.JSONParser;
import com.example.moviebuddy.model.Group;
import com.example.moviebuddy.model.GroupNight;
import com.example.moviebuddy.model.User;
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

import static android.view.View.VISIBLE;

public class NotifyActivity extends AppCompatActivity {

    //Declarations
    RecyclerView rvRequestList;
    FriendRequestListRecyclerAdapter mAdapter;
    RecyclerView.LayoutManager layoutManager;
    RecyclerView rvMovieNights;
    RecyclerView.Adapter mAdapterNights;
    RecyclerView.LayoutManager layoutNights;
    List<User> userList1 = new ArrayList<>();
    List<GroupNight> groupNights = new ArrayList<>();
    FirebaseAuth auth;
    FirebaseFirestore fStore;
    String SQLID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notify);

        //Assignments
        TextView tvNoFriendRequests = findViewById(R.id.tvNoFriendRequests);
        tvNoFriendRequests.setVisibility(View.INVISIBLE);

        rvRequestList = findViewById(R.id.rvFriendRequests);
        rvMovieNights = findViewById(R.id.rvMovieNights);

        auth = FirebaseAuth.getInstance();
        fStore = FirebaseFirestore.getInstance();
        String ID = auth.getCurrentUser().getUid();
        JSONParser jsonParser = new JSONParser();

        //Getting any pending friend requests for the current user
        DocumentReference docRef = fStore.collection("Users").document(ID);
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull @NotNull Task<DocumentSnapshot> task) {
                if(task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if(document.exists()) {
                        SQLID = document.get("id").toString();

                        jsonParser.getFriendRequests(NotifyActivity.this, new JSONParser.getFriendRequestsResponseListener() {
                            @Override
                            public void onError(String message) {
                                Log.d("IWANNASEE2",message);
                            }

                            @Override
                            public void onResponse(List<User> userlist) {
                                userList1 = userlist;
                                if(userlist.isEmpty()) {
                                    tvNoFriendRequests.setVisibility(VISIBLE);
                                }
                                else {
                                    setUpRecycler();
                                }

                            }
                        },SQLID);


                        //Gets any movie nights the user has not responded to
                        jsonParser.getUnapprovedNights(NotifyActivity.this, new JSONParser.getUnapprovedNightsResponseListener() {
                            @Override
                            public void onError(String message) {

                            }

                            @Override
                            public void onResponse(List<GroupNight> approvallist) {
                                groupNights = approvallist;
                                Log.d("56CheckGroupString",groupNights.toString());
                                setUpSecondRecycler();
                            }
                        },SQLID);

                    }
                }
            }
        });

        //Declare bottom nav, and set correct option as selected, adapted from https://stackoverflow.com/questions/40202294/set-selected-item-in-android-bottomnavigationview
        BottomNavigationView bottomNavigationView = (BottomNavigationView) findViewById(R.id.bottom_navigation);
        bottomNavigationView.setSelectedItemId(R.id.page_5);


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
                        Intent startMovieIntent = new Intent(getApplicationContext(), MovieActivity.class);
                        startActivity(startMovieIntent);
                        break;
                    case R.id.page_5:
                    break;}
                return true;
            }
        });
    }


    //Display the two recycler views
    public void setUpRecycler() {
        rvRequestList.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(NotifyActivity.this);
        rvRequestList.setLayoutManager(layoutManager);
        mAdapter = new FriendRequestListRecyclerAdapter(userList1, NotifyActivity.this,SQLID);
        rvRequestList.setAdapter(mAdapter);
    }

    public void setUpSecondRecycler() {
        rvMovieNights.setHasFixedSize(true);
        layoutNights = new LinearLayoutManager(NotifyActivity.this);
        rvMovieNights.setLayoutManager(layoutNights);
        mAdapterNights = new UnApprovedGroupNightRecyclerAdapter(groupNights,NotifyActivity.this,SQLID);
        rvMovieNights.setAdapter(mAdapterNights);
    }
}