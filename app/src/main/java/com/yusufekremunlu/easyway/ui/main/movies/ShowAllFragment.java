package com.yusufekremunlu.easyway.ui.main.movies;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.icu.util.Calendar;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.yusufekremunlu.easyway.R;
import com.yusufekremunlu.easyway.databinding.FragmentShowAllBinding;
import com.yusufekremunlu.easyway.db.remote.movies.MovieApiClient;
import com.yusufekremunlu.easyway.model.entity.movies.MovieModel;
import com.yusufekremunlu.easyway.ui.main.movies.adapters.ShowAllAdapter;
import com.yusufekremunlu.easyway.ui.main.movies.viewmodels.MoviesViewModel;
import com.yusufekremunlu.easyway.utils.Constants;
import com.yusufekremunlu.easyway.utils.MovieListType;

import java.util.ArrayList;
import java.util.List;

public class ShowAllFragment extends Fragment implements ShowAllAdapter.OnItemClickListener {
    private MoviesViewModel moviesViewModel;
    private RecyclerView showAllRecyclerView;
    private List<MovieModel> discoverListFull = new ArrayList<>();
    private ShowAllAdapter showAllAdapter;
    private ProgressBar showAllProgressBar;
    private List<MovieModel> originalDiscoverListFull = new ArrayList<>();


    @SuppressLint("ResourceType")
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        moviesViewModel = new ViewModelProvider(this).get(MoviesViewModel.class);
        showAllAdapter = new ShowAllAdapter(new ArrayList<>(), getContext());
        showAllAdapter.setOnItemClickListener(this);
        setHasOptionsMenu(true);
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
        loadMoreResults();
        Toolbar myToolbar = binding.myToolbar;
        AppCompatActivity activity = (AppCompatActivity) getActivity();
        assert activity != null;
        activity.setSupportActionBar(myToolbar);
        Bundle bundle = getArguments();
        if (bundle != null && bundle.containsKey("movieListType")) {
            MovieListType movieListType = (MovieListType) bundle.getSerializable("movieListType");
            observeDataForListType(movieListType);
        }
        return rootView;
    }
    private void observeDataForListType(MovieListType movieListType) {
        switch (movieListType) {
            case TRENDING:
                moviesViewModel.getTrendingMovies().observe(getViewLifecycleOwner(), movieModels -> {
                    showAllAdapter.setDiscoverList(movieModels);
                });
                break;
            case POPULAR:
                moviesViewModel.getPopularMovies().observe(getViewLifecycleOwner(), movieModels -> {
                    showAllAdapter.setDiscoverList(movieModels);
                });
                break;
            case UPCOMING:
                moviesViewModel.getUpComingMovies().observe(getViewLifecycleOwner(), movieModels -> {
                    showAllAdapter.setDiscoverList(movieModels);
                });
                break;
            default:
                break;
        }
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.movie_filters, menu);
        MenuItem searchItem = menu.findItem(R.id.searchMovie);
        MenuItem filterItem = menu.findItem(R.id.filterMovie);
        SearchView searchView = (SearchView) searchItem.getActionView();
        filterItem.setOnMenuItemClickListener(item -> {
            showFilterPopup();
            return false;
        });

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                moviesViewModel.searchMovieApi(query, 1);
                boolean noResults = true;
                for (MovieModel movie : discoverListFull) {
                    if (movie.getTitle().toLowerCase().contains(query.toLowerCase())) {
                        noResults = false;
                        break;
                    }
                }

                TextView noResultsTextView = requireView().findViewById(R.id.noResultsTextView);
                if (noResults) {
                    noResultsTextView.setVisibility(View.VISIBLE);
                } else {
                    noResultsTextView.setVisibility(View.GONE);
                }

                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                if (newText.isEmpty()) {
                    resetListToOriginal();
                } else {
                    moviesViewModel.searchMovieApi(newText, 1);
                }
                return false;
            }
        });

        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int itemId = item.getItemId();
        if (itemId == R.id.searchMovie) {
            return true;
        } else if (itemId == R.id.filterMovie) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void loadMoreResults(){
        showAllRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if(!showAllRecyclerView.canScrollVertically(1)){
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

    private void showFilterPopup() {
        Dialog dialog = new Dialog(requireContext());
        dialog.setContentView(R.layout.movie_popup_layout);
        EditText startDateEditText = dialog.findViewById(R.id.start_date_edittext);
        EditText endDateEditText = dialog.findViewById(R.id.end_date_edittext);
        startDateEditText.setOnClickListener(v -> showDatePickerDialog(startDateEditText));
        endDateEditText.setOnClickListener(v -> showDatePickerDialog(endDateEditText));
        SeekBar imdbRatingSeekBar = dialog.findViewById(R.id.imdb_rating_seekbar);
        TextView imdbRangeText = dialog.findViewById(R.id.imdb_range_text);
        Button filterButton = dialog.findViewById(R.id.filter_button);
        Button resetButton = dialog.findViewById(R.id.reset_filter);
        Spinner categorySpinner = dialog.findViewById(R.id.category_spinner);
        Spinner categoryMovieSpinner = dialog.findViewById(R.id.movie_category_spinner);
        String[] categories = getResources().getStringArray(R.array.film_categories);
        String[] categoriesMovies = getResources().getStringArray(R.array.movies_categories);

        imdbRatingSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                int selectedRating = progress + 1;
                imdbRangeText.setText("IMDb Range: " + selectedRating + " - " + (selectedRating + 1));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
            }
        });


        filterButton.setOnClickListener(v -> {
            String selectedCategory = categorySpinner.getSelectedItem().toString();

            for (String category : categories) {
                if (selectedCategory.equals(category)) {
                    filterMovies(selectedCategory);
                    break;
                }
            }
            String selectedMovieCategory = categoryMovieSpinner.getSelectedItem().toString();

            for (String movieCat : categoriesMovies) {
                if (selectedMovieCategory.equals(movieCat)) {
                    filterMoviesByCategory(selectedMovieCategory);
                    break;
                }
            }
            dialog.dismiss();
        });

        resetButton.setOnClickListener(v -> {
            dialog.dismiss();
            resetListToOriginal();
        });
        dialog.show();
    }

    private void showDatePickerDialog(final EditText editText) {
        Calendar calendar = null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
            calendar = Calendar.getInstance();
        }
        int year = 0;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
            year = calendar.get(Calendar.YEAR);
        }
        int month = 0;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
            month = calendar.get(Calendar.MONTH);
        }
        int day = 0;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
            day = calendar.get(Calendar.DAY_OF_MONTH);
        }

        DatePickerDialog datePickerDialog = new DatePickerDialog(requireContext(),
                (view, year1, month1, dayOfMonth) -> {
                    String selectedDate = (month1 + 1) + "/" + dayOfMonth + "/" + year1;
                    editText.setText(selectedDate);
                }, year, month, day);

        datePickerDialog.show();
    }

    @SuppressLint("NotifyDataSetChanged")
    private void resetListToOriginal() {
        discoverListFull.clear();
        discoverListFull.addAll(originalDiscoverListFull);
        showAllAdapter.setDiscoverList(discoverListFull);
        showAllAdapter.notifyDataSetChanged();
        showAllRecyclerView.smoothScrollToPosition(0);
    }

    private void filterMovies(String selectedCategory) {
        List<MovieModel> filteredMovies = new ArrayList<>();

        switch (selectedCategory) {
            case "Popular":
                filteredMovies = moviesViewModel.getPopularMovies().getValue();
                break;
            case "Trending":
                filteredMovies = moviesViewModel.getTrendingMovies().getValue();
                break;
            case "Incoming":
                filteredMovies = moviesViewModel.getUpComingMovies().getValue();
                break;
        }
        showAllAdapter.setDiscoverList(filteredMovies);
    }
    private void filterMoviesByCategory(String selectedCategory) {
        List<MovieModel> allMovies = moviesViewModel.getDiscoverMovies().getValue();
        List<MovieModel> filteredMovies = new ArrayList<>();

        assert allMovies != null;
        for (MovieModel movie : allMovies) {
            List<Integer> genreIds = movie.getGenre_ids();
            if (genreIds != null) {
                for (Integer genreId : genreIds) {
                    String genre = Constants.getGenre(genreId);
                    if (genre != null && genre.equalsIgnoreCase(selectedCategory)) {
                        filteredMovies.add(movie);
                        break;
                    }
                }
            }
        }

        showAllAdapter.setDiscoverList(filteredMovies);
    }

}

