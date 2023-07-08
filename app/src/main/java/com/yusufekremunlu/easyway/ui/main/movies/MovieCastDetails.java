package com.yusufekremunlu.easyway.ui.main.movies;

import android.os.Bundle;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import com.bumptech.glide.Glide;
import com.yusufekremunlu.easyway.R;
import com.yusufekremunlu.easyway.db.remote.movies.MovieApiClient;
import com.yusufekremunlu.easyway.model.entity.movies.MovieCastModel;
import com.yusufekremunlu.easyway.model.entity.movies.MovieModel;
import com.yusufekremunlu.easyway.model.entity.movies.MoviePerson;
import com.yusufekremunlu.easyway.model.entity.movies.MoviePersonCredits;
import com.yusufekremunlu.easyway.model.entity.movies.MoviePersonImages;
import com.yusufekremunlu.easyway.ui.main.movies.adapters.MovieCastAdapter;
import com.yusufekremunlu.easyway.ui.main.movies.adapters.MoviePersonCreditsAdapter;
import com.yusufekremunlu.easyway.ui.main.movies.adapters.MovieVideoAdapter;
import com.yusufekremunlu.easyway.ui.main.movies.adapters.MoviesPersonImagesAdapter;
import com.yusufekremunlu.easyway.ui.main.movies.viewmodels.MovieCastViewModel;
import com.yusufekremunlu.easyway.utils.Credentials;

import java.util.ArrayList;

public class MovieCastDetails extends Fragment implements MoviesPersonImagesAdapter.OnItemClickListener,MoviePersonCreditsAdapter.OnItemClickListener {
    private MoviePerson moviePerson;
    MovieCastViewModel movieCastViewModel;
    private MoviesPersonImagesAdapter moviesPersonImagesAdapter;
    private MoviePersonCreditsAdapter moviePersonCreditsAdapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            moviePerson = getArguments().getParcelable("moviePerson");
        }
        movieCastViewModel = new ViewModelProvider(this).get(MovieCastViewModel.class);
        moviesPersonImagesAdapter = new MoviesPersonImagesAdapter(new ArrayList<>(), getContext());
        moviePersonCreditsAdapter = new MoviePersonCreditsAdapter(new ArrayList<>(), getContext());
        moviesPersonImagesAdapter.setOnItemClickListener(this);
        moviePersonCreditsAdapter.setOnItemClickListener(this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_movie_cast_details, container, false);
        TextView personNameText = view.findViewById(R.id.moviePersonNameText);
        TextView personBiographyText = view.findViewById(R.id.biographyText);
        TextView known_for_department = view.findViewById(R.id.known_for_department);
        ImageView moviePersonImage = view.findViewById(R.id.moviePersonImage);
        if (moviePerson != null) {
            personNameText.setText(moviePerson.getName());
            personBiographyText.setText(moviePerson.getBiography());
            known_for_department.setText(moviePerson.getKnown_for_department());
            Glide.with(this)
                    .load(Credentials.MOVIE_PROFILE_URL + moviePerson.getProfile_path())
                    .into(moviePersonImage);
        }





        Bundle args = getArguments();
        MoviePerson moviePerson = args.getParcelable("moviePerson");
        int personId = moviePerson.getId();
        MovieApiClient.getInstance().getMoviePersonImages(personId);
        RecyclerView moviePhotosRecyclerView = view.findViewById(R.id.photosRecyclerView);
        LinearLayoutManager layoutCastPhotos = new LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false);
        moviePhotosRecyclerView.setLayoutManager(layoutCastPhotos);
        moviePhotosRecyclerView.setAdapter(moviesPersonImagesAdapter);

        MovieApiClient.getInstance().getMoviePersonCreditsFromApi(personId);
        RecyclerView movieKnowAsRecyclerView = view.findViewById(R.id.knownForRecyclerView);
        LinearLayoutManager layoutCastKnowFor = new LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false);
        movieKnowAsRecyclerView.setLayoutManager(layoutCastKnowFor);
        movieKnowAsRecyclerView.setAdapter(moviePersonCreditsAdapter);

        observeData();
        return view;
    }

    private void observeData() {
        movieCastViewModel.getPersonImagesModelMovies().observe(getViewLifecycleOwner(), personImagesList -> {
            moviesPersonImagesAdapter.setImagesList(personImagesList);
        });
        movieCastViewModel.getPersonCreditsModelMovies().observe(getViewLifecycleOwner(), personCreditsList -> {
            moviePersonCreditsAdapter.setCreditList(personCreditsList);
        });
    }

    @Override
    public void onItemClick(MoviePersonImages personImagesModel) {
        AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
        LayoutInflater inflater = LayoutInflater.from(requireContext());
        View dialogView = inflater.inflate(R.layout.dialog_image, null);
        ImageView expandedImageView = dialogView.findViewById(R.id.expandedImageView);
        Glide.with(requireContext())
                .load(Credentials.MOVIE_BACKDROP_URL + personImagesModel.getFile_path())
                .fitCenter()
                .into(expandedImageView);


        builder.setView(dialogView);
        AlertDialog dialog = builder.create();
        dialog.show();
    }



    @Override
    public void onItemClick(MoviePersonCredits moviePersonCredits) {
        AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
        LayoutInflater inflater = LayoutInflater.from(requireContext());
        View dialogView = inflater.inflate(R.layout.dialog_image, null);
        ImageView expandedImageView = dialogView.findViewById(R.id.expandedImageView);
        Glide.with(requireContext())
                .load(Credentials.MOVIE_BACKDROP_URL + moviePersonCredits.getPoster_path())
                .fitCenter()
                .into(expandedImageView);


        builder.setView(dialogView);
        AlertDialog dialog = builder.create();
        dialog.show();
    }
}
