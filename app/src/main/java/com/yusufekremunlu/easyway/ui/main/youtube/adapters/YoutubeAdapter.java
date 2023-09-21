package com.yusufekremunlu.easyway.ui.main.youtube.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.yusufekremunlu.easyway.R;
import com.yusufekremunlu.easyway.model.entity.youtube.YoutubeItemModel;
import java.util.List;

public class YoutubeAdapter extends RecyclerView.Adapter<YoutubeViewHolder> {
    private final Context context;
    private final List<YoutubeItemModel> youtubeItemModelList;

    public YoutubeAdapter(Context context, List<YoutubeItemModel> youtubeItemModelList) {
        this.context = context;
        this.youtubeItemModelList = youtubeItemModelList;
    }

    @NonNull
    @Override
    public YoutubeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new YoutubeViewHolder(LayoutInflater.from(context).inflate(R.layout.list_item_youtube_video, parent, false));
    }

    @SuppressLint("SetJavaScriptEnabled")
    public void onBindViewHolder(@NonNull YoutubeViewHolder holder, int position) {
        YoutubeItemModel youtubeItemModel = youtubeItemModelList.get(position);
        String youtubeVideoUrl = "https://www.youtube.com/embed/"+youtubeItemModel.getId().getVideoId();
        holder.youtubeWebView.loadData("<iframe width=\"100%\" height=\"100%\" src=\""+ youtubeVideoUrl+"\" frameborder=\"0\" allowfullscreen></iframe>", "text/html", "utf-8");
    }

    @Override
    public int getItemCount() {
        return youtubeItemModelList.size();
    }
    @SuppressLint("NotifyDataSetChanged")
    public void updateData(List<YoutubeItemModel> newData) {
        youtubeItemModelList.clear();
        youtubeItemModelList.addAll(newData);
        notifyDataSetChanged();
    }
}
