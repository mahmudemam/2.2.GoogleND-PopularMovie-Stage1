package com.udacity.examples.popularmovie.adapters;

import android.content.Context;
import android.database.Cursor;
import android.support.annotation.NonNull;

import com.udacity.examples.popularmovie.data.FavoriteMoviesContract;
import com.udacity.examples.popularmovie.data.Movie;

public class MovieCursorAdapter extends MoviesAdapter {
    private Cursor movies;

    public MovieCursorAdapter(Context context, OnMovieClickListener listener, Cursor movies) {
        super(context, listener);

        this.movies = movies;
    }

    @Override
    public void onBindViewHolder(@NonNull MovieViewHolder holder, int position) {
        movies.moveToPosition(position);

        Movie movie = new Movie(
                movies.getInt(movies.getColumnIndex(FavoriteMoviesContract.MovieEntry.COLUMN_MOVIE_ID)),
                movies.getString(movies.getColumnIndex(FavoriteMoviesContract.MovieEntry.COLUMN_MOVIE_RELEASE_DATE)),
                movies.getString(movies.getColumnIndex(FavoriteMoviesContract.MovieEntry.COLUMN_MOVIE_POSTER_PATH)),
                movies.getString(movies.getColumnIndex(FavoriteMoviesContract.MovieEntry.COLUMN_MOVIE_BACK_DROP_PATH)),
                movies.getDouble(movies.getColumnIndex(FavoriteMoviesContract.MovieEntry.COLUMN_MOVIE_RATE)),
                movies.getString(movies.getColumnIndex(FavoriteMoviesContract.MovieEntry.COLUMN_MOVIE_OVERVIEW)),
                movies.getString(movies.getColumnIndex(FavoriteMoviesContract.MovieEntry.COLUMN_MOVIE_RELEASE_DATE))
        );

        movie.setFavorite(true);
        holder.bind(movie);
    }

    @Override
    public int getItemCount() {
        return movies.getCount();
    }
}
