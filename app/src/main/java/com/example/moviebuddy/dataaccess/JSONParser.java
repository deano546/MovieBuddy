package com.example.moviebuddy.dataaccess;

import android.content.Context;
import android.util.Log;

import com.android.volley.NetworkResponse;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.moviebuddy.model.GroupNight;
import com.example.moviebuddy.model.Movie;
import com.example.moviebuddy.model.User;
import com.example.moviebuddy.model.UserMovie;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

public class JSONParser {

    private List<Movie> popularmovieList = new ArrayList<Movie>();
    private List<Movie> currentmovieList = new ArrayList<Movie>();
    private List<Movie> searchmovieList = new ArrayList<Movie>();
    private List<GroupNight> groupnightList = new ArrayList<>();
    private List<Movie> watchlist = new ArrayList<>();
    private List<User> friendlist = new ArrayList<>();
    private List<User> searchuserlist = new ArrayList<>();
    private List<UserMovie> usermovielist = new ArrayList<>();
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

                                if((movie.getString("release_date")).length() < 4) {
                                    year = 0;
                                }
                                else {
                                    year = Integer.parseInt(movie.getString("release_date").substring(0,4));
                                }

                                int id = Integer.parseInt(movie.getString("id"));
                                Movie movie1 = new Movie(id,title,poster,year);
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
                                if((movie.getString("release_date")).length() < 4) {
                                    year = 0;
                                }
                                else {
                                    year = Integer.parseInt(movie.getString("release_date").substring(0,4));
                                }
                                Log.d("Description", String.valueOf(year));
                                int id = Integer.parseInt(movie.getString("id"));

                                Movie movie1 = new Movie(id,title,poster,year);
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
                                String title = movie.getString("title");
                                Log.d("***********", String.valueOf(response));
                                Log.d("title",title);
                                String poster = movie.getString("poster_path");
                                Log.d("Poster",poster);

                                if (movie.has("release_date")) {
                                    if((movie.getString("release_date")).length() < 4   || movie.getString("release_date") == null) {
                                        year = 0;
                                    }
                                    else {
                                        year = Integer.parseInt(movie.getString("release_date").substring(0,4));
                                    }
                                }
                                else {
                                    year = 0;
                                }


                                int id = Integer.parseInt(movie.getString("id"));
                                Log.d("Description", String.valueOf(year));

                                Movie movie1 = new Movie(id,title,poster,year);
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

    public interface SelectedMovieResponseListener {
        void onError(String message);

        void onResponse(Movie movie);
    }

    public void getMoviebyID(Context context, SelectedMovieResponseListener selectedMovieResponseListener, int movieid) {

        RequestQueue mQueue = Volley.newRequestQueue(context);

        String movieurl = "https://api.themoviedb.org/3/movie/" + movieid + "?api_key=641b5efff7ea9e0f5b33575963cf62ec&language=en-US";

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, movieurl, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {


                            String title = response.getString("title");
                            String poster = response.getString("poster_path");
                            String overview = response.getString("overview");
                            String runtime = response.getString("runtime") + " mins";


                            if((response.getString("release_date")).length() < 4) {
                                year = 0;
                            }
                            else {
                                year = Integer.parseInt(response.getString("release_date").substring(0,4));
                            }


                            Movie movie1 = new Movie(movieid,title,poster,runtime,overview,year);
                            Log.d("Movie",movie1.toString());


                            selectedMovieResponseListener.onResponse(movie1);
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Log.d("****", String.valueOf(e));
                            Log.d("SearchError", movieurl);
                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
                selectedMovieResponseListener.onError("Error");
                Log.d("****", String.valueOf(error));
            }
        });
        mQueue.add(request);


    }


    public interface MovieNightResponseListener {
        void onError(String message);

        void onResponse(List<GroupNight> groupNight);
    }

    public void getMovieNightsbyID(Context context, MovieNightResponseListener selectedMovieResponseListener, int userid) {
        RequestQueue mQueue = Volley.newRequestQueue(context);

        String movienighturl = "https://apex.oracle.com/pls/apex/gdeane545/gr/getmovienightbyuserid/" + userid;

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, movienighturl, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {

                            JSONArray jsonArray = response.getJSONArray("items");
                            Log.d("JSONARRAY",jsonArray.length() + "");
                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject groupnight = jsonArray.getJSONObject(i);
                                String date = groupnight.getString("groupnightdate");
                                Log.d("MOVIEDATE",date);
                                Log.d("***********", String.valueOf(response));
                                String time = groupnight.getString("groupnighttime");
                                Log.d("MOVIETIME",time);
                                String groupname = groupnight.getString("groupname");
                                Log.d("GROUPNAME",groupname);


                                int id = Integer.parseInt(groupnight.getString("movieid"));
                                Log.d("GROUPID",id + "");



                                getMoviebyID(context, new SelectedMovieResponseListener() {
                                    @Override
                                    public void onError(String message) {
                                        Log.d("TAG",message);
                                    }

                                    @Override
                                    public void onResponse(Movie movie) {
                                        String title = movie.getTitle();
                                        Log.d("TitleNIGHT",title);
                                        GroupNight groupNight = new GroupNight(title,date,groupname,time);
                                        Log.d("NIGHTTOSTRING",groupNight.toString());
                                        groupnightList.add(groupNight);
                                        if(groupnightList.size() == jsonArray.length()) {
                                            selectedMovieResponseListener.onResponse(groupnightList);
                                            Log.d("CHECKINGIF",groupnightList.toString());
                                        }



                                    }
                                },id);

                            }




                        } catch (JSONException e) {
                            e.printStackTrace();
                            Log.d("****", String.valueOf(e));

                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
                selectedMovieResponseListener.onError("Error");
                Log.d("****", String.valueOf(error));
            }
        });
        mQueue.add(request);
    }


    public interface WatchListResponseListener {
        void onError(String message);

        void onResponse(List<Movie> movies);
    }

public void getWatchlistbyID(Context context, WatchListResponseListener watchListResponseListener, int userid) {
    RequestQueue mQueue = Volley.newRequestQueue(context);

    String watchlisturl = "https://apex.oracle.com/pls/apex/gdeane545/gr/getwatchlistbyid/" + userid;

    JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, watchlisturl, null,
            new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    try {

                        JSONArray jsonArray = response.getJSONArray("items");
                        Log.d("JSONARRAY",jsonArray.length() + "");
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject movie = jsonArray.getJSONObject(i);


                            int id = Integer.parseInt(movie.getString("movieid"));
                            Log.d("MOVIEID",id + "");



                            getMoviebyID(context, new SelectedMovieResponseListener() {
                                @Override
                                public void onError(String message) {
                                    Log.d("TAG",message);
                                }

                                @Override
                                public void onResponse(Movie movie) {
                                    String title = movie.getTitle();
                                    String poster = movie.getImageurl();

                                    Movie movie2 = new Movie(id,title,poster,2020);

                                    watchlist.add(movie2);
                                    if(watchlist.size() == jsonArray.length()) {
                                        watchListResponseListener.onResponse(watchlist);

                                    }



                                }
                            },id);

                        }




                    } catch (JSONException e) {
                        e.printStackTrace();
                        Log.d("****", String.valueOf(e));

                    }

                }
            }, new Response.ErrorListener() {
        @Override
        public void onErrorResponse(VolleyError error) {
            error.printStackTrace();
            watchListResponseListener.onError(error + "");
            Log.d("****", String.valueOf(error));
        }
    });
    mQueue.add(request);


}

//Returns an error but works, possible solution here but not quite sure how to implement it https://stackoverflow.com/a/32105391
public void markAsWatched(Context context, MarkWatchedResponseListener markWatchedResponseListener, int userid, int movieid, int rating) {
    RequestQueue mQueue = Volley.newRequestQueue(context);

    String markwatchlisturl = "https://apex.oracle.com/pls/apex/gdeane545/gr/updatewatchlist/" + userid + "?movieid=" + movieid + "&rating=" + rating ;
    Log.d("CHECKURL",markwatchlisturl);

    JsonObjectRequest request = new JsonObjectRequest(Request.Method.PUT, markwatchlisturl, null,
            new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    markWatchedResponseListener.onResponse(response + "");
                }
            }, new Response.ErrorListener() {
        @Override
        public void onErrorResponse(VolleyError error) {
            error.printStackTrace();
            markWatchedResponseListener.onError(error + "");
            Log.d("****", String.valueOf(error));
        }
    });
    mQueue.add(request);

}

    public interface MarkWatchedResponseListener {
        void onError(String message);

        void onResponse(String message);
    }



    public void getFriendsbyID(Context context, GetFriendsResponseListener getfriendsResponseListener, int userid) {

        RequestQueue mQueue = Volley.newRequestQueue(context);
        String url = "https://apex.oracle.com/pls/apex/gdeane545/gr/getfriendsbyid/" + userid;

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            JSONArray jsonArray = response.getJSONArray("items");
                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject user = jsonArray.getJSONObject(i);
                                String username = user.getString("username");
                                int id = Integer.parseInt(user.getString("userid"));



                                User user1 = new User(id,username);

                                friendlist.add(user1);

                            }
                            getfriendsResponseListener.onResponse(friendlist);
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Log.d("****", String.valueOf(e));

                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
                getfriendsResponseListener.onError(error + "");
                Log.d("****", String.valueOf(error));
            }
        });
        mQueue.add(request);


    }


    public interface GetFriendsResponseListener {
        void onError(String message);

        void onResponse(List<User> userList);
    }

    public void SearchUsers(Context context, SearchUsersResponseListener searchusersResponseListener, String searchquery, int userid) {
        searchuserlist.clear();
        RequestQueue mQueue = Volley.newRequestQueue(context);
        String searchuserurl = "https://apex.oracle.com/pls/apex/gdeane545/gr/getallusers/" + userid + "?username=" + searchquery;

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, searchuserurl, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            JSONArray jsonArray = response.getJSONArray("items");
                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject user = jsonArray.getJSONObject(i);
                                String username = user.getString("username");
                                int id = Integer.parseInt(user.getString("userid"));



                                User user1 = new User(id,username);
                                searchuserlist.add(user1);

                            }
                            searchusersResponseListener.onResponse(searchuserlist);
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Log.d("****", String.valueOf(e));
                            Log.d("SearchError", searchuserurl);
                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
                searchusersResponseListener.onError(error + "");
                Log.d("****", String.valueOf(error));
            }
        });
        mQueue.add(request);


    }

    public interface SearchUsersResponseListener {
        void onError(String message);

        void onResponse(List<User> userlist);
    }


    public void createGroup(Context context, CreateGroupResponseListener creategroupResponseListener, String groupname) {
        searchuserlist.clear();
        RequestQueue mQueue = Volley.newRequestQueue(context);
        String creategroupurl = "https://apex.oracle.com/pls/apex/gdeane545/gr/creategroup/" + groupname;

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, creategroupurl, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {


                        creategroupResponseListener.onResponse(groupname);

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
                creategroupResponseListener.onError(error + "");
                Log.d("****", String.valueOf(error));
            }
        });
        mQueue.add(request);


    }

    public interface CreateGroupResponseListener {
        void onError(String message);

        void onResponse(String groupname);
    }

    public interface getGroupIDResponseListener {
        void onError (String message);

        void onResponse(int groupid);
    }

    public void getGroup(Context context, getGroupIDResponseListener getgroupidResponseListener, String groupname) {
        searchuserlist.clear();
        RequestQueue mQueue = Volley.newRequestQueue(context);
        String getgroupurl = "https://apex.oracle.com/pls/apex/gdeane545/gr/getgroupidbyname/" + groupname;

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, getgroupurl, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {

                        try {

                                 JSONArray jsonArray = response.getJSONArray("items");

                                JSONObject group = jsonArray.getJSONObject(0);
                                int id = Integer.parseInt(group.getString("groupid"));

                            getgroupidResponseListener.onResponse(id);

                        } catch (JSONException e) {
                            e.printStackTrace();
                            Log.d("****", String.valueOf(e));
                        }


                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
                getgroupidResponseListener.onError(error + "");
                Log.d("****", String.valueOf(error));
            }
        });
        mQueue.add(request);


    }


    public void createUserGroup(Context context, CreateUserGroupResponseListener createusergroupResponseListener, int groupid, int id) {
        searchuserlist.clear();
        RequestQueue mQueue = Volley.newRequestQueue(context);
        String createusergroupurl = "https://apex.oracle.com/pls/apex/gdeane545/gr/addusergroup/" + id + "?passedgroupid=" + groupid;

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, createusergroupurl, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {


                        createusergroupResponseListener.onResponse(response + "");

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
                createusergroupResponseListener.onError(error + "");
                Log.d("****", String.valueOf(error));
            }
        });
        mQueue.add(request);


    }

    public interface CreateUserGroupResponseListener {
        void onError(String message);

        void onResponse(String message);
    }



    public void getUserMovie(Context context, GetUserMovieResponseListener getusermovieResponseListener, int userid, int movieid) {
        usermovielist.clear();
        UserMovie usermovie1 = new UserMovie();
        RequestQueue mQueue = Volley.newRequestQueue(context);
        String getusermovieurl = "https://apex.oracle.com/pls/apex/gdeane545/gr/getusermovie/" + userid + "?movieid=" + movieid;

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, getusermovieurl, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {

                        try {

                            JSONArray jsonArray = response.getJSONArray("items");

                            if(jsonArray != null && jsonArray.length() > 0 ){
                                JSONObject usermovie = jsonArray.getJSONObject(0);

                                usermovie1.setKnown(true);

                                if(usermovie.has("rating")) {
                                    if(usermovie.isNull("rating")) {
                                        int rating = 0;
                                        usermovie1.setRating(rating);
                                    }
                                    else {
                                        int rating = Integer.parseInt(usermovie.getString("rating"));
                                        usermovie1.setRating(rating);
                                    }

                                }
                                else {
                                    int rating = 0;
                                    usermovie1.setRating(rating);
                                }

                                if(usermovie.has("onwatchlist")) {
                                    String onwatchlist = usermovie.getString("onwatchlist");
                                    usermovie1.setOnWatchlist(onwatchlist);
                                }
                                else {
                                    String onwatchlist = "No";
                                    usermovie1.setOnWatchlist(onwatchlist);
                                }

                            }

                            else {
                                boolean known = false;
                                usermovie1.setRating(0);
                                usermovie1.setOnWatchlist("No");
                                usermovie1.setKnown(false);
                            }

                            getusermovieResponseListener.onResponse(usermovie1);

                        } catch (JSONException e) {
                            e.printStackTrace();
                            Log.d("****", String.valueOf(e));
                        }


                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
                getusermovieResponseListener.onError(error + "");
                Log.d("****", String.valueOf(error));
            }
        });
        mQueue.add(request);


    }


    public interface GetUserMovieResponseListener {
        void onError(String message);

        void onResponse(UserMovie usermovie);
    }



    public void createMovieRating(Context context, CreateMovieRatingResponseListener createmovieratingResponseListener, int userid, int movieid,int rating) {

        RequestQueue mQueue = Volley.newRequestQueue(context);
        String createratingurl = "https://apex.oracle.com/pls/apex/gdeane545/gr/newrating/" + userid + "?passedmovieid=" + movieid + "&passedrating=" + rating;

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, createratingurl, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {


                        createmovieratingResponseListener.onResponse(response + "");

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
                createmovieratingResponseListener.onError(error + "");
                Log.d("****", String.valueOf(error));
            }
        });
        mQueue.add(request);


    }

    public interface CreateMovieRatingResponseListener {
        void onError(String message);

        void onResponse(String message);
    }


    public void updateMovieRating(Context context, UpdateMovieRatingResponseListener updatemovieratingResponseListener, int userid, int movieid,int rating) {

        RequestQueue mQueue = Volley.newRequestQueue(context);
        String updateratingurl = "https://apex.oracle.com/pls/apex/gdeane545/gr/updaterating/" + userid + "?rating=" + rating + "&passedmovieid=" + movieid;

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.PUT, updateratingurl, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {


                        updatemovieratingResponseListener.onResponse(response + "");

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
                updatemovieratingResponseListener.onError(error + "");
                Log.d("****", String.valueOf(error));
            }
        });
        mQueue.add(request);


    }

    public interface UpdateMovieRatingResponseListener {
        void onError(String message);

        void onResponse(String message);
    }








}
