package com.example.moviebuddy.dataaccess;

import android.content.Context;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.moviebuddy.model.Movie;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class JSONParser {

    private List<Movie> popularmovieList = new ArrayList<Movie>();
    private List<Movie> currentmovieList = new ArrayList<Movie>();
    private List<Movie> searchmovieList = new ArrayList<Movie>();
    int year;

    private String popularurl = "https://api.themoviedb.org/3/movie/popular?api_key=641b5efff7ea9e0f5b33575963cf62ec";
    private String currenturl = "https://api.themoviedb.org/3/movie/upcoming?api_key=641b5efff7ea9e0f5b33575963cf62ec&language=en-US&page=1";


    //https://stackoverflow.com/questions/33535435/how-to-create-a-proper-volley-listener-for-cross-class-volley-method-calling
    public interface VolleyResponseListener {
        void onError(String message);

        void onResponse(List<Movie> movieList);
    }


    public void getPopularMoviez(Context context, VolleyResponseListener volleyResponseListener) {
        RequestQueue mQueue = Volley.newRequestQueue(context);
        Log.d("hello","something");

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, popularurl, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            JSONArray jsonArray = response.getJSONArray("results");
                            for (int i = 0; i < 3; i++) {
                                JSONObject movie = jsonArray.getJSONObject(i);
                                String title = movie.getString("original_title");
                                Log.d("***********", String.valueOf(response));
                                Log.d("title",title);
                                String poster = movie.getString("poster_path");
                                Log.d("Poster",poster);
                                int year = Integer.parseInt(movie.getString("release_date").substring(0,4));
                                Log.d("Description", String.valueOf(year));

                                Movie movie1 = new Movie(i,title,poster,year);
                                Log.d("Movie",movie1.toString());
                                popularmovieList.add(movie1);
                                Log.d("MovieList1",popularmovieList.toString());

                            }
                            volleyResponseListener.onResponse(popularmovieList);
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Log.d("****", String.valueOf(e));
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
                volleyResponseListener.onError("Error");
                Log.d("****", String.valueOf(error));
            }
        });
        mQueue.add(request);



    }

    public interface CurrentMovieResponseListener {
        void onError(String message);

        void onResponse(List<Movie> movieList);
    }

    public void getCurrentMovies(Context context,CurrentMovieResponseListener currentMovieResponseListener) {
        RequestQueue mQueue = Volley.newRequestQueue(context);

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, currenturl, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            JSONArray jsonArray = response.getJSONArray("results");
                            for (int i = 0; i < 5; i++) {
                                JSONObject movie = jsonArray.getJSONObject(i);
                                String title = movie.getString("original_title");
                                Log.d("***********", String.valueOf(response));
                                Log.d("title",title);
                                String poster = movie.getString("poster_path");
                                Log.d("Poster",poster);
                                int year = Integer.parseInt(movie.getString("release_date").substring(0,4));
                                Log.d("Description", String.valueOf(year));

                                Movie movie1 = new Movie(i,title,poster,year);
                                Log.d("Movie",movie1.toString());
                                currentmovieList.add(movie1);
                                Log.d("MovieList1",currentmovieList.toString());

                            }
                            currentMovieResponseListener.onResponse(currentmovieList);
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Log.d("****", String.valueOf(e));
                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
                currentMovieResponseListener.onError("Error");
                Log.d("****", String.valueOf(error));
            }
        });
        mQueue.add(request);


    }


    public interface SearchMovieResponseListener {
        void onError(String message);

        void onResponse(List<Movie> movieList);
    }

    public void SearchMovies(Context context, SearchMovieResponseListener searchMovieResponseListener, String searchquery) {
        searchmovieList.clear();
        RequestQueue mQueue = Volley.newRequestQueue(context);
        String searchurl = "https://api.themoviedb.org/3/search/movie?api_key=641b5efff7ea9e0f5b33575963cf62ec&language=en-US&query=" + searchquery + "&page=1&include_adult=false";

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, searchurl, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            JSONArray jsonArray = response.getJSONArray("results");
                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject movie = jsonArray.getJSONObject(i);
                                String title = movie.getString("original_title");
                                Log.d("***********", String.valueOf(response));
                                Log.d("title",title);
                                String poster = movie.getString("poster_path");
                                Log.d("Poster",poster);

                                if((movie.getString("release_date")).length() < 4) {
                                    year = 0;
                                }
                                else {
                                    year = Integer.parseInt(movie.getString("release_date").substring(0,4));
                                }

                                Log.d("Description", String.valueOf(year));

                                Movie movie1 = new Movie(i,title,poster,year);
                                Log.d("Movie",movie1.toString());
                                searchmovieList.add(movie1);
                                Log.d("MovieList1",searchmovieList.toString());

                            }
                            searchMovieResponseListener.onResponse(searchmovieList);
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Log.d("****", String.valueOf(e));
                            Log.d("SearchError", searchurl);
                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
                searchMovieResponseListener.onError("Error");
                Log.d("****", String.valueOf(error));
            }
        });
        mQueue.add(request);


    }




}
