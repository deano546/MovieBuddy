package com.example.moviebuddy.adapters;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.moviebuddy.R;
import com.example.moviebuddy.model.User;

import java.util.List;

public class CreateGroupUserListRecyclerAdapter extends RecyclerView.Adapter<CreateGroupUserListRecyclerAdapter.MyViewHolder> {

//TODO
    List<User> userList;
    Context context;

    public CreateGroupUserListRecyclerAdapter(List<User> userList, Context context) {
        this.userList = userList;
        this.context = context;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        TextView tvUsername;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            //btnAdd = itemView.findViewById(R.id.btnAddFriend);
            tvUsername = itemView.findViewById(R.id.tvUsername);



        }
    }

    @NonNull
    @Override
    public CreateGroupUserListRecyclerAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull CreateGroupUserListRecyclerAdapter.MyViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 0;
    }
}
