package com.example.moviebuddy.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.example.moviebuddy.R;
import com.example.moviebuddy.fragments.DatePickerFragment;
import com.example.moviebuddy.fragments.TimePickerFragment;

import java.text.DateFormat;
import java.util.Calendar;

public class PendingNightActivity extends AppCompatActivity implements DatePickerDialog.OnDateSetListener, TimePickerDialog.OnTimeSetListener {

    Button btnDate, btnTime, btnView, btnAccept, btnDecline, btnSuggest;
    TextView tvMovie, tvDate, tvTime, tvAccepted, tvDeclined, tvPending, tvAlreadyAccepted;
    String movieid, movietitle, date, time, returnedminute, returneddate, returnedmonth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pending_night);

        btnDate = findViewById(R.id.btnSelectDatePending);
        btnTime = findViewById(R.id.btnSelectTimePending);
        btnView = findViewById(R.id.btnViewPending);
        btnAccept = findViewById(R.id.btnAccept);
        btnDecline = findViewById(R.id.btnDecline);
        btnSuggest = findViewById(R.id.btnSuggestNightChange);
        tvMovie = findViewById(R.id.tvSelectedMoviePending);
        tvDate = findViewById(R.id.tvSelectedDatePending);
        tvTime = findViewById(R.id.tvSelectedTimePending);
        tvAccepted = findViewById(R.id.tvAccepted);
        tvDeclined = findViewById(R.id.tvAccepted2);
        tvPending = findViewById(R.id.tvAccepted3);
        tvAlreadyAccepted = findViewById(R.id.tvalreadyAccepted);

        btnDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogFragment datePicker = new DatePickerFragment();
                datePicker.show(getSupportFragmentManager(), "date picker");
            }
        });

        btnTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogFragment timePicker = new TimePickerFragment();
                timePicker.show(getSupportFragmentManager(), "time picker");
            }
        });


        Bundle extras = getIntent().getExtras();

        if (extras != null) {
            movieid = extras.getString("MOVIEIDP");
            Log.d("CHECKPENDING",movieid);
            movietitle = extras.getString("MOVIETITLE");
            tvMovie.setText(movietitle);
            date = extras.getString("DATE");
            time = extras.getString("TIME");
            tvDate.setText(date);
            tvTime.setText(time);
        }

    }

    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        Calendar c = Calendar.getInstance();
        c.set(Calendar.YEAR, year);
        c.set(Calendar.MONTH, month);
        c.set(Calendar.DAY_OF_MONTH, dayOfMonth);
        String currentDateString = DateFormat.getDateInstance(DateFormat.SHORT).format(c.getTime());

        if (c.getTimeInMillis() < System.currentTimeMillis() - 10000) {
            Toast.makeText(this, "Please don't choose a past date", Toast.LENGTH_SHORT).show();
        } else {
            //Adding a 0 before the returned number if its less than 10
            if (dayOfMonth < 10) {
                returneddate = "0" + dayOfMonth;
            } else {
                returneddate = String.valueOf(dayOfMonth);
            }
            //Also have to add one onto the month value due to the index starting at 0
            if (month < 10) {
                month += 1;
                returnedmonth = "0" + month;
            } else {
                month += 1;
                returnedmonth = String.valueOf(month);
            }
            //Show only the last two digits of the year, eg 2021 is shown as 21
            String returnedyear = String.valueOf(year).substring(2, 4);
            tvDate.setText(returneddate + "/" + returnedmonth + "/" + returnedyear);
        }
    }

    @Override
    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
        if(minute > 1 && minute < 10 ) {
            returnedminute = "0" + minute;
            tvTime.setText(hourOfDay + "." + returnedminute);
        }
        else if(minute == 0) {
            returnedminute = "00";
            tvTime.setText(hourOfDay + "." + returnedminute);
        }
        else {
            tvTime.setText(hourOfDay + "." + minute);
        }
    }
}