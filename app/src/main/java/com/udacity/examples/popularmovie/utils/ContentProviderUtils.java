package com.udacity.examples.popularmovie.utils;

import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.util.Log;

import com.udacity.examples.popularmovie.data.FavoriteMoviesContract;
import com.udacity.examples.popularmovie.data.Movie;
import com.udacity.examples.popularmovie.data.Review;
import com.udacity.examples.popularmovie.data.Video;

import java.util.List;

public class ContentProviderUtils {
    private static final String TAG = ContentProviderUtils.class.getSimpleName();

    public static Cursor getFavoriteMovies(Context context) {
        return context.getContentResolver().query(FavoriteMoviesContract.MovieEntry.CONTENT_URI, null, null, null, null);
    }

    public static Cursor getVideos(Context context, Movie movie) {
        Uri uri = FavoriteMoviesContract.VideoEntry.CONTENT_URI.buildUpon().appendPath(String.valueOf(movie.getId())).build();
        return context.getContentResolver().query(uri, null, null, null, null);
    }

    public static Cursor getReviews(Context context, Movie movie) {
        Uri uri = FavoriteMoviesContract.ReviewEntry.CONTENT_URI.buildUpon().appendPath(String.valueOf(movie.getId())).build();
        return context.getContentResolver().query(uri, null, null, null, null);
    }

    public static void addFavoriteMovie(Context context, Movie movie) {
        final String M = "addFavoriteMovie: ";
        Log.v(TAG, M + "Started, movie=" + movie);

        ContentValues movieCV = new ContentValues();
        movieCV.put(FavoriteMoviesContract.MovieEntry.COLUMN_MOVIE_TITLE, movie.getTitle());
        movieCV.put(FavoriteMoviesContract.MovieEntry.COLUMN_MOVIE_ID, movie.getId());
        movieCV.put(FavoriteMoviesContract.MovieEntry.COLUMN_MOVIE_POSTER_PATH, movie.getPosterPath());
        movieCV.put(FavoriteMoviesContract.MovieEntry.COLUMN_MOVIE_BACK_DROP_PATH, movie.getBackdropPath());
        movieCV.put(FavoriteMoviesContract.MovieEntry.COLUMN_MOVIE_RATE, movie.getVoteAverage());
        movieCV.put(FavoriteMoviesContract.MovieEntry.COLUMN_MOVIE_RELEASE_DATE, movie.getReleaseDate());
        movieCV.put(FavoriteMoviesContract.MovieEntry.COLUMN_MOVIE_OVERVIEW, movie.getOverview());
        Log.v(TAG, M + "cv=" + movieCV);

        Uri movieUri = context.getContentResolver().insert(FavoriteMoviesContract.MovieEntry.CONTENT_URI, movieCV);
        Log.d(TAG, M + "movieUri=" + movieUri);

        long movieId = ContentUris.parseId(movieUri);
        Log.d(TAG, M + "movieId=" + movieId);

        Log.v(TAG, M + "Finished");
    }

    public static void addMovieVideos(Context context, int movieId, List<Video> videos) {
        final String M = "addMovieVideos: ";

        Log.d(TAG, M + "videos=" + videos);

        if (videos == null)
            return;

        for (Video video: videos) {
            ContentValues videoCV = new ContentValues();
            videoCV.put(FavoriteMoviesContract.VideoEntry.COLUMN_MOVIE_ID, movieId);
            videoCV.put(FavoriteMoviesContract.VideoEntry.COLUMN_VIDEO_NAME, video.getName());
            videoCV.put(FavoriteMoviesContract.VideoEntry.COLUMN_VIDEO_KEY, video.getKey());
            videoCV.put(FavoriteMoviesContract.VideoEntry.COLUMN_VIDEO_TYPE, video.getType());

            context.getContentResolver().insert(FavoriteMoviesContract.VideoEntry.CONTENT_URI, videoCV);
        }
    }

    public static void addMovieReviews(Context context, int movieId, List<Review> reviews) {
        final String M = "addMovieReviews: ";

        Log.d(TAG, M + "reviews=" + reviews);

        if (reviews == null)
            return;

        for (Review review: reviews) {
            ContentValues reviewCV = new ContentValues();
            reviewCV.put(FavoriteMoviesContract.ReviewEntry.COLUMN_MOVIE_ID, movieId);
            reviewCV.put(FavoriteMoviesContract.ReviewEntry.COLUMN_REVIEW_AUTHOR, review.getAuthor());
            reviewCV.put(FavoriteMoviesContract.ReviewEntry.COLUMN_REVIEW_CONTENT, review.getComment());

            context.getContentResolver().insert(FavoriteMoviesContract.ReviewEntry.CONTENT_URI, reviewCV);
        }
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

        Log.v(TAG, M + "Finished, isFavorite=" + (cursor != null && cursor.getCount() == 1));
        return  (cursor != null && cursor.getCount() == 1);
    }
}
