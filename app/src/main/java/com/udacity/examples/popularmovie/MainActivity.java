package com.udacity.examples.popularmovie;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;

import com.udacity.examples.popularmovie.data.Movie;
import com.udacity.examples.popularmovie.utils.JsonUtils;
import com.udacity.examples.popularmovie.utils.NetworkUtils;

import java.util.List;

public class MainActivity extends AppCompatActivity implements MoviesAdapter.OnItemClickListener {
    private static final String TAG = MainActivity.class.getSimpleName();

    private RecyclerView moviesRecyclerView;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        moviesRecyclerView = findViewById(R.id.rv_movies);
        progressBar = findViewById(R.id.pb_loading_bar);

        new FetchingMovieTask().execute(new Void[]{null});
    }

    @Override
    public void onClick(Movie movie) {
        Log.d(TAG, "Listener: " + movie.toString());

        Intent detailsIntent = new Intent(this, DetailsActivity.class);

        detailsIntent.putExtra(DetailsActivity.INTENT_MOVIE_TITLE_KEY, movie.getTitle());
        detailsIntent.putExtra(DetailsActivity.INTENT_MOVIE_POSTER_KEY, movie.getBackdropPath());
        detailsIntent.putExtra(DetailsActivity.INTENT_MOVIE_RELEASE_DATE_KEY, movie.getReleaseDate());
        detailsIntent.putExtra(DetailsActivity.INTENT_MOVIE_VOTE_KEY, movie.getVoteAverage());
        detailsIntent.putExtra(DetailsActivity.INTENT_MOVIE_SYNOPSIS_KEY, movie.getOverview());

        startActivity(detailsIntent);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case R.id.menu_popular_movies:
                new FetchingMovieTask().execute(FetchingMovieTask.POPULAR_MOVIES_ID);
                return true;
            case R.id.menu_top_rated_movies:
                new FetchingMovieTask().execute(FetchingMovieTask.TOP_RATED_MOVIES_ID);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public class FetchingMovieTask extends AsyncTask<Integer, Void, String> {
        public static final int POPULAR_MOVIES_ID = 1;
        public static final int TOP_RATED_MOVIES_ID = 2;

        @Override
        protected void onPreExecute() {
            progressBar.setVisibility(View.VISIBLE);
        }

        @Override
        protected String doInBackground(Integer... ids) {
            if (ids[0] == POPULAR_MOVIES_ID)
                return NetworkUtils.loadPopularMovies();
            else if (ids[0] == TOP_RATED_MOVIES_ID)
                return NetworkUtils.loadTopRatedMovies();
            else return null;
        }

        @Override
        protected void onPostExecute(String jsonStr) {
            progressBar.setVisibility(View.INVISIBLE);

            List<Movie> movies = JsonUtils.parseMovies(jsonStr);

            MoviesAdapter adapter = new MoviesAdapter(MainActivity.this, MainActivity.this, movies);
            moviesRecyclerView.setAdapter(adapter);

            moviesRecyclerView.setLayoutManager(new GridLayoutManager(MainActivity.this, 3));
            moviesRecyclerView.setHasFixedSize(true);
        }
    }
}
