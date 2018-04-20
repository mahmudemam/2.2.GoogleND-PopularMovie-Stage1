package com.udacity.examples.popularmovie;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.Toast;

import com.udacity.examples.popularmovie.adapters.VideosAdapter;
import com.udacity.examples.popularmovie.data.Movie;
import com.udacity.examples.popularmovie.data.Video;
import com.udacity.examples.popularmovie.utils.ContentProviderUtils;
import com.udacity.examples.popularmovie.utils.CursorUtils;
import com.udacity.examples.popularmovie.utils.JsonUtils;
import com.udacity.examples.popularmovie.utils.NetworkUtils;

import java.util.List;

public class VideosActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<List<Video>> {
    public static final String INTENT_KEY_MOVIE = "MOVIE";
    public static final String BUNDLE_KEY_MOVIE_ID = "MOVIE_ID";
    private static final int MOVIE_DETAILS_LOADER_ID = 100;
    private static final String RV_POSITION_KEY = "RV_KEY";

    private RecyclerView videoRecyclerView;
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
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        if (videoRecyclerView != null && videoRecyclerView.getLayoutManager() != null)
            outState.putParcelable(RV_POSITION_KEY, videoRecyclerView.getLayoutManager().onSaveInstanceState());
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);

        if (savedInstanceState != null && savedInstanceState.containsKey(RV_POSITION_KEY)) {
            rvSavedState = savedInstanceState.getParcelable(RV_POSITION_KEY);
        }
    }

    @Override
    public Loader<List<Video>> onCreateLoader(int i, final Bundle bundle) {
        return new AsyncTaskLoader<List<Video>>(this) {
            private List<Video> videos = null;

            @Override
            public List<Video> loadInBackground() {
                if (bundle == null || !bundle.containsKey(BUNDLE_KEY_MOVIE_ID))
                    return null;
                else {
                    if (movie.isFavorite())
                        return CursorUtils.parseVideos(ContentProviderUtils.getVideos(VideosActivity.this, movie));
                    else
                        return JsonUtils.parseVideos(NetworkUtils.loadVideos(bundle.getInt(BUNDLE_KEY_MOVIE_ID)));
                }
            }

            @Override
            public void deliverResult(List<Video> data) {
                videos = data;

                super.deliverResult(data);
            }

            @Override
            protected void onStartLoading() {
                if (videos != null) {
                    deliverResult(videos);
                } else {
                    forceLoad();
                }
            }
        };
    }

    @Override
    public void onLoaderReset(Loader<List<Video>> loader) {

    }

    @Override
    public void onLoadFinished(Loader<List<Video>> loader, List<Video> videos) {
        if (videos == null || videos.size() == 0) {
            Toast.makeText(this, "There is no videos for " + movie.getTitle(), Toast.LENGTH_SHORT).show();
            finish();
        }

        videoRecyclerView = findViewById(R.id.rv_videos);

        videoRecyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));

        VideosAdapter adapter = new VideosAdapter(this, videos,

                new VideosAdapter.OnVideoClickLinstener() {
                    @Override
                    public void onVideoClick(Video video) {
                        if (video == null)
                            return;

                        if (NetworkUtils.isNetworkActive(VideosActivity.this)) {
                            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.youtube.com/watch?v=" + video.getKey()));
                            if (intent.resolveActivity(getPackageManager()) != null) {
                                startActivity(intent);
                            }
                        } else {
                            Toast.makeText(VideosActivity.this, "The network is inactive", Toast.LENGTH_SHORT).show();
                        }
                    }
                });

        videoRecyclerView.getLayoutManager().onRestoreInstanceState(rvSavedState);
        videoRecyclerView.setAdapter(adapter);
    }
}
