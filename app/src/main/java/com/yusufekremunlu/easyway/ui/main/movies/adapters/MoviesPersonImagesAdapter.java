package com.yusufekremunlu.easyway.ui.main.movies.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import com.yusufekremunlu.easyway.R;
import com.yusufekremunlu.easyway.model.entity.movies.MoviePersonImages;
import com.yusufekremunlu.easyway.utils.Credentials;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class MoviesPersonImagesAdapter extends RecyclerView.Adapter<MoviesPersonImagesAdapter.MoviePersonImagesViewHolder> {

    private List<MoviePersonImages> imagesList;
    private final Context context;
    private OnItemClickListener listener;

    public MoviesPersonImagesAdapter(List<MoviePersonImages> imagesList, Context context) {
        this.imagesList = imagesList;
        this.context = context;
    }

    public interface OnItemClickListener {
        void onItemClick(MoviePersonImages personImagesModel);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }

    @NonNull
    @Override
    public MoviePersonImagesViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_item_cast, parent, false);
        return new MoviePersonImagesViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MoviePersonImagesViewHolder holder, int position) {
        MoviePersonImages moviePersonImages = imagesList.get(position);
        holder.bind(moviePersonImages);
        holder.itemView.setOnClickListener(v -> {
            if (listener != null) {
                listener.onItemClick(moviePersonImages);
            }
        });
    }

    @Override
    public int getItemCount() {
        return imagesList != null ? imagesList.size() : 0;
    }

    public class MoviePersonImagesViewHolder extends RecyclerView.ViewHolder {
        ImageView castImage;

        public MoviePersonImagesViewHolder(@NonNull View itemView) {
            super(itemView);
            castImage = itemView.findViewById(R.id.castImage);
        }

        public void bind(MoviePersonImages personImagesModel) {
            Glide.with(context)
                    .load(Credentials.MOVIE_BASE_POSTER_URL + personImagesModel.getFile_path())
                    .into(castImage);
        }
    }

    public void setImagesList(List<MoviePersonImages> newImagesList) {
        MoviePersonImagesDiffCallback diffCallback = new MoviePersonImagesDiffCallback(imagesList, newImagesList);
        DiffUtil.DiffResult diffResult = DiffUtil.calculateDiff(diffCallback);
        imagesList.clear();
        imagesList.addAll(newImagesList);
        diffResult.dispatchUpdatesTo(this);
    }

    private static class MoviePersonImagesDiffCallback extends DiffUtil.Callback {
        private final List<MoviePersonImages> oldList;
        private final List<MoviePersonImages> newList;

        public MoviePersonImagesDiffCallback(List<MoviePersonImages> oldList, List<MoviePersonImages> newList) {
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
            MoviePersonImages oldImages = oldList.get(oldItemPosition);
            MoviePersonImages newImages = newList.get(newItemPosition);
            return Objects.equals(oldImages.getFile_path(), newImages.getFile_path());
        }

        @Override
        public boolean areContentsTheSame(int oldItemPosition, int newItemPosition) {
            MoviePersonImages oldImages = oldList.get(oldItemPosition);
            MoviePersonImages newImages = newList.get(newItemPosition);
            return Objects.equals(oldImages, newImages);
        }
    }
}
