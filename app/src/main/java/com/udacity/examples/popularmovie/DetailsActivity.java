package com.udacity.examples.popularmovie;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.udacity.examples.popularmovie.utils.NetworkUtils;

public class DetailsActivity extends AppCompatActivity {

    public static final String INTENT_MOVIE_TITLE_KEY = "TITLE_KEY";
    public static final String INTENT_MOVIE_POSTER_KEY = "POSTER_KEY";
    public static final String INTENT_MOVIE_RELEASE_DATE_KEY = "RELEASE_DATE_KEY";
    public static final String INTENT_MOVIE_VOTE_KEY = "VOTE_KEY";
    public static final String INTENT_MOVIE_SYNOPSIS_KEY = "SYNOPSIS_KEY";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);

        ImageButton mVideoImageButton = findViewById(R.id.ib_videos);
        mVideoImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(DetailsActivity.this, MovieVideosActivity.class);
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

        if (!intent.hasExtra(INTENT_MOVIE_TITLE_KEY) || !intent.hasExtra(INTENT_MOVIE_POSTER_KEY) || !intent.hasExtra(INTENT_MOVIE_RELEASE_DATE_KEY) ||
                !intent.hasExtra(INTENT_MOVIE_VOTE_KEY) || !intent.hasExtra(INTENT_MOVIE_SYNOPSIS_KEY)) {
            finish();
            return;
        }

        String title = intent.getStringExtra(INTENT_MOVIE_TITLE_KEY);
        String poster = intent.getStringExtra(INTENT_MOVIE_POSTER_KEY);
        String releaseDate = intent.getStringExtra(INTENT_MOVIE_RELEASE_DATE_KEY);
        double vote = intent.getDoubleExtra(INTENT_MOVIE_VOTE_KEY, 0);
        String synopsis = intent.getStringExtra(INTENT_MOVIE_SYNOPSIS_KEY);

        TextView titleTextView = findViewById(R.id.tv_title);
        TextView releaseDateTextView = findViewById(R.id.tv_release_date);
        TextView voteTextView = findViewById(R.id.tv_vote);
        TextView synopsisTextView = findViewById(R.id.tv_plot_synopsis);
        ImageView posterImageView = findViewById(R.id.iv_poster);

        titleTextView.setText(title);
        releaseDateTextView.setText(releaseDate);
        voteTextView.setText(String.valueOf(vote));
        synopsisTextView.setText(synopsis);

        NetworkUtils.loadImage(this, poster, posterImageView);
    }
}
