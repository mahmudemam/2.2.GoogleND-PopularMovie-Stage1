package com.udacity.examples.popularmovie.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;

import com.udacity.examples.popularmovie.R;
import com.udacity.examples.popularmovie.data.Movie;
import com.udacity.examples.popularmovie.utils.NetworkUtils;

/**
 * Created by Mahmoud Emam on 2/21/18.
 */

public abstract class MoviesAdapter extends RecyclerView.Adapter<MoviesAdapter.MovieViewHolder> {
    private static final String TAG = MoviesAdapter.class.getSimpleName();

    protected final Context mContext;
    private final OnMovieClickListener mListener;

    public interface OnMovieClickListener {
        void onImageClicked(Movie movie);

        void onFavoritePressed(Movie movie, boolean selected);
    }

    MoviesAdapter(Context context, OnMovieClickListener listener) {
        mContext = context;
        mListener = listener;
    }

    @Override
    public MovieViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.movie_item, parent, false);
        return new MovieViewHolder(view);
    }

    public abstract void dataChanged(Object data);

    class MovieViewHolder extends RecyclerView.ViewHolder {
        private final ImageView movieImageView;
        private final ImageButton favoriteImageButton;

        MovieViewHolder(View view) {
            super(view);

            movieImageView = view.findViewById(R.id.iv_movie_img);
            movieImageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mListener.onImageClicked((Movie) itemView.getTag());
                    Log.v(TAG, "Movie Img: " + itemView.getTag());
                }
            });

            favoriteImageButton = view.findViewById(R.id.ib_favorite);
            favoriteImageButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    boolean selected = !view.isSelected();

                    Movie movie = (Movie) itemView.getTag();
                    movie.setFavorite(selected);

                    mListener.onFavoritePressed(movie, selected);

                    view.setSelected(selected);
                    Log.v(TAG, "Fav Button: " + view.isSelected());
                }
            });
        }

        void bind(Movie movie) {
            itemView.setTag(movie);

            Log.d(TAG, "Poster: " + movie.getPosterPath());

            NetworkUtils.loadImage(mContext, movie.getPosterPath(), movieImageView);

            favoriteImageButton.setSelected(movie.isFavorite());
        }
    }
}
