package com.example.moviebuddy.adapters;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.service.controls.templates.TemperatureControlTemplate;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.moviebuddy.Activities.CreateGroupActivity;
import com.example.moviebuddy.Activities.MainActivity;
import com.example.moviebuddy.Activities.MovieDetailActivity;
import com.example.moviebuddy.Activities.WatchListActivity;
import com.example.moviebuddy.R;
import com.example.moviebuddy.dataaccess.JSONParser;
import com.example.moviebuddy.model.GroupNight;
import com.example.moviebuddy.model.Movie;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

import static java.lang.Boolean.FALSE;
import static java.lang.Boolean.TRUE;
import static java.lang.Boolean.valueOf;

public class WatchListRecyclerAdapter extends RecyclerView.Adapter<WatchListRecyclerAdapter.MyViewHolder> implements Filterable {

    //This adapter is used in the watch list activity
    //Mostly adapted from https://www.youtube.com/watch?v=FFCpjZkqfb0

    List<Movie> movieList;
    List<Movie> movieListFull;
    Context context;
    String SQLID;
    FirebaseAuth auth;
    FirebaseFirestore fStore;

    public WatchListRecyclerAdapter(List<Movie> movieList, Context context) {
        this.movieList = movieList;
        this.context = context;
        movieListFull = new ArrayList<>(movieList);
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        ImageView imMovieList;
        TextView tvMovieListTitle;
        Button btnMark;
        RatingBar rbWatchlist;
        Button btnDelete;
        ConstraintLayout parentLayout;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            imMovieList = itemView.findViewById(R.id.imWatchlistPoster);
            tvMovieListTitle = itemView.findViewById(R.id.tvWatchlistTitle);
            btnMark = itemView.findViewById(R.id.btnMarkWatched);
            rbWatchlist = itemView.findViewById(R.id.rbWatchlist);
            btnDelete = itemView.findViewById(R.id.btnDeleteFromWatchlist);
            parentLayout = itemView.findViewById(R.id.onewatchrow);
        }
    }


    @NonNull
    @Override
    public WatchListRecyclerAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.watchlistitem,parent,false);
        WatchListRecyclerAdapter.MyViewHolder holder = new WatchListRecyclerAdapter.MyViewHolder(view);

        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull WatchListRecyclerAdapter.MyViewHolder holder, int position) {

        Log.d("WATCHLISTSIZE",movieList.size() + "");

        if(movieList.size() == 0) {
            holder.tvMovieListTitle.setText("No Movies on Your Watchlist!");
        }

        auth = FirebaseAuth.getInstance();
        fStore = FirebaseFirestore.getInstance();
        String ID = auth.getCurrentUser().getUid();

        DocumentReference docRef = fStore.collection("Users").document(ID);
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull @NotNull Task<DocumentSnapshot> task) {
                if(task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if(document.exists()) {
                        SQLID = document.get("id").toString();
                    }
                }
            }
        });

        Glide.with(this.context).load("https://image.tmdb.org/t/p/w300/" + movieList.get(position).getImageurl()).into(holder.imMovieList);
        holder.tvMovieListTitle.setText(movieList.get(position).getTitle());

        //Make rating bar increment in whole numbers https://stackoverflow.com/questions/14218029/how-to-allow-full-values-integers-only-in-ratingbars
        holder.rbWatchlist.setStepSize(1);

        //Greying out button https://stackoverflow.com/a/27116948
        holder.btnMark.setAlpha(.5f);
        holder.btnMark.setClickable(false);

        //Detecting the user giving a rating https://stackoverflow.com/a/4010379
        holder.rbWatchlist.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_UP) {
                    holder.btnMark.setClickable(TRUE);
                    holder.btnMark.setAlpha(1f);
                }
                return false;

            }
        });

        holder.btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                AlertDialog.Builder alertName = new AlertDialog.Builder(context, R.style.MyDialogTheme);
                // final EditText editTextName1 = new EditText(context);
                // add line after initializing editTextName1
                //editTextName1.setHint("Please Enter your email");
                //editTextName1.setTextColor(Color.GRAY);

                alertName.setTitle( Html.fromHtml("<font color='#70FFFFFF'>Are you sure you want to Delete " + movieList.get(position).getTitle() + " from your Watchlist?</font>"));
                // titles can be used regardless of a custom layout or not
                // alertName.setView(editTextName1);
                LinearLayout layoutName = new LinearLayout(context);
                layoutName.setOrientation(LinearLayout.VERTICAL);
                //layoutName.addView(editTextName1); // displays the user input bar
                alertName.setView(layoutName);

                alertName.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {

                        removeItem2(position,Integer.parseInt(SQLID));

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

        holder.btnMark.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                removeItem(position,holder.rbWatchlist.getNumStars(),Integer.parseInt(SQLID));

            }
        });

    }

    //This method allows a movie to be removed from the watchlist when the user marks it as watched
    public void removeItem(int position, int rating, int id) {

        JSONParser jsonParser = new JSONParser();
        jsonParser.markAsWatched(context, new JSONParser.MarkWatchedResponseListener() {
            @Override
            public void onError(String message) {
                Toast.makeText(context, "Marked as Watched", Toast.LENGTH_SHORT).show();

                //Delete record from recycler https://stackoverflow.com/a/35819233
                movieList.remove(position);
                notifyItemRemoved(position);

            }

            @Override
            public void onResponse(String message) {
                Toast.makeText(context, "Marked as watched", Toast.LENGTH_SHORT).show();
            }
        },id,movieList.get(position).getId(),rating);
    }

    //This method allows a movie to be removed from the watchlist when the user clicks delete
    public void removeItem2(int position, int id) {

        JSONParser jsonParser = new JSONParser();
        jsonParser.deletefromWatchlist(context, new JSONParser.deletefromWatchlistResponseListener() {
            @Override
            public void onError(String message) {
                Toast.makeText(context, "Deleted", Toast.LENGTH_SHORT).show();

                //Delete record from recycler https://stackoverflow.com/a/35819233
                movieList.remove(position);
                notifyItemRemoved(position);
            }

            @Override
            public void onResponse(String message) {

            }
        },String.valueOf(id),String.valueOf(movieList.get(position).getId()));



    }

    @Override
    public int getItemCount() {
        return movieList.size();
    }

    //Filter allows search of the list in the recycler
    //adapted from https://codinginflow.com/tutorials/android/searchview-recyclerview

    @Override
    public Filter getFilter() {
        return exampleFilter;
    }

    private Filter exampleFilter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            List<Movie> filteredList = new ArrayList<>();
            if (constraint == null || constraint.length() == 0) {
                filteredList.addAll(movieListFull);
            } else {
                String filterPattern = constraint.toString().toLowerCase().trim();
                for (Movie movie : movieListFull) {
                    if (movie.getTitle().toLowerCase().contains(filterPattern)) {
                        filteredList.add(movie);
                    }
                }
            }
            FilterResults results = new FilterResults();
            results.values = filteredList;
            return results;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            movieList.clear();
            movieList.addAll((List) results.values);
            notifyDataSetChanged();
        }
    };

}
