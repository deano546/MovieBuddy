package com.example.moviebuddy.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.moviebuddy.R;
import com.example.moviebuddy.dataaccess.JSONParser;
import com.example.moviebuddy.model.GroupMember;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class UpcomingNightActivity extends AppCompatActivity {


    TextView tvGroup, tvMovie, tvDate, tvTime, tvApproved, tvDeclined;
    Button btnViewMovie, btnInvis;
    String movieid, movietitle, date, time, returnedminute, returneddate, returnedmonth, groupid, groupnightid;
    int Counter;
    FirebaseAuth auth;
    FirebaseFirestore fStore;
    String SQLID;
    private List<GroupMember> groupMemberList = new ArrayList<>();
    private List<String> approvedusers = new ArrayList<>();
    private List<String> declinedusers = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upcoming_night);
        tvGroup = findViewById(R.id.tvCurrentGroupApproved);
        tvMovie = findViewById(R.id.tvSelectedMovieApproved);
        tvDate = findViewById(R.id.tvSelectedDateApproved);
        tvTime = findViewById(R.id.tvSelectedTimeApproved);
        tvApproved = findViewById(R.id.tvAcceptedApproved);
        tvDeclined = findViewById(R.id.tvDeclinedApproved);
        btnViewMovie = findViewById(R.id.btnViewApproved);
        btnInvis = findViewById(R.id.btnSuggestChangeInvis);
        btnInvis.setVisibility(View.INVISIBLE);

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
            tvGroup.setText(extras.getString("GROUPNAME"));
        }

        jsonParser.getGroupMembersforNight(UpcomingNightActivity.this, new JSONParser.getGroupMembersforNightResponseListener() {
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
                    else if(gmember.getApproval().equals("Declined")) {
                        declinedusers.add(gmember.getUsername());
                    }
                }
                //Add to activity
                tvApproved.setText("Accepted: " + approvedusers.toString().replace("[", "").replace("]", ""));
                if(declinedusers.size() == 0) {
                    tvDeclined.setText("Declined: No one!");
                }
                else {
                    tvDeclined.setText("Declined: " + declinedusers.toString().replace("[", "").replace("]", ""));
                }

            }
        },groupnightid);


        btnViewMovie.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(UpcomingNightActivity.this, MovieDetailActivity.class);
                intent.putExtra("id", Integer.parseInt(movieid));
                startActivity(intent);
            }
        });




    }
}