package com.yusufekremunlu.easyway.ui.main.home.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.squareup.picasso.Picasso;
import com.yusufekremunlu.easyway.R;
import com.yusufekremunlu.easyway.model.entity.news.NewsHeadlines;
import com.yusufekremunlu.easyway.ui.main.home.SelectListener;
import java.util.List;

public class NewsAdapter extends RecyclerView.Adapter<NewsViewHolder> {
    private Context context;
    private List<NewsHeadlines> newsHeadlinesList;
    private SelectListener listener;

    public NewsAdapter(Context context, List<NewsHeadlines> newsHeadlinesList, SelectListener listener) {
        this.context = context;
        this.newsHeadlinesList = newsHeadlinesList;
        this.listener = listener;
    }


    @NonNull
    @Override
    public NewsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new NewsViewHolder(LayoutInflater.from(context).inflate(R.layout.headline_news_items, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull NewsViewHolder holder, int position) {
        NewsHeadlines newsHeadline = newsHeadlinesList.get(position);

        holder.text_source.setText(newsHeadline.getSource().getName());
        holder.text_title.setText(newsHeadline.getTitle());
        if (newsHeadline.getUrlToImage() != null) {
            Picasso.get().load(newsHeadline.getUrlToImage()).into(holder.img_headline);
        }
        holder.cardView.setOnClickListener(v -> {
            listener.OnNewsClicked(newsHeadlinesList.get(position));
        });
    }

    @Override
    public int getItemCount() {
        return newsHeadlinesList.size();
    }

    public void updateData(List<NewsHeadlines> newData) {
        newsHeadlinesList.clear();
        newsHeadlinesList.addAll(newData);
        notifyDataSetChanged();
    }

}
