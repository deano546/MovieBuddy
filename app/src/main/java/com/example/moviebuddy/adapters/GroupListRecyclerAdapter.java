package com.example.moviebuddy.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.moviebuddy.R;
import com.example.moviebuddy.model.GroupNight;

import java.util.List;

public class GroupListRecyclerAdapter extends RecyclerView.Adapter<GroupListRecyclerAdapter.MyViewHolder> {

    List<GroupNight> groupNightList;
    Context context;

    public GroupListRecyclerAdapter(List<GroupNight> groupNightList, Context context) {
        this.groupNightList = groupNightList;
        this.context = context;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        TextView tvGroupName;
        TextView tvDateAndTime;
        TextView tvTitle;
        Button btnSuggest;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            tvGroupName = itemView.findViewById(R.id.tvGroupNameList);
            tvDateAndTime = itemView.findViewById(R.id.tvDateTimeGroupList);
            tvTitle = itemView.findViewById(R.id.tvMovieTitleGroupList);
            btnSuggest = itemView.findViewById(R.id.btnSuggest);


        }
    }


    @NonNull
    @Override
    public GroupListRecyclerAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.grouplistitem,parent,false);
        GroupListRecyclerAdapter.MyViewHolder holder = new GroupListRecyclerAdapter.MyViewHolder(view);

        return holder;
    }



    @Override
    public void onBindViewHolder(@NonNull GroupListRecyclerAdapter.MyViewHolder holder, int position) {

        holder.tvTitle.setText(groupNightList.get(position).getMovieTitle());
        holder.tvDateAndTime.setText(String.valueOf(groupNightList.get(position).getDate()));
        holder.tvGroupName.setText(String.valueOf(groupNightList.get(position).getGroupName()));
        holder.btnSuggest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(context, groupNightList.get(position).getMovieTitle() + "", Toast.LENGTH_SHORT).show();
            }
        });



    }

    @Override
    public int getItemCount() {
        return groupNightList.size();
    }

}
