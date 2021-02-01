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
import com.example.moviebuddy.adapters.CreateGroupUserListRecyclerAdapter;
import com.example.moviebuddy.dataaccess.JSONParser;
import com.example.moviebuddy.model.User;

import java.util.ArrayList;
import java.util.List;

public class CreateGroupActivity extends AppCompatActivity {

    //Declarations
    RecyclerView rvUserList;
    CreateGroupUserListRecyclerAdapter mAdapter;
    RecyclerView.LayoutManager layoutManager;
    Button btnCreateGroup;
    EditText etGroupName;
    String groupname;
    List<User> userList = new ArrayList<>();
    List<User> selectedfriends = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_group);

        //Assignments
        rvUserList = findViewById(R.id.rvCreateGroupList);
        btnCreateGroup = findViewById(R.id.btnCreateGroup);
        etGroupName = findViewById(R.id.etNewGroup);



        JSONParser jsonParser = new JSONParser();

        //This method retrieves all the users friends, so they can be displayed in the recycler view
        jsonParser.getFriendsbyID(CreateGroupActivity.this, new JSONParser.GetFriendsResponseListener() {
            @Override
            public void onError(String message) {

            }

            @Override
            public void onResponse(List<User> userlist) {
                userList = userlist;
                setUpRecycler();
            }
        },1);

        //This button calls 3 methods. The first creates a group in my group table, then, it retrieves the ID of the newly created group, lastly, it takes that ID and each of the selected users and inserts them
        //into my usergroup table
        btnCreateGroup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectedfriends = mAdapter.getSelectedUsers();
                Log.d("TESTSELECTEDUSER",selectedfriends.toString());

                //Validation
                if(etGroupName.getText().toString().trim().length() > 0 && selectedfriends.size() > 0) {
                    groupname = etGroupName.getText().toString();
                    User currentuser = new User(1,"deano");
                    selectedfriends.add(currentuser);

                    jsonParser.createGroup(CreateGroupActivity.this, new JSONParser.CreateGroupResponseListener() {
                        @Override
                        public void onError(String message) {
                            Log.d("TAG",message);

                            jsonParser.getGroup(CreateGroupActivity.this, new JSONParser.getGroupIDResponseListener() {
                                @Override
                                public void onError(String message) {

                                }

                                @Override
                                public void onResponse(int groupid) {

                                    for (int i=0; i<selectedfriends.size(); i++)
                                    {

                                        jsonParser.createUserGroup(CreateGroupActivity.this, new JSONParser.CreateUserGroupResponseListener() {
                                            @Override
                                            public void onError(String message) {
                                                Log.d("TAG",message);
                                            }

                                            @Override
                                            public void onResponse(String message) {
                                                Log.d("TAG",message);
                                            }
                                        },groupid,selectedfriends.get(i).getId());
                                    }
                                    etGroupName.getText().clear();
                                    Toast.makeText(CreateGroupActivity.this, "Group Created!", Toast.LENGTH_SHORT).show();

                                }
                            },groupname);



                        }

                        @Override
                        public void onResponse(String message) {
                            Log.d("TAG",message);
                        }
                    },groupname);

                }
                else {
                    Toast.makeText(CreateGroupActivity.this, "Error", Toast.LENGTH_SHORT).show();
                }
            }
        });



    }

    //Displaying Recycler View
    public void setUpRecycler() {
        rvUserList.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(CreateGroupActivity.this);
        rvUserList.setLayoutManager(layoutManager);
        mAdapter = new CreateGroupUserListRecyclerAdapter(userList,CreateGroupActivity.this);
        rvUserList.setAdapter(mAdapter);
    }
}