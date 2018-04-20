package com.udacity.examples.popularmovie;

import android.content.Intent;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.Toast;

import com.udacity.examples.popularmovie.adapters.ReviewsAdapter;
import com.udacity.examples.popularmovie.data.Movie;
import com.udacity.examples.popularmovie.data.Review;
import com.udacity.examples.popularmovie.utils.ContentProviderUtils;
import com.udacity.examples.popularmovie.utils.CursorUtils;
import com.udacity.examples.popularmovie.utils.JsonUtils;
import com.udacity.examples.popularmovie.utils.NetworkUtils;

import java.util.List;

public class ReviewActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<List<Review>> {
    public static final String INTENT_KEY_MOVIE = "MOVIE";
    public static final String BUNDLE_KEY_MOVIE_ID = "MOVIE_ID";
    private static final int MOVIE_DETAILS_LOADER_ID = 200;
    private static final String RV_POSITION_KEY = "RV_KEY";

    private RecyclerView reviewRecyclerView;
    private Parcelable rvSavedState;
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

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        if (reviewRecyclerView != null && reviewRecyclerView.getLayoutManager() != null)
            outState.putParcelable(RV_POSITION_KEY, reviewRecyclerView.getLayoutManager().onSaveInstanceState());
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);

        if (savedInstanceState != null && savedInstanceState.containsKey(RV_POSITION_KEY)) {
            rvSavedState = savedInstanceState.getParcelable(RV_POSITION_KEY);
        }
    }

    @NonNull
    @Override
    public Loader<List<Review>> onCreateLoader(int id, @Nullable final Bundle args) {
        return new AsyncTaskLoader<List<Review>>(this) {
            private List<Review> reviewsStr = null;

            @Nullable
            @Override
            public List<Review> loadInBackground() {
                if (args == null || !args.containsKey(BUNDLE_KEY_MOVIE_ID))
                    return null;
                if (movie.isFavorite())
                    return CursorUtils.parseReviews(ContentProviderUtils.getReviews(ReviewActivity.this, movie));
                else
                    return JsonUtils.parseReviews(NetworkUtils.loadReviews(args.getInt(BUNDLE_KEY_MOVIE_ID)));
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
            public void deliverResult(@Nullable List<Review> data) {
                reviewsStr = data;
                super.deliverResult(data);
            }
        };
    }

    @Override
    public void onLoaderReset(@NonNull Loader<List<Review>> loader) {

    }

    @Override
    public void onLoadFinished(@NonNull Loader<List<Review>> loader, List<Review> reviews) {
        if (reviews == null || reviews.size() == 0) {
            Toast.makeText(this, "There is no reviews for " + movie.getTitle(), Toast.LENGTH_SHORT).show();
            finish();
        }

        reviewRecyclerView = findViewById(R.id.rv_reviews);

        reviewRecyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        reviewRecyclerView.getLayoutManager().onRestoreInstanceState(rvSavedState);

        ReviewsAdapter adapter = new ReviewsAdapter(this, reviews);
        reviewRecyclerView.setAdapter(adapter);
    }
}
