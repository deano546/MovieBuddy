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

import static android.view.View.INVISIBLE;

public class MovieDetailActivity extends AppCompatActivity {

    //Declarations
    TextView tvTitle;
    TextView tvYear;
    ImageView imPoster;
    int movieid;
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

        //Assignments
        tvTitle = findViewById(R.id.tvDetailTitle);
        tvYear = findViewById(R.id.tvDetailYear);
        imPoster = findViewById(R.id.imDetailPoster);
        etSynopsis = findViewById(R.id.etDetailSynopsis);
        btnRate = findViewById(R.id.btnDetailRate);
        btnAddtoWatchlist = findViewById(R.id.btnDetailWatchlist);
        rbRateMovie = findViewById(R.id.rbRateMovie);
        tvDetailLength = findViewById(R.id.tvDetailLength);

        JSONParser jsonParser = new JSONParser();

        //Takes movie ID which was passed from the previous activity, and retrieves a movie Object from the API
        //Then the movie details are displayed on this activity
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

        //This allows me to determine if the user has either previously rated or watched the movie, or have it on their watchlist
        //The activity is edited depending on the result
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
                    //If the user has the movie on their watchlist, I change the text (and function) of the
                    onWatchlist = true;
                    btnAddtoWatchlist.setText("Mark as Watched");
                    btnRate.setVisibility(INVISIBLE);

                }
                else {
                    onWatchlist = false;
                }
                known = usermovie.isKnown();
                if(known && !onWatchlist) {
                    //If the user has previously watched the movie, the watchlist button is greyed out
                    btnAddtoWatchlist.setAlpha(.5f);
                    btnAddtoWatchlist.setClickable(false);
                    btnAddtoWatchlist.setText("Already Watched!");
                }

            }
        },1,movieid);



        //I determine if the user already has a record in the usermovie table associated with the current movie
        //This determines whether I am updating or inserting a record
        btnRate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //If known, update rating, if not, insert a rating
                if(!known) {
                    if(rbRateMovie.getRating() != 0) {
                        jsonParser.createMovieRating(MovieDetailActivity.this, new JSONParser.CreateMovieRatingResponseListener() {
                            @Override
                            public void onError(String message) {
                                Toast.makeText(MovieDetailActivity.this, "Movie Rated!", Toast.LENGTH_SHORT).show();
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
                                Toast.makeText(MovieDetailActivity.this, "Rating updated!", Toast.LENGTH_SHORT).show();
                            }

                            @Override
                            public void onResponse(String message) {

                            }
                        },1,movieid,Math.round(rbRateMovie.getRating()));
                    }

                }
            }
        });


        //This button changes function depending on the user's relationship with the movie
        //If its on their watchlist, it allows them to mark it as watched
        //If they have not previously watched or rated it, it allows them to add the movie to their watchlist
        btnAddtoWatchlist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onWatchlist) {
                    //This makes sure the user has given a rating to the movie they are marking as watched
                    if(rbRateMovie.getRating() != 0) {

                        jsonParser.markAsWatched(MovieDetailActivity.this, new JSONParser.MarkWatchedResponseListener() {
                            @Override
                            public void onError(String message) {
                                btnAddtoWatchlist.setText("Watched!");
                                btnAddtoWatchlist.setAlpha(.5f);
                                btnAddtoWatchlist.setClickable(false);
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
                    //Adding the movie to their watchlist
                    if(!known) {
                        jsonParser.addtoWatchlist(MovieDetailActivity.this, new JSONParser.addtoWatchlistResponseListener() {
                            @Override
                            public void onError(String message) {
                                Toast.makeText(MovieDetailActivity.this, "Error", Toast.LENGTH_SHORT).show();
                                btnAddtoWatchlist.setText("Added to Watchlist");
                            }

                            @Override
                            public void onResponse(String message) {
                            }
                        },1,movieid);
                    }
                }
            }
        });

        //This makes the synopsis text scrollable
        //adapted from https://stackoverflow.com/a/50234924
        etSynopsis.setFocusableInTouchMode(false);
        etSynopsis.clearFocus();

    }
}