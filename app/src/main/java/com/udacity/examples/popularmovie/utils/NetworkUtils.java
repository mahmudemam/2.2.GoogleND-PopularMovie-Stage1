package com.udacity.examples.popularmovie.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.util.Log;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Scanner;

/**
 * Created by Mahmoud Emam on 2/25/18.
 */

public class NetworkUtils {
    private static final String TAG = NetworkUtils.class.getSimpleName();
    private static final String API_KEY = "";
    private static final String BASE_URL = "http://api.themoviedb.org/3/movie/";
    private static final String POPULAR_MOVIES_URL = BASE_URL + "popular";
    private static final String TOP_RATED_MOVIES_URL = BASE_URL + "top_rated";

    private static final String IMG_BASE_URL = "http://image.tmdb.org/t/p/w780/";

    public static String loadPopularMovies() {
        return fetchMovies(POPULAR_MOVIES_URL);
    }

    public static String loadTopRatedMovies() {
        return fetchMovies(TOP_RATED_MOVIES_URL);
    }

    public static String loadVideos(int id) {
        return fetchMovies(BASE_URL + id + "/videos");
    }

    public static String loadReviews(int id) {
        return fetchMovies(BASE_URL + id + "/reviews");
    }

    private static String fetchMovies(String urlString) {
        Uri uri = Uri.parse(urlString).buildUpon()
                .appendQueryParameter("api_key", API_KEY)
                .build();

        String movies = null;
        URL url;
        HttpURLConnection connection = null;
        try {
            url = new URL(uri.toString());

            connection = (HttpURLConnection) url.openConnection();

            InputStream in = connection.getInputStream();

            Scanner scanner = new Scanner(in);
            scanner.useDelimiter("\\A");

            boolean hasInput = scanner.hasNext();
            if (hasInput) {
                movies = scanner.next();
            }
        } catch (MalformedURLException ex) {
            Log.e(TAG, "doInBackground: URL is malformed", ex);
        } catch (IOException ex) {
            Log.e(TAG, "doInBackground: ", ex);
        } finally {
            if (connection != null)
                connection.disconnect();
        }

        return movies;
    }

    public static void loadImage(Context context, String imgPath, ImageView imgView) {
        Picasso.with(context).load(IMG_BASE_URL + imgPath).into(imgView);
    }

    public static boolean isNetworkActive(Context context) {
        ConnectivityManager cm =
                (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);

        if (cm == null)
            return false;

        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        return activeNetwork != null && activeNetwork.isConnectedOrConnecting();
    }
}
