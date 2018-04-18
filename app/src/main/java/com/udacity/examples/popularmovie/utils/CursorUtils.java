package com.udacity.examples.popularmovie.utils;

import android.database.Cursor;

import com.udacity.examples.popularmovie.data.FavoriteMoviesContract;
import com.udacity.examples.popularmovie.data.Review;
import com.udacity.examples.popularmovie.data.Video;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Mahmoud Emam on 4/17/18.
 */

public class CursorUtils {
    public static List<Video> parseVideos(Cursor cursor) {
        if (cursor == null || cursor.getCount() == 0)
            return null;

        List<Video> videos = new ArrayList<>();

        for (int i = 0; i < cursor.getCount(); i++) {
            cursor.moveToPosition(i);
            videos.add(new Video(
                    cursor.getString(cursor.getColumnIndex(FavoriteMoviesContract.VideoEntry.COLUMN_VIDEO_NAME)),
                    cursor.getString(cursor.getColumnIndex(FavoriteMoviesContract.VideoEntry.COLUMN_VIDEO_KEY)),
                    cursor.getString(cursor.getColumnIndex(FavoriteMoviesContract.VideoEntry.COLUMN_VIDEO_TYPE))));
        }

        return videos;
    }

    public static List<Review> parseReviews(Cursor cursor) {
        if (cursor == null || cursor.getCount() == 0)
            return null;

        List<Review> reviews = new ArrayList<>();

        for (int i = 0; i < cursor.getCount(); i++) {
            cursor.moveToPosition(i);
            reviews.add(new Review(
                    cursor.getString(cursor.getColumnIndex(FavoriteMoviesContract.ReviewEntry.COLUMN_REVIEW_AUTHOR)),
                    cursor.getString(cursor.getColumnIndex(FavoriteMoviesContract.ReviewEntry.COLUMN_REVIEW_CONTENT))));
        }

        return reviews;
    }
}
