package com.example.moviebuddy.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
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
import com.example.moviebuddy.adapters.UpcomingNightRecyclerAdapter;
import com.example.moviebuddy.dataaccess.JSONParser;
import com.example.moviebuddy.fragments.DatePickerFragment;
import com.example.moviebuddy.fragments.TimePickerFragment;
import com.example.moviebuddy.model.GroupNight;
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
import java.util.List;

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
    String SQLID;

    String movieid, movietitle, groupname, groupid;

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

            Log.d("Worked",extras.toString());

            movieid = extras.getString("MOVIEID");
            Log.d("CHECKIDONMOVIENIGHT",movieid);
            movietitle = extras.getString("MOVIETITLE");
            tvSelectedMovie.setText(movietitle);
            etselectedMovie.setText(movietitle);
            etselectedMovie.setEnabled(false);
            btnAutoSuggest.setEnabled(false);


            groupname = extras.getString("GROUPNAME");
            tvCurrentGroup.setText(groupname);

            groupid = String.valueOf(extras.getLong("GROUPID"));
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
            }
        },groupid);


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