package com.example.moviebuddy.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import com.example.moviebuddy.R;
import com.example.moviebuddy.adapters.AddtoGroupRecyclerAdapter;
import com.example.moviebuddy.adapters.CreateGroupUserListRecyclerAdapter;
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

public class AddtoGroupActivity extends AppCompatActivity {
    //Declarations
    TextView tvAddtoGroup;
    String groupid;
    RecyclerView rvUsersAddtoGroup;
    AddtoGroupRecyclerAdapter mAdapter;
    RecyclerView.LayoutManager layoutManager;
    String groupname;
    List<User> userList = new ArrayList<>();
    List<User> selectedfriends = new ArrayList<>();
    FirebaseAuth auth;
    FirebaseFirestore fStore;
    String SQLID;
    String username;
    String newuserid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_addto_group);
        //Assignments
        rvUsersAddtoGroup = findViewById(R.id.rvAddtoGroupList);
        tvAddtoGroup = findViewById(R.id.tvAddtoGroup);
        JSONParser jsonParser = new JSONParser();

        //Get extras on the intent
        Intent intent = getIntent();
        if(intent.hasExtra("GROUPID")) {
            tvAddtoGroup.setText(intent.getExtras().getString("GROUPNAME"));
            groupid = String.valueOf(intent.getExtras().getLong("GROUPID"));
        }
        auth = FirebaseAuth.getInstance();
        fStore = FirebaseFirestore.getInstance();

        String ID = auth.getCurrentUser().getUid();

        DocumentReference docRef = fStore.collection("Users").document(ID);

        //Get friends list of current user
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if(task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        SQLID = document.get("id").toString();
                        username = document.get("username").toString();

                        //This method retrieves all the users friends, so they can be displayed in the recycler view
                        jsonParser.getFriendsbyID(AddtoGroupActivity.this, new JSONParser.GetFriendsResponseListener() {
                            @Override
                            public void onError(String message) {

                            }

                            @Override
                            public void onResponse(List<User> userlist) {
                                userList = userlist;
                                setUpRecycler();
                            }
                        },Integer.parseInt(SQLID));

                    }
                }

            }
        });



    }
    //Displaying the recycler view
    private void setUpRecycler() {
        rvUsersAddtoGroup.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(AddtoGroupActivity.this);
        rvUsersAddtoGroup.setLayoutManager(layoutManager);
        mAdapter = new AddtoGroupRecyclerAdapter(userList,AddtoGroupActivity.this,groupid);
        rvUsersAddtoGroup.setAdapter(mAdapter);
    }
}