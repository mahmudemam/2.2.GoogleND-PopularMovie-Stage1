package com.udacity.examples.popularmovie;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.udacity.examples.popularmovie.data.Movie;
import com.udacity.examples.popularmovie.data.Video;
import com.udacity.examples.popularmovie.utils.MockMoviesUtils;

import java.util.Arrays;

public class VideosActivity extends AppCompatActivity {
    public static final String MOVIE_ID_KEY = "movie_id";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Intent intent = getIntent();

        /*if (intent == null || ! intent.hasExtra(MOVIE_ID_KEY))
            finish();

        int id = intent.getIntExtra(MOVIE_ID_KEY, 0);
*/

        setContentView(R.layout.activity_videos);

        RecyclerView videoRecyclerView = findViewById(R.id.rv_videos);

        videoRecyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));

        VideosAdapter adapter = new VideosAdapter(this, Arrays.asList(MockMoviesUtils.MOCK_VIDEOS),

                new VideosAdapter.OnVideoClickLinstener() {
                    @Override
                    public void onVideoClick(Video video) {
                        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.youtube.com/watch?v=nJCc5HRPxYA"));
                        if (intent.resolveActivity(getPackageManager()) != null) {
                            startActivity(intent);
                        }
                    }
                });
        videoRecyclerView.setAdapter(adapter);

    }
}
