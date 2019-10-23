package com.example.flixster.models;

import com.codepath.asynchttpclient.AsyncHttpClient;
import com.codepath.asynchttpclient.callback.JsonHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.parceler.Parcel;

import java.util.ArrayList;
import java.util.List;

import okhttp3.Headers;

@Parcel
public class Movie {

    public static final String IMAGE_SIZES_URL = "https://api.themoviedb.org/3/configuration?api_key=a07e22bc18f5cb106bfe4cc1f83ad8ed";

    String backdropPath;
    String backdropSize;
    String posterPath;
    String posterSize;
    String title;
    String overview;
    double rating;
    int movieId;

    // for Parceler library
    public Movie() {

    }

    public Movie(JSONObject jsonObject) throws JSONException {
        backdropPath = jsonObject.getString("backdrop_path");
        posterPath = jsonObject.getString("poster_path");
        title = jsonObject.getString("title");
        overview = jsonObject.getString("overview");
        rating = jsonObject.getDouble("vote_average");
        movieId = jsonObject.getInt("id");
    }

    public static List<Movie> fromJSONArray(JSONArray movieJsonArray) throws JSONException {
        List<Movie> movies = new ArrayList<>();
        for (int i = 0; i < movieJsonArray.length(); i++) {
            movies.add(new Movie(movieJsonArray.getJSONObject(i)));
        }
        return movies;
    }

    public String getBackdropPath() {
        AsyncHttpClient client = new AsyncHttpClient();
        client.get(IMAGE_SIZES_URL, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Headers headers, JSON json) {
                JSONObject jsonObject = json.jsonObject;
                try {
                    JSONObject images = jsonObject.getJSONObject("images");
                    JSONArray backdropSizes = images.getJSONArray("backdrop_sizes");
                    backdropSize = backdropSizes.getString(0);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int statusCode, Headers headers, String response, Throwable throwable) {

            }
        });
        return String.format("https://image.tmdb.org/t/p/%s/%s", backdropSize, backdropPath);
    }

    public String getPosterPath() {
        AsyncHttpClient client = new AsyncHttpClient();
        client.get(IMAGE_SIZES_URL, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Headers headers, JSON json) {
                JSONObject jsonObject = json.jsonObject;
                try {
                    JSONObject images = jsonObject.getJSONObject("images");
                    JSONArray posterSizes = images.getJSONArray("poster_sizes");
                    posterSize = posterSizes.getString(3);

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int statusCode, Headers headers, String response, Throwable throwable) {

            }
        });
        return String.format("https://image.tmdb.org/t/p/%s/%s", posterSize, posterPath);
    }

    public String getTitle() {
        return title;
    }

    public String getOverview() {
        return overview;
    }

    public double getRating() { return rating; }

    public int getMovieId() { return movieId; }
}
