package com.udacity.examples.popularmovie;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.Toast;

import com.udacity.examples.popularmovie.data.Movie;
import com.udacity.examples.popularmovie.data.Review;
import com.udacity.examples.popularmovie.utils.JsonUtils;
import com.udacity.examples.popularmovie.utils.NetworkUtils;

import java.util.List;

public class ReviewActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks {
    public static final String INTENT_KEY_MOVIE = "MOVIE";
    public static final String BUNDLE_KEY_MOVIE_ID = "MOVIE_ID";
    private static final int MOVIE_DETAILS_LOADER_ID = 200;

    private Movie movie;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Intent intent = getIntent();

        if (intent == null || !intent.hasExtra(INTENT_KEY_MOVIE))
            finish();

        movie = intent.getParcelableExtra(INTENT_KEY_MOVIE);

        if (movie == null) {
            finish();
            return;
        }

        setContentView(R.layout.activity_review);

        Bundle movieIdBundle = new Bundle();
        movieIdBundle.putInt(BUNDLE_KEY_MOVIE_ID, movie.getId());

        LoaderManager loaderManager = getSupportLoaderManager();
        Loader reviewsLoader = loaderManager.getLoader(MOVIE_DETAILS_LOADER_ID);
        if (reviewsLoader != null) {
            loaderManager.restartLoader(MOVIE_DETAILS_LOADER_ID, movieIdBundle, this);
        } else {
            loaderManager.initLoader(MOVIE_DETAILS_LOADER_ID, movieIdBundle, this);
        }
    }

    @NonNull
    @Override
    public Loader onCreateLoader(int id, @Nullable final Bundle args) {
        return new AsyncTaskLoader(this) {
            private String reviewsStr = null;

            @Nullable
            @Override
            public Object loadInBackground() {
                if (args == null || !args.containsKey(BUNDLE_KEY_MOVIE_ID))
                    return null;

                return NetworkUtils.loadReviews(args.getInt(BUNDLE_KEY_MOVIE_ID));
            }

            @Override
            protected void onStartLoading() {
                if (reviewsStr != null) {
                    deliverResult(reviewsStr);
                } else {
                    forceLoad();
                }
            }

            @Override
            public void deliverResult(@Nullable Object data) {
                reviewsStr = (String) data;
                super.deliverResult(data);
            }
        };
    }

    @Override
    public void onLoaderReset(@NonNull Loader loader) {

    }

    @Override
    public void onLoadFinished(@NonNull Loader loader, Object data) {
        if (data == null)
            return;

        List<Review> reviews = JsonUtils.parseReviews((String) data);
        if (reviews == null || reviews.size() == 0) {
            Toast.makeText(this, "There is no reviews for " + movie.getTitle(), Toast.LENGTH_SHORT).show();
            finish();
        }

        RecyclerView recyclerView = findViewById(R.id.rv_reviews);

        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));

        ReviewsAdapter adapter = new ReviewsAdapter(this, reviews);
        recyclerView.setAdapter(adapter);
    }
}
