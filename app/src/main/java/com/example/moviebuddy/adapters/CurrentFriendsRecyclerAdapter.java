package com.example.moviebuddy.adapters;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.moviebuddy.Activities.CreateGroupActivity;
import com.example.moviebuddy.Activities.GroupActivity;
import com.example.moviebuddy.R;
import com.example.moviebuddy.dataaccess.JSONParser;
import com.example.moviebuddy.model.User;

import java.util.ArrayList;
import java.util.List;

public class CurrentFriendsRecyclerAdapter extends RecyclerView.Adapter<CurrentFriendsRecyclerAdapter.MyViewHolder> {

    List<User> userList;
    Context context;
    String userid;

    public CurrentFriendsRecyclerAdapter(List<User> userList, Context context, String userid) {
        this.userList = userList;
        this.context = context;
        this.userid = userid;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        TextView tvUsername;
        Button btnDelete;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            tvUsername = itemView.findViewById(R.id.tvCurrentFriendUsername);
            btnDelete = itemView.findViewById(R.id.btnDeleteFriend);


        }
    }

    @NonNull
    @Override
    public CurrentFriendsRecyclerAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.currentfrienditem,parent,false);
        CurrentFriendsRecyclerAdapter.MyViewHolder holder = new CurrentFriendsRecyclerAdapter.MyViewHolder(view);

        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull CurrentFriendsRecyclerAdapter.MyViewHolder holder, int position) {
        holder.tvUsername.setText(userList.get(position).getUsername());

        JSONParser jsonParser = new JSONParser();

        holder.btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder alertName = new AlertDialog.Builder(context, R.style.MyDialogTheme);
                // final EditText editTextName1 = new EditText(context);
                // add line after initializing editTextName1
                //editTextName1.setHint("Please Enter your email");
                //editTextName1.setTextColor(Color.GRAY);

                alertName.setTitle( Html.fromHtml("<font color='#70FFFFFF'>Are you sure you want to delete " + userList.get(position).getUsername() + " from your friend list?</font>"));
                // titles can be used regardless of a custom layout or not
                // alertName.setView(editTextName1);
                LinearLayout layoutName = new LinearLayout(context);
                layoutName.setOrientation(LinearLayout.VERTICAL);
                //layoutName.addView(editTextName1); // displays the user input bar
                alertName.setView(layoutName);

                alertName.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                    jsonParser.deleteFriend(context, new JSONParser.deleteFriendResponseListener() {
                        @Override
                        public void onError(String message) {
                            Toast.makeText(context, "Friend Deleted", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(context, GroupActivity.class);
                            context.startActivity(intent);
                        }

                        @Override
                        public void onResponse(String message) {

                        }
                    },Integer.parseInt(userid),userList.get(position).getId());


                    }
                });

                alertName.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        dialog.cancel(); // closes dialog alertName.show() // display the dialog

                    }
                });


                alertName.show();
            }
        });



    }

    @Override
    public int getItemCount() {
        return userList.size();
    }

}
