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
import com.example.moviebuddy.dataaccess.JSONParser;
import com.example.moviebuddy.model.GroupNight;
import com.example.moviebuddy.model.User;

import java.util.List;

public class UnApprovedGroupNightRecyclerAdapter extends RecyclerView.Adapter<UnApprovedGroupNightRecyclerAdapter.MyViewHolder> {

    //This is used in the friend list activity
    //Mostly adapted from https://www.youtube.com/watch?v=FFCpjZkqfb0

    List<GroupNight> groupNights;
    Context context;
    String SQLID;
    int Counter;
    String groupnightid;


    public UnApprovedGroupNightRecyclerAdapter(List<GroupNight> groupNights, Context context, String userid) {
        this.groupNights = groupNights;
        this.context = context;
        SQLID = userid;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        Button btnAccept;
        Button btnReject;
        TextView tvMovie;
        TextView tvGroup;
        TextView tvDateAndTime;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            btnAccept = itemView.findViewById(R.id.btnAcceptNight);
            btnReject = itemView.findViewById(R.id.btnRejectNight);
            tvMovie = itemView.findViewById(R.id.tvMovieforNight);
            tvGroup = itemView.findViewById(R.id.tvGroupforNight);
            tvDateAndTime = itemView.findViewById(R.id.tvDAndTforNight);


        }
    }

    @NonNull
    @Override
    public UnApprovedGroupNightRecyclerAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.approvenight,parent,false);
        UnApprovedGroupNightRecyclerAdapter.MyViewHolder holder = new UnApprovedGroupNightRecyclerAdapter.MyViewHolder(view);

        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull UnApprovedGroupNightRecyclerAdapter.MyViewHolder holder, int position) {
        JSONParser jsonParser = new JSONParser();

        groupnightid = groupNights.get(position).getId();



        holder.btnAccept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                jsonParser.approveGroupNight(context, new JSONParser.approveGroupNightResponseListener() {
                    @Override
                    public void onError(String message) {
                        Toast.makeText(context, "Accepted!", Toast.LENGTH_SHORT).show();
                        groupNights.remove(position);
                        notifyItemRemoved(position);

                        jsonParser.getGroupNightApproval(context, new JSONParser.getGroupNightApprovalResponseListener() {
                            @Override
                            public void onError(String message) {

                            }

                            @Override
                            public void onResponse(List<String> approvallist) {

                                for (String s : approvallist) {
                                    if(s.equals("True"));
                                    Counter +=1;

                                }
                                if(Counter == approvallist.size()) {

                                    jsonParser.fullyApproveGroupNight(context, new JSONParser.fullyApproveGroupNightResponseListener() {
                                        @Override
                                        public void onError(String message) {
                                            Toast.makeText(context, "Fully Approved", Toast.LENGTH_SHORT).show();
                                        }

                                        @Override
                                        public void onResponse(String message) {

                                        }
                                    },groupnightid);

                                }

                            }
                        },groupnightid);

                    }

                    @Override
                    public void onResponse(String message) {

                    }
                },SQLID,groupnightid);
            }
        });

        holder.tvMovie.setText(groupNights.get(position).getMovieTitle());
        holder.tvDateAndTime.setText(groupNights.get(position).getDate() + " " + groupNights.get(position).getTime());
        holder.tvGroup.setText(String.valueOf(groupNights.get(position).getGroupName()));


    }

    @Override
    public int getItemCount() {
        return groupNights.size();
    }

}
