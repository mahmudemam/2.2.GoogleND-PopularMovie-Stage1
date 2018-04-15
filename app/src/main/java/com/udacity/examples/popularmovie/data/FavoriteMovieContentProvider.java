package com.udacity.examples.popularmovie.data;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

public class FavoriteMovieContentProvider extends ContentProvider {
    private FavoriteMoviesDBHelper favoriteMoviesDBHelper;

    private static final int MOVIES_CODE = 100;
    private static final int MOVIES_WITH_ID_CODE = 101;
    private static final int VIDEOS_CODE = 200;
    private static final int REVIEWS_CODE = 300;

    public static final UriMatcher URI_MATCHER = buildUriMatcher();

    @Override
    public boolean onCreate() {
        favoriteMoviesDBHelper = new FavoriteMoviesDBHelper(getContext());
        return true;
    }

    public static UriMatcher buildUriMatcher() {
        UriMatcher matcher = new UriMatcher(UriMatcher.NO_MATCH);

        matcher.addURI(FavoriteMoviesContract.CONTENT_AUTHORITY, FavoriteMoviesContract.MovieEntry.PATH_MOVIES, MOVIES_CODE);
        matcher.addURI(FavoriteMoviesContract.CONTENT_AUTHORITY, FavoriteMoviesContract.MovieEntry.PATH_MOVIES + "/#", MOVIES_CODE);
        matcher.addURI(FavoriteMoviesContract.CONTENT_AUTHORITY, FavoriteMoviesContract.VideoEntry.PATH_VIDEOS, VIDEOS_CODE);
        matcher.addURI(FavoriteMoviesContract.CONTENT_AUTHORITY, FavoriteMoviesContract.ReviewEntry.PATH_REVIEWS, REVIEWS_CODE);

        return matcher;
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues contentValues) {
        int code = URI_MATCHER.match(uri);
        switch (code) {
            case MOVIES_CODE:
                long id = favoriteMoviesDBHelper.getWritableDatabase().insert(FavoriteMoviesContract.MovieEntry.TABLE_NAME, null, contentValues);
                if (id > 0)
                    ContentUris.withAppendedId(uri, id);
                else
                    throw new android.database.SQLException("Failed to insert row into " + uri);
                break;
            default:
                throw new UnsupportedOperationException("Unknown Uri: " + uri);
        }

        getContext().getContentResolver().notifyChange(uri, null);

        return uri;
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] projection, @Nullable String selection, @Nullable String[] selectionArgs, @Nullable String sortOrder) {
        Cursor cursor;

        int code = URI_MATCHER.match(uri);
        switch (code) {
            case MOVIES_CODE:
                cursor = favoriteMoviesDBHelper.getReadableDatabase().query(FavoriteMoviesContract.MovieEntry.TABLE_NAME,
                        projection, selection, selectionArgs, null, null, sortOrder);
                break;
            case MOVIES_WITH_ID_CODE:
                String selectStmnt = "_id=?";
                String[] selectArgs = {uri.getPathSegments().get(1)};

                cursor = favoriteMoviesDBHelper.getReadableDatabase().query(FavoriteMoviesContract.MovieEntry.TABLE_NAME,
                        projection, selectStmnt, selectArgs, null, null, sortOrder);
                break;
            default:
                throw new UnsupportedOperationException("Not yet implemented");
        }

        cursor.setNotificationUri(getContext().getContentResolver(), uri);

        return cursor;
    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String selection, String[] selectionArgs) {
        int code = URI_MATCHER.match(uri);
        int noOfDeleted = 0;
        switch (code) {
            case MOVIES_WITH_ID_CODE:
                String selectStmnt = "_id=?";
                String[] selectArgs = {uri.getPathSegments().get(1)};
                noOfDeleted = favoriteMoviesDBHelper.getWritableDatabase().delete(FavoriteMoviesContract.MovieEntry.TABLE_NAME, selectStmnt, selectArgs);
                break;
            default:
                throw new UnsupportedOperationException("Not yet implemented");
        }

        if (noOfDeleted > 0) {
            getContext().getContentResolver().notifyChange(uri, null);
        }

        return noOfDeleted;
    }


    @Override
    public int update(@NonNull Uri uri, ContentValues values, String selection,
                      String[] selectionArgs) {
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public String getType(@NonNull Uri uri) {
        throw new UnsupportedOperationException("Not yet implemented");
    }
}
