package com.udacity.examples.popularmovie;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.udacity.examples.popularmovie.data.Movie;
import com.udacity.examples.popularmovie.utils.ContentProviderUtils;
import com.udacity.examples.popularmovie.utils.NetworkUtils;

public class DetailsActivity extends AppCompatActivity {
    public static final String INTENT_MOVIE_KEY = "MOVIE_KEY";

    private static final String TAG = DetailsActivity.class.getSimpleName();
    private Movie movie;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        final String M = "onCreate: ";

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);

        Intent intent = getIntent();

        if (intent == null) {
            finish();
            return;
        }

        if (!intent.hasExtra(INTENT_MOVIE_KEY)) {
            finish();
            return;
        }

        movie = intent.getParcelableExtra(INTENT_MOVIE_KEY);
        Log.v(TAG, M + "movie={" + movie + "}");

        TextView titleTextView = findViewById(R.id.tv_title);
        TextView releaseDateTextView = findViewById(R.id.tv_release_date);
        TextView voteTextView = findViewById(R.id.tv_vote);
        TextView synopsisTextView = findViewById(R.id.tv_plot_synopsis);
        ImageView posterImageView = findViewById(R.id.iv_poster);

        titleTextView.setText(movie.getTitle());
        releaseDateTextView.setText(movie.getReleaseDate());
        voteTextView.setText(String.valueOf(movie.getVoteAverage()));
        synopsisTextView.setText(movie.getOverview());

        NetworkUtils.loadImage(this, movie.getBackdropPath(), posterImageView);

        ImageButton mVideoImageButton = findViewById(R.id.ib_videos);
        mVideoImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(DetailsActivity.this, VideosActivity.class);
                intent.putExtra(VideosActivity.INTENT_KEY_MOVIE, movie);
                startActivity(intent);
            }
        });

        ImageButton mReviewImageButton = findViewById(R.id.ib_reviews);
        mReviewImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(DetailsActivity.this, ReviewActivity.class);
                intent.putExtra(ReviewActivity.INTENT_KEY_MOVIE, movie);
                startActivity(intent);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.details_menu, menu);
        MenuItem item = menu.getItem(0);
        if (movie.isFavorite()) {
            item.setIcon(R.drawable.ic_favorite_selected_24dp);
            item.setChecked(true);
        } else {
            item.setIcon(R.drawable.ic_favorite_unselected_24dp);
            item.setChecked(false);
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case R.id.menu_details_favorite:
                if (item.isChecked()) {
                    item.setIcon(R.drawable.ic_favorite_unselected_24dp);
                    item.setChecked(false);
                    movie.setFavorite(false);

                    ContentProviderUtils.removeFavoriteMovie(this, movie);
                } else {
                    item.setIcon(R.drawable.ic_favorite_selected_24dp);
                    item.setChecked(true);
                    movie.setFavorite(true);

                    ContentProviderUtils.addFavoriteMovie(this, movie);
                }
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
