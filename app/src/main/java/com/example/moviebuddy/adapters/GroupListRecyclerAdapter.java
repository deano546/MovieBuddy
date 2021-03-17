package com.example.moviebuddy.adapters;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.moviebuddy.Activities.MovieNightActivity;
import com.example.moviebuddy.Activities.PendingNightActivity;
import com.example.moviebuddy.Activities.UpcomingNightActivity;
import com.example.moviebuddy.R;
import com.example.moviebuddy.dataaccess.JSONParser;
import com.example.moviebuddy.model.Group;
import com.example.moviebuddy.model.GroupNight;
import com.example.moviebuddy.model.Movie;

import java.util.List;

public class GroupListRecyclerAdapter extends RecyclerView.Adapter<GroupListRecyclerAdapter.MyViewHolder> {

    //This adapter shows all the groups the user is a part of, and whether they have any upcoming movie night
    //(This adapter is not yet connected to a database, it is called in the GroupActivity)
    //Mostly adapted from https://www.youtube.com/watch?v=FFCpjZkqfb0

    List<GroupNight> groupList;
    Context context;
    GroupNight groupNight1;
    String passedmovieid1, passedmovietitle1, passedgenre;
    String pendinggenre;

    public GroupListRecyclerAdapter(List<GroupNight> groupList, Context context, String passedmovieid, String passedmovietitle, String passedgenre) {
        this.groupList = groupList;
        this.context = context;
        this.passedmovieid1 = passedmovieid;
        this.passedmovietitle1 = passedmovietitle;
        this.passedgenre = passedgenre;

    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        TextView tvGroupName;
        TextView tvDateAndTime;
        TextView tvTitle;
        Button btnSuggest;
        Button btnTest;

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

                holder.tvGroupName.setText(String.valueOf(groupList.get(position).getGroupName()));

                if(groupList.get(position).getApproval().equals("Nope")) {
                    holder.tvTitle.setText("No Movie Night Yet!");
                    holder.btnSuggest.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            //Toast.makeText(context, groupNightList.get(position).getMovieTitle() + "", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(context, MovieNightActivity.class);
                            intent.putExtra("GROUPID", groupList.get(position).getGroupid());
                            Log.d("MOVIENIGHTGROUPIDADAPTER",groupList.get(position).getGroupid());
                            intent.putExtra("GROUPNIGHTID", groupList.get(position).getId());
                            intent.putExtra("GROUPNAME", groupList.get(position).getGroupName());
                            intent.putExtra("MOVIEID", passedmovieid1);
                            intent.putExtra("MOVIETITLE", passedmovietitle1);
                            intent.putExtra("GENRE", passedgenre);
                            //intent.putExtra("CREATORID",groupList.get(position).getCreatorid());
                            context.startActivity(intent);
                        }
                    });
                }

                else {

                    jsonParser.getMoviebyID(context, new JSONParser.SelectedMovieResponseListener() {
                        @Override
                        public void onError(String message) {

                        }

                        @Override
                        public void onResponse(Movie movie) {
                            String title = movie.getTitle();
                            pendinggenre = movie.getGenre();
                            if (groupList.get(position).getApproval().equals("True")) {
                                holder.tvDateAndTime.setText(groupList.get(position).getDate() + " " + groupList.get(position).getTime());
                                holder.tvTitle.setText(movie.getTitle());
                                holder.btnSuggest.setText("View Upcoming Night");
                                holder.btnSuggest.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        Intent intent = new Intent(context, UpcomingNightActivity.class);
                                        intent.putExtra("GROUPNIGHTID", groupList.get(position).getId());
                                        Log.d("CHECKBUTTONEXTRAS2", groupList.get(position).getId() + "");
                                        intent.putExtra("GROUPID",groupList.get(position).getGroupid());
                                        intent.putExtra("GROUPNAME", groupList.get(position).getGroupName());
                                        Log.d("CHECKBUTTONEXTRAS3", groupList.get(position).getGroupName());
                                        intent.putExtra("MOVIEIDP", groupList.get(position).getMovieid());
                                        Log.d("CHECKBUTTONEXTRAS4", groupList.get(position).getMovieid());
                                        intent.putExtra("MOVIETITLE", title);
                                        Log.d("CHECKBUTTONEXTRAS5", title);
                                        intent.putExtra("DATE", groupList.get(position).getDate());
                                        intent.putExtra("TIME", groupList.get(position).getTime());
                                        intent.putExtra("CREATORID",groupList.get(position).getCreatorid());
                                        context.startActivity(intent);
                                    }
                                });

                            } else if (groupList.get(position).getApproval().equals("False")) {
                                holder.tvDateAndTime.setText(groupList.get(position).getDate() + " " + groupList.get(position).getTime());
                                holder.tvTitle.setText(movie.getTitle());
                                holder.btnSuggest.setText("View Pending Night");
                                holder.btnSuggest.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        Intent intent = new Intent(context, PendingNightActivity.class);
                                        //intent.putExtra("PENDING",groupNight1.getApproval());
                                        //Log.d("CHECKBUTTONEXTRAS1",groupNight1.getApproval());
                                        intent.putExtra("GROUPNIGHTID", groupList.get(position).getId());
                                        Log.d("CHECKBUTTONEXTRAS2", groupList.get(position).getId() + "");
                                        intent.putExtra("GROUPID",groupList.get(position).getGroupid());
                                        intent.putExtra("GROUPNAME", groupList.get(position).getGroupName());
                                        Log.d("CHECKBUTTONEXTRAS3", groupList.get(position).getGroupName());
                                        intent.putExtra("MOVIEIDP", groupList.get(position).getMovieid());
                                        Log.d("CHECKBUTTONEXTRAS4", groupList.get(position).getMovieid());
                                        intent.putExtra("MOVIETITLE", title);
                                        Log.d("CHECKBUTTONEXTRAS5", title);
                                        intent.putExtra("DATE", groupList.get(position).getDate());
                                        intent.putExtra("TIME", groupList.get(position).getTime());
                                        intent.putExtra("CREATORID",groupList.get(position).getCreatorid());
                                        intent.putExtra("GENRE",pendinggenre);
                                        context.startActivity(intent);
                                    }
                                });
                            }
                        }
                    },Integer.parseInt(groupList.get(position).getMovieid()));




                }




        if(groupList.get(position).getGroupName().equals("No Groups Yet!")) {
            holder.btnSuggest.setVisibility(View.INVISIBLE);
        }





    }

    @Override
    public int getItemCount() {
        return groupList.size();
    }

}
