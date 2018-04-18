package com.udacity.examples.popularmovie.tasks;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v4.content.Loader;
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

public class MovieAsyncTaskLoader implements LoaderManager.LoaderCallbacks<Movie>{
    private static final String TAG = MovieAsyncTaskLoader.class.getSimpleName();
    public static final String ASYNC_LOADER_MOVIE_BUNDLE_ID = "movie";

    private MovieAsyncTaskLoaderListener listener;
    private Context context;

    public interface MovieAsyncTaskLoaderListener {
        void notify(Movie movie);
    }

    public MovieAsyncTaskLoader(@NonNull Context context, MovieAsyncTaskLoaderListener activityListener) {
        this.context = context;
        listener = activityListener;
    }

    @Override
    public Loader<Movie> onCreateLoader(int i, final Bundle bundle) {
        //return new MovieAsyncTaskLoader(getContext(), (Movie) bundle.getParcelable(ASYNC_LOADER_MOVIE_BUNDLE_ID));
        return new AsyncTaskLoader<Movie>(context) {
            private Movie movie = null;

            @Nullable
            @Override
            public Movie loadInBackground() {
                final String M = "loadInBackground: ";

                Movie movie = bundle.getParcelable(ASYNC_LOADER_MOVIE_BUNDLE_ID);

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
                movie = data;

                super.deliverResult(data);
            }

            @Override
            protected void onStartLoading() {
                if (movie != null) {
                    deliverResult(movie);
                } else {
                    forceLoad();
                }
            }
        };
    }

    @Override
    public void onLoadFinished(@NonNull Loader<Movie> loader, Movie data) {
        listener.notify(data);
    }

    @Override
    public void onLoaderReset(@NonNull Loader<Movie> loader) { }


}
