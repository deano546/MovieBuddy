package com.example.moviebuddy.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.LinearLayout;
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

import static android.view.View.VISIBLE;

public class PendingNightActivity extends AppCompatActivity implements DatePickerDialog.OnDateSetListener, TimePickerDialog.OnTimeSetListener {

    //Declarations
    Button btnDate, btnTime, btnView, btnAccept, btnDecline, btnSuggest, btnDeleteNight;
    TextView tvMovie, tvDate, tvTime, tvAccepted, tvDeclined, tvPending, tvAlreadyAccepted, tvCurrentGroup, tvSuggestDate, tvSuggestTime;
    String movieid, movietitle, date, time, returnedminute, returneddate, returnedmonth, groupid, groupnightid, genre;
    int Counter;
    FirebaseAuth auth;
    FirebaseFirestore fStore;
    String SQLID;
    private List<GroupMember> groupMemberList = new ArrayList<>();
    private List<String> approvedusers = new ArrayList<>();
    private List<String> declinedusers = new ArrayList<>();
    private List<String> pendingusers = new ArrayList<>();
    boolean timeselected, dateselected;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pending_night);

        //Assignments
        timeselected =false;
        dateselected = false;
        tvSuggestDate = findViewById(R.id.tvSuggestDate);
        tvSuggestTime = findViewById(R.id.tvSuggestTime);
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
        btnDeleteNight = findViewById(R.id.btnDeleteNight);

        btnDeleteNight.setVisibility(View.INVISIBLE);

        tvAlreadyAccepted.setVisibility(View.INVISIBLE);
        btnSuggest.setAlpha(.5f);
        btnSuggest.setClickable(false);


        tvSuggestTime.setVisibility(View.INVISIBLE);
        tvSuggestDate.setVisibility(View.INVISIBLE);

        JSONParser jsonParser = new JSONParser();

        //Getting extras on the intent
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
            genre = extras.getString("GENRE");
            tvDate.setText(date);
            tvTime.setText(time);
            tvCurrentGroup.setText(extras.getString("GROUPNAME"));
        }

        auth = FirebaseAuth.getInstance();
        fStore = FirebaseFirestore.getInstance();

        String ID = auth.getCurrentUser().getUid();

        //Check if the current user is the creator of the movie night, if they are, they can delete it
        DocumentReference docRef = fStore.collection("Users").document(ID);
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull @NotNull Task<DocumentSnapshot> task) {
                if(task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if(document.exists()) {
                        SQLID = document.get("id").toString();

                        if(SQLID.matches(extras.getString("CREATORID"))) {
                            btnDeleteNight.setVisibility(VISIBLE);
                            btnDeleteNight.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {

                                    //Dialog box

                                    AlertDialog.Builder alertName = new AlertDialog.Builder(PendingNightActivity.this, R.style.MyDialogTheme);
                                    // final EditText editTextName1 = new EditText(context);
                                    // add line after initializing editTextName1
                                    //editTextName1.setHint("Please Enter your email");
                                    //editTextName1.setTextColor(Color.GRAY);

                                    alertName.setTitle( Html.fromHtml("<font color='#70FFFFFF'>Are you sure you want to delete this movie night?</font>"));
                                    // titles can be used regardless of a custom layout or not
                                    // alertName.setView(editTextName1);
                                    LinearLayout layoutName = new LinearLayout(PendingNightActivity.this);
                                    layoutName.setOrientation(LinearLayout.VERTICAL);
                                    //layoutName.addView(editTextName1); // displays the user input bar
                                    alertName.setView(layoutName);

                                    alertName.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int whichButton) {

                                            jsonParser.deleteGroupNightpart1(PendingNightActivity.this, new JSONParser.deleteGroupNightpart1ResponseListener() {
                                        @Override
                                        public void onError(String message) {
                                            jsonParser.deleteGroupNightpart2(PendingNightActivity.this, new JSONParser.deleteGroupNightpart2ResponseListener() {
                                                @Override
                                                public void onError(String message) {
                                                    Toast.makeText(PendingNightActivity.this, "Movie Night Deleted!", Toast.LENGTH_SHORT).show();
                                                    Intent intent = new Intent(PendingNightActivity.this,GroupActivity.class);
                                                    startActivity(intent);
                                                }

                                                @Override
                                                public void onResponse(String message) {

                                                }
                                            },groupnightid);
                                        }

                                        @Override
                                        public void onResponse(String message) {

                                        }
                                    },groupnightid);

                                        }
                                    });

                                    alertName.setNegativeButton("No", new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int whichButton) {
                                            dialog.cancel(); // closes dialog alertName.show() // display the dialog
                                        }
                                    });
                                    alertName.show();
                                }
                            });
                        }

                    }
                }
            }
        });
        //https://www.youtube.com/watch?v=33BFCdL0Di0
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

        //Suggest a change to the date/time of the movie night, this set the other users to unapproved
        btnSuggest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(dateselected && timeselected) {

                    //Dialog Box
                    AlertDialog.Builder alertName = new AlertDialog.Builder(PendingNightActivity.this, R.style.MyDialogTheme);
                    // final EditText editTextName1 = new EditText(context);
                    // add line after initializing editTextName1
                    //editTextName1.setHint("Please Enter your email");
                    //editTextName1.setTextColor(Color.GRAY);

                    alertName.setTitle( Html.fromHtml("<font color='#70FFFFFF'>Are you sure you want to suggest this change?</font>"));
                    // titles can be used regardless of a custom layout or not
                    // alertName.setView(editTextName1);
                    LinearLayout layoutName = new LinearLayout(PendingNightActivity.this);
                    layoutName.setOrientation(LinearLayout.VERTICAL);
                    //layoutName.addView(editTextName1); // displays the user input bar
                    alertName.setView(layoutName);

                    alertName.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int whichButton) {

                            String date = tvSuggestDate.getText().toString();
                            int datecounter = date.length();
                            String passeddate = date.substring(21,datecounter);
                            String time = tvSuggestTime.getText().toString();
                            int timecounter = time.length();
                            String passedtime = time.substring(21,timecounter);

                            jsonParser.updateGroupNight(PendingNightActivity.this, new JSONParser.updateGroupNightResponseListener() {
                                @Override
                                public void onError(String message) {
                                    Toast.makeText(PendingNightActivity.this, "Date/Time Updated", Toast.LENGTH_SHORT).show();
                                    jsonParser.setapprovalsafterchange(PendingNightActivity.this, new JSONParser.setapprovalsafterchangeResponseListener() {
                                        @Override
                                        public void onError(String message) {
                                            jsonParser.approveGroupNight(PendingNightActivity.this, new JSONParser.approveGroupNightResponseListener() {
                                                @Override
                                                public void onError(String message) {
                                                    Intent intent = new Intent(PendingNightActivity.this,GroupActivity.class);
                                                    startActivity(intent);
                                                }

                                                @Override
                                                public void onResponse(String message) {

                                                }
                                            },SQLID,groupnightid);
                                        }

                                        @Override
                                        public void onResponse(String message) {

                                        }
                                    },groupnightid);
                                }

                                @Override
                                public void onResponse(String message) {
                                }
                            },groupnightid,passeddate,passedtime);

                        }
                    });

                    alertName.setNegativeButton("No", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int whichButton) {
                            dialog.cancel(); // closes dialog alertName.show() // display the dialog

                        }
                    });

                    alertName.show();
                }
                else{
                    Toast.makeText(PendingNightActivity.this, "Please choose both a date and time to suggest", Toast.LENGTH_SHORT).show();
                }
            }
        });

        //Displaying which users have accepted the movie night, which have declined, and which have not responded yet
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
                        Log.d("CHECKMEMBERS",gmember.toString());
                        Log.d("CHECKCURRENTID",SQLID);
                        if(gmember.getId().equals(SQLID)) {
                            tvAlreadyAccepted.setVisibility(VISIBLE);
                            btnSuggest.setEnabled(false);
                            btnAccept.setAlpha(.5f);
                            btnAccept.setClickable(false);
                            btnDecline.setAlpha(.5f);
                            btnDecline.setClickable(false);
                            btnDate.setAlpha(.5f);
                            btnDate.setClickable(false);
                            btnTime.setAlpha(.5f);
                            btnTime.setClickable(false);
                        }
                    }
                    else if(gmember.getApproval().equals("False")) {
                        pendingusers.add(gmember.getUsername());
                    }
                    else if(gmember.getApproval().equals("Declined")) {
                        declinedusers.add(gmember.getUsername());
                        if(gmember.getId().equals(SQLID)) {
                            tvAlreadyAccepted.setText("Already Declined!");
                            tvAlreadyAccepted.setVisibility(VISIBLE);
                            btnSuggest.setEnabled(false);
                            btnDecline.setAlpha(.5f);
                            btnDecline.setClickable(false);
                            btnDate.setAlpha(.5f);
                            btnDate.setClickable(false);
                            btnTime.setAlpha(.5f);
                            btnTime.setClickable(false);
                        }
                    }
                }
                //Add to activity
                tvAccepted.setText("Accepted: " + approvedusers.toString().replace("[", "").replace("]", ""));
                tvPending.setText("Pending: " + pendingusers.toString().replace("[", "").replace("]", ""));
                tvDeclined.setText("Declined: " + declinedusers.toString().replace("[", "").replace("]", ""));
            }
        },groupnightid);


        //View details of the movie
        btnView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(PendingNightActivity.this, MovieDetailActivity.class);
                intent.putExtra("id", Integer.parseInt(movieid));
                startActivity(intent);
            }
        });

        //Accept the movie night
        btnAccept.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                AlertDialog.Builder alertName = new AlertDialog.Builder(PendingNightActivity.this, R.style.MyDialogTheme);
                // final EditText editTextName1 = new EditText(context);
                // add line after initializing editTextName1
                //editTextName1.setHint("Please Enter your email");
                //editTextName1.setTextColor(Color.GRAY);

                alertName.setTitle( Html.fromHtml("<font color='#70FFFFFF'>Are you sure you want to Accept this Movie Night?</font>"));
                // titles can be used regardless of a custom layout or not
                // alertName.setView(editTextName1);
                LinearLayout layoutName = new LinearLayout(PendingNightActivity.this);
                layoutName.setOrientation(LinearLayout.VERTICAL);
                //layoutName.addView(editTextName1); // displays the user input bar
                alertName.setView(layoutName);

                alertName.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {

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
                                            jsonParser.addtoWatchlist(PendingNightActivity.this, new JSONParser.addtoWatchlistResponseListener() {
                                                @Override
                                                public void onError(String message) {
                                                    Toast.makeText(PendingNightActivity.this, "Fully Approved", Toast.LENGTH_SHORT).show();
                                                    Intent intent = new Intent(PendingNightActivity.this,MainActivity.class);
                                                    startActivity(intent);
                                                }

                                                @Override
                                                public void onResponse(String message) {

                                                }
                                            },Integer.parseInt(SQLID),Integer.parseInt(movieid),genre);

                                        }

                                        @Override
                                        public void onResponse(String message) {

                                        }
                                    },groupnightid);

                                }
                                else{
                                    jsonParser.addtoWatchlist(PendingNightActivity.this, new JSONParser.addtoWatchlistResponseListener() {
                                        @Override
                                        public void onError(String message) {
                                            //Toast.makeText(PendingNightActivity.this, "Fully Approved", Toast.LENGTH_SHORT).show();
                                            Intent intent = new Intent(PendingNightActivity.this,MainActivity.class);
                                            startActivity(intent);
                                        }

                                        @Override
                                        public void onResponse(String message) {

                                        }
                                    },Integer.parseInt(SQLID),Integer.parseInt(movieid),genre);
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

                alertName.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        dialog.cancel(); // closes dialog alertName.show() // display the dialog

                    }
                });
                alertName.show();
            }
        });


        //Decline attending the movie night
        btnDecline.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                AlertDialog.Builder alertName = new AlertDialog.Builder(PendingNightActivity.this, R.style.MyDialogTheme);
                // final EditText editTextName1 = new EditText(context);
                // add line after initializing editTextName1
                //editTextName1.setHint("Please Enter your email");
                //editTextName1.setTextColor(Color.GRAY);

                alertName.setTitle( Html.fromHtml("<font color='#70FFFFFF'>Are you sure you want to Decline this Movie Night?</font>"));
                // titles can be used regardless of a custom layout or not
                // alertName.setView(editTextName1);
                LinearLayout layoutName = new LinearLayout(PendingNightActivity.this);
                layoutName.setOrientation(LinearLayout.VERTICAL);
                //layoutName.addView(editTextName1); // displays the user input bar
                alertName.setView(layoutName);

                alertName.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {

                        jsonParser.rejectGroupNight(PendingNightActivity.this, new JSONParser.rejectGroupNightResponseListener() {
                    @Override
                    public void onError(String message) {
                        Toast.makeText(PendingNightActivity.this, "Rejected!", Toast.LENGTH_SHORT).show();

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
                                            //Toast.makeText(PendingNightActivity.this, "Fully Approved", Toast.LENGTH_SHORT).show();
                                            Intent intent = new Intent(PendingNightActivity.this,MainActivity.class);
                                            startActivity(intent);
                                        }

                                        @Override
                                        public void onResponse(String message) {

                                        }
                                    },groupnightid);

                                }else{
                                    Intent intent = new Intent(PendingNightActivity.this,MainActivity.class);
                                    startActivity(intent);
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

                alertName.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        dialog.cancel(); // closes dialog alertName.show() // display the dialog

                    }
                });
                alertName.show();
            }
        });
    }

    //https://www.youtube.com/watch?v=33BFCdL0Di0
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
            tvSuggestDate.append(" " + returneddate + "/" + returnedmonth + "/" + returnedyear);
            tvSuggestDate.setVisibility(VISIBLE);
            btnSuggest.setAlpha(1f);
            btnSuggest.setClickable(true);
            dateselected = true;
        }
    }

    //adapted from https://www.youtube.com/watch?v=QMwaNN_aM3U
    @Override
    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
        if(minute > 1 && minute < 10 ) {
            returnedminute = "0" + minute;
            tvSuggestTime.append(" " +hourOfDay + "." + returnedminute);
            tvSuggestTime.setVisibility(VISIBLE);
            btnSuggest.setAlpha(1f);
            btnSuggest.setClickable(true);
            timeselected = true;
        }
        else if(minute == 0) {
            returnedminute = "00";
            tvSuggestTime.append(" " +hourOfDay + "." + returnedminute);
            tvSuggestTime.setVisibility(VISIBLE);
            btnSuggest.setAlpha(1f);
            btnSuggest.setClickable(true);
            timeselected = true;
        }
        else {
            tvSuggestTime.append(" " +hourOfDay + "." + minute);
            tvSuggestTime.setVisibility(VISIBLE);
            btnSuggest.setAlpha(1f);
            btnSuggest.setClickable(true);
            timeselected = true;
        }

    }

}