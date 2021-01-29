package com.example.moviebuddy.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.moviebuddy.R;
import com.example.moviebuddy.model.GroupNight;

import java.util.List;

public class UpcomingNightRecyclerAdapter extends RecyclerView.Adapter<UpcomingNightRecyclerAdapter.MyViewHolder> {

    //Mostly adapted from https://www.youtube.com/watch?v=FFCpjZkqfb0


    List<GroupNight> groupNightList;
    Context context;

    public UpcomingNightRecyclerAdapter(List<GroupNight> groupNightList, Context context) {
        this.groupNightList = groupNightList;
        this.context = context;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        TextView tvTitle;
        TextView tvDateAndTime;
        TextView tvGroup;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            tvTitle = itemView.findViewById(R.id.tvMovieGroupList);
            tvDateAndTime = itemView.findViewById(R.id.tvDateTimeGroupList);
            tvGroup = itemView.findViewById(R.id.tvGroupName);


        }
    }


    @NonNull
    @Override
    public UpcomingNightRecyclerAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.upcomingnight,parent,false);
        UpcomingNightRecyclerAdapter.MyViewHolder holder = new UpcomingNightRecyclerAdapter.MyViewHolder(view);

        return holder;
    }



    @Override
    public void onBindViewHolder(@NonNull UpcomingNightRecyclerAdapter.MyViewHolder holder, int position) {

        holder.tvTitle.setText(groupNightList.get(position).getMovieTitle());
        holder.tvDateAndTime.setText(String.valueOf(groupNightList.get(position).getDate()) + " " + groupNightList.get(position).getTime());
        holder.tvGroup.setText(String.valueOf(groupNightList.get(position).getGroupName()));



    }

    @Override
    public int getItemCount() {
        return groupNightList.size();
    }

}
