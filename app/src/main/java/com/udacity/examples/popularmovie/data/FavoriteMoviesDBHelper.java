package com.udacity.examples.popularmovie.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class FavoriteMoviesDBHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "favoritemovie.db";
    private static final int DATABASE_VERSION = 2;

    public FavoriteMoviesDBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        final String SQL_CREATE_MOVIE_TABLE = "CREATE TABLE " + FavoriteMoviesContract.MovieEntry.TABLE_NAME + "( "
                + FavoriteMoviesContract.MovieEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + FavoriteMoviesContract.MovieEntry.COLUMN_MOVIE_ID + " INTEGER NOT NULL, "
                + FavoriteMoviesContract.MovieEntry.COLUMN_MOVIE_TITLE + " TEXT NOT NULL, "
                + FavoriteMoviesContract.MovieEntry.COLUMN_MOVIE_POSTER_PATH + " TEXT NOT NULL, "
                + FavoriteMoviesContract.MovieEntry.COLUMN_MOVIE_BACK_DROP_PATH + " TEXT NOT NULL, "
                + FavoriteMoviesContract.MovieEntry.COLUMN_MOVIE_RATE + " INTEGER NOT NULL, "
                + FavoriteMoviesContract.MovieEntry.COLUMN_MOVIE_RELEASE_DATE + " TEXT NOT NULL, "
                + FavoriteMoviesContract.MovieEntry.COLUMN_MOVIE_OVERVIEW + " TEXT NOT NULL"
                + ");";

        final String SQL_CREATE_VIDEO_TABLE = "CREATE TABLE " + FavoriteMoviesContract.VideoEntry.TABLE_NAME + "( "
                + FavoriteMoviesContract.VideoEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + FavoriteMoviesContract.VideoEntry.COLUMN_MOVIE_ID + " INTEGER NOT NULL, "
                + FavoriteMoviesContract.VideoEntry.COLUMN_VIDEO_NAME + " TEXT NOT NULL, "
                + FavoriteMoviesContract.VideoEntry.COLUMN_VIDEO_KEY + " TEXT NOT NULL, "
                + FavoriteMoviesContract.VideoEntry.COLUMN_VIDEO_TYPE + " TEXT NOT NULL, "
                + " FOREIGN KEY ("+ FavoriteMoviesContract.VideoEntry.COLUMN_MOVIE_ID +") REFERENCES " + FavoriteMoviesContract.MovieEntry.TABLE_NAME + "(" + FavoriteMoviesContract.MovieEntry._ID + ")"
                + ");";

        final String SQL_CREATE_REVIEW_TABLE = "CREATE TABLE " + FavoriteMoviesContract.ReviewEntry.TABLE_NAME + "( "
                + FavoriteMoviesContract.ReviewEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + FavoriteMoviesContract.ReviewEntry.COLUMN_MOVIE_ID + " TEXT NOT NULL, "
                + FavoriteMoviesContract.ReviewEntry.COLUMN_REVIEW_AUTHOR + " TEXT NOT NULL, "
                + FavoriteMoviesContract.ReviewEntry.COLUMN_REVIEW_CONTENT + " TEXT NOT NULL, "
                + " FOREIGN KEY ("+ FavoriteMoviesContract.ReviewEntry.COLUMN_MOVIE_ID +") REFERENCES " + FavoriteMoviesContract.MovieEntry.TABLE_NAME + "(" + FavoriteMoviesContract.MovieEntry._ID + ")"
                + ");";

        sqLiteDatabase.execSQL(SQL_CREATE_MOVIE_TABLE);
        sqLiteDatabase.execSQL(SQL_CREATE_VIDEO_TABLE);
        sqLiteDatabase.execSQL(SQL_CREATE_REVIEW_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + FavoriteMoviesContract.MovieEntry.TABLE_NAME);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + FavoriteMoviesContract.VideoEntry.TABLE_NAME);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + FavoriteMoviesContract.ReviewEntry.TABLE_NAME);

        onCreate(sqLiteDatabase);
    }
}
