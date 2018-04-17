package com.udacity.examples.popularmovie;


import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.content.Intent;
import android.database.Cursor;
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

import com.udacity.examples.popularmovie.adapters.MovieCursorAdapter;
import com.udacity.examples.popularmovie.adapters.MovieListAdapter;
import com.udacity.examples.popularmovie.adapters.MoviesAdapter;
import com.udacity.examples.popularmovie.data.Movie;
import com.udacity.examples.popularmovie.tasks.MovieAsyncTaskLoader;
import com.udacity.examples.popularmovie.utils.ContentProviderUtils;
import com.udacity.examples.popularmovie.utils.JsonUtils;
import com.udacity.examples.popularmovie.utils.NetworkUtils;

import java.util.List;

public class MainActivity extends AppCompatActivity implements MoviesAdapter.OnMovieClickListener, LoaderManager.LoaderCallbacks<Movie> {
    private static final String TAG = MainActivity.class.getSimpleName();
    private static final String SORT_ORDER_KEY = "SORT_ORDER";
    private RecyclerView moviesRecyclerView;
    private ProgressBar progressBar;
    private int sort_order = FetchingMovieTask.POPULAR_MOVIES_ID;

    private MoviesAdapter adapter;
    private Object movies;

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
            Toast.makeText(this, "Network Connection is not Active, favorites are displayed", Toast.LENGTH_SHORT).show();
            sort_order = FetchingMovieTask.FAVORITE_MOVIES_ID;
            new FetchingMovieTask().execute();
        }
    }

    /** Use shared preference to save the movies **/

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        outState.putInt(SORT_ORDER_KEY, sort_order);
    }

    @Override
    public void onImageClicked(Movie movie) {
        Log.d(TAG, "Listener: " + movie.toString());

        Intent detailsIntent = new Intent(this, DetailsActivity.class);
        detailsIntent.putExtra(DetailsActivity.INTENT_MOVIE_KEY, movie);

        startActivity(detailsIntent);
    }

    @Override
    public void onFavoritePressed(Movie movie, boolean selected) {
        if (selected) {
            Bundle bundle = new Bundle();
            bundle.putParcelable(BUNDLE_KEY_MOVIE_ID, movie);

            LoaderManager loaderManager = getSupportLoaderManager();
            if (loaderManager.getLoader(MOVIE_TASK_LOADER_ID) != null) {
                getSupportLoaderManager().restartLoader(MOVIE_TASK_LOADER_ID, bundle, this);
            } else {
                getSupportLoaderManager().initLoader(MOVIE_TASK_LOADER_ID, bundle, this);
            }
        } else
            ContentProviderUtils.removeFavoriteMovie(this, movie);

        adapter.dataChanged(movies);
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
            case R.id.menu_favorite_movies:
                sort_order = FetchingMovieTask.FAVORITE_MOVIES_ID;
                new FetchingMovieTask().execute();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public static final String BUNDLE_KEY_MOVIE_ID = "MOVIE_ID";
    private static final int MOVIE_TASK_LOADER_ID = 1;

    @Override
    public Loader onCreateLoader(int i, Bundle bundle) {
        return new MovieAsyncTaskLoader(this, (Movie) bundle.getParcelable(BUNDLE_KEY_MOVIE_ID));
    }

    @Override
    public void onLoadFinished(Loader loader, Movie o) {
        ContentProviderUtils.addFavoriteMovie(this, o);
    }

    @Override
    public void onLoaderReset(Loader loader) {

    }

    public class FetchingMovieTask extends AsyncTask<Void, Void, Object> {
        static final int POPULAR_MOVIES_ID = 1;
        static final int TOP_RATED_MOVIES_ID = 2;
        static final int FAVORITE_MOVIES_ID = 3;

        @Override
        protected void onPreExecute() {
            progressBar.setVisibility(View.VISIBLE);
        }

        @Override
        protected Object doInBackground(Void... voids) {
            if (sort_order == POPULAR_MOVIES_ID)
                return NetworkUtils.loadPopularMovies();
            else if (sort_order == TOP_RATED_MOVIES_ID)
                return NetworkUtils.loadTopRatedMovies();
            else if (sort_order == FAVORITE_MOVIES_ID)
                return ContentProviderUtils.getFavoriteMovies(MainActivity.this);
            else return null;
        }

        @Override
        protected void onPostExecute(Object data) {
            progressBar.setVisibility(View.INVISIBLE);

            if (sort_order != FAVORITE_MOVIES_ID) {
                movies = JsonUtils.parseMovies((String) data);
                adapter = new MovieListAdapter(MainActivity.this, MainActivity.this, (List<Movie>) movies);
            } else {
                movies = data;
                adapter = new MovieCursorAdapter(MainActivity.this, MainActivity.this, (Cursor) movies);
            }
            moviesRecyclerView.setAdapter(adapter);

            moviesRecyclerView.setLayoutManager(new GridLayoutManager(MainActivity.this, 3));
            moviesRecyclerView.setHasFixedSize(true);
        }
    }
}
