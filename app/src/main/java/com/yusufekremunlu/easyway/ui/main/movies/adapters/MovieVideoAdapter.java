package com.yusufekremunlu.easyway.ui.main.movies.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import com.yusufekremunlu.easyway.R;
import com.yusufekremunlu.easyway.model.entity.movies.MovieVideoModel;
import com.yusufekremunlu.easyway.utils.Credentials;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class MovieVideoAdapter extends RecyclerView.Adapter<MovieVideoAdapter.MovieVideoViewHolder> {

    private List<MovieVideoModel> videoModelList;
    private final Context context;
    private OnItemClickListener listener;

    public MovieVideoAdapter(List<MovieVideoModel> videoModelList, Context context) {
        this.videoModelList = videoModelList;
        this.context = context;
    }

    public interface OnItemClickListener {
        void onItemClick(MovieVideoModel movieVideoModel);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }

    @NonNull
    @Override
    public MovieVideoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_item_video, parent, false);
        return new MovieVideoViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MovieVideoViewHolder holder, int position) {
        MovieVideoModel movieVideoModel = videoModelList.get(position);
        holder.bind(movieVideoModel);
        holder.itemView.setOnClickListener(v -> {
            if (listener != null) {
                listener.onItemClick(movieVideoModel);
            }
        });
    }

    @Override
    public int getItemCount() {
        return videoModelList != null ? videoModelList.size() : 0;
    }

    public class MovieVideoViewHolder extends RecyclerView.ViewHolder {
        ImageView movieDetailVideoImage;
        ImageButton movieDetailImageButton;

        public MovieVideoViewHolder(@NonNull View itemView) {
            super(itemView);
            movieDetailVideoImage = itemView.findViewById(R.id.movieDetailVideoImage);
            movieDetailImageButton = itemView.findViewById(R.id.playImageButton);
        }

        public void bind(MovieVideoModel videoModel) {
            Glide.with(context)
                    .load(Credentials.MOVIE_YT_IMG_URL + videoModel.getKey()+"/maxresdefault.jpg")
                    .into(movieDetailVideoImage);
            movieDetailImageButton.setOnClickListener(v -> {
                String youtubeUrl = Credentials.MOVIE_YT_WATCH_URL + videoModel.getKey();

                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(youtubeUrl));
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.setPackage("com.google.android.youtube");

                if (intent.resolveActivity(context.getPackageManager()) != null) {
                    context.startActivity(intent);
                } else {
                    // Youtube uygulaması yüklü değilse, Youtube'un web sitesini açabilirsiniz.
                    intent.setPackage(null);
                    context.startActivity(intent);
                }
            });
        }
    }

    public void setVideoModelList(List<MovieVideoModel> newVideoModelList) {
        MovieVideoModelDiffCallback diffCallback = new MovieVideoModelDiffCallback(videoModelList, newVideoModelList);
        DiffUtil.DiffResult diffResult = DiffUtil.calculateDiff(diffCallback);
        videoModelList.clear();
        videoModelList.addAll(newVideoModelList);
        diffResult.dispatchUpdatesTo(this);
    }

    private static class MovieVideoModelDiffCallback extends DiffUtil.Callback {
        private final List<MovieVideoModel> oldList;
        private final List<MovieVideoModel> newList;

        public MovieVideoModelDiffCallback(List<MovieVideoModel> oldList, List<MovieVideoModel> newList) {
            this.oldList = oldList;
            this.newList = newList;
        }

        @Override
        public int getOldListSize() {
            return oldList.size();
        }

        @Override
        public int getNewListSize() {
            return newList.size();
        }

        @Override
        public boolean areItemsTheSame(int oldItemPosition, int newItemPosition) {
            MovieVideoModel oldVideoModel = oldList.get(oldItemPosition);
            MovieVideoModel newVideoModel = newList.get(newItemPosition);
            return Objects.equals(oldVideoModel.getId(), newVideoModel.getId());
        }

        @Override
        public boolean areContentsTheSame(int oldItemPosition, int newItemPosition) {
            MovieVideoModel oldVideoModel = oldList.get(oldItemPosition);
            MovieVideoModel newVideoModel = newList.get(newItemPosition);
            return Objects.equals(oldVideoModel, newVideoModel);
        }
    }
}
