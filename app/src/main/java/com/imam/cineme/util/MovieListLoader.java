package com.imam.cineme.util;

import android.content.AsyncTaskLoader;
import android.content.Context;
import android.util.Log;

import com.imam.cineme.model.Movie;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.SyncHttpClient;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

import cz.msebera.android.httpclient.Header;

public class MovieListLoader extends AsyncTaskLoader<ArrayList<Movie>> {
    public static final String LIST_CATEGORY = "list_category";
    private String listCategory;

    public MovieListLoader(Context context, String listCategory) {
        super(context);
        this.listCategory = listCategory;
        forceLoad();
    }

    @Override
    public ArrayList<Movie> loadInBackground() {
        final ArrayList<Movie> movieList = new ArrayList<>();

        String url = API.MOVIE_BASE_URL + listCategory + API.API_KEY + API.REGION;
        SyncHttpClient client = new SyncHttpClient();
        client.get(url, new AsyncHttpResponseHandler() {
            @Override
            public void onStart() {
                super.onStart();
                setUseSynchronousMode(true);
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                try {
                    String responseStr = new String(responseBody);
                    JSONObject responseObj = new JSONObject(responseStr);

                    JSONArray movieResults = responseObj.getJSONArray("results");
                    for (int i = 0; i < movieResults.length(); i++) {
                        JSONObject currMovie = movieResults.getJSONObject(i);

                        Movie movie = new Movie();
                        movie.setId(currMovie.getInt("id"));
                        movie.setReleaseDate(currMovie.getString("release_date"));
                        movie.setPosterPath(currMovie.getString("poster_path"));
                        movie.setRate(currMovie.getDouble("vote_average"));

                        movieList.add(movie);
                    }
                } catch (Exception e) {
                    Log.e("CineMe", e.getMessage());
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {

            }
        });

        return movieList;
    }
}
