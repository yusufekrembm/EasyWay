package com.yusufekremunlu.easyway.ui.main.movies;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import com.yusufekremunlu.easyway.R;
import com.yusufekremunlu.easyway.db.remote.movies.MovieApiClient;
import com.yusufekremunlu.easyway.model.entity.movies.MovieFav;
import com.yusufekremunlu.easyway.ui.main.movies.adapters.MovieCastAdapter;
import com.yusufekremunlu.easyway.ui.main.movies.adapters.MovieVideoAdapter;
import com.yusufekremunlu.easyway.ui.main.movies.viewmodels.FavouritesViewModel;
import com.yusufekremunlu.easyway.ui.main.movies.viewmodels.MovieDetailViewModel;
import com.yusufekremunlu.easyway.ui.main.movies.viewmodels.ViewModelFactory;;
import com.yusufekremunlu.easyway.utils.Credentials;
import java.util.ArrayList;
import androidx.appcompat.widget.Toolbar;


public class FavouritesDetailFragment extends Fragment {
    private MovieCastAdapter movieCastAdapter;
    private MovieVideoAdapter movieVideoAdapter;
    MovieDetailViewModel movieDetailViewModel;
    FavouritesViewModel favouritesViewModel;
    MovieFav movieFav;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        movieDetailViewModel = new ViewModelProvider(this).get(MovieDetailViewModel.class);
        favouritesViewModel = new ViewModelProvider(this, new ViewModelFactory(requireActivity().getApplication())).get(FavouritesViewModel.class);
        movieCastAdapter = new MovieCastAdapter(new ArrayList<>(), getContext());
        movieVideoAdapter = new MovieVideoAdapter(new ArrayList<>(), getContext());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_favorites_details, container, false);
        Bundle args = getArguments();
        if (args != null) {
            movieFav = args.getParcelable("movie");
            if (movieFav != null) {
                int movieId = movieFav.uid;
                MovieApiClient.getInstance().getMovieCastModelFromApi(movieId);
                MovieApiClient.getInstance().getMovieVideosFromApi(movieId);
                MovieApiClient.getInstance().getMovieCastModelFromApi(movieId);
                MovieApiClient.getInstance().getMovieVideosFromApi(movieId);
                TextView detailMovieTitleText = view.findViewById(R.id.detailMovieTitleText);
                detailMovieTitleText.setText(movieFav.title);
                ImageView detailMovieImageView = view.findViewById(R.id.detailsMovieImage);
                Glide.with(this)
                        .load(Credentials.MOVIE_BACKDROP_URL + movieFav.getPoster_path())
                        .into(detailMovieImageView);
                RatingBar detailMovieRatingBar = view.findViewById(R.id.detailMovieRatingBar);
                detailMovieRatingBar.setRating(movieFav.getVote_average() / 2);
                TextView detailMovieNumOfVotes = view.findViewById(R.id.detailMovienumOfVotes);
                detailMovieNumOfVotes.setText(String.valueOf(movieFav.getVote_count() + " votes"));
                TextView detailMovieOverView = view.findViewById(R.id.detailMovieOverView);
                detailMovieOverView.setText(movieFav.getOverview());
                TextView detailMovieReleasedDate = view.findViewById(R.id.detailMovieReleasedDate);
                detailMovieReleasedDate.setText(movieFav.getRelease_date());
                TextView detailMovieLanguageText = view.findViewById(R.id.detailMovieLanguageText);
                detailMovieLanguageText.setText(movieFav.getOriginal_language().toUpperCase());
                TextView detailTitleOriginal = view.findViewById(R.id.detailTitleOriginal);
                detailTitleOriginal.setText(movieFav.getOriginal_title());
                detailTitleOriginal.setMaxLines(1);
                Toolbar toolbar = view.findViewById(R.id.myToolbar);
                toolbar.setTitle("Watch List : "+movieFav.getTitle());
            }
        }
        ToggleButton favouriteButton = view.findViewById(R.id.favouriteButtonMovie);
        favouriteButton.setOnClickListener(v -> {
            boolean isFavourite = readState(movieFav.getUid());
            if (!isFavourite) {
            } else {
                favouriteButton.setBackground(ContextCompat.getDrawable(requireContext(), R.drawable.empty_heart));
                Toast.makeText(getContext(), "Removed from watch list", Toast.LENGTH_SHORT).show();
                MovieFav favMovie = new MovieFav(movieFav.getUid(),movieFav.getTitle(),movieFav.getPoster_path(),movieFav.getOriginal_language(),movieFav.getOriginal_title(),movieFav.getOverview(),movieFav.getBackdrop_path(),movieFav.getRelease_date(),movieFav.getVote_average(),movieFav.getVote_count());
                favouritesViewModel.deleteFavMovie(favMovie);
                saveState(movieFav.getUid(), false);
            }
        });

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
            movieVideoAdapter.setVideoModelList(videoModelList);
        });
    }
    private void saveState(int movieId, boolean isFavourite) {
        SharedPreferences aSharedPreferences = requireContext().getSharedPreferences("FavouriteMovies", Context.MODE_PRIVATE);
        SharedPreferences.Editor aSharedPreferencesEdit = aSharedPreferences.edit();
        aSharedPreferencesEdit.putBoolean(getFavouriteKey(movieId), isFavourite);
        aSharedPreferencesEdit.apply();
    }

    @Override
    public void onResume() {
        super.onResume();
        if (movieFav != null) {
            boolean isFavourite = readState(movieFav.getUid());
            if (!isFavourite) {
                requireView().findViewById(R.id.favouriteButtonMovie).setBackground(ContextCompat.getDrawable(requireContext(), R.drawable.empty_heart));
            } else {
                requireView().findViewById(R.id.favouriteButtonMovie).setBackground(ContextCompat.getDrawable(requireContext(), R.drawable.heart_fill));
            }
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        boolean isFavourite = readState(movieFav.getUid());
        if (!isFavourite) {
            requireView().findViewById(R.id.favouriteButtonMovie).setBackground(ContextCompat.getDrawable(requireContext(), R.drawable.empty_heart));
        } else {
            requireView().findViewById(R.id.favouriteButtonMovie).setBackground(ContextCompat.getDrawable(requireContext(), R.drawable.heart_fill));
        }
    }

    private boolean readState(int movieId) {
        SharedPreferences aSharedPreferences = requireContext().getSharedPreferences("FavouriteMovies", Context.MODE_PRIVATE);
        return aSharedPreferences.getBoolean(getFavouriteKey(movieId), false);
    }

    private String getFavouriteKey(int movieId) {
        return "favState_" + movieId;
    }
}
