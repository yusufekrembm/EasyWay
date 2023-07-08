package com.yusufekremunlu.easyway.ui.main.movies;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import com.yusufekremunlu.easyway.R;
import com.yusufekremunlu.easyway.db.remote.movies.MovieApiClient;
import com.yusufekremunlu.easyway.model.entity.movies.MovieModel;
import com.yusufekremunlu.easyway.ui.main.movies.adapters.MoviesAdapter;
import com.yusufekremunlu.easyway.ui.main.movies.viewmodels.MoviesViewModel;
import com.yusufekremunlu.easyway.utils.Constants;
import com.yusufekremunlu.easyway.utils.Credentials;
import java.util.ArrayList;


public class MoviesFragment extends Fragment implements MoviesAdapter.OnItemClickListener {
    private MoviesViewModel moviesViewModel;
    private RecyclerView trendingRecycler;
    private RecyclerView popularRecycler;
    private RecyclerView upComingRecycler;
    private MoviesAdapter moviesTrending;
    private MoviesAdapter moviesPopular;
    private MoviesAdapter moviesUpComing;
    MovieApiClient movieApiClient = MovieApiClient.getInstance();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        moviesViewModel = new ViewModelProvider(this).get(MoviesViewModel.class);
        moviesTrending = new MoviesAdapter(new ArrayList<>(), getContext());
        moviesPopular = new MoviesAdapter(new ArrayList<>(), getContext());
        moviesUpComing = new MoviesAdapter(new ArrayList<>(), getContext());

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        moviesTrending.setOnItemClickListener(this);
        moviesPopular.setOnItemClickListener(this);
        moviesUpComing.setOnItemClickListener(this);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_movies, container, false);

        trendingRecycler = view.findViewById(R.id.trendingRecyclerView);
        popularRecycler = view.findViewById(R.id.popularRecyclerView);
        upComingRecycler = view.findViewById(R.id.upcomingRecyclerView);

        TextView showAllTrendingText = view.findViewById(R.id.showAllTrendingText);
        TextView showAllPopularText = view.findViewById(R.id.showAllPopularText);
        TextView showAllUpcomingText = view.findViewById(R.id.showAllUpcomingText);

        showAllTrendingText.setOnClickListener(v -> {
            Navigation.findNavController(requireView()).navigate(R.id.action_fragment_movies_to_showAllFragment);
        });

        showAllPopularText.setOnClickListener(v -> {
            Navigation.findNavController(requireView()).navigate(R.id.action_fragment_movies_to_showAllFragment);
        });

        showAllUpcomingText.setOnClickListener(v -> {
            Navigation.findNavController(requireView()).navigate(R.id.action_fragment_movies_to_showAllFragment);
        });




        LinearLayoutManager trendingLayoutManager = new LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false);
        trendingRecycler.setLayoutManager(trendingLayoutManager);
        LinearLayoutManager popularLayoutManager = new LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false);
        popularRecycler.setLayoutManager(popularLayoutManager);
        LinearLayoutManager upComingLayoutManager = new LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false);
        upComingRecycler.setLayoutManager(upComingLayoutManager);
        trendingRecycler.setAdapter(moviesTrending);
        popularRecycler.setAdapter(moviesPopular);
        upComingRecycler.setAdapter(moviesUpComing);

        loadNextPages();
        observeData();
        return view;
    }

    private void observeData() {
        moviesViewModel.getTrendingMovies().observe(getViewLifecycleOwner(), movieModels -> {
            moviesTrending.setMovieList(movieModels);
            if (movieModels.size() > 0) {
                MovieModel firstMovie = movieModels.get(0);
                updateUI(firstMovie);
                goToMovieDetails(firstMovie);
            }
        });

        moviesViewModel.getPopularMovies().observe(getViewLifecycleOwner(), movieModels -> {
            moviesPopular.setMovieList(movieModels);
        });

        moviesViewModel.getUpComingMovies().observe(getViewLifecycleOwner(), movieModels -> {
            moviesUpComing.setMovieList(movieModels);
        });
    }

    private void loadNextPages() {
        trendingRecycler.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if (!recyclerView.canScrollHorizontally(1)) {
                    movieApiClient.loadMoreTrendingMovies();
                }
            }
        });

        popularRecycler.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if (!recyclerView.canScrollHorizontally(1)) {
                    movieApiClient.loadMorePopularMovies();
                }
            }
        });
        upComingRecycler.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if (!recyclerView.canScrollHorizontally(1)) {
                    movieApiClient.loadMoreUpComingMovies();
                }
            }
        });
    }

    @Override
    public void onItemClick(MovieModel movie) {
        ImageView hlImageView = requireView().findViewById(R.id.hlMovieImage);
        TextView hlTitleView = requireView().findViewById(R.id.hlMovieTitle);
        RatingBar hlRatingBar = requireView().findViewById(R.id.hlRatingBar);
        TextView hlNumOfVotes = requireView().findViewById(R.id.hlNumOfVotes);
        TextView hlGenreOne = requireView().findViewById(R.id.hlMovieGenrePrimary);
        TextView hlGenreSecondOne = requireView().findViewById(R.id.hlMovieGenreSecondary);
        Glide.with(requireContext())
                .load(Credentials.MOVIE_BACKDROP_URL +movie.getBackdrop_path())
                .into(hlImageView);
        hlTitleView.setText(movie.getTitle());
        hlRatingBar.setRating(movie.getVote_average()/2);
        hlNumOfVotes.setText(String.valueOf(movie.getVote_count()));
        String genreOne = Constants.getGenre(movie.getGenre_ids().get(0));
        String genreSecondOne = Constants.getGenre(movie.getGenre_ids().get(1));
        hlGenreOne.setText(genreOne);
        hlGenreSecondOne.setText(genreSecondOne);
        goToMovieDetails(movie);
    }
    private void updateUI(MovieModel movie) {
        ImageView hlImageView = requireView().findViewById(R.id.hlMovieImage);
        TextView hlTitleView = requireView().findViewById(R.id.hlMovieTitle);
        RatingBar hlRatingBar = requireView().findViewById(R.id.hlRatingBar);
        TextView hlNumOfVotes = requireView().findViewById(R.id.hlNumOfVotes);
        TextView hlGenreOne = requireView().findViewById(R.id.hlMovieGenrePrimary);
        TextView hlGenreSecondOne = requireView().findViewById(R.id.hlMovieGenreSecondary);

        Glide.with(this)
                .load(Credentials.MOVIE_BACKDROP_URL + movie.getBackdrop_path())
                .into(hlImageView);
        hlTitleView.setText(movie.getTitle());
        hlRatingBar.setRating(movie.getVote_average() / 2);
        hlNumOfVotes.setText(String.valueOf(movie.getVote_count()+" votes"));
        String genreOne = Constants.getGenre(movie.getGenre_ids().get(0));
        String genreSecondOne = Constants.getGenre(movie.getGenre_ids().get(1));
        hlGenreOne.setText(genreOne);
        hlGenreSecondOne.setText(genreSecondOne);
    }

    private void goToMovieDetails(MovieModel movie) {
        ImageView hlImageView = requireView().findViewById(R.id.hlMovieImage);
        hlImageView.setOnClickListener(v -> {
            Bundle bundle = new Bundle();
            bundle.putParcelable("movie", movie);
            Navigation.findNavController(requireView())
                    .navigate(R.id.action_fragment_movies_to_movieDetailsFragment, bundle);
        });
    }
}



