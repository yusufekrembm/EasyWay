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
import com.yusufekremunlu.easyway.model.entity.movies.MovieCastModel;
import com.yusufekremunlu.easyway.utils.Credentials;
import java.util.List;


public class MovieCastAdapter extends RecyclerView.Adapter<MovieCastAdapter.MovieCastViewHolder> {

    private List<MovieCastModel> castModelList;
    private final Context context;
    private MovieCastAdapter.OnItemClickListener listener;

    public MovieCastAdapter(List<MovieCastModel> castModelList, Context context) {
        this.castModelList = castModelList;
        this.context = context;
    }

    public interface OnItemClickListener {
        void onItemClick(MovieCastModel castModel);
    }

    public void setOnItemClickListener(MovieCastAdapter.OnItemClickListener listener) {
        this.listener = listener;
    }

    @NonNull
    @Override
    public MovieCastAdapter.MovieCastViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_item_cast, parent, false);
        return new MovieCastAdapter.MovieCastViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MovieCastAdapter.MovieCastViewHolder holder, int position) {
        MovieCastModel movieCastModel = castModelList.get(position);
        holder.bind(movieCastModel);
        holder.itemView.setOnClickListener(v -> {
            if (listener != null) {
                listener.onItemClick(movieCastModel);
            }
        });
    }

    @Override
    public int getItemCount() {
        return castModelList != null ? castModelList.size() : 0;
    }

    public class MovieCastViewHolder extends RecyclerView.ViewHolder {
        ImageView castImage;
        TextView castTitleText;

        public MovieCastViewHolder(@NonNull View itemView) {
            super(itemView);
            castImage = itemView.findViewById(R.id.castImage);
            castTitleText = itemView.findViewById(R.id.castTitleText);
        }

        public void bind(MovieCastModel castModel) {
            Glide.with(context)
                    .load(Credentials.MOVIE_PROFILE_URL + castModel.getProfile_path())
                    .into(castImage);
            castTitleText.setText(castModel.getName());
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    public void setMovieCastList(List<MovieCastModel> castModelList) {
        this.castModelList = castModelList;
        notifyDataSetChanged();
    }
}
