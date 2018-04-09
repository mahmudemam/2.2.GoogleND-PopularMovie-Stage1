package com.udacity.examples.popularmovie;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.udacity.examples.popularmovie.utils.MockMoviesUtils;

import java.util.Arrays;

public class ReviewActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_review);

        RecyclerView recyclerView = findViewById(R.id.rv_reviews);

        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));

        ReviewsAdapter adapter = new ReviewsAdapter(this, Arrays.asList(MockMoviesUtils.MOCK_REVIEWS));
        recyclerView.setAdapter(adapter);
    }
}
