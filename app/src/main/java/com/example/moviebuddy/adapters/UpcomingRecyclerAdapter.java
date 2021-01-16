package com.example.moviebuddy.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.moviebuddy.Activities.MovieDetailActivity;
import com.example.moviebuddy.R;
import com.example.moviebuddy.model.Movie;

import java.util.List;

public class UpcomingRecyclerAdapter extends RecyclerView.Adapter<UpcomingRecyclerAdapter.MyViewHolder> {


    //Mostly adapted from https://www.youtube.com/watch?v=FFCpjZkqfb0


    List<Movie> movieList;
    Context context;

    public UpcomingRecyclerAdapter(List<Movie> studentList, Context context) {
        this.movieList = studentList;
        this.context = context;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        ImageView imMovie;
        CardView parentLayout;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            imMovie = itemView.findViewById(R.id.imgview);
            parentLayout = itemView.findViewById(R.id.cardview);


        }
    }


    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.upcomingitem,parent,false);
        MyViewHolder holder = new MyViewHolder(view);

        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

        Glide.with(this.context).load("https://image.tmdb.org/t/p/w300/" + movieList.get(position).getImageurl()).into(holder.imMovie);
        holder.parentLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, MovieDetailActivity.class);
                intent.putExtra("title", movieList.get(position).getTitle());
                intent.putExtra("poster",movieList.get(position).getImageurl());
                intent.putExtra("year",movieList.get(position).getYear());
                context.startActivity(intent);
            }
        });


    }

    @Override
    public int getItemCount() {
        return movieList.size();
    }

}
