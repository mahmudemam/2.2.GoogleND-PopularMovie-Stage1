package com.udacity.examples.popularmovie.adapters;

import android.content.Context;
import android.database.Cursor;
import android.support.annotation.NonNull;
import android.util.Log;

import com.udacity.examples.popularmovie.data.FavoriteMoviesContract;
import com.udacity.examples.popularmovie.data.Movie;

public class MovieCursorAdapter extends MoviesAdapter {
    public static final String TAG = MovieCursorAdapter.class.getSimpleName();
    private Cursor movies;

    public MovieCursorAdapter(Context context, OnMovieClickListener listener, Cursor movies) {
        super(context, listener);

        this.movies = movies;
    }

    @Override
    public void onBindViewHolder(@NonNull MovieViewHolder holder, int position) {
        Log.v(TAG, "onBindViewHolder: Started");

        movies.moveToPosition(position);

        Movie movie = new Movie(
                movies.getInt(movies.getColumnIndex(FavoriteMoviesContract.MovieEntry.COLUMN_MOVIE_ID)),
                movies.getString(movies.getColumnIndex(FavoriteMoviesContract.MovieEntry.COLUMN_MOVIE_TITLE)),
                movies.getString(movies.getColumnIndex(FavoriteMoviesContract.MovieEntry.COLUMN_MOVIE_POSTER_PATH)),
                movies.getString(movies.getColumnIndex(FavoriteMoviesContract.MovieEntry.COLUMN_MOVIE_BACK_DROP_PATH)),
                movies.getDouble(movies.getColumnIndex(FavoriteMoviesContract.MovieEntry.COLUMN_MOVIE_RATE)),
                movies.getString(movies.getColumnIndex(FavoriteMoviesContract.MovieEntry.COLUMN_MOVIE_OVERVIEW)),
                movies.getString(movies.getColumnIndex(FavoriteMoviesContract.MovieEntry.COLUMN_MOVIE_RELEASE_DATE))
        );
        Log.v(TAG, "onBindViewHolder: uri=" + movie);

        movie.setFavorite(true);
        holder.bind(movie);

        Log.v(TAG, "onBindViewHolder: Finished");
    }

    @Override
    public int getItemCount() {
        if (movies == null) return 0;

        return movies.getCount();
    }

    @Override
    public void dataChanged(Object newCursor) {
        movies = (Cursor) newCursor;
        notifyDataSetChanged();
    }
}
