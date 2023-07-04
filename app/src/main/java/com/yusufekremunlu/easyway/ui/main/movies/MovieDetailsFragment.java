package com.yusufekremunlu.easyway.ui.main.movies;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.yusufekremunlu.easyway.R;
import com.yusufekremunlu.easyway.model.entity.movies.MovieModel;
import com.yusufekremunlu.easyway.utils.Constants;
import com.yusufekremunlu.easyway.utils.Credentials;

import org.w3c.dom.Text;

import java.util.List;

public class MovieDetailsFragment extends Fragment {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_movie_details, container, false);

        Bundle args = getArguments();
        if (args != null) {
            MovieModel movie = args.getParcelable("movie");
            if (movie != null) {
                TextView detailMovieTitleText = view.findViewById(R.id.detailMovieTitleText);
                detailMovieTitleText.setText(movie.getTitle());
                ImageView detailMovieImageView = view.findViewById(R.id.detailsMovieImage);
                Glide.with(this)
                        .load(Credentials.MOVIE_BACKDROP_URL + movie.getBackdrop_path())
                        .into(detailMovieImageView);
                RatingBar detailMovieRatingBar = view.findViewById(R.id.detailMovieRatingBar);
                detailMovieRatingBar.setRating(movie.getVote_average()/2);
                TextView detailMovienumOfVotes = view.findViewById(R.id.detailMovienumOfVotes);
                detailMovienumOfVotes.setText(String.valueOf(movie.getVote_count()+" votes"));
                TextView detailMovieOverView = view.findViewById(R.id.detailMovieOverView);
                detailMovieOverView.setText(movie.getOverview());
                TextView detailMovieReleasedDate = view.findViewById(R.id.detailMovieReleasedDate);
                detailMovieReleasedDate.setText(movie.getRelease_date());
                TextView detailMovieLanguageText = view.findViewById(R.id.detailMovieLanguageText);
                detailMovieLanguageText.setText(movie.getOriginal_language().toUpperCase());
                TextView detailTitleOriginal = view.findViewById(R.id.detailTitleOriginal);
                detailTitleOriginal.setText(movie.getOriginal_title());
                detailTitleOriginal.setMaxLines(1);
                List<Integer> genreIds = movie.getGenre_ids();
                StringBuilder genreBuilder = new StringBuilder();

                if (genreIds != null && !genreIds.isEmpty()) {
                    int maxGenres = Math.min(genreIds.size(), 4); // Limit to 4 genres

                    for (int i = 0; i < maxGenres; i++) {
                        String genre = Constants.getGenre(genreIds.get(i));
                        genreBuilder.append(genre);

                        if (i < maxGenres - 1) {
                            genreBuilder.append("/");
                        }
                    }
                }

                TextView detailMovieGenres = view.findViewById(R.id.movieDetailGenresText);
                detailMovieGenres.setText(genreBuilder.toString());





            }
        }

        return view;
    }
}