package com.example.moviebuddy.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.moviebuddy.R;

public class MovieDetailActivity extends AppCompatActivity {

    TextView tvTitle;
    TextView tvYear;
    ImageView imPoster;
    String movieTitle;
    int movieYear;
    String imageurl;
    EditText etSynopsis;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_detail);

        //Take the extras from the intent passed from the movie activity as to display movie details here

        Intent intent = getIntent();
        movieTitle = intent.getStringExtra("title");
        movieYear = intent.getIntExtra("year",-1);
        imageurl = intent.getStringExtra("poster");

        tvTitle = findViewById(R.id.tvDetailTitle);
        tvYear = findViewById(R.id.tvDetailYear);
        imPoster = findViewById(R.id.imDetailPoster);
        etSynopsis = findViewById(R.id.etDetailSynopsis);

        tvTitle.setText(movieTitle);
        tvYear.setText(String.valueOf(movieYear));
        Glide.with(this).load(imageurl).into(imPoster);
        etSynopsis.setEnabled(false);

    }
}