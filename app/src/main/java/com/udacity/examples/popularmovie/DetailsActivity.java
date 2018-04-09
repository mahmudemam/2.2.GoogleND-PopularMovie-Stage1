package com.udacity.examples.popularmovie;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.udacity.examples.popularmovie.data.Movie;
import com.udacity.examples.popularmovie.utils.NetworkUtils;

public class DetailsActivity extends AppCompatActivity {

    public static final String INTENT_MOVIE_KEY = "MOVIE_KEY";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);

        ImageButton mVideoImageButton = findViewById(R.id.ib_videos);
        mVideoImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(DetailsActivity.this, VideosActivity.class);
                startActivity(intent);
            }
        });

        ImageButton mReviewImageButton = findViewById(R.id.ib_reviews);
        mReviewImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(DetailsActivity.this, ReviewActivity.class);
                startActivity(intent);
            }
        });

        Intent intent = getIntent();

        if (intent == null) {
            finish();
            return;
        }

        if (!intent.hasExtra(INTENT_MOVIE_KEY)) {
            finish();
            return;
        }

        Movie movie = intent.getParcelableExtra(INTENT_MOVIE_KEY);

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
    }
}
