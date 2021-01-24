package com.example.moviebuddy.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.moviebuddy.R;
import com.example.moviebuddy.dataaccess.JSONParser;
import com.example.moviebuddy.model.Movie;

public class MovieDetailActivity extends AppCompatActivity {

    TextView tvTitle;
    TextView tvYear;
    ImageView imPoster;
    String movieTitle;
    int movieYear;
    int movieid;
    String imageurl;
    EditText etSynopsis;
    Movie currentmovie;
    TextView tvDetailLength;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_detail);

        //Take the extras from the intent passed from the movie activity as to display movie details here

        Intent intent = getIntent();
        movieid = intent.getIntExtra("id",-1);


        tvTitle = findViewById(R.id.tvDetailTitle);
        tvYear = findViewById(R.id.tvDetailYear);
        imPoster = findViewById(R.id.imDetailPoster);
        etSynopsis = findViewById(R.id.etDetailSynopsis);

        JSONParser jsonParser = new JSONParser();

        jsonParser.getMoviebyID(MovieDetailActivity.this, new JSONParser.SelectedMovieResponseListener() {
            @Override
            public void onError(String message) {
                Toast.makeText(MovieDetailActivity.this, message, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onResponse(Movie movie) {
                currentmovie = movie;
                tvTitle.setText(currentmovie.getTitle());
                tvYear.setText(String.valueOf(currentmovie.getYear()));
                Glide.with(MovieDetailActivity.this).load("https://image.tmdb.org/t/p/w300/" + currentmovie.getImageurl()).into(imPoster);
                etSynopsis.setText(currentmovie.getOverview());
                tvDetailLength.setText(currentmovie.getRuntime());
            }
        },movieid);



        tvTitle = findViewById(R.id.tvDetailTitle);
        tvYear = findViewById(R.id.tvDetailYear);
        imPoster = findViewById(R.id.imDetailPoster);
        etSynopsis = findViewById(R.id.etDetailSynopsis);
        tvDetailLength = findViewById(R.id.tvDetailLength);



        etSynopsis.setFocusableInTouchMode(false);
        etSynopsis.clearFocus();

    }
}