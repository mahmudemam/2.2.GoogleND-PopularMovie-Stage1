package com.udacity.examples.popularmovie;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import com.udacity.examples.popularmovie.data.Movie;
import com.udacity.examples.popularmovie.utils.JsonUtils;
import com.udacity.examples.popularmovie.utils.NetworkUtils;

import java.util.List;

public class MainActivity extends AppCompatActivity implements MoviesAdapter.OnItemClickListener {
    private static final String TAG = MainActivity.class.getSimpleName();

    private RecyclerView moviesRecyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        moviesRecyclerView = findViewById(R.id.rv_movies);

        new FetchingMovieTask().execute(new Void[]{null});
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

    public class FetchingMovieTask extends AsyncTask<Void, Void, String> {
        @Override
        protected String doInBackground(Void... voids) {
            return NetworkUtils.loadPopularMovies();
        }

        @Override
        protected void onPostExecute(String jsonStr) {
            List<Movie> movies = JsonUtils.parseMovies(jsonStr);

            MoviesAdapter adapter = new MoviesAdapter(MainActivity.this, MainActivity.this, movies);
            moviesRecyclerView.setAdapter(adapter);

            moviesRecyclerView.setLayoutManager(new GridLayoutManager(MainActivity.this, 3));
            moviesRecyclerView.setHasFixedSize(true);
        }
    }
}
