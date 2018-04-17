package com.udacity.examples.popularmovie.data;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by noname on 4/9/18.
 */

public class Review implements Parcelable {
    public static final Creator<Review> CREATOR = new Creator<Review>() {
        @Override
        public Review createFromParcel(Parcel parcel) {
            return new Review(parcel);
        }

        @Override
        public Review[] newArray(int i) {
            return new Review[i];
        }
    };
    private String author;
    private String comment;

    public Review(String author, String comment) {
        this.author = author;
        this.comment = comment;
    }

    private Review(Parcel parcel) {
        author = parcel.readString();
        comment = parcel.readString();
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    @Override
    public String toString() {
        return "Review{" +
                "author='" + author + '\'' +
                ", comment='" + comment + '\'' +
                '}';
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(author);
        parcel.writeString(comment);
    }
}