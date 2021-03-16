package com.example.moviebuddy.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;

import com.example.moviebuddy.R;
import com.example.moviebuddy.adapters.ManageGroupRecyclerAdapter;
import com.example.moviebuddy.adapters.UpcomingNightRecyclerAdapter;
import com.example.moviebuddy.dataaccess.JSONParser;
import com.example.moviebuddy.model.Group;
import com.example.moviebuddy.model.GroupNight;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import org.jetbrains.annotations.NotNull;

import java.util.List;

public class ManageGroupsActivity extends AppCompatActivity {

    RecyclerView rvManageGroups;
    List<Group> groupsource;
    FirebaseAuth auth;
    FirebaseFirestore fStore;
    RecyclerView.LayoutManager RecyclerViewLayoutManager;
    ManageGroupRecyclerAdapter adapter;
    LinearLayoutManager Layout;
    String SQLID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_groups);

        rvManageGroups = findViewById(R.id.rvManageGroup);
        auth = FirebaseAuth.getInstance();
        fStore = FirebaseFirestore.getInstance();
        JSONParser jsonParser = new JSONParser();
        String ID = auth.getCurrentUser().getUid();

        DocumentReference docRef = fStore.collection("Users").document(ID);
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull @NotNull Task<DocumentSnapshot> task) {
                if(task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if(document.exists()) {
                        SQLID = document.get("id").toString();


                        jsonParser.getGroupbyUser(ManageGroupsActivity.this, new JSONParser.getGroupResponseListener() {
                            @Override
                            public void onError(String message) {

                            }

                            @Override
                            public void onResponse(List<Group> groupList) {
                                groupsource = groupList;

                                adapter = new ManageGroupRecyclerAdapter(groupList,ManageGroupsActivity.this, SQLID);
                                RecyclerViewLayoutManager = new LinearLayoutManager(
                                        ManageGroupsActivity.this);
                                rvManageGroups.setLayoutManager(RecyclerViewLayoutManager);
                                rvManageGroups.setAdapter(adapter);
                            }
                        },SQLID);

                    }
                }
            }
        });


    }
}