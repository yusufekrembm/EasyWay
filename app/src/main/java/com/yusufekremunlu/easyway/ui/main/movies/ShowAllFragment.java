package com.yusufekremunlu.easyway.ui.main.movies;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.MenuHost;
import androidx.core.view.MenuProvider;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.yusufekremunlu.easyway.R;
import com.yusufekremunlu.easyway.databinding.FragmentShowAllBinding;
import com.yusufekremunlu.easyway.model.entity.movies.MovieModel;
import com.yusufekremunlu.easyway.ui.main.movies.adapters.ShowAllAdapter;
import com.yusufekremunlu.easyway.ui.main.movies.viewmodels.MoviesViewModel;
import com.yusufekremunlu.easyway.utils.MovieListType;
import java.util.ArrayList;
import java.util.List;

public class ShowAllFragment extends Fragment implements ShowAllAdapter.OnItemClickListener {
    private MoviesViewModel moviesViewModel;
    private RecyclerView showAllRecyclerView;
    private ShowAllAdapter showAllAdapter;
    private ProgressBar showAllProgressBar;
    private MovieListType currentListType;

    @SuppressLint("ResourceType")
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        moviesViewModel = new ViewModelProvider(this).get(MoviesViewModel.class);
        showAllAdapter = new ShowAllAdapter(new ArrayList<>(), getContext());
        showAllAdapter.setOnItemClickListener(this);
    }


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        com.yusufekremunlu.easyway.databinding.FragmentShowAllBinding binding = FragmentShowAllBinding.inflate(inflater, container, false);
        View rootView = binding.getRoot();
        showAllRecyclerView = rootView.findViewById(R.id.showAllRecyclerView);
        showAllProgressBar = rootView.findViewById(R.id.moviesProgressBar);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(requireContext(), 3);
        showAllRecyclerView.setLayoutManager(gridLayoutManager);
        showAllRecyclerView.setAdapter(showAllAdapter);
        showAllProgressBar.setVisibility(View.VISIBLE);
        Toolbar myToolbar = binding.myToolbar;
        AppCompatActivity activity = (AppCompatActivity) getActivity();
        assert activity != null;
        activity.setSupportActionBar(myToolbar);
        getCurrentListType();
        return rootView;
    }

    private MovieListType getCurrentListType() {
        Bundle bundle = getArguments();
        if (bundle != null && bundle.containsKey("movieListType")) {
            MovieListType movieListType = (MovieListType) bundle.getSerializable("movieListType");
            observeDataForListType(movieListType);
        }
        return MovieListType.TRENDING;
    }


    private void observeDataForListType(MovieListType movieListType) {
        currentListType = movieListType;

        switch (movieListType) {
            case TRENDING:
                moviesViewModel.getTrendingMovies().observe(requireParentFragment().getViewLifecycleOwner(), movieModels -> {
                    showAllAdapter.setDiscoverList(movieModels);
                    showAllProgressBar.setVisibility(View.INVISIBLE);
                });
                break;
            case POPULAR:
                moviesViewModel.getPopularMovies().observe(requireParentFragment().getViewLifecycleOwner(), movieModels -> {
                    showAllAdapter.setDiscoverList(movieModels);
                    showAllProgressBar.setVisibility(View.INVISIBLE);
                });
                break;
            case UPCOMING:
                moviesViewModel.getUpComingMovies().observe(requireParentFragment().getViewLifecycleOwner(), movieModels -> {
                    showAllAdapter.setDiscoverList(movieModels);
                    showAllProgressBar.setVisibility(View.INVISIBLE);
                });
                break;
            default:
                break;
        }
        showAllRecyclerView.removeOnScrollListener(scrollListener);
        showAllRecyclerView.addOnScrollListener(scrollListener);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        MenuHost menuHost = requireActivity();
        menuHost.addMenuProvider(new MenuProvider() {
            @Override
            public void onCreateMenu(@NonNull Menu menu, @NonNull MenuInflater menuInflater) {
                menuInflater.inflate(R.menu.movie_filters, menu);
                MenuItem searchItem = menu.findItem(R.id.searchMovie);
                SearchView searchView = (SearchView) searchItem.getActionView();
                searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {

                    @Override
                    public boolean onQueryTextSubmit(String query) {
                        moviesViewModel.searchMovieApi(query, 1);
                        loadMoreSearchList();
                        showAllRecyclerView.removeOnScrollListener(scrollListener);
                        return false;
                    }

                    @Override
                    public boolean onQueryTextChange(String newText) {
                        MovieListType listType = getCurrentListType();
                        if (newText.isEmpty()) {
                            showAllRecyclerView.addOnScrollListener(scrollListener);
                            List<MovieModel> movieList;
                            switch (listType) {
                                case POPULAR:
                                    movieList = moviesViewModel.getPopularMovies().getValue();
                                    break;
                                case TRENDING:
                                    movieList = moviesViewModel.getTrendingMovies().getValue();
                                    break;
                                case UPCOMING:
                                    movieList = moviesViewModel.getUpComingMovies().getValue();
                                    break;
                                default:
                                    movieList = new ArrayList<>();
                                    break;
                            }
                            showAllAdapter.setDiscoverList(movieList);
                            showAllRecyclerView.removeOnScrollListener(scrollListener);
                        }
                        return false;
                    }
                });
            }

            @Override
            public boolean onMenuItemSelected(@NonNull MenuItem menuItem) {
                return menuItem.getItemId() == R.id.searchMovie;
            }
        }, getViewLifecycleOwner(), Lifecycle.State.RESUMED);
    }

    private void loadMoreSearchList() {
        showAllRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if (!showAllRecyclerView.canScrollVertically(1)) {
                    moviesViewModel.searchNextPage();
                }
            }
        });
    }


    @Override
    public void onItemClick(MovieModel movie) {
        Bundle bundle = new Bundle();
        bundle.putParcelable("movie", movie);
        Navigation.findNavController(requireView())
                .navigate(R.id.action_showAllFragment_to_movieDetailsFragment, bundle);
    }

    private final RecyclerView.OnScrollListener scrollListener = new RecyclerView.OnScrollListener() {
        @Override
        public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
            super.onScrollStateChanged(recyclerView, newState);
            if (!recyclerView.canScrollVertically(1)) {
                if (currentListType == MovieListType.TRENDING) {
                    moviesViewModel.searchNextPageTrending();
                } else if (currentListType == MovieListType.POPULAR) {
                    moviesViewModel.searchNextPagePopular();
                } else if (currentListType == MovieListType.UPCOMING) {
                    moviesViewModel.searchNextPageUnComing();
                }
            }
        }
    };
}

