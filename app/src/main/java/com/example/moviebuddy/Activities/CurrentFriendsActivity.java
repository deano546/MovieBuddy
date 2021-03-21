package com.example.moviebuddy.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;

import com.example.moviebuddy.R;
import com.example.moviebuddy.adapters.CreateGroupUserListRecyclerAdapter;
import com.example.moviebuddy.adapters.CurrentFriendsRecyclerAdapter;
import com.example.moviebuddy.dataaccess.JSONParser;
import com.example.moviebuddy.model.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;

public class CurrentFriendsActivity extends AppCompatActivity {

    //Declarations
    List<User> userList = new ArrayList<>();
    FirebaseAuth auth;
    RecyclerView rvcurrentfriends;
    FirebaseFirestore fStore;
    String SQLID;
    String username;
    CurrentFriendsRecyclerAdapter mAdapter;
    RecyclerView.LayoutManager layoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_current_friends);

        //Assignments
        rvcurrentfriends = findViewById(R.id.rvCurrentFriends);
        auth = FirebaseAuth.getInstance();
        fStore = FirebaseFirestore.getInstance();
        JSONParser jsonParser = new JSONParser();

        String ID = auth.getCurrentUser().getUid();

        DocumentReference docRef = fStore.collection("Users").document(ID);
        //Show the friends of the current user
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if(task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        SQLID = document.get("id").toString();
                        username = document.get("username").toString();

                        //This method retrieves all the users friends, so they can be displayed in the recycler view
                        jsonParser.getFriendsbyID(CurrentFriendsActivity.this, new JSONParser.GetFriendsResponseListener() {
                            @Override
                            public void onError(String message) {

                            }

                            @Override
                            public void onResponse(List<User> userlist) {
                                Log.d("userlistcurrent",userlist.toString());
                                userList = userlist;
                                setUpRecycler();
                            }
                        },Integer.parseInt(SQLID));

                    }
                }

            }
        });
        
    }
    //Display Recycler View
    private void setUpRecycler() {
        rvcurrentfriends.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(CurrentFriendsActivity.this);
        rvcurrentfriends.setLayoutManager(layoutManager);
        mAdapter = new CurrentFriendsRecyclerAdapter(userList,CurrentFriendsActivity.this,SQLID);
        rvcurrentfriends.setAdapter(mAdapter);

    }
}