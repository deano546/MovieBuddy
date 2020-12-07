package com.example.moviebuddy.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.moviebuddy.R;
import com.example.moviebuddy.model.Movie;

import java.util.List;

public class MovieListRecyclerAdapter extends RecyclerView.Adapter<MovieListRecyclerAdapter.MyViewHolder> {

    List<Movie> movieList;
    Context context;

    public MovieListRecyclerAdapter(List<Movie> studentList, Context context) {
        this.movieList = studentList;
        this.context = context;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        ImageView imMovieList;
        TextView tvMovieListTitle;
        TextView tvMovieListYear;
        ConstraintLayout parentLayout;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            imMovieList = itemView.findViewById(R.id.imMovieList);
            tvMovieListTitle = itemView.findViewById(R.id.tvMovieListTitle);
            tvMovieListYear = itemView.findViewById(R.id.tvMovieListYear);
            parentLayout = itemView.findViewById(R.id.onerow);

        }
    }



    @NonNull
    @Override
    public MovieListRecyclerAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.movieitem,parent,false);
        MovieListRecyclerAdapter.MyViewHolder holder = new MovieListRecyclerAdapter.MyViewHolder(view);

        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull MovieListRecyclerAdapter.MyViewHolder holder, int position) {
        Glide.with(this.context).load(movieList.get(position).getImageurl()).into(holder.imMovieList);
        holder.tvMovieListTitle.setText(movieList.get(position).getTitle());
        holder.tvMovieListYear.setText(String.valueOf(movieList.get(position).getYear()));
        holder.parentLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //show movie detail screen in future, for now just toast as proof of concept
                Toast.makeText(context, "Detail of " + movieList.get(position).getTitle(), Toast.LENGTH_SHORT).show();
            }
        });

    }

    @Override
    public int getItemCount() {
        return movieList.size();
    }
}
