package com.imam.cineme.model;

import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

import com.imam.cineme.util.API;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.SyncHttpClient;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import cz.msebera.android.httpclient.Header;

public class Movie implements Parcelable {
    private int id;
    private String title;
    private String overview;
    private String posterPath;
    private String backdropPath;
    private String director;
    private ArrayList<Cast> casts;
    private ArrayList<String> genres;
    private String releaseDate;
    private int runtime;
    private double rate;
    private int popularity;

    public void setMovieDetails(JSONObject jsonResponse) throws JSONException {
        title = jsonResponse.getString("title");
        overview = jsonResponse.getString("overview");
        backdropPath = jsonResponse.getString("backdrop_path");
        releaseDate = jsonResponse.getString("release_date");
        runtime = jsonResponse.getInt("runtime");
        rate = jsonResponse.getDouble("vote_average");
        popularity = (int)jsonResponse.getDouble("popularity");

        JSONArray genreList = jsonResponse.getJSONArray("genres");
        for (int i = 0; i < genreList.length(); i++) {
            genres.add(genreList.getJSONObject(i).getString("name"));
        }

        String url = API.MOVIE_BASE_URL + id + "/credits" + API.API_KEY;
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
                    JSONArray castList = responseObj.getJSONArray("cast");
                    for (int i = 0; i < castList.length(); i++) {
                        JSONObject currCast = castList.getJSONObject(i);
                        Cast cast = new Cast();
                        cast.setId(currCast.getInt("id"));
                        cast.setName(currCast.getString("name"));
                        cast.setCharacter(currCast.getString("character"));
                        cast.setProfilePath(currCast.getString("profile_path"));
                        casts.add(cast);
                    }

                    JSONArray crewList = responseObj.getJSONArray("crew");
                    for (int i = 0; i < crewList.length(); i++) {
                        JSONObject currCrew = crewList.getJSONObject(i);
                        if (currCrew.getString("job").equals("Director")) {
                            director = currCrew.getString("name");
                            break;
                        }
                    }
                } catch (Exception e) {
                    Log.e("CineMe", e.getMessage());
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {

            }
        });
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getOverview() {
        return overview;
    }

    public void setOverview(String overview) {
        this.overview = overview;
    }

    public String getPosterPath() {
        return posterPath;
    }

    public void setPosterPath(String posterPath) {
        this.posterPath = posterPath;
    }

    public String getBackdropPath() {
        return backdropPath;
    }

    public void setBackdropPath(String backdropPath) {
        this.backdropPath = backdropPath;
    }

    public String getDirector() {
        return director;
    }

    public void setDirector(String director) {
        this.director = director;
    }

    public ArrayList<Cast> getCasts() {
        return casts;
    }

    public void setCasts(ArrayList<Cast> casts) {
        this.casts = casts;
    }

    public ArrayList<String> getGenres() {
        return genres;
    }

    public void setGenres(ArrayList<String> genres) {
        this.genres = genres;
    }

    public String getReleaseDate() {
        return releaseDate;
    }

    public void setReleaseDate(String releaseDate) {
        this.releaseDate = releaseDate;
    }

    public int getRuntime() {
        return runtime;
    }

    public void setRuntime(int runtime) {
        this.runtime = runtime;
    }

    public double getRate() {
        return rate;
    }

    public void setRate(float rate) {
        this.rate = rate;
    }

    public int getPopularity() {
        return popularity;
    }

    public void setPopularity(int popularity) {
        this.popularity = popularity;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.id);
        dest.writeString(this.title);
        dest.writeString(this.overview);
        dest.writeString(this.posterPath);
        dest.writeString(this.backdropPath);
        dest.writeString(this.director);
        dest.writeTypedList(this.casts);
        dest.writeStringList(this.genres);
        dest.writeString(this.releaseDate);
        dest.writeInt(this.runtime);
        dest.writeDouble(this.rate);
        dest.writeInt(this.popularity);
    }

    public Movie() {
    }

    protected Movie(Parcel in) {
        this.id = in.readInt();
        this.title = in.readString();
        this.overview = in.readString();
        this.posterPath = in.readString();
        this.backdropPath = in.readString();
        this.director = in.readString();
        this.casts = in.createTypedArrayList(Cast.CREATOR);
        this.genres = in.createStringArrayList();
        this.releaseDate = in.readString();
        this.runtime = in.readInt();
        this.rate = in.readDouble();
        this.popularity = in.readInt();
    }

    public static final Parcelable.Creator<Movie> CREATOR = new Parcelable.Creator<Movie>() {
        @Override
        public Movie createFromParcel(Parcel source) {
            return new Movie(source);
        }

        @Override
        public Movie[] newArray(int size) {
            return new Movie[size];
        }
    };
}
