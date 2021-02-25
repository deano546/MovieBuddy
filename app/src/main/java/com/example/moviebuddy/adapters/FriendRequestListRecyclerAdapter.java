package com.example.moviebuddy.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.moviebuddy.Activities.MainActivity;
import com.example.moviebuddy.R;
import com.example.moviebuddy.dataaccess.JSONParser;
import com.example.moviebuddy.model.User;

import java.util.List;

public class FriendRequestListRecyclerAdapter extends RecyclerView.Adapter<FriendRequestListRecyclerAdapter.MyViewHolder> {

    //This is used in the friend list activity
    //Mostly adapted from https://www.youtube.com/watch?v=FFCpjZkqfb0

    List<User> userList;
    Context context;
    String SQLID;

    public FriendRequestListRecyclerAdapter(List<User> userList, Context context, String userid) {
        this.userList = userList;
        this.context = context;
        SQLID = userid;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        Button btnAccept;
        Button btnReject;
        TextView tvUsername;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            tvUsername = itemView.findViewById(R.id.tvRequestUsername);
            btnAccept = itemView.findViewById(R.id.btnAcceptRequest);
            btnReject = itemView.findViewById(R.id.btnRejectRequest);
        }
    }

    @NonNull
    @Override
    public FriendRequestListRecyclerAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.onefriendrequest,parent,false);
        FriendRequestListRecyclerAdapter.MyViewHolder holder = new FriendRequestListRecyclerAdapter.MyViewHolder(view);

        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull FriendRequestListRecyclerAdapter.MyViewHolder holder, int position) {

        if(userList.size() == 0)
        {
            holder.tvUsername.setText("No Friend Requests!");
        }



        JSONParser jsonParser = new JSONParser();

        holder.tvUsername.setText(userList.get(position).getUsername());

        holder.btnAccept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                jsonParser.acceptFriendRequest(context, new JSONParser.acceptFriendRequestResponseListener() {
                    @Override
                    public void onError(String message) {
                        Toast.makeText(context, "Accepted!", Toast.LENGTH_SHORT).show();
                        userList.remove(position);
                        notifyItemRemoved(position);
                    }

                    @Override
                    public void onResponse(String message) {

                    }
                },Integer.parseInt(SQLID),userList.get(position).getId());
            }
        });


        holder.btnReject.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                jsonParser.rejectFriendRequest(context, new JSONParser.rejectFriendRequestResponseListener() {
                    @Override
                    public void onError(String message) {
                        Toast.makeText(context, "Rejected!", Toast.LENGTH_SHORT).show();
                        userList.remove(position);
                        notifyItemRemoved(position);
                    }

                    @Override
                    public void onResponse(String message) {

                    }
                },Integer.parseInt(SQLID),userList.get(position).getId());
            }
        });


    }

    @Override
    public int getItemCount() {
        return userList.size();
    }

}
