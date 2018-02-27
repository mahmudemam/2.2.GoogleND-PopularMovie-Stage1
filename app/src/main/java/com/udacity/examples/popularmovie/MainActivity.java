package com.udacity.examples.popularmovie;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import com.udacity.examples.popularmovie.data.Movie;
import com.udacity.examples.popularmovie.utils.MockMoviesUtils;

import java.util.Arrays;

public class MainActivity extends AppCompatActivity implements MoviesAdapter.OnItemClickListener {
    private static final String TAG = MainActivity.class.getSimpleName();

    private RecyclerView moviesRecyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        moviesRecyclerView = findViewById(R.id.rv_movies);

        MoviesAdapter adapter = new MoviesAdapter(this, this, Arrays.asList(MockMoviesUtils.MOCK_MOVIES));
        moviesRecyclerView.setAdapter(adapter);

        moviesRecyclerView.setLayoutManager(new GridLayoutManager(this, 2));
        moviesRecyclerView.setHasFixedSize(true);
    }

    @Override
    public void onClick(Movie movie) {
        Log.d(TAG, "Listener: " + movie.toString());

        Intent detailsIntent = new Intent(this, DetailsActivity.class);

        detailsIntent.putExtra(DetailsActivity.INTENT_MOVIE_TITLE_KEY, movie.getTitle());
        detailsIntent.putExtra(DetailsActivity.INTENT_MOVIE_POSTER_KEY, movie.getPosterPath());
        detailsIntent.putExtra(DetailsActivity.INTENT_MOVIE_RELEASE_DATE_KEY, movie.getReleaseDate());
        detailsIntent.putExtra(DetailsActivity.INTENT_MOVIE_VOTE_KEY, movie.getVoteAverage());
        detailsIntent.putExtra(DetailsActivity.INTENT_MOVIE_SYNOPSIS_KEY, movie.getOverview());

        startActivity(detailsIntent);
    }
}
