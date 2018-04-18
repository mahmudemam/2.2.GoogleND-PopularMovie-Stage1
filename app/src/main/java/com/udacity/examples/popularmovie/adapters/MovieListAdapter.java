package com.udacity.examples.popularmovie.adapters;

import android.content.Context;
import android.support.annotation.NonNull;

import com.udacity.examples.popularmovie.data.Movie;
import com.udacity.examples.popularmovie.utils.ContentProviderUtils;

import java.util.List;

public class MovieListAdapter extends MoviesAdapter {
    private List<Movie> mMovies;

    public MovieListAdapter(Context context, OnMovieClickListener listener, List<Movie> movies) {
        super(context, listener);

        mMovies = movies;
    }

    @Override
    public void onBindViewHolder(@NonNull MovieViewHolder holder, int position) {
        Movie movie = mMovies.get(position);
        movie.setFavorite(ContentProviderUtils.isFavorite(mContext, movie));
        holder.bind(movie);
    }

    @Override
    public int getItemCount() {
        if (mMovies == null) return 0;
        return mMovies.size();
    }

    @Override
    public void dataChanged(Object data) {
        mMovies = (List<Movie>) data;
        notifyDataSetChanged();
    }
}
