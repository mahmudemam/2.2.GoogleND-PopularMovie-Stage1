package com.udacity.examples.popularmovie;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import com.udacity.examples.popularmovie.data.Movie;

import java.util.List;

public class VideosAdapter extends RecyclerView.Adapter<VideosAdapter.VideoViewHolder> {
    private List<Movie.Video> videos;
    private Context context;
    private OnVideoClickLinstener mListener;

    public VideosAdapter(Context context, @NonNull List<Movie.Video> videos, @NonNull OnVideoClickLinstener listener) {
        this.videos = videos;
        this.context = context;
        this.mListener = listener;
    }

    @NonNull
    @Override
    public VideoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new VideoViewHolder(LayoutInflater.from(context)
                .inflate(R.layout.video_item, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull VideoViewHolder holder, int position) {
        holder.bind(videos.get(position));
    }

    @Override
    public int getItemCount() {
        return videos.size();
    }

    class VideoViewHolder extends RecyclerView.ViewHolder {

        ImageButton imageButton;
        TextView textView;

        VideoViewHolder(View view) {
            super(view);

            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mListener.onVideoClick((Movie.Video) view.getTag());
                }
            });

            imageButton = view.findViewById(R.id.ib_video);
            textView = view.findViewById(R.id.tv_video);
        }

        void bind(Movie.Video video) {
            int imgRes = R.drawable.ic_trailer;
            if (!video.getType().equals("Trailer")) {
                imgRes = R.drawable.ic_clip;
            }
            imageButton.setImageResource(imgRes);
            textView.setText(video.getName());
        }
    }

    public interface OnVideoClickLinstener {
        void onVideoClick(Movie.Video video);
    }
}
