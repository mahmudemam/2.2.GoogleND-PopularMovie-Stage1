package com.udacity.examples.popularmovie;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.udacity.examples.popularmovie.data.Movie;
import com.udacity.examples.popularmovie.data.Video;
import com.udacity.examples.popularmovie.utils.JsonUtils;
import com.udacity.examples.popularmovie.utils.NetworkUtils;

import java.util.List;

public class VideosActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks {
    public static final String INTENT_KEY_MOVIE = "MOVIE";
    public static final String BUNDLE_KEY_MOVIE_ID = "MOVIE_ID";
    private static final int MOVIE_DETAILS_LOADER_ID = 100;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Intent intent = getIntent();

        if (intent == null || !intent.hasExtra(INTENT_KEY_MOVIE))
            finish();

        Movie movie = intent.getParcelableExtra(INTENT_KEY_MOVIE);

        if (movie == null) {
            finish();
            return;
        }

        setContentView(R.layout.activity_videos);

        Bundle movieIdBundle = new Bundle();
        movieIdBundle.putInt(BUNDLE_KEY_MOVIE_ID, movie.getId());

        LoaderManager loaderManager = getSupportLoaderManager();
        Loader videoLoader = loaderManager.getLoader(MOVIE_DETAILS_LOADER_ID);
        if (videoLoader != null) {
            loaderManager.restartLoader(MOVIE_DETAILS_LOADER_ID, movieIdBundle, this);
        } else {
            loaderManager.initLoader(MOVIE_DETAILS_LOADER_ID, movieIdBundle, this);
        }
    }

    @Override
    public Loader onCreateLoader(int i, final Bundle bundle) {
        return new AsyncTaskLoader(this) {
            private String videosStr = null;

            @Override
            public Object loadInBackground() {
                if (bundle == null || !bundle.containsKey(BUNDLE_KEY_MOVIE_ID))
                    return null;
                else
                    return NetworkUtils.loadVideos(bundle.getInt(BUNDLE_KEY_MOVIE_ID));
            }

            @Override
            public void deliverResult(Object data) {
                videosStr = (String) data;

                super.deliverResult(data);
            }

            @Override
            protected void onStartLoading() {
                if (videosStr != null) {
                    deliverResult(videosStr);
                } else {
                    forceLoad();
                }
            }
        };
    }

    @Override
    public void onLoaderReset(Loader loader) {

    }

    @Override
    public void onLoadFinished(Loader loader, Object o) {
        String jsonStr = (String) o;
        List<Video> videos = JsonUtils.parseVideos(jsonStr);

        RecyclerView videoRecyclerView = findViewById(R.id.rv_videos);

        videoRecyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));

        VideosAdapter adapter = new VideosAdapter(this, videos,

                new VideosAdapter.OnVideoClickLinstener() {
                    @Override
                    public void onVideoClick(Video video) {
                        if (video == null)
                            return;

                        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.youtube.com/watch?v=" + video.getKey()));
                        if (intent.resolveActivity(getPackageManager()) != null) {
                            startActivity(intent);
                        }
                    }
                });
        videoRecyclerView.setAdapter(adapter);
    }
}
