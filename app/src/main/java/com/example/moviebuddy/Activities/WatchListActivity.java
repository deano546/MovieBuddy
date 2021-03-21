package com.example.moviebuddy.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.Html;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.moviebuddy.R;
import com.example.moviebuddy.adapters.UpcomingNightRecyclerAdapter;
import com.example.moviebuddy.adapters.WatchListRecyclerAdapter;
import com.example.moviebuddy.dataaccess.JSONParser;
import com.example.moviebuddy.model.GroupNight;
import com.example.moviebuddy.model.Movie;
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


public class WatchListActivity extends AppCompatActivity {

    //Setting up recyclerview, mainly adapted from my IS4447 project
    RecyclerView rvMovieList;
    WatchListRecyclerAdapter mAdapter;
    RecyclerView.LayoutManager layoutManager;
    List<Movie> movieList = new ArrayList<>();
    FirebaseAuth auth;
    FirebaseFirestore fStore;
    String SQLID;
    TextView tvWatchlist, tvEmptyWatchlist;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_watch_list);


        //Assignments
        EditText etSearch = findViewById(R.id.etSearchWatchlist);

        auth = FirebaseAuth.getInstance();
        fStore = FirebaseFirestore.getInstance();
        String ID = auth.getCurrentUser().getUid();
        tvWatchlist = findViewById(R.id.tvWatchlistHeading);
        tvEmptyWatchlist = findViewById(R.id.tvEmptyWatchlist);
        tvEmptyWatchlist.setVisibility(View.INVISIBLE);

        rvMovieList = findViewById(R.id.rvWatchlist);

        Log.d("WATCHLISTSIZE4",movieList.size() + "");

        JSONParser jsonParser = new JSONParser();


        DocumentReference docRef = fStore.collection("Users").document(ID);
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull @NotNull Task<DocumentSnapshot> task) {
                if(task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if(document.exists()) {
                        SQLID = document.get("id").toString();
                        Log.d("WATCHLISTSIZE5",movieList.size() + "" + SQLID);


                        //This gets the watchlist of the current user by using their ID
                        jsonParser.getWatchlistbyID(WatchListActivity.this, new JSONParser.WatchListResponseListener() {
                            @Override
                            public void onError(String message) {
                                Log.d("WATCHLISTSIZE3",movieList.size() + "");
                                Log.d("TAG",message);

                            }

                            @Override
                            public void onResponse(List<Movie> movies) {
                                Log.d("WATCHLISTSIZE2",movieList.size() + "");
                                //Once list is retrieved, it is displayed in the recycler
                                if(movies.size() == 0) {
                                    tvEmptyWatchlist.setVisibility(VISIBLE);
                                }
                                else {
                                    movieList = movies;
                                    updateRecycler();

                                    //This calls the filter created in the adapter using a listener for the edit text
                                    etSearch.addTextChangedListener(new TextWatcher() {
                                        @Override
                                        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                                        }

                                        @Override
                                        public void onTextChanged(CharSequence s, int start, int before, int count) {
                                            mAdapter.getFilter().filter(s.toString());
                                        }

                                        @Override
                                        public void afterTextChanged(Editable s) {

                                        }
                                    });
                                }

                            }
                        },Integer.parseInt(SQLID));

                    }
                }
            }
        });




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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.search, menu);
        MenuItem searchItem = menu.findItem(R.id.action_search);
        SearchView searchView = (SearchView) searchItem.getActionView();
        searchView.setBackgroundColor(Color.DKGRAY);
        searchView.setImeOptions(EditorInfo.IME_ACTION_DONE);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }
            @Override
            public boolean onQueryTextChange(String newText) {
                mAdapter.getFilter().filter(newText);
                return false;
            }
        });
        return true;
    }

}