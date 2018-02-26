package com.udacity.examples.popularmovie;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.udacity.examples.popularmovie.utils.MockMoviesUtils;

import java.util.Arrays;

public class MainActivity extends AppCompatActivity {

    private RecyclerView moviesRecyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        moviesRecyclerView = findViewById(R.id.rv_movies);

        MoviesAdapter adapter = new MoviesAdapter(this, Arrays.asList(MockMoviesUtils.MOCK_MOVIES));
        moviesRecyclerView.setAdapter(adapter);

        moviesRecyclerView.setLayoutManager(new GridLayoutManager(this, 2));
        moviesRecyclerView.setHasFixedSize(true);
    }
}
