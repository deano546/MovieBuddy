package com.example.moviebuddy.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.moviebuddy.R;
import com.example.moviebuddy.dataaccess.JSONParser;
import com.example.moviebuddy.model.Movie;
import com.example.moviebuddy.model.UserMovie;

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
    Button btnRate;
    RatingBar rbRateMovie;
    Button btnAddtoWatchlist;
    int userrating;
    boolean onWatchlist;
    boolean known;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_detail);

        //Take the extras from the intent passed from the movie activity as to display movie details here

        Intent intent = getIntent();
        movieid = intent.getIntExtra("id",-1);
        onWatchlist = false;


        tvTitle = findViewById(R.id.tvDetailTitle);
        tvYear = findViewById(R.id.tvDetailYear);
        imPoster = findViewById(R.id.imDetailPoster);
        etSynopsis = findViewById(R.id.etDetailSynopsis);
        btnRate = findViewById(R.id.btnDetailRate);
        btnAddtoWatchlist = findViewById(R.id.btnDetailWatchlist);
        rbRateMovie = findViewById(R.id.rbRateMovie);

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

        jsonParser.getUserMovie(MovieDetailActivity.this, new JSONParser.GetUserMovieResponseListener() {
            @Override
            public void onError(String message) {
                Log.d("Tag",message);
            }

            @Override
            public void onResponse(UserMovie usermovie) {
                Log.d("UMSTRING",usermovie.toString());
                userrating = usermovie.getRating();
                rbRateMovie.setRating(userrating);
                String test = usermovie.getOnWatchlist();
                if(test.equals("Yes")) {
                    onWatchlist = true;
                    btnAddtoWatchlist.setText("Mark as Watched");

                }
                else {
                    Log.d("WTH",usermovie.getOnWatchlist());
                }
                known = usermovie.isKnown();

            }
        },1,movieid);

        tvTitle = findViewById(R.id.tvDetailTitle);
        tvYear = findViewById(R.id.tvDetailYear);
        imPoster = findViewById(R.id.imDetailPoster);
        etSynopsis = findViewById(R.id.etDetailSynopsis);
        tvDetailLength = findViewById(R.id.tvDetailLength);

        btnRate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //If known, update rating, if not, insert a rating
                if(known == false) {
                    if(rbRateMovie.getRating() != 0) {
                        jsonParser.createMovieRating(MovieDetailActivity.this, new JSONParser.CreateMovieRatingResponseListener() {
                            @Override
                            public void onError(String message) {

                            }

                            @Override
                            public void onResponse(String message) {

                            }
                        },1,movieid,Math.round(rbRateMovie.getRating()));
                    }

                }
                else {
                    if(rbRateMovie.getRating() != 0) {
                        jsonParser.updateMovieRating(MovieDetailActivity.this, new JSONParser.UpdateMovieRatingResponseListener() {
                            @Override
                            public void onError(String message) {

                            }

                            @Override
                            public void onResponse(String message) {

                            }
                        },1,movieid,Math.round(rbRateMovie.getRating()));
                    }

                }
            }
        });


        btnAddtoWatchlist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onWatchlist == true) {

                    if(rbRateMovie.getRating() != 0) {

                        jsonParser.markAsWatched(MovieDetailActivity.this, new JSONParser.MarkWatchedResponseListener() {
                            @Override
                            public void onError(String message) {

                            }

                            @Override
                            public void onResponse(String message) {

                            }
                        },1,movieid,Math.round(rbRateMovie.getRating()));

                    }
                    else {
                        Toast.makeText(MovieDetailActivity.this, "Please rate the movie", Toast.LENGTH_SHORT).show();
                    }


                }
                else {
                    //TODO
                    //Add to Watchlist, insert if knew, if not, button should be disabled, change text to already watched
                }
            }
        });



        etSynopsis.setFocusableInTouchMode(false);
        etSynopsis.clearFocus();

    }
}