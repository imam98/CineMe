package com.imam.cineme.util;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.content.AsyncTaskLoader;
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
    public static final String QUERY_STRING = "query_string";
    private String listCategory;
    private String query;

    public MovieListLoader(Context context, Bundle data) {
        super(context);
        listCategory = data.getString(LIST_CATEGORY);
        query = data.getString(QUERY_STRING);
        forceLoad();
    }

    @Override
    public ArrayList<Movie> loadInBackground() {
        final ArrayList<Movie> movieList = new ArrayList<>();

        String url;
        if (listCategory.equals(API.QUERY)) {
            url = API.SEARCH_BASE_URL + "movie" + API.API_KEY + API.REGION + API.QUERY + query;
        } else {
            url = API.MOVIE_BASE_URL + listCategory + API.API_KEY + API.REGION;
        }

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
                        movie.setTitle(currMovie.getString("title"));
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
