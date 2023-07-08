package com.yusufekremunlu.easyway.ui.main.movies.adapters;

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
import java.util.List;
import java.util.Objects;

public class ShowAllAdapter extends RecyclerView.Adapter<ShowAllAdapter.ShowAllViewHolder> {

    private List<MovieModel> discoverList;
    private final Context context;
    private OnItemClickListener listener;

    public ShowAllAdapter(List<MovieModel> discoverList, Context context) {
        this.discoverList = discoverList;
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
    public ShowAllViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_item_movie_grid, parent, false);
        return new ShowAllViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ShowAllViewHolder holder, int position) {
        MovieModel movie = discoverList.get(position);
        holder.bind(movie);
        holder.itemView.setOnClickListener(v -> {
            if (listener != null) {
                listener.onItemClick(movie);
            }
        });
    }

    @Override
    public int getItemCount() {
        return discoverList != null ? discoverList.size() : 0;
    }

    public class ShowAllViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;
        TextView titleTextView;

        public ShowAllViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.item_movie_grid_image);
            titleTextView = itemView.findViewById(R.id.item_grid_title_text);
        }

        public void bind(MovieModel movie) {
            Glide.with(context)
                    .load(Credentials.MOVIE_BASE_POSTER_URL + movie.getPoster_path())
                    .into(imageView);
            titleTextView.setText(movie.getTitle());
        }
    }

    public void setDiscoverList(List<MovieModel> newMovieList) {
        MovieDiffCallback diffCallback = new MovieDiffCallback(discoverList, newMovieList);
        DiffUtil.DiffResult diffResult = DiffUtil.calculateDiff(diffCallback);
        discoverList.clear();
        discoverList.addAll(newMovieList);
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