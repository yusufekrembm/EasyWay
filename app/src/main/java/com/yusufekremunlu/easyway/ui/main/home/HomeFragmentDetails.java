package com.yusufekremunlu.easyway.ui.main.home;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import com.squareup.picasso.Picasso;
import com.yusufekremunlu.easyway.R;
import com.yusufekremunlu.easyway.model.entity.news.NewsHeadlines;

public class HomeFragmentDetails extends Fragment {
    NewsHeadlines newsHeadlines;
    TextView txt_title, txt_author, txt_time, txt_detail, txt_content, txt_url;
    ImageView img_news;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home_details, container, false);
        txt_title = view.findViewById(R.id.text_detail_title);
        txt_author = view.findViewById(R.id.text_detail_author);
        txt_time = view.findViewById(R.id.text_detail_time);
        txt_detail = view.findViewById(R.id.text_detail_detail);
        txt_content = view.findViewById(R.id.text_detail_content);
        img_news = view.findViewById(R.id.img_detail_news);
        txt_url = view.findViewById(R.id.go_to_details);

        Bundle args = getArguments();
        assert args != null;
        newsHeadlines = args.getParcelable("news");
        txt_title.setText(newsHeadlines.getTitle());
        txt_author.setText((CharSequence) newsHeadlines.getAuthor());
        txt_time.setText(newsHeadlines.getPublishedAt());
        txt_detail.setText(newsHeadlines.getDescription());
        txt_content.setText(newsHeadlines.getContent());
        Picasso.get().load(newsHeadlines.getUrlToImage()).into(img_news);
        txt_url.setText(newsHeadlines.getUrl());
        txt_url.setOnClickListener(v -> {
            String url = newsHeadlines.getUrl();
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
            startActivity(intent);
        });

        return view;
    }
}