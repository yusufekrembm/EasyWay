package com.yusufekremunlu.easyway.ui.main.movies.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import com.yusufekremunlu.easyway.R;
import com.yusufekremunlu.easyway.model.entity.movies.MovieFav;
import com.yusufekremunlu.easyway.utils.Credentials;
import java.util.List;

public class FavouritesAdapter extends RecyclerView.Adapter<FavouritesAdapter.FavouriteMovieHolder> {
    private final List<MovieFav> favouriteList;
    private final Context context;
    private OnItemClickListener listener;

    public FavouritesAdapter(List<MovieFav> favouriteList, Context context) {
        this.favouriteList = favouriteList;
        this.context = context;
    }

    public interface OnItemClickListener {
        void onItemClick(MovieFav moviefav);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }

    @NonNull
    @Override
    public FavouritesAdapter.FavouriteMovieHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_item_movie, parent, false);
        return new FavouritesAdapter.FavouriteMovieHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull FavouritesAdapter.FavouriteMovieHolder holder, int position) {
        MovieFav movie = favouriteList.get(position);
        holder.bind(movie);
        holder.itemView.setOnClickListener(v -> {
            if (listener != null) {
                listener.onItemClick(movie);
            }
        });
    }

    @Override
    public int getItemCount() {
        return favouriteList != null ? favouriteList.size() : 0;
    }

    public class FavouriteMovieHolder extends RecyclerView.ViewHolder {
        ImageView imageView;
        TextView titleTextView;

        public FavouriteMovieHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.image);
            titleTextView = itemView.findViewById(R.id.titleText);
        }

        public void bind(MovieFav movie) {
            Glide.with(context)
                    .load(Credentials.MOVIE_BASE_POSTER_URL + movie.backdrop_path)
                    .into(imageView);
            titleTextView.setText(movie.title);
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    public void setFavouriteList(List<MovieFav> newFavouriteList) {
        favouriteList.clear();
        favouriteList.addAll(newFavouriteList);
        notifyDataSetChanged();
    }
}
