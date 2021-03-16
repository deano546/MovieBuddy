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

import com.example.moviebuddy.Activities.AddtoGroupActivity;
import com.example.moviebuddy.Activities.GroupActivity;
import com.example.moviebuddy.R;
import com.example.moviebuddy.dataaccess.JSONParser;
import com.example.moviebuddy.model.Group;
import com.example.moviebuddy.model.GroupNight;

import java.util.List;

public class ManageGroupRecyclerAdapter extends RecyclerView.Adapter<ManageGroupRecyclerAdapter.MyViewHolder> {

    List<Group> groupList;
    Context context;
    String userID;

    public ManageGroupRecyclerAdapter(List<Group> groupList, Context context, String userid) {
        this.groupList = groupList;
        this.context = context;
        this.userID = userid;

    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        TextView tvGroupName;
        Button btnAddMember;
        Button btnLeaveGroup;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            tvGroupName = itemView.findViewById(R.id.tvManageGroupName);
            btnAddMember = itemView.findViewById(R.id.btnAddMember);
            btnLeaveGroup = itemView.findViewById(R.id.btnLeave);
        }
    }


    @NonNull
    @Override
    public ManageGroupRecyclerAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.managegroupitem,parent,false);
        ManageGroupRecyclerAdapter.MyViewHolder holder = new ManageGroupRecyclerAdapter.MyViewHolder(view);

        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ManageGroupRecyclerAdapter.MyViewHolder holder, int position) {


            holder.tvGroupName.setText(groupList.get(position).getGroupname());

        JSONParser jsonParser = new JSONParser();

            holder.btnLeaveGroup.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    jsonParser.deleteUserGroup(context, new JSONParser.deleteUserGroupResponseListener() {
                        @Override
                        public void onError(String message) {
                            Toast.makeText(context, "You have left the Group", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(context, GroupActivity.class);
                            context.startActivity(intent);
                        }

                        @Override
                        public void onResponse(String message) {

                        }
                    },userID,String.valueOf(groupList.get(position).getId()));
                }
            });


            holder.btnAddMember.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(context, "Add someone to " + groupList.get(position).getGroupname(), Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(context, AddtoGroupActivity.class);
                    intent.putExtra("GROUPID",groupList.get(position).getId());
                    intent.putExtra("GROUPNAME",groupList.get(position).getGroupname());
                    context.startActivity(intent);
                }
            });



    }

    @Override
    public int getItemCount() {
        return groupList.size();
    }
}
