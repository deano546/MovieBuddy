package com.example.moviebuddy.Activities;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.moviebuddy.R;
import com.example.moviebuddy.adapters.UpcomingNightRecyclerAdapter;
import com.example.moviebuddy.adapters.UpcomingRecyclerAdapter;
import com.example.moviebuddy.dataaccess.JSONParser;
import com.example.moviebuddy.dataaccess.MovieDataAccess;
import com.example.moviebuddy.model.GroupNight;
import com.example.moviebuddy.model.Movie;
import com.firebase.ui.auth.AuthUI;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import org.jetbrains.annotations.NotNull;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    //Code for setting up horizontal recycler view adapted from https://www.geeksforgeeks.org/android-horizontal-recyclerview-with-examples/

    // Recycler View object
    RecyclerView recyclerView;
    RecyclerView grouprecycler;

    // Array list for recycler view data source
    List<Movie> source;
    List<GroupNight> nightsource;
    FirebaseAuth auth;
    FirebaseFirestore fStore;

    // Layout Manager
    RecyclerView.LayoutManager RecyclerViewLayoutManager;
    RecyclerView.LayoutManager NightRecyclerManager;

    // adapter class object
    RecyclerView.Adapter adapter, nightadapter;

    // Linear Layout Manager
    LinearLayoutManager HorizontalLayout;
    LinearLayoutManager NightHorizontalLayout;
    String SQLID;

    Button btnLogOut;
    Button btnCredits;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnLogOut = findViewById(R.id.btnLogOut);
        TextView tvCurrentUser = findViewById(R.id.tvCurrentUser);
        btnCredits = findViewById(R.id.btnCredits);

        auth = FirebaseAuth.getInstance();
        fStore = FirebaseFirestore.getInstance();
        JSONParser jsonParser = new JSONParser();

        String ID = auth.getCurrentUser().getUid();

        btnCredits.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this,CreditsActivity.class);
                startActivity(intent);
            }
        });

        DocumentReference docRef = fStore.collection("Users").document(ID);
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull @NotNull Task<DocumentSnapshot> task) {
                if(task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if(document.exists()) {
                         SQLID = document.get("id").toString();
                         tvCurrentUser.setText(document.get("username").toString());

                        //This retrieves any upcoming user nights for the user by passing their ID
                        jsonParser.getMovieNightsbyID(MainActivity.this, new JSONParser.MovieNightResponseListener() {
                            @Override
                            public void onError(String message) {
                                Log.d("error1234",message);
                            }

                            @Override
                            public void onResponse(List<GroupNight> groupNight) {
                                //Once the data is retrieved, the recycler view is populated
                                nightsource = groupNight;
                                Log.d("CHECKINGNIGHTSOURCE",nightsource.toString());
                                nightadapter = new UpcomingNightRecyclerAdapter(nightsource,MainActivity.this);
                                NightHorizontalLayout = new LinearLayoutManager(
                                        MainActivity.this,
                                        LinearLayoutManager.HORIZONTAL,
                                        false);
                                grouprecycler.setLayoutManager(NightHorizontalLayout);
                                grouprecycler.setAdapter(nightadapter);


                            }
                        },Integer.parseInt(SQLID));

                    }
                }
            }
        });

        //Assignments
        grouprecycler = findViewById(R.id.rvGroupNight);
        NightRecyclerManager = new LinearLayoutManager(getApplicationContext());
        grouprecycler.setLayoutManager(NightRecyclerManager);


        btnLogOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AuthUI.getInstance()
                        .signOut(MainActivity.this)
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull @NotNull Task<Void> task) {
                                Intent intent = new Intent(MainActivity.this,RegisterActivity.class);
                                startActivity(intent);
                                finish();
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull @NotNull Exception e) {
                        Toast.makeText(MainActivity.this, "Sign Out Failed", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });









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

        //This displays the posters across the upper half, it calls the Movie DB API I have linked the application to
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