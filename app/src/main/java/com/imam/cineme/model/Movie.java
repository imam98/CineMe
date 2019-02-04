package com.imam.cineme.model;

import android.os.Parcel;
import android.os.Parcelable;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

public class Movie implements Parcelable {
    private int id;
    private String title;
    private String overview;
    private String posterPath;
    private String backdropPath;
    private String director;
    private ArrayList<Cast> casts = new ArrayList<>();
    private ArrayList<String> genres = new ArrayList<>();
    private Calendar releaseDate = Calendar.getInstance();
    private int runtime;
    private double rate;
    private int popularity;
    private ArrayList<Movie> recommendations = new ArrayList<>();

    public void parseDetail(JSONObject jsonResponse) throws JSONException, ParseException {
        overview = jsonResponse.getString("overview");
        backdropPath = jsonResponse.getString("backdrop_path");
        runtime = jsonResponse.getInt("runtime");
        popularity = (int)jsonResponse.getDouble("popularity");

        JSONArray genreList = jsonResponse.getJSONArray("genres");
        for (int i = 0; i < genreList.length(); i++) {
            genres.add(genreList.getJSONObject(i).getString("name"));
        }

        JSONObject credits = jsonResponse.getJSONObject("credits");
        JSONArray castList = credits.getJSONArray("cast");
        for (int i = 0; i < 5; i++) {
            JSONObject currCast = castList.getJSONObject(i);
            Cast cast = new Cast();
            cast.setId(currCast.getInt("id"));
            cast.setName(currCast.getString("name"));
            cast.setCharacter(currCast.getString("character"));
            cast.setProfilePath(currCast.getString("profile_path"));
            casts.add(cast);
        }

        JSONArray crewList = credits.getJSONArray("crew");
        for (int i = 0; i < crewList.length(); i++) {
            JSONObject currCrew = crewList.getJSONObject(i);
            if (currCrew.getString("job").equals("Director")) {
                director = currCrew.getString("name");
                break;
            }
        }

        JSONArray recommendations = jsonResponse.getJSONObject("recommendations")
                .getJSONArray("results");
        for (int i = 0; i < 10; i++) {
            JSONObject currMovie = recommendations.getJSONObject(i);

            Movie movie = new Movie();
            movie.setId(currMovie.getInt("id"));
            movie.setTitle(currMovie.getString("title"));
            movie.setPosterPath(currMovie.getString("poster_path"));
            movie.setRate(currMovie.getDouble("vote_average"));
            movie.setReleaseDate(currMovie.getString("release_date"));

            this.recommendations.add(movie);
        }
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
        DateFormat formatter = new SimpleDateFormat("dd-MM-yyyy", Locale.ENGLISH);
        return formatter.format(releaseDate.getTime());
    }

    public String getReleaseYear() {
        return String.valueOf(releaseDate.get(Calendar.YEAR));
    }

    public void setReleaseDate(String formattedDate) throws ParseException {
        DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);
        releaseDate.setTime(formatter.parse(formattedDate));
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

    public void setRate(double rate) {
        this.rate = rate;
    }

    public int getPopularity() {
        return popularity;
    }

    public void setPopularity(int popularity) {
        this.popularity = popularity;
    }

    public ArrayList<Movie> getRecommendations() {
        return recommendations;
    }

    public void setRecommendations(ArrayList<Movie> recommendations) {
        this.recommendations = recommendations;
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
        dest.writeSerializable(this.releaseDate);
        dest.writeInt(this.runtime);
        dest.writeDouble(this.rate);
        dest.writeInt(this.popularity);
        dest.writeTypedList(this.recommendations);
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
        this.releaseDate = (Calendar) in.readSerializable();
        this.runtime = in.readInt();
        this.rate = in.readDouble();
        this.popularity = in.readInt();
        this.recommendations = in.createTypedArrayList(Movie.CREATOR);
    }

    public static final Creator<Movie> CREATOR = new Creator<Movie>() {
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
