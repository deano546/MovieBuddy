package com.example.moviebuddy.adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.moviebuddy.R;
import com.example.moviebuddy.dataaccess.JSONParser;
import com.example.moviebuddy.model.User;

import java.util.ArrayList;
import java.util.List;

public class AddtoGroupRecyclerAdapter extends RecyclerView.Adapter<AddtoGroupRecyclerAdapter.MyViewHolder> {

    List<User> userList;
    List<User> selecteduserList;
    Context context;
    String GroupID;


    public AddtoGroupRecyclerAdapter(List<User> userList, Context context, String groupid) {
        this.userList = userList;
        this.context = context;
        this.GroupID = groupid;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView tvUsername;
        Button btnAdd;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            tvUsername = itemView.findViewById(R.id.tvAddtoGroupUsername);
            btnAdd = itemView.findViewById(R.id.btnAddtoGroup);

        }
    }

    @NonNull
    @Override
    public AddtoGroupRecyclerAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.addtogroupitem,parent,false);
        AddtoGroupRecyclerAdapter.MyViewHolder holder = new AddtoGroupRecyclerAdapter.MyViewHolder(view);

        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull AddtoGroupRecyclerAdapter.MyViewHolder holder, int position) {

        holder.tvUsername.setText(userList.get(position).getUsername());

        JSONParser jsonParser = new JSONParser();
        jsonParser.getGroupMembers(context, new JSONParser.getGroupMembersResponseListener() {
            @Override
            public void onError(String message) {

            }

            @Override
            public void onResponse(List<User> userlist) {


                holder.btnAdd.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        jsonParser.createUserGroup(context, new JSONParser.CreateUserGroupResponseListener() {
                            @Override
                            public void onError(String message) {
                                holder.btnAdd.setEnabled(false);
                                Toast.makeText(context, "A new member was added to your group!", Toast.LENGTH_SHORT).show();
                            }

                            @Override
                            public void onResponse(String message) {

                            }
                        },Integer.parseInt(GroupID),userList.get(position).getId());
                    }
                });

                selecteduserList = userlist;

                for(User user: selecteduserList) {
                    if(String.valueOf(user.getId()).matches(String.valueOf(userList.get(position).getId()))) {
                        holder.btnAdd.setEnabled(false);
                    }
                }



            }
        },GroupID);


    }

    @Override
    public int getItemCount() {
        return userList.size();
    }

}
