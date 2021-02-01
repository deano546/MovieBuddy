package com.example.moviebuddy.dataaccess;

import com.example.moviebuddy.model.GroupNight;
import com.example.moviebuddy.model.Movie;

import java.util.ArrayList;
import java.util.List;

public class MovieDataAccess {


    //This is just faking a database, it allows me to populate the recycler views

    public List<GroupNight> getNight() {
        List<GroupNight> groupNightList = new ArrayList<>();

        GroupNight groupNight1 = new GroupNight("Dark Knight", "8th Oct 8.00pm", "OG BIS");
        GroupNight groupNight2 = new GroupNight("Dark Knights", "9th Oct 8.00pm", "OG BIS");

        groupNightList.add(groupNight1);
        groupNightList.add(groupNight2);

        return groupNightList;
    }



}
