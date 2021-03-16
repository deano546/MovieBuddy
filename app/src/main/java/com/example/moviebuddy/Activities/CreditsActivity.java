package com.example.moviebuddy.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.example.moviebuddy.R;

public class CreditsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_credits);

        ImageView imTMDB = findViewById(R.id.imTMDB);
        ImageView imflaticon = findViewById(R.id.imflaticon);

        Glide.with(this).load("https://i.imgur.com/mdKYSV4.png").into(imTMDB);
        Glide.with(this).load("https://i.imgur.com/muAJURt.png").into(imflaticon);
    }
}