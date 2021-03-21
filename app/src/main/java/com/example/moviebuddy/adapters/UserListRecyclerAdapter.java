package com.example.moviebuddy.adapters;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.text.Html;
import android.util.Log;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.moviebuddy.Activities.LogInActivity;
import com.example.moviebuddy.Activities.MainActivity;
import com.example.moviebuddy.R;
import com.example.moviebuddy.dataaccess.JSONParser;
import com.example.moviebuddy.model.GroupNight;
import com.example.moviebuddy.model.Movie;
import com.example.moviebuddy.model.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
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

                    AlertDialog.Builder alertName = new AlertDialog.Builder(context, R.style.MyDialogTheme);
                   // final EditText editTextName1 = new EditText(context);
                    // add line after initializing editTextName1
                    //editTextName1.setHint("Please Enter your email");
                    //editTextName1.setTextColor(Color.GRAY);

                    alertName.setTitle( Html.fromHtml("<font color='#70FFFFFF'>Are you sure you want to send a Friend Request to " + userList.get(position).getUsername() + "?</font>"));
                    // titles can be used regardless of a custom layout or not
                   // alertName.setView(editTextName1);
                    LinearLayout layoutName = new LinearLayout(context);
                    layoutName.setOrientation(LinearLayout.VERTICAL);
                    //layoutName.addView(editTextName1); // displays the user input bar
                    alertName.setView(layoutName);

                    alertName.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int whichButton) {

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

                    alertName.setNegativeButton("No", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int whichButton) {
                            dialog.cancel(); // closes dialog alertName.show() // display the dialog

                        }
                    });
                    alertName.show();
                }
            });
        }

    }

    @Override
    public int getItemCount() {
        return userList.size();
    }
}
