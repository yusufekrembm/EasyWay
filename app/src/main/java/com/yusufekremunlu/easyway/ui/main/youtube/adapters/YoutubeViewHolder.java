package com.yusufekremunlu.easyway.ui.main.youtube.adapters;

import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.yusufekremunlu.easyway.R;

public class YoutubeViewHolder extends RecyclerView.ViewHolder {
     WebView youtubeWebView;
    public YoutubeViewHolder(@NonNull View itemView) {
        super(itemView);
        youtubeWebView = (WebView) itemView.findViewById(R.id.videoView);
        youtubeWebView.getSettings().setJavaScriptEnabled(true);
        youtubeWebView.setWebChromeClient(new WebChromeClient());
    }
}
