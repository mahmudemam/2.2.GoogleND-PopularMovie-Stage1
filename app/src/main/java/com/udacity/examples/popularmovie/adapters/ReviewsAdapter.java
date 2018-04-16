package com.udacity.examples.popularmovie.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.udacity.examples.popularmovie.R;
import com.udacity.examples.popularmovie.data.Review;

import java.util.List;

public class ReviewsAdapter extends RecyclerView.Adapter<ReviewsAdapter.ReviewViewHolder> {
    private Context context;
    private List<Review> reviews;

    public ReviewsAdapter(Context context, List<Review> reviews) {
        this.context = context;
        this.reviews = reviews;
    }

    @NonNull
    @Override
    public ReviewsAdapter.ReviewViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ReviewViewHolder(
                LayoutInflater.from(context)
                        .inflate(R.layout.review_item, parent, false)
        );
    }

    @Override
    public void onBindViewHolder(@NonNull ReviewsAdapter.ReviewViewHolder holder, int position) {
        Review review = reviews.get(position);
        holder.bind(position + 1, review);
    }

    @Override
    public int getItemCount() {
        return reviews.size();
    }

    class ReviewViewHolder extends RecyclerView.ViewHolder {
        private TextView sequence;
        private TextView author;
        private TextView comment;

        ReviewViewHolder(View view) {
            super(view);

            sequence = view.findViewById(R.id.tv_review_sequence);
            author = view.findViewById(R.id.tv_review_author);
            comment = view.findViewById(R.id.tv_review_comment);
        }

        public void bind(int seq, Review review) {
            sequence.setText(String.valueOf(seq));
            author.setText(review.getAuthor());
            comment.setText(review.getComment());
        }
    }
}
