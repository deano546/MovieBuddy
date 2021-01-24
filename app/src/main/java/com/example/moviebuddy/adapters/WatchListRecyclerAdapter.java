package com.example.moviebuddy.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.moviebuddy.Activities.MovieDetailActivity;
import com.example.moviebuddy.R;
import com.example.moviebuddy.model.Movie;

import java.util.List;

public class WatchListRecyclerAdapter extends RecyclerView.Adapter<WatchListRecyclerAdapter.MyViewHolder> {

    List<Movie> movieList;
    Context context;

    public WatchListRecyclerAdapter(List<Movie> movieList, Context context) {
        this.movieList = movieList;
        this.context = context;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        ImageView imMovieList;
        TextView tvMovieListTitle;
        Button btnMark;
        ConstraintLayout parentLayout;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            imMovieList = itemView.findViewById(R.id.imWatchlistPoster);
            tvMovieListTitle = itemView.findViewById(R.id.tvWatchlistTitle);
            btnMark = itemView.findViewById(R.id.btnMarkWatched);
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
        Glide.with(this.context).load("https://image.tmdb.org/t/p/w300/" + movieList.get(position).getImageurl()).into(holder.imMovieList);
        holder.tvMovieListTitle.setText(movieList.get(position).getTitle());
        holder.btnMark.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //show movie detail screen
                Intent intent = new Intent(context, MovieDetailActivity.class);
                intent.putExtra("id", movieList.get(position).getId());
                context.startActivity(intent);
            }
        });

    }

    @Override
    public int getItemCount() {
        return movieList.size();
    }


}
