package com.example.moviebuddy.adapters;

import android.content.Context;
import android.view.LayoutInflater;
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

    List<User> userList;
    Context context;

    public CreateGroupUserListRecyclerAdapter(List<User> userList, Context context) {
        this.userList = userList;
        this.context = context;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        CheckBox chUser;
        TextView tvUsername;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            chUser = itemView.findViewById(R.id.checkBox);
            tvUsername = itemView.findViewById(R.id.tvCreateGroupUsername);


        }
    }

    @NonNull
    @Override
    public CreateGroupUserListRecyclerAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.creategroupitem,parent,false);
        CreateGroupUserListRecyclerAdapter.MyViewHolder holder = new CreateGroupUserListRecyclerAdapter.MyViewHolder(view);

        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull CreateGroupUserListRecyclerAdapter.MyViewHolder holder, int position) {
        holder.tvUsername.setText(userList.get(position).getUsername());
    }

    @Override
    public int getItemCount() {
        return userList.size();
    }
}
