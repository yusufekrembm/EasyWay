package com.yusufekremunlu.easyway.ui.main.movies;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import com.yusufekremunlu.easyway.R;
import com.yusufekremunlu.easyway.db.remote.movies.MovieApiClient;
import com.yusufekremunlu.easyway.db.remote.movies.MovieApiService;
import com.yusufekremunlu.easyway.model.entity.movie.Movies;
import com.yusufekremunlu.easyway.model.network.movies.MoviesResponse;
import com.yusufekremunlu.easyway.utils.Credentials;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MoviesFragment extends Fragment {



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_movies, container, false);
        ImageView imageView = view.findViewById(R.id.hlMovieImage);
        imageView.setOnClickListener(v -> {
            MovieApiService movieApiService = MovieApiClient.getClient().create(MovieApiService.class);
            Call<MoviesResponse> call = movieApiService.fetchPopularList(1, Credentials.MOVIE_API_KEY);
            call.enqueue(new Callback<MoviesResponse>() {
                @Override
                public void onResponse(@NonNull Call<MoviesResponse> call, Response<MoviesResponse> response) {
                    MoviesResponse moviesResponse = response.body();
                    if (moviesResponse != null) {
                        List<Movies> movies = moviesResponse.getMovies();
                        for (Movies movie : movies) {
                            Log.v("Tag", "Title " + movie.getTitle());
                        }
                    }
                }

                @Override
                public void onFailure(@NonNull Call<MoviesResponse> call, Throwable t) {
                    // Handle failure
                }
            });
        });
        return view;
    }
}
