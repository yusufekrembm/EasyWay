package com.yusufekremunlu.easyway.ui.main.movies.adapters;

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
import com.yusufekremunlu.easyway.model.entity.movies.MoviePersonCredits;
import com.yusufekremunlu.easyway.utils.Credentials;
import java.util.List;

public class MoviePersonCreditsAdapter extends RecyclerView.Adapter<MoviePersonCreditsAdapter.MoviePersonCreditsViewHolder> {
    private List<MoviePersonCredits> creditList;
    private final Context context;
    private OnItemClickListener listener;

    public MoviePersonCreditsAdapter(List<MoviePersonCredits> creditList, Context context) {
        this.creditList = creditList;
        this.context = context;
    }

    public interface OnItemClickListener {
        void onItemClick(MoviePersonCredits moviePersonCredits);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }

    @NonNull
    @Override
    public MoviePersonCreditsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_item_cast, parent, false);
        return new MoviePersonCreditsViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MoviePersonCreditsViewHolder holder, int position) {
        MoviePersonCredits moviePersonCredits = creditList.get(position);
        holder.bind(moviePersonCredits);
        holder.itemView.setOnClickListener(v -> {
            if (listener != null) {
                listener.onItemClick(moviePersonCredits);
            }
        });
    }

    @Override
    public int getItemCount() {
        return creditList != null ? creditList.size() : 0;
    }

    public class MoviePersonCreditsViewHolder extends RecyclerView.ViewHolder {
        ImageView castImage;
        TextView castTitleText;

        public MoviePersonCreditsViewHolder(@NonNull View itemView) {
            super(itemView);
            castImage = itemView.findViewById(R.id.castImage);
            castTitleText = itemView.findViewById(R.id.castTitleText);
        }

        public void bind(MoviePersonCredits personCredits) {
            Glide.with(context)
                    .load(Credentials.MOVIE_BASE_POSTER_URL + personCredits.getPoster_path())
                    .into(castImage);
            castTitleText.setText(personCredits.getOriginal_title());
        }
    }

    public void setCreditList(List<MoviePersonCredits> newCreditList) {
        notifyDataSetChanged();
        creditList.clear();
        creditList.addAll(newCreditList);

    }
}
