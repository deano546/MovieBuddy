package com.example.moviebuddy.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.moviebuddy.R;
import com.example.moviebuddy.adapters.UpcomingNightRecyclerAdapter;
import com.example.moviebuddy.adapters.UserListRecyclerAdapter;
import com.example.moviebuddy.dataaccess.JSONParser;
import com.example.moviebuddy.model.GroupNight;
import com.example.moviebuddy.model.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

import static android.view.View.VISIBLE;

public class FriendListActivity extends AppCompatActivity {

    //Declarations
    RecyclerView rvUserList;
    RecyclerView.Adapter mAdapter;
    RecyclerView.LayoutManager layoutManager;
    EditText etUserSearch;
    Button btnSearch;
   List<User> userList = new ArrayList<>();
    List<User> friendlist = new ArrayList<>();
    FirebaseAuth auth;
    FirebaseFirestore fStore;
    String SQLID;
    TextView tvResults;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friend_list);
        tvResults = findViewById(R.id.tvNoResults);

        tvResults.setVisibility(View.INVISIBLE);

        //Assignments
        etUserSearch = findViewById(R.id.etUserSearch);
        btnSearch = findViewById(R.id.btnSearchUser);
        Button btnViewFriends = findViewById(R.id.btnViewFriends);


        auth = FirebaseAuth.getInstance();
        fStore = FirebaseFirestore.getInstance();

        String ID = auth.getCurrentUser().getUid();

        JSONParser jsonParser = new JSONParser();

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

        btnViewFriends.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(FriendListActivity.this,CurrentFriendsActivity.class);
                startActivity(intent);
            }
        });



        //Clicking this button will retrieve a list of usernames matching the inputted search, and will exclude any users who the current user is already friends with
        btnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tvResults.setVisibility(View.INVISIBLE);
                jsonParser.getFriendsbyID(FriendListActivity.this, new JSONParser.GetFriendsResponseListener() {
                    @Override
                    public void onError(String message) {
                        Toast.makeText(FriendListActivity.this, "Error" + message, Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onResponse(List<User> userList) {
                        friendlist = userList;
                        Log.d("FIRSTCHECK",friendlist.toString());

                        jsonParser.SearchUsers(FriendListActivity.this, new JSONParser.SearchUsersResponseListener() {
                            @Override
                            public void onError(String message) {
                            Log.d("SEARCHUSERSERROR",message);
                            }

                            @Override
                            public void onResponse(List<User> userlist) {
                                //Comparing list of users returned in search vs current friend list, this allows me to exclude current friends
                                List<User> bList = new ArrayList<>();
                                List<User> aList = new ArrayList<>();

                                bList = userlist;
                                Log.d("**CHECKB",bList.toString());
                                aList = friendlist;
                                Log.d("**CHECKA",aList.toString());

                                bList.removeAll(aList);
                                Log.d("CHECKINGLIST",bList.toString());
                                if(bList.isEmpty()) {
                                    tvResults.setVisibility(VISIBLE);
                                }
                                setupRecycler(bList);
                                Log.d("CHECKID",SQLID);

                            }
                        },etUserSearch.getText().toString(),Integer.parseInt(SQLID));


                    }
                },Integer.parseInt(SQLID));
            }

            });


        setupRecycler(userList);
        }







    //Displaying recycler view
    private void setupRecycler(List<User> displaylist) {
        rvUserList = findViewById(R.id.rvUsers);
        rvUserList.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(FriendListActivity.this);
        rvUserList.setLayoutManager(layoutManager);
        mAdapter = new UserListRecyclerAdapter(displaylist,FriendListActivity.this,SQLID);
        rvUserList.setAdapter(mAdapter);
    }


}
