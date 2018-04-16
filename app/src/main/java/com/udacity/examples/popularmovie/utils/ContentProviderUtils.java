package com.udacity.examples.popularmovie.utils;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.util.Log;

import com.udacity.examples.popularmovie.data.FavoriteMoviesContract;
import com.udacity.examples.popularmovie.data.Movie;

public class ContentProviderUtils {
    private static final String TAG = ContentProviderUtils.class.getSimpleName();

    public static Cursor getFavoriteMovies(Context context) {
        return context.getContentResolver().query(FavoriteMoviesContract.MovieEntry.CONTENT_URI, null, null, null, null);
    }

    public static void addFavoriteMovie(Context context, Movie movie) {
        final String M = "addFavoriteMovie: ";
        Log.v(TAG, M + "Started, movie=" + movie);

        ContentValues cv = new ContentValues();
        cv.put(FavoriteMoviesContract.MovieEntry.COLUMN_MOVIE_TITLE, movie.getTitle());
        cv.put(FavoriteMoviesContract.MovieEntry.COLUMN_MOVIE_ID, movie.getId());
        cv.put(FavoriteMoviesContract.MovieEntry.COLUMN_MOVIE_POSTER_PATH, movie.getPosterPath());
        cv.put(FavoriteMoviesContract.MovieEntry.COLUMN_MOVIE_BACK_DROP_PATH, movie.getBackdropPath());
        cv.put(FavoriteMoviesContract.MovieEntry.COLUMN_MOVIE_RATE, movie.getVoteAverage());
        cv.put(FavoriteMoviesContract.MovieEntry.COLUMN_MOVIE_RELEASE_DATE, movie.getReleaseDate());
        cv.put(FavoriteMoviesContract.MovieEntry.COLUMN_MOVIE_OVERVIEW, movie.getOverview());
        Log.v(TAG, M + "cv=" + cv);

        context.getContentResolver().insert(FavoriteMoviesContract.MovieEntry.CONTENT_URI, cv);

        Log.v(TAG, M + "Finished");
    }

    public static void removeFavoriteMovie(Context context, Movie movie) {
        final String M = "removeFavoriteMovie: ";
        Log.v(TAG, M + "Started, movie=" + movie);

        Uri movieWithIdUri = FavoriteMoviesContract.MovieEntry.CONTENT_URI.buildUpon().appendPath(String.valueOf(movie.getId())).build();
        Log.v(TAG, M + "Uri=" + movieWithIdUri);

        context.getContentResolver().delete(movieWithIdUri, null, null);
        Log.v(TAG, M + "Finished");
    }

    public static boolean isFavorite(Context context, Movie movie) {
        final String M = "isFavorite: ";
        Log.v(TAG, M + "Started, movie=" + movie);

        Uri movieWithIdUri = FavoriteMoviesContract.MovieEntry.CONTENT_URI.buildUpon().appendPath(String.valueOf(movie.getId())).build();
        Log.v(TAG, M + "Uri=" + movieWithIdUri);

        Cursor cursor = context.getContentResolver().query(movieWithIdUri, null, null, null, null);

        Log.v(TAG, M + "Finished");
        return  (cursor != null && cursor.getCount() == 1);
    }
}
