package com.example.moviebuddy.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.example.moviebuddy.R;
import com.example.moviebuddy.dataaccess.JSONParser;
import com.example.moviebuddy.fragments.DatePickerFragment;
import com.example.moviebuddy.fragments.TimePickerFragment;
import com.example.moviebuddy.model.GroupsRatings;
import com.example.moviebuddy.model.Movie;
import com.example.moviebuddy.model.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import org.jetbrains.annotations.NotNull;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MovieNightActivity extends AppCompatActivity implements DatePickerDialog.OnDateSetListener, TimePickerDialog.OnTimeSetListener {

    //Declarations
    Button btnTime;
    Button btnDate;
    Button btnCreateNight;
    Button btnAutoSuggest;
    TextView tvSelectedDate, tvSelectedTime, tvSelectedMovie, tvCurrentGroup;
    EditText etselectedMovie;
    String returnedminute;
    String returneddate;
    String returnedmonth;
    List<User> groupmembers = new ArrayList<>();
    String groupnightid;
    FirebaseAuth auth;
    FirebaseFirestore fStore;
    int counter = 0;
    String SQLID;
    String check;
    String genre;
    Map<String, Double> mapratings = new HashMap<String, Double>();
    List<Movie> movieList = new ArrayList<>();
    String maxormin;
    Boolean approval;

    String unapproveddate, unapprovedtime;



    String movieid, movietitle, groupname, groupid;



    final String[] Options = {"One We'd Like", "One different than usual"};
    AlertDialog.Builder window;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_night);

        //Assignments
        btnTime = findViewById(R.id.btnSelectTime);
        btnDate = findViewById(R.id.btnSelectDate);
        btnCreateNight = findViewById(R.id.btnCreateNight);
        tvSelectedDate = findViewById(R.id.tvSelectedDate);
        tvSelectedTime = findViewById(R.id.tvSelectedTime);
        tvSelectedMovie = findViewById(R.id.tvSelectedMovie);
        etselectedMovie = findViewById((R.id.etSelectedMovieforNight));
        btnAutoSuggest = findViewById(R.id.btnAutoSuggest);
        tvCurrentGroup = findViewById(R.id.tvCurrentGroup);

        btnCreateNight.setEnabled(false);

        auth = FirebaseAuth.getInstance();
        fStore = FirebaseFirestore.getInstance();

        String ID = auth.getCurrentUser().getUid();

        DocumentReference docRef = fStore.collection("Users").document(ID);
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull @NotNull Task<DocumentSnapshot> task) {
                if(task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if(document.exists()) {
                        SQLID = document.get("id").toString();
                    }
                }
            }
        });

        Bundle extras = getIntent().getExtras();

        if (extras != null) {
            if(extras.containsKey("MOVIEID")) {
                Toast.makeText(this, "Yeah", Toast.LENGTH_SHORT).show();
                movieid = extras.getString("MOVIEID");
                movietitle = extras.getString("MOVIETITLE");
                tvSelectedMovie.setText(movietitle);
                etselectedMovie.setText(movietitle);
                etselectedMovie.setEnabled(false);
                btnAutoSuggest.setEnabled(false);
            }

            //Log.d("Worked",extras.toString());


            //Log.d("CHECKIDONMOVIENIGHT",movieid);
            groupname = extras.getString("GROUPNAME");
            tvCurrentGroup.setText(groupname);
            Log.d("MOVIENIGHTGROUPID",extras.getLong("GROUPID") + "");
            groupid = extras.getString("GROUPID");
            // and get whatever type user account id is

        }
        else {
            Log.d("Did not work","lame");
        }


        //adapted from https://www.youtube.com/watch?v=33BFCdL0Di0
        btnDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogFragment datePicker = new DatePickerFragment();
                datePicker.show(getSupportFragmentManager(), "date picker");

            }
        });


        //adapted from https://www.youtube.com/watch?v=QMwaNN_aM3U
        btnTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogFragment timePicker = new TimePickerFragment();
                timePicker.show(getSupportFragmentManager(), "time picker");
            }
        });



        JSONParser jsonParser = new JSONParser();

        jsonParser.getGroupMembers(MovieNightActivity.this, new JSONParser.getGroupMembersResponseListener() {
            @Override
            public void onError(String message) {

            }

            @Override
            public void onResponse(List<User> userlist) {
                    groupmembers = userlist;
                    btnCreateNight.setEnabled(true);
                    btnAutoSuggest.setEnabled(true);
            }
        },groupid);


        btnAutoSuggest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                window = new AlertDialog.Builder(MovieNightActivity.this);
                window.setTitle("Pick please");
                window.setItems(Options, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if(which == 0){
                            jsonParser.getGroupRatings(MovieNightActivity.this, new JSONParser.getGroupRatingsResponseListener() {
                                @Override
                                public void onError(String message) {

                                }

                                @Override
                                public void onResponse(List<GroupsRatings> ratinglist) {
                                    Log.d("CHECKRATINGLIST",ratinglist.toString());

                                    maxormin = "Max";
                                    Toast.makeText(MovieNightActivity.this, "0", Toast.LENGTH_SHORT).show();
                                    genre = breakoutGenres(ratinglist, maxormin);
                                    getSuggestion(genre);

                                }
                            },groupid);

                        }else if(which == 1){
                            jsonParser.getGroupRatings(MovieNightActivity.this, new JSONParser.getGroupRatingsResponseListener() {
                                @Override
                                public void onError(String message) {

                                }

                                @Override
                                public void onResponse(List<GroupsRatings> ratinglist) {
                                    Log.d("CHECKRATINGLIST",ratinglist.toString());

                                    maxormin = "Min";
                                    Toast.makeText(MovieNightActivity.this, "1", Toast.LENGTH_SHORT).show();
                                    genre = breakoutGenres(ratinglist, maxormin);
                                    getSuggestion(genre);

                                }
                            },groupid);

                        }else{
                            //theres an error in what was selected
                            Toast.makeText(getApplicationContext(), "Hmmm I messed up. I detected that you clicked on : " + which + "?", Toast.LENGTH_LONG).show();
                        }
                    }
                });

                window.show();








            }
        });


        btnCreateNight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Log.d("MovieNightGroup",groupid);
                Log.d("MovieNightMovie",movieid);
                Log.d("MovieNightTime",tvSelectedTime.getText().toString());
                Log.d("MovieNightDate",tvSelectedDate.getText().toString());

                jsonParser.createMovieNight(MovieNightActivity.this, new JSONParser.CreateMovieNightResponseListener() {
                    @Override
                    public void onError(String message) {
                        Toast.makeText(MovieNightActivity.this, "Night Created!", Toast.LENGTH_SHORT).show();


                        jsonParser.getSpecificNight(MovieNightActivity.this, new JSONParser.getSpecificNightResponseListener() {
                            @Override
                            public void onError(String message) {

                            }

                            @Override
                            public void onResponse(String id) {
                                groupnightid = id;
                                Log.d("12GROUPNIGHTID",groupnightid);
                                Log.d("12GMembers",groupmembers.toString());

                                for (User user : groupmembers)
                                {
                                    jsonParser.insertGroupApproval(MovieNightActivity.this, new JSONParser.insertGroupApprovalResponseListener() {
                                        @Override
                                        public void onError(String message) {
                                            Log.d("12PRINTINGINSERTAPPROVAL",message);

                                            //Approve for owner
                                            jsonParser.approveGroupNight(MovieNightActivity.this, new JSONParser.approveGroupNightResponseListener() {
                                                @Override
                                                public void onError(String message) {
                                                    Log.d("OnError",message);
                                                }

                                                @Override
                                                public void onResponse(String message) {
                                                    Log.d("OnReponse",message);
                                                }
                                            },SQLID,groupnightid);

                                        }

                                        @Override
                                        public void onResponse(String friendRequest) {

                                        }
                                    },groupnightid,String.valueOf(user.getId()));
                                }

                            }
                        },groupid,movieid);

                        Intent intent = new Intent(MovieNightActivity.this,MainActivity.class);
                        startActivity(intent);
                    }

                    @Override
                    public void onResponse(String username) {
                        Toast.makeText(MovieNightActivity.this, "Response", Toast.LENGTH_SHORT).show();
                    }
                },groupid,movieid,tvSelectedDate.getText().toString(),tvSelectedTime.getText().toString(),SQLID);
            }
        });


    }

    private String breakoutGenres(List<GroupsRatings> ratinglist, String decider) {

        double actionaverage = getAverage("Action",ratinglist);
        double adventureaverage = getAverage("Adventure",ratinglist);
        double animationaverage = getAverage("Animation",ratinglist);
        double comedyaverage = getAverage("Comedy",ratinglist);
        double crimeaverage = getAverage("Crime",ratinglist);
        double documentaryaverage = getAverage("Documentary",ratinglist);
        double dramaaverage = getAverage("Drama",ratinglist);
        double familyaverage = getAverage("Family",ratinglist);
        double fantasyaverage = getAverage("Fantasy",ratinglist);
        double horroraverage = getAverage("Horror",ratinglist);
        double musicaverage = getAverage("Music",ratinglist);
        double mysteryaverage = getAverage("Mystery",ratinglist);
        double romanceaverage = getAverage("Romance",ratinglist);
        double scienceaverage = getAverage("Science Fiction",ratinglist);
        double thrilleraverage = getAverage("Thriller",ratinglist);
        double tvmovieaverage = getAverage("TV Movie",ratinglist);
        double waraverage = getAverage("War",ratinglist);
        double westernaverage = getAverage("Western",ratinglist);


        mapratings.put("Action",actionaverage);
        mapratings.put("Adventure",adventureaverage);
        mapratings.put("Animation",animationaverage);
        mapratings.put("Comedy",comedyaverage);
        mapratings.put("Crime",crimeaverage);
        mapratings.put("Documentary",documentaryaverage);
        mapratings.put("Drama",dramaaverage);
        mapratings.put("Family",familyaverage);
        mapratings.put("Fantasy",fantasyaverage);
        mapratings.put("Horror",horroraverage);
        mapratings.put("Music",musicaverage);
        mapratings.put("Mystery",mysteryaverage);
        mapratings.put("Romance",romanceaverage);
        mapratings.put("Science Fiction",scienceaverage);
        mapratings.put("Thriller",thrilleraverage);
        mapratings.put("TV Movie",tvmovieaverage);
        mapratings.put("War",waraverage);
        mapratings.put("Western",westernaverage);


        Map.Entry<String, Double> maxEntry = null;
        for (Map.Entry<String, Double> entry : mapratings.entrySet()) {
            if (maxEntry == null || entry.getValue() > maxEntry.getValue()) {
                maxEntry = entry;
            }
        }
        String maxKey = maxEntry.getKey();
        Log.d("CHECKHASH",mapratings.toString());
        Log.d("CHECKKEY",maxKey);

        Map.Entry<String, Double> min = null;
        for (Map.Entry<String, Double> entry : mapratings.entrySet()) {
            if (min == null || min.getValue() > entry.getValue()) {
                min = entry;
            }
        }
        String minKey = min.getKey();

        Log.d("CHECKLOWHASH",minKey);
        if(decider.equals("Max")) {
            return maxKey;
        }
        else {
            return minKey;
        }

    }

    //adapted from https://www.youtube.com/watch?v=QMwaNN_aM3U
    @Override
    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {

        if(minute > 1 && minute < 10 ) {
            returnedminute = "0" + minute;
            tvSelectedTime.setText(hourOfDay + "." + returnedminute);
        }
        else if(minute == 0) {
            returnedminute = "00";
            tvSelectedTime.setText(hourOfDay + "." + returnedminute);
        }
        else {
            tvSelectedTime.setText(hourOfDay + "." + minute);
        }


    }

    //adapted from https://www.youtube.com/watch?v=33BFCdL0Di0
    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        Calendar c = Calendar.getInstance();
        c.set(Calendar.YEAR, year);
        c.set(Calendar.MONTH, month);
        c.set(Calendar.DAY_OF_MONTH, dayOfMonth);
        String currentDateString = DateFormat.getDateInstance(DateFormat.SHORT).format(c.getTime());

        if(c.getTimeInMillis() < System.currentTimeMillis() - 10000) {
            Toast.makeText(this, "Please don't choose a past date", Toast.LENGTH_SHORT).show();
        }
        else {
            //Adding a 0 before the returned number if its less than 10
            if(dayOfMonth < 10) {
                returneddate = "0" + dayOfMonth;
            }
            else{
                returneddate = String.valueOf(dayOfMonth);
            }
            //Also have to add one onto the month value due to the index starting at 0
            if(month < 10) {
                month += 1;
                returnedmonth = "0" + month;
            }
            else {
                month +=1;
                returnedmonth = String.valueOf(month);
            }
            //Show only the last two digits of the year, eg 2021 is shown as 21
            String returnedyear = String.valueOf(year).substring(2,4);
            tvSelectedDate.setText(returneddate + "/" + returnedmonth + "/" + returnedyear);
        }


    }

public double getAverage(String genre, List<GroupsRatings> Ratings) {

        int genreCounter = 0;
        int ratingsTotal = 0;

        for (GroupsRatings rating: Ratings) {
            if(rating.getGenre().equals(genre)) {
                genreCounter +=1;
                ratingsTotal += Integer.parseInt(rating.getRating());
            }
        }

        if(genreCounter == 0) {
            return 0;
        }

        double genreAverage = ratingsTotal /(double) genreCounter;

    Log.d("SEEWHATSHAPPS",genre + " " + ratingsTotal + " " + genreCounter + " " + genreAverage);

        return  Math.round(genreAverage);

}


public Movie getSuggestion(String genre) {

        Movie movie2 = new Movie();

        JSONParser jsonParser = new JSONParser();
        jsonParser.getSuggestedMovie(MovieNightActivity.this, new JSONParser.SuggestedMovieResponseListener() {
            @Override
            public void onError(String message) {

            }

            @Override
            public void onResponse(Movie movie) {

            String check = checkifwatched(movie);

           // if(check.equals("Yes")) {
                //tvSelectedMovie.setText("Hi");
                //Toast.makeText(MovieNightActivity.this, movie.toString(), Toast.LENGTH_SHORT).show();
            //}


            }
        },genre,groupid,counter);

        return movie2;


}

public String checkifwatched(Movie movie) {


        JSONParser jsonParser = new JSONParser();

    jsonParser.checkifGroupWatched(MovieNightActivity.this, new JSONParser.checkifGroupWatchedResponseListener() {
        @Override
        public void onError(String message) {

        }

        @Override
        public void onResponse(String message) {

            if(message.equals("No")) {
                Toast.makeText(MovieNightActivity.this, movie.toString(), Toast.LENGTH_SHORT).show();
                movieid = String.valueOf(movie.getId());
                movietitle = movie.getTitle();
                tvSelectedMovie.setText(movietitle);
                etselectedMovie.setText(movietitle);
                etselectedMovie.setEnabled(false);
                //btnAutoSuggest.setEnabled(false);
            }
            else {
                counter += 1;
                getSuggestion(genre);
            }
            check = "something";

        }
    },groupid,String.valueOf(movie.getId()));

    return check;


}


}