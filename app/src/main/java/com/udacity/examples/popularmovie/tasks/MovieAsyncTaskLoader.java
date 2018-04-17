package com.udacity.examples.popularmovie.tasks;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.content.AsyncTaskLoader;
import android.util.Log;

import com.udacity.examples.popularmovie.data.Movie;
import com.udacity.examples.popularmovie.data.Review;
import com.udacity.examples.popularmovie.data.Video;
import com.udacity.examples.popularmovie.utils.JsonUtils;
import com.udacity.examples.popularmovie.utils.NetworkUtils;

import java.util.List;

/**
 * Created by noname on 4/17/18.
 */

public class MovieAsyncTaskLoader extends AsyncTaskLoader<Movie> {
    private static final String TAG = MovieAsyncTaskLoader.class.getSimpleName();
    private Movie movie = null;
    private Movie cachedMovie = null;

    public MovieAsyncTaskLoader(@NonNull Context context, Movie movie) {
        super(context);

        this.movie = movie;
    }

    @Override
    public Movie loadInBackground() {
        final String M = "loadInBackground: ";

        String videosStr = NetworkUtils.loadVideos(movie.getId());
        List<Video> videos = JsonUtils.parseVideos(videosStr);

        String reviewsStr = NetworkUtils.loadReviews(movie.getId());
        List<Review> reviews = JsonUtils.parseReviews(reviewsStr);

        movie.setVideos(videos);
        movie.setReviews(reviews);

        Log.d(TAG, M + "movie=" + movie);
        return movie;
    }

    @Override
    public void deliverResult(Movie data) {
        cachedMovie = data;

        super.deliverResult(data);
    }

    @Override
    protected void onStartLoading() {
        if (cachedMovie != null) {
            deliverResult(cachedMovie);
        } else {
            forceLoad();
        }
    }
}
