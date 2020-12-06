package com.example.moviebuddy.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.moviebuddy.R;
import com.example.moviebuddy.model.Movie;

import java.util.List;

public class UpcomingRecyclerAdapter extends RecyclerView.Adapter<UpcomingRecyclerAdapter.MyViewHolder> {

    List<Movie> movieList;
    Context context;

    public UpcomingRecyclerAdapter(List<Movie> studentList, Context context) {
        this.movieList = studentList;
        this.context = context;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        ImageView imMovie;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            imMovie = itemView.findViewById(R.id.imgview);

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

        Glide.with(this.context).load(movieList.get(position).getImageurl()).into(holder.imMovie);


    }

    @Override
    public int getItemCount() {
        return movieList.size();
    }

}
