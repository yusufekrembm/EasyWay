package com.yusufekremunlu.easyway.ui.main.movies.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import com.yusufekremunlu.easyway.R;
import com.yusufekremunlu.easyway.model.entity.movies.MovieModel;
import com.yusufekremunlu.easyway.utils.Credentials;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class MoviesAdapter extends RecyclerView.Adapter<MoviesAdapter.MovieViewHolder> {

    private List<MovieModel> movieList;
    private final Context context;
    private OnItemClickListener listener;

    public MoviesAdapter(List<MovieModel> movieList, Context context) {
        this.movieList = movieList;
        this.context = context;
    }

    public interface OnItemClickListener {
        void onItemClick(MovieModel movie);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }

    @NonNull
    @Override
    public MovieViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_item_movie, parent, false);
        return new MovieViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MovieViewHolder holder, int position) {
        MovieModel movie = movieList.get(position);
        holder.bind(movie);
        holder.itemView.setOnClickListener(v -> {
            if (listener != null) {
                listener.onItemClick(movie);
            }
        });
    }

    @Override
    public int getItemCount() {
        return movieList != null ? movieList.size() : 0;
    }

    public class MovieViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;
        TextView titleTextView;

        public MovieViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.image);
            titleTextView = itemView.findViewById(R.id.titleText);
        }

        public void bind(MovieModel movie) {
            Glide.with(context)
                    .load(Credentials.MOVIE_BASE_POSTER_URL + movie.getPoster_path())
                    .into(imageView);
            titleTextView.setText(movie.getTitle());
        }
    }

    public void setMovieList(List<MovieModel> newMovieList) {
        MovieDiffCallback diffCallback = new MovieDiffCallback(movieList, newMovieList);
        DiffUtil.DiffResult diffResult = DiffUtil.calculateDiff(diffCallback);
        movieList.clear();
        movieList.addAll(newMovieList);
        diffResult.dispatchUpdatesTo(this);
    }

    private static class MovieDiffCallback extends DiffUtil.Callback {
        private final List<MovieModel> oldList;
        private final List<MovieModel> newList;

        public MovieDiffCallback(List<MovieModel> oldList, List<MovieModel> newList) {
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
            MovieModel oldMovie = oldList.get(oldItemPosition);
            MovieModel newMovie = newList.get(newItemPosition);
            return Objects.equals(oldMovie.getMovie_id(), newMovie.getMovie_id());
        }

        @Override
        public boolean areContentsTheSame(int oldItemPosition, int newItemPosition) {
            MovieModel oldMovie = oldList.get(oldItemPosition);
            MovieModel newMovie = newList.get(newItemPosition);
            return Objects.equals(oldMovie, newMovie);
        }
    }
}


