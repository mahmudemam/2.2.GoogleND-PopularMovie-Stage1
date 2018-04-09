package com.udacity.examples.popularmovie.utils;

import android.util.Log;

import com.udacity.examples.popularmovie.data.Movie;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Mahmoud Emam on 2/28/18.
 */

public class JsonUtils {
    private static final String TAG = JsonUtils.class.getSimpleName();

    public static List<Movie> parseMovies(String jsonStr) {
        List<Movie> movies = null;

        try {
            JSONObject jsonObject = new JSONObject(jsonStr);

            JSONArray results = jsonObject.optJSONArray("results");

            if (results != null) {
                movies = new ArrayList<>(results.length());

                for (int i = 0; i < results.length(); i++) {
                    JSONObject movieObject = results.getJSONObject(i);

                    JSONArray genresArray = movieObject.optJSONArray("genre_ids");
                    int[] genres = null;

                    if (genresArray != null) {
                        genres = new int[genresArray.length()];

                        for (int j = 0; j < genresArray.length(); j++) {
                            genres[j] = genresArray.optInt(j);
                        }
                    }

                    movies.add(new Movie(
                            movieObject.optInt("vote_count", 0),
                            movieObject.optInt("id", 0),
                            movieObject.optBoolean("video", false),
                            movieObject.optDouble("vote_average", 0d),
                            movieObject.optString("title", ""),
                            movieObject.optDouble("popularity", 0d),
                            movieObject.optString("poster_path", ""),
                            movieObject.optString("original_language", ""),
                            movieObject.optString("original_title", ""),
                            genres,
                            movieObject.optString("backdrop_path", ""),
                            movieObject.optBoolean("adult", false),
                            movieObject.optString("overview", ""),
                            movieObject.optString("release_date", "")));

                }
            }
        } catch (JSONException ex) {
            Log.e(TAG, "parseMovies: ", ex);
        }

        return movies;
    }

    public static List<Movie.Video> parseVideos(String jsonStr) {
        List<Movie.Video> videos = null;

        try {
            JSONObject jsonObject = new JSONObject(jsonStr);

            JSONArray results = jsonObject.optJSONArray("results");

            if (results != null) {
                videos = new ArrayList<>(results.length());

                for (int i = 0; i < results.length(); i++) {
                    JSONObject videosObject = results.getJSONObject(i);

                    videos.add(new Movie.Video(videosObject.optString("name", "No Name"),
                            videosObject.optString("key", ""),
                            videosObject.optString("type", "Trailer")));
                }
            }
        } catch (JSONException ex) {
            Log.e(TAG, "parseVideos: ", ex);
        }

        return videos;
    }
}
