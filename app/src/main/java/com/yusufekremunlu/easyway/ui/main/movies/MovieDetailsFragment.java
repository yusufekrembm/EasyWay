package com.yusufekremunlu.easyway.ui.main.movies;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.yusufekremunlu.easyway.R;
import com.yusufekremunlu.easyway.db.remote.movies.MovieApiClient;
import com.yusufekremunlu.easyway.model.entity.movies.MovieCastModel;
import com.yusufekremunlu.easyway.model.entity.movies.MovieModel;
import com.yusufekremunlu.easyway.model.entity.movies.MoviePerson;
import com.yusufekremunlu.easyway.ui.main.movies.adapters.MovieCastAdapter;
import com.yusufekremunlu.easyway.ui.main.movies.adapters.MovieVideoAdapter;
import com.yusufekremunlu.easyway.ui.main.movies.viewmodels.MovieDetailViewModel;
import com.yusufekremunlu.easyway.utils.Constants;
import com.yusufekremunlu.easyway.utils.Credentials;

import java.util.ArrayList;
import java.util.List;

public class MovieDetailsFragment extends Fragment implements MovieCastAdapter.OnItemClickListener {
    MovieDetailViewModel movieDetailViewModel;
    private MovieCastAdapter movieCastAdapter;
    private MovieVideoAdapter movieVideoAdapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        movieDetailViewModel = new ViewModelProvider(this).get(MovieDetailViewModel.class);
        movieCastAdapter = new MovieCastAdapter(new ArrayList<>(), getContext());
        movieVideoAdapter = new MovieVideoAdapter(new ArrayList<>(), getContext());
        movieCastAdapter.setOnItemClickListener(this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_movie_details, container, false);
        Bundle args = getArguments();
        if (args != null) {
            MovieModel movie = args.getParcelable("movie");
            if (movie != null) {

                int movieId = movie.getMovie_id();
                MovieApiClient.getInstance().getMovieCastModelFromApi(movieId);
                MovieApiClient.getInstance().getMovieVideosFromApi(movieId);

                TextView detailMovieTitleText = view.findViewById(R.id.detailMovieTitleText);
                detailMovieTitleText.setText(movie.getTitle());

                ImageView detailMovieImageView = view.findViewById(R.id.detailsMovieImage);
                Glide.with(this)
                        .load(Credentials.MOVIE_BACKDROP_URL + movie.getBackdrop_path())
                        .into(detailMovieImageView);

                RatingBar detailMovieRatingBar = view.findViewById(R.id.detailMovieRatingBar);
                detailMovieRatingBar.setRating(movie.getVote_average() / 2);

                TextView detailMovienumOfVotes = view.findViewById(R.id.detailMovienumOfVotes);
                detailMovienumOfVotes.setText(String.valueOf(movie.getVote_count() + " votes"));

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
        RecyclerView movieCastRecycler = view.findViewById(R.id.castRecyclerView);
        LinearLayoutManager layoutCast = new LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false);
        movieCastRecycler.setLayoutManager(layoutCast);
        movieCastRecycler.setAdapter(movieCastAdapter);

        RecyclerView movieVideoRecycler = view.findViewById(R.id.videosRecyclerView);
        LinearLayoutManager layoutVideos = new LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false);
        movieVideoRecycler.setLayoutManager(layoutVideos);
        movieVideoRecycler.setAdapter(movieVideoAdapter);

        observeData();
        return view;
    }

    private void observeData() {
        movieDetailViewModel.getCastModelMovies().observe(getViewLifecycleOwner(), castModelList -> {
            movieCastAdapter.setMovieCastList(castModelList);
        });
        movieDetailViewModel.getVideoModelMovies().observe(getViewLifecycleOwner(), videoModelList -> {
            movieVideoAdapter.setMovieVideoList(videoModelList);
        });
    }

    @Override
    public void onItemClick(MovieCastModel movieCastModel) {
        Bundle bundle = new Bundle();
        bundle.putParcelable("castModel", movieCastModel);

        MovieApiClient.getInstance().getMoviesPersonsFromApi(movieCastModel.getId(), new MovieApiClient.MovieApiCallback() {
            @Override
            public void onSuccess(MoviePerson moviePerson) {
                Bundle bundle = new Bundle();
                bundle.putParcelable("moviePerson", moviePerson);
                Navigation.findNavController(requireView())
                        .navigate(R.id.action_movieDetailsFragment_to_movieCastDetails, bundle);
            }

            @Override
            public void onFailure(Throwable throwable) {
                // Hata durumunda yapılacak işlemler
            }
        });
    }
}