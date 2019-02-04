package com.imam.cineme.util;

import android.content.AsyncTaskLoader;
import android.content.Context;
import android.util.Log;

import com.imam.cineme.model.Movie;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.SyncHttpClient;

import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;

public class MovieDetailLoader extends AsyncTaskLoader<Movie> {
    private Movie movie;

    public MovieDetailLoader(Context context, Movie movie) {
        super(context);
        this.movie = movie;
        forceLoad();
    }

    @Override
    public Movie loadInBackground() {
        String url = API.MOVIE_BASE_URL + movie.getId() + API.API_KEY + API.ADDITIONAL_DETAILS;
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
                    movie.parseDetail(responseObj);
                } catch (Exception e) {
                    Log.e("CineMe", e.getMessage());
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {

            }
        });
        return movie;
    }
}
