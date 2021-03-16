package com.example.moviebuddy.adapters;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.provider.CalendarContract;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.moviebuddy.Activities.CreateGroupActivity;
import com.example.moviebuddy.R;
import com.example.moviebuddy.dataaccess.JSONParser;
import com.example.moviebuddy.model.GroupNight;
import com.example.moviebuddy.model.Movie;

import java.util.Calendar;
import java.util.List;

public class UpcomingNightRecyclerAdapter extends RecyclerView.Adapter<UpcomingNightRecyclerAdapter.MyViewHolder> {//Mostly adapted from https://www.youtube.com/watch?v=FFCpjZkqfb0
    //This is displayed on the main activity, and shows any upcoming movie nights for the current user

    List<GroupNight> groupNightList;
    Context context;
    String movietitle;

    public UpcomingNightRecyclerAdapter(List<GroupNight> groupNightList, Context context) {
        this.groupNightList = groupNightList;
        this.context = context;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView tvTitle;
        TextView tvDateAndTime;
        TextView tvGroup;
        TextView tvMovieNight;
        Button btnCalendar;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            tvTitle = itemView.findViewById(R.id.tvMovieGroupList);
            tvDateAndTime = itemView.findViewById(R.id.tvDateTimeGroupList);
            tvGroup = itemView.findViewById(R.id.tvGroupName);
            tvMovieNight = itemView.findViewById(R.id.tvMovieNightUpcoming);
            btnCalendar = itemView.findViewById(R.id.btnCalendar);
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
        Log.d("CHECKLISTSIZE1234",groupNightList.size() + "");
        Log.d("CHECKLISTSIZE1234",groupNightList.toString() + "");

        JSONParser jsonParser = new JSONParser();

        if(groupNightList.size() == 0) {
            Log.d("Letscheckthis12","Hello");
            holder.tvTitle.setText("");
            holder.tvDateAndTime.setText("");
            holder.tvGroup.setText((""));
            holder.tvMovieNight.setText(groupNightList.get(position).getMovieTitle());
        }
        else {
            jsonParser.getMoviebyID(context, new JSONParser.SelectedMovieResponseListener() {
                @Override
                public void onError(String message) {

                }

                @Override
                public void onResponse(Movie movie) {
                    holder.tvTitle.setText(movie.getTitle());
                    holder.tvDateAndTime.setText(groupNightList.get(position).getDate() + " " + groupNightList.get(position).getTime());
                    holder.tvGroup.setText(String.valueOf(groupNightList.get(position).getGroupName()));
                    holder.btnCalendar.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                            AlertDialog.Builder alertName = new AlertDialog.Builder(context, R.style.MyDialogTheme);
                            // final EditText editTextName1 = new EditText(context);
                            // add line after initializing editTextName1
                            //editTextName1.setHint("Please Enter your email");
                            //editTextName1.setTextColor(Color.GRAY);

                            alertName.setTitle( Html.fromHtml("<font color='#70FFFFFF'>Would you like to add this to your calendar?</font>"));
                            // titles can be used regardless of a custom layout or not
                            // alertName.setView(editTextName1);
                            LinearLayout layoutName = new LinearLayout(context);
                            layoutName.setOrientation(LinearLayout.VERTICAL);
                            //layoutName.addView(editTextName1); // displays the user input bar
                            alertName.setView(layoutName);

                            alertName.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int whichButton) {
                                    Calendar cal = Calendar.getInstance();
                                    long endTime;
                                    long startTime;

                                    String date = groupNightList.get(position).getDate();
                                    String daydate = date.substring(0,2);
                                    String monthdate = date.substring(3,5);
                                    String year = "20" + date.substring(6,8);

                                    String time = groupNightList.get(position).getTime();
                                    String hour = time.substring(0,2);
                                    String minute = time.substring(3,5);

                                    cal.set(Calendar.HOUR_OF_DAY, Integer.parseInt(hour));
                                    cal.set(Calendar.MINUTE, Integer.parseInt(minute));
                                    cal.set(Calendar.YEAR, Integer.parseInt(year));
                                    cal.set(Calendar.MONTH, Integer.parseInt(monthdate)- 1);
                                    cal.set(Calendar.DAY_OF_MONTH, Integer.parseInt(daydate));


                                    startTime = cal.getTimeInMillis();
                                    endTime = startTime + 30 * 60 * 1000 * 4;
                                    String title= groupNightList.get(position).getMovieTitle() + " w/ " + groupNightList.get(position).getGroupName();

                                    long event_id = (startTime+endTime)/10000;

                                    Intent intent = new Intent(Intent.ACTION_EDIT);
                                    intent.setType("vnd.android.cursor.item/event");
                                    intent.putExtra("beginTime", startTime);
                                    intent.putExtra("endTime", endTime);
                                    intent.putExtra("title", title);
                                    intent.putExtra(CalendarContract.Events.DESCRIPTION,"Created With MovieBuddy!");
                                    context.startActivity(intent);


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
            },Integer.parseInt(groupNightList.get(position).getMovieid()));

        }







    }

    @Override
    public int getItemCount() {
        return groupNightList.size();
    }

}
