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
import android.widget.Toast;

import com.udacity.examples.popularmovie.data.Movie;
import com.udacity.examples.popularmovie.utils.JsonUtils;
import com.udacity.examples.popularmovie.utils.NetworkUtils;

import java.util.List;

public class MainActivity extends AppCompatActivity implements MoviesAdapter.OnItemClickListener {
    private static final String TAG = MainActivity.class.getSimpleName();
    private static final String SORT_ORDER_KEY = "SORT_ORDER";
    private RecyclerView moviesRecyclerView;
    private ProgressBar progressBar;
    private int sort_order = FetchingMovieTask.POPULAR_MOVIES_ID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        moviesRecyclerView = findViewById(R.id.rv_movies);
        progressBar = findViewById(R.id.pb_loading_bar);

        if (NetworkUtils.isNetworkActive(this)) {
            if (savedInstanceState != null && savedInstanceState.containsKey(SORT_ORDER_KEY)) {
                sort_order = savedInstanceState.getInt(SORT_ORDER_KEY);
            }
            new FetchingMovieTask().execute();
        } else {
            Toast.makeText(this, "Network Connection is not Active", Toast.LENGTH_SHORT).show();
            finish();
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        outState.putInt(SORT_ORDER_KEY, sort_order);
    }

    @Override
    public void onClick(Movie movie) {
        Log.d(TAG, "Listener: " + movie.toString());

        Intent detailsIntent = new Intent(this, DetailsActivity.class);
        detailsIntent.putExtra(DetailsActivity.INTENT_MOVIE_KEY, movie);

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
                sort_order = FetchingMovieTask.POPULAR_MOVIES_ID;
                new FetchingMovieTask().execute();
                return true;
            case R.id.menu_top_rated_movies:
                sort_order = FetchingMovieTask.TOP_RATED_MOVIES_ID;
                new FetchingMovieTask().execute();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public class FetchingMovieTask extends AsyncTask<Void, Void, String> {
        static final int POPULAR_MOVIES_ID = 1;
        static final int TOP_RATED_MOVIES_ID = 2;

        @Override
        protected void onPreExecute() {
            progressBar.setVisibility(View.VISIBLE);
        }

        @Override
        protected String doInBackground(Void... voids) {
            if (sort_order == POPULAR_MOVIES_ID)
                return NetworkUtils.loadPopularMovies();
            else if (sort_order == TOP_RATED_MOVIES_ID)
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
