package com.example.moviebuddy.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Toast;

import com.example.moviebuddy.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class WatchListActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_watch_list);

        BottomNavigationView bottomNavigationView;
        bottomNavigationView = (BottomNavigationView) findViewById(R.id.bottom_navigation);
        //bottomNavigationView.setOnNavigationItemSelectedListener(myNavigationItemListener);
        bottomNavigationView.setSelectedItemId(R.id.page_2);

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
}