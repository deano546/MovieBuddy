package com.example.moviebuddy.adapters;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.moviebuddy.Activities.MovieNightActivity;
import com.example.moviebuddy.R;
import com.example.moviebuddy.dataaccess.JSONParser;
import com.example.moviebuddy.model.Group;
import com.example.moviebuddy.model.GroupNight;

import java.util.List;

public class GroupListRecyclerAdapter extends RecyclerView.Adapter<GroupListRecyclerAdapter.MyViewHolder> {

    //This adapter shows all the groups the user is a part of, and whether they have any upcoming movie night
    //(This adapter is not yet connected to a database, it is called in the GroupActivity)
    //Mostly adapted from https://www.youtube.com/watch?v=FFCpjZkqfb0

    List<Group> groupList;
    Context context;
    GroupNight groupNight1;
    String passedmovieid1, passedmovietitle1;

    public GroupListRecyclerAdapter(List<Group> groupList, Context context, String passedmovieid, String passedmovietitle) {
        this.groupList = groupList;
        this.context = context;
        passedmovieid1 = passedmovieid;
        passedmovietitle1 = passedmovietitle;

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

        JSONParser jsonParser = new JSONParser();

        jsonParser.getMovienightbyGroupid(context, new JSONParser.getMovieNightbyGroupIDResponseListener() {
            @Override
            public void onError(String message) {

            }

            @Override
            public void onResponse(GroupNight groupNight) {
                groupNight1 = groupNight;



                if(groupNight1.getGroupName().equals("No Movie Night Yet!")) {
                    holder.tvTitle.setText("No Movie Night Yet!");
                }

                else {
                    holder.tvDateAndTime.setText(groupNight1.getDate() + " " + groupNight1.getTime());




                    holder.tvTitle.setText((groupNight1.getMovieTitle()));
                    holder.btnSuggest.setVisibility(View.INVISIBLE);
                }


            }
        },String.valueOf(groupList.get(position).getId()));


        holder.tvGroupName.setText(String.valueOf(groupList.get(position).getGroupname()));

        if(groupList.get(position).getGroupname().equals("No Groups Yet!")) {
            holder.btnSuggest.setVisibility(View.INVISIBLE);
        }

        holder.btnSuggest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Toast.makeText(context, groupNightList.get(position).getMovieTitle() + "", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(context, MovieNightActivity.class);
                intent.putExtra("GROUPID",groupList.get(position).getId());
                intent.putExtra("GROUPNAME",groupList.get(position).getGroupname());
                intent.putExtra("MOVIEID",passedmovieid1);
                intent.putExtra("MOVIETITLE",passedmovietitle1);
                context.startActivity(intent);
            }
        });



    }

    @Override
    public int getItemCount() {
        return groupList.size();
    }

}
