package com.yusufekremunlu.easyway.ui.main.movies;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;
import com.bumptech.glide.Glide;
import com.yusufekremunlu.easyway.R;
import com.yusufekremunlu.easyway.db.remote.movies.MovieApiClient;
import com.yusufekremunlu.easyway.model.entity.movies.MovieCastModel;
import com.yusufekremunlu.easyway.model.entity.movies.MovieFav;
import com.yusufekremunlu.easyway.model.entity.movies.MovieModel;
import com.yusufekremunlu.easyway.model.entity.movies.MoviePerson;
import com.yusufekremunlu.easyway.ui.main.movies.adapters.MovieCastAdapter;
import com.yusufekremunlu.easyway.ui.main.movies.adapters.MovieVideoAdapter;
import com.yusufekremunlu.easyway.ui.main.movies.viewmodels.FavouritesViewModel;
import com.yusufekremunlu.easyway.ui.main.movies.viewmodels.MovieDetailViewModel;
import com.yusufekremunlu.easyway.ui.main.movies.viewmodels.ViewModelFactory;
import com.yusufekremunlu.easyway.utils.Constants;
import com.yusufekremunlu.easyway.utils.Credentials;
import java.util.ArrayList;
import java.util.List;

public class MovieDetailsFragment extends Fragment implements MovieCastAdapter.OnItemClickListener {
    MovieDetailViewModel movieDetailViewModel;
    FavouritesViewModel favouritesViewModel;
    private MovieCastAdapter movieCastAdapter;
    private MovieVideoAdapter movieVideoAdapter;
    MovieModel movie;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        movieDetailViewModel = new ViewModelProvider(this).get(MovieDetailViewModel.class);
        favouritesViewModel = new ViewModelProvider(this, new ViewModelFactory(requireActivity().getApplication())).get(FavouritesViewModel.class);
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
            movie = args.getParcelable("movie");
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
                TextView detailMovieNumOfVotes = view.findViewById(R.id.detailMovienumOfVotes);
                detailMovieNumOfVotes.setText(String.valueOf(movie.getVote_count() + " votes"));
                TextView detailMovieOverView = view.findViewById(R.id.detailMovieOverView);
                detailMovieOverView.setText(movie.getOverview());
                TextView detailMovieReleasedDate = view.findViewById(R.id.detailMovieReleasedDate);
                detailMovieReleasedDate.setText(movie.getRelease_date());
                TextView detailMovieLanguageText = view.findViewById(R.id.detailMovieLanguageText);
                detailMovieLanguageText.setText(movie.getOriginal_language().toUpperCase());
                TextView detailTitleOriginal = view.findViewById(R.id.detailTitleOriginal);
                detailTitleOriginal.setText(movie.getOriginal_title());
                detailTitleOriginal.setMaxLines(1);
                Toolbar myToolbar = view.findViewById(R.id.myToolbar);
                myToolbar.setTitle("Movie Detail : " + movie.getTitle());
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

        ToggleButton favouriteButton = view.findViewById(R.id.favouriteButtonMovie);

        favouriteButton.setOnClickListener(v -> {
            boolean isFavourite = readState(movie.getMovie_id());
            if (!isFavourite) {
                favouriteButton.setBackground(ContextCompat.getDrawable(requireContext(), R.drawable.heart_fill));
                Toast.makeText(getContext(), "Added to watch list", Toast.LENGTH_SHORT).show();
                MovieFav favMovie = new MovieFav(movie.getMovie_id(), movie.getTitle(), movie.getPoster_path(), movie.getOriginal_language(), movie.getOriginal_title(), movie.getOverview(), movie.getBackdrop_path(), movie.getRelease_date(), movie.getVote_average(), movie.getVote_count());
                favouritesViewModel.insertFavMovie(favMovie);
                saveState(movie.getMovie_id(), true);
            } else {
                favouriteButton.setBackground(ContextCompat.getDrawable(requireContext(), R.drawable.empty_heart));
                Toast.makeText(getContext(), "Removed from watch list", Toast.LENGTH_SHORT).show();
                MovieFav favMovie = new MovieFav(movie.getMovie_id(), movie.getTitle(), movie.getPoster_path(), movie.getOriginal_language(), movie.getOriginal_title(), movie.getOverview(), movie.getBackdrop_path(), movie.getRelease_date(), movie.getVote_average(), movie.getVote_count());
                favouritesViewModel.deleteFavMovie(favMovie);
                saveState(movie.getMovie_id(), false);
            }
        });

        Button goToFavouriteButton = view.findViewById(R.id.goToFavouriteFragment);
        goToFavouriteButton.setOnClickListener(v -> Navigation.findNavController(requireView())
                .navigate(R.id.action_movieDetailsFragment_to_favouritesFragment));
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
            }
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
        if (movie != null) {
            boolean isFavourite = readState(movie.getMovie_id());
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
        boolean isFavourite = readState(movie.getMovie_id());
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