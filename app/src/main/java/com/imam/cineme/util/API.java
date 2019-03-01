package com.imam.cineme.util;

import com.imam.cineme.BuildConfig;

public class API {
    public static final String API_KEY = "?api_key=" + BuildConfig.API_KEY;
    public static final String MOVIE_BASE_URL = "https://api.themoviedb.org/3/movie/";
    public static final String SEARCH_BASE_URL = "https://api.themoviedb.org/3/search/";
    public static final String IMAGE_BASE_URL = "https://image.tmdb.org/t/p/";
    public static final String POSTER_SIZE = "w185";
    public static final String PROFILE_SIZE = "w185";
    public static final String BACKDROP_SIZE = "w780";
    public static final String REGION = "&region=US";
    public static final String NOW_PLAYING = "now_playing";
    public static final String UPCOMING = "upcoming";
    public static final String POPULAR = "popular";
    public static final String QUERY = "&query=";
    public static final String ADDITIONAL_DETAILS = "&append_to_response=credits,recommendations";
}
