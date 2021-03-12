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
import com.example.moviebuddy.model.Group;
import com.example.moviebuddy.model.GroupMember;
import com.example.moviebuddy.model.GroupNight;
import com.example.moviebuddy.model.GroupsRatings;
import com.example.moviebuddy.model.Movie;
import com.example.moviebuddy.model.User;
import com.example.moviebuddy.model.UserMovie;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class JSONParser {

    //Lists to use with methods
    private List<GroupMember> membersfornight = new ArrayList<>();
    private List<Movie> popularmovieList = new ArrayList<>();
    private List<Movie> currentmovieList = new ArrayList<>();
    private List<Movie> searchmovieList = new ArrayList<>();
    private List<GroupNight> groupnightList = new ArrayList<>();
    private List<Movie> watchlist = new ArrayList<>();
    private List<User> friendlist = new ArrayList<>();
    private List<User> searchuserlist = new ArrayList<>();
    private List<User> friendrequestlist = new ArrayList<>();
    private List<User> groupMemberList = new ArrayList<>();
    private List<UserMovie> usermovielist = new ArrayList<>();
    private List <String> approvalList = new ArrayList<>();
    int year;
    int Movieid;
    private List <String> genresList = new ArrayList<>();
    private List<String> newgenresList = new ArrayList<>();
    private List<Group> groupList = new ArrayList<>();
    private List<GroupNight> groupnightListbygroup = new ArrayList<>();
    private List<GroupNight> nightstoapprove = new ArrayList<>();
    List<GroupNight> ultralist = new ArrayList<>();
    private List<GroupsRatings> ratingsList = new ArrayList<>();
    GroupNight groupNight1;
    GroupsRatings rating;
    List <String> genre;
    private String moviegenre;
    private String checkifwatched;
    private boolean breaknow = false;


    //These are all methods that interface with my database (through rest services created with oracle apex)
    //or with the movie database API I am accessing, I use the Volley library to simplify the process
    //some code was adapted from https://www.youtube.com/watch?v=FFCpjZkqfb0





    //https://stackoverflow.com/questions/33535435/how-to-create-a-proper-volley-listener-for-cross-class-volley-method-calling
    public interface VolleyResponseListener {
        void onError(String message);

        void onResponse(List<Movie> movieList);
    }

    //Gets popular movies from the API
    //A list of movies is retrieved and inserted into an array list
    //The array list is returned using the Response Listener
    public void getPopularMoviez(Context context, VolleyResponseListener volleyResponseListener) {
        RequestQueue mQueue = Volley.newRequestQueue(context);
        Log.d("hello","something");
        String popularurl = "https://api.themoviedb.org/3/movie/popular?api_key=641b5efff7ea9e0f5b33575963cf62ec";

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

    //Similar to last method, this gets a different list from the API, listed in their documentation as "Upcoming"
    public void getCurrentMovies(Context context,CurrentMovieResponseListener currentMovieResponseListener) {
        RequestQueue mQueue = Volley.newRequestQueue(context);

        String currenturl = "https://api.themoviedb.org/3/movie/upcoming?api_key=641b5efff7ea9e0f5b33575963cf62ec&language=en-US&page=1";

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

    //Calling the search on the movie database API, and return the resulting list
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

                                //Some movies in thir lists have no release date, so I have this validation to account for that
                                //I take out the year portion of the release date if they do have one
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

    //Retrive a single movie form the API using the movieid
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

                            JSONArray genres = response.getJSONArray("genres");
                            Log.d("Check getting the array",genres.toString());


                                JSONObject jsongenre = genres.getJSONObject(0);
                                String moviegenre = jsongenre.getString("name");

                                Log.d("Lets Check Genre",moviegenre);



                            Movie movie1 = new Movie(movieid,title,poster,runtime,overview,year,moviegenre);
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

    //This gets any upcoming movie nights for the current user, using their userid
    //I call the get movie by ID method in this so I can also retrieve the movie details from the API
    public void getMovieNightsbyID(Context context, MovieNightResponseListener selectedMovieResponseListener, int userid) {
        groupnightList.clear();
        RequestQueue mQueue = Volley.newRequestQueue(context);

        String movienighturl = "https://apex.oracle.com/pls/apex/gdeane545/gr/getmovienightbyuserid/" + userid;

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, movienighturl, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {

                            JSONArray jsonArray = response.getJSONArray("items");
                            Log.d("JSONARRAY11",jsonArray.length() + "");

                            if(jsonArray.length() == 0) {
                                Log.d("CHECKTHISIF","Hi");
                                GroupNight groupNight = new GroupNight();
                                groupNight.setMovieTitle("No Upcoming Nights!");
                                groupnightList.add(groupNight);
                                selectedMovieResponseListener.onResponse(groupnightList);
                            }

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

    //gets the watchlist for the current user
    //it also calls the get movie by id method so the details of the movies can be displayed also
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

                                    //This ensures each movie has been looped through before the response listener is called
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
    //This marks a movie on a user's watchlist as watched
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


    //Gets the current user's friend list
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

    //This searches through all users on the database
    //I pass the id of the user performing the search so they can be excluded
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

    //This creates a new group
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

    //This retrieves a group ID by using the group name
    //I need to validate each groups name is unique
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

    //This adds records to the UserGroup table of the user and group ids
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


    //This retrieves records from the user movie table
    //It allows me to know whether they have rated or watched the movie
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
                            //Validation, as it is possible the user has never interacted (rated or watched) the movie before
                            if(jsonArray != null && jsonArray.length() > 0 ){
                                JSONObject usermovie = jsonArray.getJSONObject(0);
                                usermovie1.setKnown(true);

                                //If the user has not rated the movie, I automatically set it to 0
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
                            //If no record was found, known is set to false, so I know what controls to enable on the movie detail activity
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


    //Creates a record in the usermovie table and adds a rating
    public void createMovieRating(Context context, CreateMovieRatingResponseListener createmovieratingResponseListener, int userid, int movieid,int rating, String genre) {
        RequestQueue mQueue = Volley.newRequestQueue(context);
        String createratingurl = "https://apex.oracle.com/pls/apex/gdeane545/gr/newrating/" + userid + "?passedmovieid=" + movieid + "&passedrating=" + rating + "&passedgenre=" + genre;

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

    //Updates a record in the user movie table and adds a rating
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


    //This adds a movie to a users watchlist
    public void addtoWatchlist(Context context, addtoWatchlistResponseListener addtowatchlistResponseListener, int userid, int movieid, String genre) {
        RequestQueue mQueue = Volley.newRequestQueue(context);
        String addwatchlisturl = "https://apex.oracle.com/pls/apex/gdeane545/gr/newwatchlist/" + userid + "?passedmovieid=" + movieid + "&passedgenre=" + genre;

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, addwatchlisturl, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        addtowatchlistResponseListener.onResponse(response + "");
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
                addtowatchlistResponseListener.onError(error + "");
                Log.d("****", String.valueOf(error));
            }
        });
        mQueue.add(request);
    }

    public interface addtoWatchlistResponseListener {
        void onError(String message);

        void onResponse(String message);
    }

public void verifyUniqueGroup(Context context, verifyUniqueGroupResponseListener verifyuniquegroupresponselistener, String groupname) {
    RequestQueue mQueue = Volley.newRequestQueue(context);
    String checkuniqueurl = "https://apex.oracle.com/pls/apex/gdeane545/gr/checkgroupname/" + groupname;

    JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, checkuniqueurl, null,
            new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {

                    try {
                        JSONArray jsonArray = response.getJSONArray("items");

                        if (jsonArray.length() > 0) {
                            verifyuniquegroupresponselistener.onResponse("Not Unique");
                        }
                        else {
                            verifyuniquegroupresponselistener.onResponse("Unique");
                        }


                    } catch (JSONException e) {
                        e.printStackTrace();
                    }



                }
            }, new Response.ErrorListener() {
        @Override
        public void onErrorResponse(VolleyError error) {
            error.printStackTrace();
            verifyuniquegroupresponselistener.onError(error + "");
            Log.d("****", String.valueOf(error));
        }
    });
    mQueue.add(request);



}

    public interface verifyUniqueGroupResponseListener {
        void onError(String message);

        void onResponse(String message);
    }

    //This creates a new group
    public void createUser(Context context, CreateUserResponseListener createuserResponseListener, String email, String username) {
        ;
        RequestQueue mQueue = Volley.newRequestQueue(context);
        String createuserurl = "https://apex.oracle.com/pls/apex/gdeane545/gr/createuser/" + email + "?username=" + username;


        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, createuserurl, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        createuserResponseListener.onResponse(username);
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
                createuserResponseListener.onError(error + "");
                Log.d("****", String.valueOf(error));
            }
        });
        mQueue.add(request);


    }

    public interface CreateUserResponseListener {
        void onError(String message);

        void onResponse(String username);
    }

    public interface getGroupResponseListener {
        void onError (String message);

        void onResponse(List<Group> groupList);
    }

    //This retrieves a group ID by using the group name
    //I need to validate each groups name is unique
    public void getGroupbyUser(Context context, getGroupResponseListener getgroupResponseListener, String id) {
        groupList.clear();
        RequestQueue mQueue = Volley.newRequestQueue(context);
        String getgroupurl = "https://apex.oracle.com/pls/apex/gdeane545/gr/getgroupnamebyuserid/" + id;

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, getgroupurl, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            JSONArray jsonArray = response.getJSONArray("items");

                            if (jsonArray.length() == 0)  {
                                Group group1 = new Group();
                                group1.setGroupname("No Groups Yet!");
                                groupList.add(group1);
                                getgroupResponseListener.onResponse(groupList);
                            }
                            else {
                                for (int i = 0; i < jsonArray.length(); i++) {

                                    JSONObject group = jsonArray.getJSONObject(i);
                                    String groupname = group.getString("groupname");
                                    String groupid = group.getString("groupid");

                                    Group group1 = new Group();
                                    group1.setGroupname(groupname);
                                    group1.setId(Long.parseLong(groupid));
                                    groupList.add(group1);
                                }
                                getgroupResponseListener.onResponse(groupList);

                            }
                        }
                        catch (JSONException e) {
                            e.printStackTrace();
                            Log.d("****", String.valueOf(e));
                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
                getgroupResponseListener.onError(error + "");
                Log.d("****", String.valueOf(error));
            }
        });
        mQueue.add(request);


    }

    public void getMovienightbyGroupid(Context context, getMovieNightbyGroupIDResponseListener getmovienightbygroupidResponseListener, String id) {
        groupnightListbygroup.clear();
        Log.d("Checking group id", id);

        RequestQueue mQueue = Volley.newRequestQueue(context);

        String movienighturl = "https://apex.oracle.com/pls/apex/gdeane545/gr/getmovienightbygroupid/" + id;

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, movienighturl, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            JSONArray jsonArray = response.getJSONArray("items");
                            if (jsonArray.length() == 0)  {
                                GroupNight group1 = new GroupNight();
                                group1.setGroupName("No Movie Night Yet!");
                                getmovienightbygroupidResponseListener.onResponse(group1);
                            }

                            else {
                                Log.d("JSONARRAY1",jsonArray.length() + "");
                               // for (int i = 0; i < 1; i++) {
                                    JSONObject groupnight = jsonArray.getJSONObject(0);
                                    groupNight1 = new GroupNight();
                                    groupNight1.setGroupName("Doesn't matter");
                                    String date = groupnight.getString("groupnightdate");
                                    Log.d("MOVIEDATE2",date);
                                    groupNight1.setDate(date);

                                    String time = groupnight.getString("groupnighttime");
                                    groupNight1.setTime(time);
                                    Log.d("MOVIETIME3",time);

                                    String approval = groupnight.getString("fullapproval");
                                    Log.d("approvalJSON4",approval);
                                    groupNight1.setApproval(approval);

                                    Movieid = Integer.parseInt(groupnight.getString("movieid"));
                                    Log.d("MOVIEIDJSON5",Movieid + "");
                                    groupNight1.setMovieid(String.valueOf(Movieid));


                                    getMoviebyID(context, new SelectedMovieResponseListener() {
                                        @Override
                                        public void onError(String message) {
                                            Log.d("TAG",message);
                                        }

                                        @Override
                                        public void onResponse(Movie movie) {
                                            String title = movie.getTitle();
                                            Log.d("TitleNIGHT6",title);

                                            groupNight1.setMovieTitle(title);

                                            Log.d("CHECKINGgroupnight7",groupNight1.toString());

                                            getmovienightbygroupidResponseListener.onResponse(groupNight1);

                                        }
                                    },Movieid);

                                //}

                            }
                        }

                        catch (JSONException e) {
                            e.printStackTrace();
                            Log.d("****", String.valueOf(e));

                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
                getmovienightbygroupidResponseListener.onError("Error");
                Log.d("****", String.valueOf(error));
            }
        });
        mQueue.add(request);

    }

    public interface getMovieNightbyGroupIDResponseListener {
        void onError (String message);

        void onResponse(GroupNight groupNight);
    }

    //This creates a new group
    public void createMovieNight(Context context, CreateMovieNightResponseListener createmovienightResponseListener, String groupid, String movieid, String date, String time, String userid) {
        ;
        RequestQueue mQueue = Volley.newRequestQueue(context);
        String createmovienighturl = "https://apex.oracle.com/pls/apex/gdeane545/gr/addmovienight/" + groupid + "?movieid=" + movieid + "&passeddate=" + date + "&passedtime=" + time + "&passeduserid=" + userid;


        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, createmovienighturl, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        createmovienightResponseListener.onResponse("yay");
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
                createmovienightResponseListener.onError(error + "");
                Log.d("****", String.valueOf(error));
            }
        });
        mQueue.add(request);


    }

    public interface CreateMovieNightResponseListener {
        void onError(String message);

        void onResponse(String username);
    }

    //This sends a friend request
    public void sendFriendRequest(Context context, sendFriendRequestResponseListener sendfriendrequestResponseListener, String senderid, String receiverid) {
        ;
        RequestQueue mQueue = Volley.newRequestQueue(context);
        String createmovienighturl = "https://apex.oracle.com/pls/apex/gdeane545/gr/sendfriendrequest/" + senderid + "?passedrequest=" + receiverid;


        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, createmovienighturl, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        sendfriendrequestResponseListener.onResponse("yay");
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
                sendfriendrequestResponseListener.onError(error + "");
                Log.d("****", String.valueOf(error));
            }
        });
        mQueue.add(request);


    }

    public interface sendFriendRequestResponseListener {
        void onError(String message);

        void onResponse(String friendRequest);
    }

    public void getFriendRequests(Context context, getFriendRequestsResponseListener getfriendsrequestsResponseListener, String id) {
        friendrequestlist.clear();

        RequestQueue mQueue = Volley.newRequestQueue(context);

        String friendrequesturl = "https://apex.oracle.com/pls/apex/gdeane545/gr/getfriendrequests/" + id;

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, friendrequesturl, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            JSONArray jsonArray = response.getJSONArray("items");


                                Log.d("JSONARRAY",jsonArray.length() + "");

                                for (int i = 0; i < jsonArray.length(); i++) {

                                    User user1 = new User();

                                    JSONObject friendrequest = jsonArray.getJSONObject(i);

                                    String user1id = friendrequest.getString("user1id");

                                    user1.setId(Integer.parseInt(user1id));

                                    String username = friendrequest.getString("username");
                                    user1.setUsername(username);

                                    friendrequestlist.add(user1);

                                }

                                 getfriendsrequestsResponseListener.onResponse(friendrequestlist);


                        }

                        catch (JSONException e) {
                            e.printStackTrace();
                            Log.d("****", String.valueOf(e));

                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
                getfriendsrequestsResponseListener.onError("Error");
                Log.d("****", String.valueOf(error));
            }
        });
        mQueue.add(request);

    }

    public interface getFriendRequestsResponseListener {
        void onError (String message);

        void onResponse(List<User> userlist);
    }

    //Updates a record in the user movie table and adds a rating
    public void acceptFriendRequest(Context context, acceptFriendRequestResponseListener acceptfriendrequestResponseListener, int userid, int senderid) {
        RequestQueue mQueue = Volley.newRequestQueue(context);
        String acceptrequesturl = "https://apex.oracle.com/pls/apex/gdeane545/gr/acceptfriendrequest/" + userid + "?senderid=" + senderid;

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.PUT, acceptrequesturl, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        acceptfriendrequestResponseListener.onResponse(response + "");
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
                acceptfriendrequestResponseListener.onError(error + "");
                Log.d("****", String.valueOf(error));
            }
        });
        mQueue.add(request);


    }

    public interface acceptFriendRequestResponseListener {
        void onError(String message);

        void onResponse(String message);
    }


    //Updates a record in the user movie table and adds a rating
    public void rejectFriendRequest(Context context, rejectFriendRequestResponseListener rejectfriendrequestResponseListener, int userid, int senderid) {
        RequestQueue mQueue = Volley.newRequestQueue(context);
        String rejectrequesturl = "https://apex.oracle.com/pls/apex/gdeane545/gr/rejectfriendrequest/" + userid + "?senderid=" + senderid;

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.DELETE, rejectrequesturl, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        rejectfriendrequestResponseListener.onResponse(response + "");
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
                rejectfriendrequestResponseListener.onError(error + "");
                Log.d("****", String.valueOf(error));
            }
        });
        mQueue.add(request);


    }

    public interface rejectFriendRequestResponseListener {
        void onError(String message);

        void onResponse(String message);
    }

    public void getGroupMembers(Context context, getGroupMembersResponseListener getgroupmembersResponseListener, String groupid) {
        groupMemberList.clear();

        RequestQueue mQueue = Volley.newRequestQueue(context);

        String getmemberrequesturl = "https://apex.oracle.com/pls/apex/gdeane545/gr/getmembersbygroupid/" + groupid;



        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, getmemberrequesturl, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            JSONArray jsonArray = response.getJSONArray("items");


                            Log.d("JSONARRAYGMEMBERS",jsonArray.length() + "");

                            for (int i = 0; i < jsonArray.length(); i++) {

                                User user1 = new User();

                                JSONObject groupMember = jsonArray.getJSONObject(i);

                                String user1id = groupMember.getString("userid");

                                Log.d("12USERIDFROMGROUP",user1id);

                                user1.setId(Integer.parseInt(user1id));


                                groupMemberList.add(user1);

                            }

                            getgroupmembersResponseListener.onResponse(groupMemberList);


                        }

                        catch (JSONException e) {
                            e.printStackTrace();
                            Log.d("****", String.valueOf(e));

                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
                getgroupmembersResponseListener.onError("Error");
                Log.d("****", String.valueOf(error));
            }
        });
        mQueue.add(request);

    }

    public interface getGroupMembersResponseListener {
        void onError (String message);

        void onResponse(List<User> userlist);
    }




    public void insertGroupApproval(Context context, insertGroupApprovalResponseListener insertgroupapprovalResponseListener, String groupnightid, String userid) {

        RequestQueue mQueue = Volley.newRequestQueue(context);
        String createmovienighturl = "https://apex.oracle.com/pls/apex/gdeane545/gr/insertintogroupnightapproval/" + groupnightid + "?userid=" + userid;



        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, createmovienighturl, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        insertgroupapprovalResponseListener.onResponse("yay");
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
                insertgroupapprovalResponseListener.onError(error + "");
                Log.d("Inserted", String.valueOf(error));
            }
        });
        mQueue.add(request);


    }

    public interface insertGroupApprovalResponseListener {
        void onError(String message);

        void onResponse(String friendRequest);
    }




    public void approveGroupNight(Context context, approveGroupNightResponseListener approvegroupnightResponseListener, String userid, String groupnightid) {
        RequestQueue mQueue = Volley.newRequestQueue(context);

        String acceptrequesturl = "https://apex.oracle.com/pls/apex/gdeane545/gr/approvemovienight/" + userid + "?passedgroupid=" + groupnightid;

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.PUT, acceptrequesturl, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        approvegroupnightResponseListener.onResponse(response + "");
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
                approvegroupnightResponseListener.onError(error + "");
                Log.d("****", String.valueOf(error));
            }
        });
        mQueue.add(request);


    }

    public interface approveGroupNightResponseListener {
        void onError(String message);

        void onResponse(String message);
    }


    public void getGroupNightApproval(Context context, getGroupNightApprovalResponseListener getgroupnightapprovalResponseListener, String groupnightid) {
        approvalList.clear();

        RequestQueue mQueue = Volley.newRequestQueue(context);

        String getmemberrequesturl = "https://apex.oracle.com/pls/apex/gdeane545/gr/getgroupapproval/" + groupnightid;

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, getmemberrequesturl, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            JSONArray jsonArray = response.getJSONArray("items");


                            Log.d("JSONARRAYcheckapprove",jsonArray.length() + "");

                            for (int i = 0; i < jsonArray.length(); i++) {

                                JSONObject groupMember = jsonArray.getJSONObject(i);

                                String user1id = groupMember.getString("approval");

                                approvalList.add(user1id);

                            }
                            getgroupnightapprovalResponseListener.onResponse(approvalList);
                        }

                        catch (JSONException e) {
                            e.printStackTrace();
                            Log.d("****", String.valueOf(e));

                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
                getgroupnightapprovalResponseListener.onError("Error");
                Log.d("****", String.valueOf(error));
            }
        });
        mQueue.add(request);

    }

    public interface getGroupNightApprovalResponseListener {
        void onError (String message);

        void onResponse(List<String> approvallist);
    }

    public void rejectGroupNight(Context context, rejectGroupNightResponseListener rejectgroupnightResponseListener, String userid, String groupnightid) {
        RequestQueue mQueue = Volley.newRequestQueue(context);

        String acceptrequesturl = "https://apex.oracle.com/pls/apex/gdeane545/gr/declinemovienight/" + userid + "?passedgroupid=" + groupnightid;

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.PUT, acceptrequesturl, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        rejectgroupnightResponseListener.onResponse(response + "");
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
                rejectgroupnightResponseListener.onError(error + "");
                Log.d("****", String.valueOf(error));
            }
        });
        mQueue.add(request);


    }

    public interface rejectGroupNightResponseListener {
        void onError(String message);

        void onResponse(String message);
    }


    public void getUnapprovedNights(Context context, getUnapprovedNightsResponseListener getunapprovednightsResponseListener, String userid) {

        nightstoapprove.clear();


        RequestQueue mQueue = Volley.newRequestQueue(context);

        String getmemberrequesturl = "https://apex.oracle.com/pls/apex/gdeane545/gr/getunapprovednights/" + userid;


        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, getmemberrequesturl, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            JSONArray jsonArray = response.getJSONArray("items");


                            Log.d("JSONARRAYcheckapprove",jsonArray.length() + "");

                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject groupnight = jsonArray.getJSONObject(i);
                                String date = groupnight.getString("groupnightdate");
                                Log.d("MOVIEDATE", date);
                                Log.d("***********", String.valueOf(response));
                                String time = groupnight.getString("groupnighttime");
                                Log.d("MOVIETIME", time);
                                String groupname = groupnight.getString("groupname");
                                Log.d("GROUPNAME", groupname);
                                String groupnightid = groupnight.getString("groupnightid");


                                int id = Integer.parseInt(groupnight.getString("movieid"));
                                Log.d("GROUPID", id + "");


                                getMoviebyID(context, new SelectedMovieResponseListener() {
                                    @Override
                                    public void onError(String message) {
                                        Log.d("TAG", message);
                                    }

                                    @Override
                                    public void onResponse(Movie movie) {
                                        String title = movie.getTitle();
                                        Log.d("TitleNIGHT", title);
                                        GroupNight groupNight = new GroupNight(groupnightid, title, date, groupname, time);
                                        Log.d("NIGHTTOSTRING", groupNight.toString());
                                        nightstoapprove.add(groupNight);
                                        if (nightstoapprove.size() == jsonArray.length()) {
                                            getunapprovednightsResponseListener.onResponse(nightstoapprove);
                                            Log.d("CHECKINGIF", nightstoapprove.toString());
                                        }

                                    }
                                }, id);
                            }



                        }

                        catch (JSONException e) {
                            e.printStackTrace();
                            Log.d("****", String.valueOf(e));

                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
                getunapprovednightsResponseListener.onError("Error");
                Log.d("****", String.valueOf(error));
            }
        });
        mQueue.add(request);

    }

    public interface getUnapprovedNightsResponseListener {
        void onError (String message);

        void onResponse(List<GroupNight> approvallist);
    }



    public void getSpecificNight(Context context, getSpecificNightResponseListener getspecificnightResponseListener, String groupid, String movieid) {

        RequestQueue mQueue = Volley.newRequestQueue(context);

        String getmemberrequesturl = "https://apex.oracle.com/pls/apex/gdeane545/gr/getspecificgroupnight/" + groupid + "?movieid=" + movieid;


        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, getmemberrequesturl, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            JSONArray jsonArray = response.getJSONArray("items");


                            Log.d("JSONARRAYcheckapprove",jsonArray.length() + "");


                                JSONObject groupMember = jsonArray.getJSONObject(0);
                                String id = groupMember.getString("groupnightid");


                            getspecificnightResponseListener.onResponse(id);


                        }

                        catch (JSONException e) {
                            e.printStackTrace();
                            Log.d("****", String.valueOf(e));

                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
                getspecificnightResponseListener.onError("Error");
                Log.d("****", String.valueOf(error));
            }
        });
        mQueue.add(request);

    }

    public interface getSpecificNightResponseListener {
        void onError (String message);

        void onResponse(String id);
    }

    public void fullyApproveGroupNight(Context context, fullyApproveGroupNightResponseListener fullyapprovegroupnightResponseListener, String groupnightid) {
        RequestQueue mQueue = Volley.newRequestQueue(context);


        String acceptrequesturl = "https://apex.oracle.com/pls/apex/gdeane545/gr/fullyapprovenight/" + groupnightid;

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.PUT, acceptrequesturl, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        fullyapprovegroupnightResponseListener.onResponse(response + "");
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
                fullyapprovegroupnightResponseListener.onError(error + "");
                Log.d("****", String.valueOf(error));
            }
        });
        mQueue.add(request);


    }

    public interface fullyApproveGroupNightResponseListener {
        void onError(String message);

        void onResponse(String message);
    }

    public void getGroupRatings(Context context, getGroupRatingsResponseListener getgroupratingsResponseListener, String groupid) {
        ratingsList.clear();

        RequestQueue mQueue = Volley.newRequestQueue(context);

        String getmemberrequesturl = "https://apex.oracle.com/pls/apex/gdeane545/gr/groupsusermovies/" + groupid;

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, getmemberrequesturl, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            JSONArray jsonArray = response.getJSONArray("items");


                            Log.d("JSONARRAYGMEMBERS",jsonArray.length() + "");


                            List<String> newratinglist = new ArrayList<>();
                            List<List<String>> newgenrelist = new ArrayList<>();
                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject groupRating = jsonArray.getJSONObject(i);

                                //genre.clear();
                                //GroupsRatings rating;

                                String strRating = groupRating.getString("rating");
                                newratinglist.add(strRating);
                                Log.d("FORLOOPRATING",strRating);
                                String strgenre = groupRating.getString("genre");
                                GroupsRatings groupsRatings = new GroupsRatings();
                                groupsRatings.setRating(strRating);
                                groupsRatings.setGenre(strgenre);
                                ratingsList.add(groupsRatings);

                                String movieid = groupRating.getString("movieid");
                                Log.d("FORLOOPMOVIE",movieid);


                                int counter = i;

                            }
                            Log.d("LETSTEST",ratingsList.toString());
                            getgroupratingsResponseListener.onResponse(ratingsList);


                        }

                        catch (JSONException e) {
                            e.printStackTrace();
                            Log.d("****", String.valueOf(e));

                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
                getgroupratingsResponseListener.onError("Error");
                Log.d("****", String.valueOf(error));
            }
        });
        mQueue.add(request);

    }

    public interface getGroupRatingsResponseListener {
        void onError (String message);

        void onResponse(List<GroupsRatings> ratinglist);
    }


    public interface SuggestedMovieResponseListener {
        void onError(String message);

        void onResponse(Movie movie);
    }

    //Retrive a single movie form the API using the movieid
    public void getSuggestedMovie(Context context, SuggestedMovieResponseListener suggestedMovieResponseListener, String genre, String groupid, int passednumber) {

        int genreid = 0;

        switch(genre) {
            case "Action":
                genreid = 28;
                break;
            case "Adventure":
                genreid = 12;
                break;
            case "Animation":
                genreid = 16;
                break;
            case "Comedy":
                genreid = 35;
                break;
            case "Crime":
                genreid = 80;
                break;
            case "Documentary":
                genreid = 99;
                break;
            case "Drama":
                genreid = 18;
                break;
            case "Family":
                genreid = 10751;
                break;
            case "Fantasy":
                genreid = 14;
                break;
            case "History":
                genreid = 36;
                break;
            case "Horror":
                genreid = 27;
                break;
            case "Music":
                genreid = 10402;
                break;
            case "Mystery":
                genreid = 9648;
                break;
            case "Romance":
                genreid = 10749;
                break;
            case "Science Fiction":
                genreid = 878;
                break;
            case "Thriller":
                genreid = 53;
                break;
            case "TV Movie":
                genreid = 10770;
                break;
            case "War":
                genreid = 10752;
                break;
            case "Western":
                genreid = 37;
                break;
            default:
                // code block
        }



        RequestQueue mQueue = Volley.newRequestQueue(context);

        String movieurl = "https://api.themoviedb.org/3/discover/movie?api_key=641b5efff7ea9e0f5b33575963cf62ec&language=en-US&sort_by=popularity.desc&include_adult=false&include_video=false&page=1&with_genres=" + genreid ;

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, movieurl, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            JSONArray jsonArray = response.getJSONArray("results");

                                JSONObject movie = jsonArray.getJSONObject(passednumber);


                                String title = movie.getString("title");
                                String poster = movie.getString("poster_path");
                                String overview = movie.getString("overview");
                                //String runtime = movie.getString("runtime") + " mins";
                                String id = movie.getString("id");
                                String runtime = "120 mins";


                                if ((movie.getString("release_date")).length() < 4) {
                                    year = 0;
                                } else {
                                    year = Integer.parseInt(movie.getString("release_date").substring(0, 4));
                                }

                                //JSONArray genres = movie.getJSONArray("genres");
                                //Log.d("Check getting the array", genres.toString());


                                //JSONObject jsongenre = genres.getJSONObject(0);
                                //String moviegenre = jsongenre.getString("name");

                                //Log.d("Lets Check Genre", moviegenre);
                                Movie movie1 = new Movie(Integer.parseInt(id),title,poster,runtime,overview,year);

                                suggestedMovieResponseListener.onResponse(movie1);


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
                suggestedMovieResponseListener.onError("Error");
                Log.d("****", String.valueOf(error));
            }
        });
        mQueue.add(request);


    }


    public void checkifGroupWatched(Context context, checkifGroupWatchedResponseListener checkifgroupwatchedResponseListener, String groupid, String movieid) {
        ratingsList.clear();

        RequestQueue mQueue = Volley.newRequestQueue(context);

        String getmemberrequesturl = "https://apex.oracle.com/pls/apex/gdeane545/gr/checkifwatchedbygroup/" + groupid +  "?movieid=" + movieid;

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, getmemberrequesturl, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            JSONArray jsonArray = response.getJSONArray("items");


                            Log.d("JSONARRAYGMEMBERS",jsonArray.length() + "");

                            if(jsonArray.length() == 0) {
                                String message = "No";
                                checkifgroupwatchedResponseListener.onResponse(message);
                            }
                            else {
                                String message = "Yes";
                                checkifgroupwatchedResponseListener.onResponse(message);
                            }




                        }

                        catch (JSONException e) {
                            e.printStackTrace();
                            Log.d("****", String.valueOf(e));

                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
                checkifgroupwatchedResponseListener.onError("Error");
                Log.d("****", String.valueOf(error));
            }
        });
        mQueue.add(request);

    }

    public interface checkifGroupWatchedResponseListener {
        void onError (String message);

        void onResponse(String message);
    }


    public interface getUltraGroupResponseListener {
        void onError (String message);

        void onResponse(List<GroupNight> groupList);
    }

    public void getUltramovienightbyuserid(Context context, getUltraGroupResponseListener getgroupResponseListener, String id) {
        ultralist.clear();
        RequestQueue mQueue = Volley.newRequestQueue(context);
        String getgroupurl = "https://apex.oracle.com/pls/apex/gdeane545/gr/ultramovienightbyuserid/" + id;

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, getgroupurl, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            JSONArray jsonArray = response.getJSONArray("items");

                            if (jsonArray.length() == 0)  {
                                GroupNight group1 = new GroupNight();
                                group1.setGroupName("No Groups Yet!");
                                group1.setApproval("Nope");
                                ultralist.add(group1);
                                getgroupResponseListener.onResponse(ultralist);
                            }
                            else {
                                for (int i = 0; i < jsonArray.length(); i++) {

                                    JSONObject groupnight = jsonArray.getJSONObject(i);
                                    groupNight1 = new GroupNight();
                                    if(groupnight.isNull("groupnightid")) {
                                        groupNight1.setId(String.valueOf(0));
                                    }
                                    else {
                                        groupNight1.setId(groupnight.getString("groupnightid"));
                                    }

                                    groupNight1.setGroupName(groupnight.getString("groupname"));
                                    String date = groupnight.getString("groupnightdate");
                                    Log.d("MOVIEDATE2",date);
                                    groupNight1.setDate(date);

                                    String time = groupnight.getString("groupnighttime");
                                    groupNight1.setTime(time);
                                    Log.d("MOVIETIME3",time);

                                    if(groupnight.isNull("fullapproval")) {
                                        groupNight1.setApproval("Nope");
                                    }
                                    else {
                                        String approval = groupnight.getString("fullapproval");
                                        Log.d("approvalJSON4",approval);
                                        groupNight1.setApproval(approval);
                                    }



                                    String groupid = groupnight.getString("groupid");
                                    Log.d("MOVIENIGHTGROUPIDJSON", groupnight.getString("groupid"));
                                    groupNight1.setGroupid(groupid);

                                    if(groupnight.isNull("movieid")) {
                                        ultralist.add(groupNight1);
                                        continue;
                                    }
                                    else {
                                        Movieid = Integer.parseInt(groupnight.getString("movieid"));
                                        Log.d("MOVIEIDJSON5",Movieid + "");
                                        groupNight1.setMovieid(String.valueOf(Movieid));
                                    }

                                    Log.d("UHOHLIST2",ultralist.toString());
                                    ultralist.add(groupNight1);

                                }

                                getgroupResponseListener.onResponse(ultralist);
                            }
                        }
                        catch (JSONException e) {
                            e.printStackTrace();
                            Log.d("****", String.valueOf(e));
                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
                getgroupResponseListener.onError(error + "");
                Log.d("****", String.valueOf(error));
            }
        });
        mQueue.add(request);


    }


    public void getGroupMembersforNight(Context context, getGroupMembersforNightResponseListener getgroupratingsResponseListener, String groupid) {
        membersfornight.clear();

        RequestQueue mQueue = Volley.newRequestQueue(context);

        String getmemberrequesturl = "https://apex.oracle.com/pls/apex/gdeane545/gr/getusersandapprovalsbygroupnightid/" + groupid;

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, getmemberrequesturl, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            JSONArray jsonArray = response.getJSONArray("items");


                            Log.d("JSONARRAYGMEMBERS",jsonArray.length() + "");


                            List<String> newratinglist = new ArrayList<>();
                            List<List<String>> newgenrelist = new ArrayList<>();
                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject groupRating = jsonArray.getJSONObject(i);
                                GroupMember groupMember = new GroupMember();

                                //genre.clear();
                                //GroupsRatings rating;

                                String username = groupRating.getString("username");
                                groupMember.setUsername(username);

                                String approval = groupRating.getString("approval");
                                groupMember.setApproval(approval);

                                membersfornight.add(groupMember);


                            }
                            Log.d("MEMBERLISTNIGHT",membersfornight.toString());
                            getgroupratingsResponseListener.onResponse(membersfornight);


                        }

                        catch (JSONException e) {
                            e.printStackTrace();
                            Log.d("****", String.valueOf(e));

                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
                getgroupratingsResponseListener.onError("Error");
                Log.d("****", String.valueOf(error));
            }
        });
        mQueue.add(request);

    }

    public interface getGroupMembersforNightResponseListener {
        void onError (String message);

        void onResponse(List<GroupMember> memberlist);
    }









}
