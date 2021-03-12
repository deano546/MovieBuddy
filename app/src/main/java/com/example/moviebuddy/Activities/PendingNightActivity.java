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
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.example.moviebuddy.R;
import com.example.moviebuddy.adapters.UpcomingNightRecyclerAdapter;
import com.example.moviebuddy.dataaccess.JSONParser;
import com.example.moviebuddy.fragments.DatePickerFragment;
import com.example.moviebuddy.fragments.TimePickerFragment;
import com.example.moviebuddy.model.GroupMember;
import com.example.moviebuddy.model.GroupNight;
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

public class PendingNightActivity extends AppCompatActivity implements DatePickerDialog.OnDateSetListener, TimePickerDialog.OnTimeSetListener {

    Button btnDate, btnTime, btnView, btnAccept, btnDecline, btnSuggest;
    TextView tvMovie, tvDate, tvTime, tvAccepted, tvDeclined, tvPending, tvAlreadyAccepted, tvCurrentGroup;
    String movieid, movietitle, date, time, returnedminute, returneddate, returnedmonth, groupid, groupnightid;
    int Counter;
    FirebaseAuth auth;
    FirebaseFirestore fStore;
    String SQLID;
    private List<GroupMember> groupMemberList = new ArrayList<>();
    private List<String> approvedusers = new ArrayList<>();
    private List<String> declinedusers = new ArrayList<>();
    private List<String> pendingusers = new ArrayList<>();

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
        tvCurrentGroup = findViewById(R.id.tvCurrentGroupPending);

        JSONParser jsonParser = new JSONParser();



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
            groupid = extras.getString("GROUPID");
            groupnightid = extras.getString("GROUPNIGHTID");
            tvMovie.setText(movietitle);
            date = extras.getString("DATE");
            time = extras.getString("TIME");
            tvDate.setText(date);
            tvTime.setText(time);
            tvCurrentGroup.setText(extras.getString("GROUPNAME"));
        }

        jsonParser.getGroupMembersforNight(PendingNightActivity.this, new JSONParser.getGroupMembersforNightResponseListener() {
            @Override
            public void onError(String message) {

            }

            @Override
            public void onResponse(List<GroupMember> memberlist) {
                groupMemberList = memberlist;
                for(GroupMember gmember: groupMemberList) {
                    if(gmember.getApproval().equals("True")) {
                        approvedusers.add(gmember.getUsername());
                    }
                    else if(gmember.getApproval().equals("False")) {
                        pendingusers.add(gmember.getUsername());
                    }
                    else if(gmember.getApproval().equals("Declined")) {
                        declinedusers.add(gmember.getUsername());
                    }
                }
                //Add to activity
                tvAccepted.setText("Accepted: " + approvedusers.toString().replace("[", "").replace("]", ""));
                tvPending.setText("Pending: " + pendingusers.toString().replace("[", "").replace("]", ""));
                tvDeclined.setText("Declined: " + declinedusers.toString().replace("[", "").replace("]", ""));
            }
        },groupnightid);


        btnView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(PendingNightActivity.this, MovieDetailActivity.class);
                intent.putExtra("id", Integer.parseInt(movieid));
                startActivity(intent);
            }
        });



        btnAccept.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                jsonParser.approveGroupNight(PendingNightActivity.this, new JSONParser.approveGroupNightResponseListener() {
                    @Override
                    public void onError(String message) {
                        Toast.makeText(PendingNightActivity.this, "Accepted!", Toast.LENGTH_SHORT).show();

                        jsonParser.getGroupNightApproval(PendingNightActivity.this, new JSONParser.getGroupNightApprovalResponseListener() {
                            @Override
                            public void onError(String message) {

                            }

                            @Override
                            public void onResponse(List<String> approvallist) {
                                Counter = 0;
                                Log.d("LOOPING",approvallist.toString());
                                Log.d("LOOPING",approvallist.size() + "");

                                for (String s : approvallist) {
                                    if(s.equals("True") || s.equals("Declined")) {
                                        Log.d("LOOPINGAPPROVAL",s);
                                        Counter +=1;
                                    }


                                }
                                if(Counter == approvallist.size()) {

                                    jsonParser.fullyApproveGroupNight(PendingNightActivity.this, new JSONParser.fullyApproveGroupNightResponseListener() {
                                        @Override
                                        public void onError(String message) {
                                            Toast.makeText(PendingNightActivity.this, "Fully Approved", Toast.LENGTH_SHORT).show();
                                        }

                                        @Override
                                        public void onResponse(String message) {

                                        }
                                    },groupnightid);

                                }

                            }
                        },groupnightid);

                    }

                    @Override
                    public void onResponse(String message) {

                    }
                },SQLID,groupnightid);
            }
        });

        btnDecline.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                jsonParser.rejectGroupNight(PendingNightActivity.this, new JSONParser.rejectGroupNightResponseListener() {
                    @Override
                    public void onError(String message) {
                        Toast.makeText(PendingNightActivity.this, "Accepted!", Toast.LENGTH_SHORT).show();

                        jsonParser.getGroupNightApproval(PendingNightActivity.this, new JSONParser.getGroupNightApprovalResponseListener() {
                            @Override
                            public void onError(String message) {

                            }

                            @Override
                            public void onResponse(List<String> approvallist) {
                                Counter = 0;


                                for (String s : approvallist) {

                                    if(s.equals("True") || s.equals("Declined")) {
                                        Log.d("LOOPINGAPPROVAL",s);
                                        Counter +=1;
                                    }

                                }
                                if(Counter == approvallist.size()) {

                                    jsonParser.fullyApproveGroupNight(PendingNightActivity.this, new JSONParser.fullyApproveGroupNightResponseListener() {
                                        @Override
                                        public void onError(String message) {
                                            Toast.makeText(PendingNightActivity.this, "Fully Approved", Toast.LENGTH_SHORT).show();
                                        }

                                        @Override
                                        public void onResponse(String message) {

                                        }
                                    },groupid);

                                }

                            }
                        },groupid);

                    }

                    @Override
                    public void onResponse(String message) {

                    }
                },SQLID,groupid);
            }
        });



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