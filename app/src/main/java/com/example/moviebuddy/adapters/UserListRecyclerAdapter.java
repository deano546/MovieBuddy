package com.example.moviebuddy.adapters;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.moviebuddy.Activities.MainActivity;
import com.example.moviebuddy.R;
import com.example.moviebuddy.dataaccess.JSONParser;
import com.example.moviebuddy.model.GroupNight;
import com.example.moviebuddy.model.Movie;
import com.example.moviebuddy.model.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.List;

public class UserListRecyclerAdapter extends RecyclerView.Adapter<UserListRecyclerAdapter.MyViewHolder> {

    //This is used in the friend list activity
    //Mostly adapted from https://www.youtube.com/watch?v=FFCpjZkqfb0

    List<User> userList;
    Context context;
    String SQLID;

    public UserListRecyclerAdapter(List<User> userList, Context context, String senderid) {
        this.userList = userList;
        this.context = context;
        SQLID = senderid;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        Button btnAdd;
        TextView tvUsername;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            btnAdd = itemView.findViewById(R.id.btnAddFriend);
            tvUsername = itemView.findViewById(R.id.tvUsername);
        }
    }

    @NonNull
    @Override
    public UserListRecyclerAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.userlistitem,parent,false);
        UserListRecyclerAdapter.MyViewHolder holder = new UserListRecyclerAdapter.MyViewHolder(view);

        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull UserListRecyclerAdapter.MyViewHolder holder, int position) {
        Log.d("CANISEETHIS","HI");


        if(userList.isEmpty()) {
            holder.tvUsername.setText("No Results!");
        }
        else {
            JSONParser jsonParser = new JSONParser();

            holder.tvUsername.setText(userList.get(position).getUsername());
            holder.btnAdd.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    jsonParser.sendFriendRequest(context, new JSONParser.sendFriendRequestResponseListener() {
                        @Override
                        public void onError(String message) {
                            Toast.makeText(context, "Request Sent!", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(context, MainActivity.class);
                            context.startActivity(intent);
                        }

                        @Override
                        public void onResponse(String friendRequest) {

                        }
                    },SQLID,String.valueOf(userList.get(position).getId()));
                }
            });
        }

    }

    @Override
    public int getItemCount() {
        return userList.size();
    }
}
