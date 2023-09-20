package com.yusufekremunlu.easyway.ui.main.youtube.adapters;

import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.FrameLayout;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.yusufekremunlu.easyway.R;

public class YoutubeViewHolder extends RecyclerView.ViewHolder {
    WebView youtubeWebView;
    FrameLayout customViewContainer;


    public YoutubeViewHolder(@NonNull View itemView) {
        super(itemView);
        youtubeWebView = (WebView) itemView.findViewById(R.id.videoView);
        youtubeWebView.getSettings().setJavaScriptEnabled(true);
        youtubeWebView.setWebViewClient(new WebViewClient());
        youtubeWebView.getSettings().setDomStorageEnabled(true);
        youtubeWebView.getSettings().setAllowContentAccess(true);
        youtubeWebView.getSettings().setAllowFileAccess(true);
        youtubeWebView.setWebChromeClient(new WebChromeClient(){
            private View mCustomView;
            private WebChromeClient.CustomViewCallback mCustomViewCallback;

            @Override
            public void onShowCustomView(View view, WebChromeClient.CustomViewCallback callback) {
                if (mCustomView != null) {
                    onHideCustomView();
                    return;
                }
                mCustomView = view;
                mCustomViewCallback = callback;
                ViewGroup rootView = (ViewGroup) itemView.getRootView();
                rootView.addView(mCustomView);
                youtubeWebView.setVisibility(View.GONE);
            }

            @Override
            public void onHideCustomView() {
                if (mCustomView == null) {
                    return;
                }
                ViewGroup rootView = (ViewGroup) itemView.getRootView();
                rootView.removeView(mCustomView);
                mCustomView = null;
                mCustomViewCallback.onCustomViewHidden();
                youtubeWebView.setVisibility(View.VISIBLE);
            }
        });

    }
}
