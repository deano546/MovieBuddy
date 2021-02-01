package com.example.moviebuddy.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.moviebuddy.R;
import com.example.moviebuddy.adapters.UserListRecyclerAdapter;
import com.example.moviebuddy.dataaccess.JSONParser;
import com.example.moviebuddy.model.User;

import java.util.ArrayList;
import java.util.List;

public class FriendListActivity extends AppCompatActivity {

    //Declarations
    RecyclerView rvUserList;
    RecyclerView.Adapter mAdapter;
    RecyclerView.LayoutManager layoutManager;
    EditText etUserSearch;
    Button btnSearch;
   List<User> userList = new ArrayList<>();
    List<User> friendlist = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friend_list);

        //Assignments
        etUserSearch = findViewById(R.id.etUserSearch);
        btnSearch = findViewById(R.id.btnSearchUser);

        JSONParser jsonParser = new JSONParser();

        //Clicking this button will retrieve a list of usernames matching the inputted search, and will exclude any users who the current user is already friends with
        btnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
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
                                List<User> finalResult = new ArrayList<>();
                                bList = userlist;
                                Log.d("**CHECKB",bList.toString());
                                aList = friendlist;
                                Log.d("**CHECKA",aList.toString());

                                bList.removeAll(aList);
                                Log.d("CHECKINGLIST",bList.toString());

                                setupRecycler(bList);

                            }
                        },etUserSearch.getText().toString(),1);


                    }
                },1);
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
        mAdapter = new UserListRecyclerAdapter(displaylist,FriendListActivity.this);
        rvUserList.setAdapter(mAdapter);
    }


}
