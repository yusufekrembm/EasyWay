package com.yusufekremunlu.easyway.db.remote.movies;

import android.util.Log;
import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;
import com.yusufekremunlu.easyway.model.entity.movies.MovieCastModel;
import com.yusufekremunlu.easyway.model.entity.movies.MovieModel;
import com.yusufekremunlu.easyway.model.entity.movies.MoviePerson;
import com.yusufekremunlu.easyway.model.entity.movies.MoviePersonCredits;
import com.yusufekremunlu.easyway.model.entity.movies.MoviePersonImages;
import com.yusufekremunlu.easyway.model.entity.movies.MovieVideoModel;
import com.yusufekremunlu.easyway.model.network.movies.CreditsResponse;
import com.yusufekremunlu.easyway.model.network.movies.MoviePersonCreditsResponse;
import com.yusufekremunlu.easyway.model.network.movies.MoviePersonImagesResponse;
import com.yusufekremunlu.easyway.model.network.movies.MovieResponse;
import com.yusufekremunlu.easyway.model.network.movies.VideosResponse;
import com.yusufekremunlu.easyway.ui.AppExecutors;
import com.yusufekremunlu.easyway.utils.builders.MovieRetrofitBuilder;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MovieApiClient {
    static MovieApiInterface movieApiInterface = MovieRetrofitBuilder.buildService(MovieApiInterface.class);
    private static MovieApiClient instance;
    private final MutableLiveData<List<MovieModel>> mTrendingMovies;
    private final MutableLiveData<List<MovieModel>> mPopularMovies;
    private final MutableLiveData<List<MovieModel>> mUpComingMovies;
    private final MutableLiveData<List<MovieCastModel>> mCastModelMovies;
    private final MutableLiveData<List<MovieVideoModel>> mVideoModelMovies;
    private final MutableLiveData<List<MoviePersonImages>> mPersonImagesModelMovies;
    private final MutableLiveData<List<MoviePersonCredits>> mPersonCreditsModelMovies;
    private RetrieveMoviesRunnable retrieveMoviesRunnable;
    int currentPage = 1;

    public static MovieApiClient getInstance() {
        if (instance == null) {
            instance = new MovieApiClient();
        }
        return instance;
    }

    private MovieApiClient() {
        mTrendingMovies = new MutableLiveData<>();
        mPopularMovies = new MutableLiveData<>();
        mUpComingMovies = new MutableLiveData<>();
        mCastModelMovies = new MutableLiveData<>();
        mVideoModelMovies = new MutableLiveData<>();
        mPersonImagesModelMovies = new MutableLiveData<>();
        mPersonCreditsModelMovies = new MutableLiveData<>();
    }

    public MutableLiveData<List<MovieModel>> getTrendingMovies() {
        getTrendingMoviesFromApi(currentPage);
        return mTrendingMovies;
    }

    public MutableLiveData<List<MovieModel>> getPopularMovies() {
        getPopularMoviesFromApi(currentPage);
        return mPopularMovies;
    }

    public MutableLiveData<List<MovieModel>> getUpComingMovies() {
        getUpComingMoviesFromApi(currentPage);
        return mUpComingMovies;
    }

    public MutableLiveData<List<MovieCastModel>> getCastModelMovies() {
        return mCastModelMovies;
    }

    public MutableLiveData<List<MovieVideoModel>> getVideoModelMovies() {
        return mVideoModelMovies;
    }

    public MutableLiveData<List<MoviePersonImages>> getPersonImagesModelMovies() {
        return mPersonImagesModelMovies;
    }

    public MutableLiveData<List<MoviePersonCredits>> getPersonCreditsModelMovies() {
        return mPersonCreditsModelMovies;
    }

    private void getMoviesFromApi(Call<MovieResponse> call, MutableLiveData<List<MovieModel>> targetLiveData,
                                  int currentPage) {
        call.enqueue(new Callback<MovieResponse>() {
            @Override
            public void onResponse(@NonNull Call<MovieResponse> call, @NonNull Response<MovieResponse> response) {
                if (response.isSuccessful()) {
                    MovieResponse movieResponse = response.body();
                    if (movieResponse != null) {
                        List<MovieModel> movies = movieResponse.getMovies();
                        if (currentPage == 1) {
                            targetLiveData.postValue(movies);
                        } else {
                            List<MovieModel> currentMovies = targetLiveData.getValue();
                            if (currentMovies != null) {
                                currentMovies.addAll(movies);
                                targetLiveData.postValue(currentMovies);
                            }
                        }
                    }
                }
            }

            @Override
            public void onFailure(@NonNull Call<MovieResponse> call, @NonNull Throwable t) {
            }
        });
    }

    public void getTrendingMoviesFromApi(int currentPage) {
        Call<MovieResponse> movieResponseCall = movieApiInterface.fetchTrendingMovies(currentPage);
        getMoviesFromApi(movieResponseCall, mTrendingMovies, currentPage);
    }

    public void getPopularMoviesFromApi(int currentPage) {
        Call<MovieResponse> movieResponseCall = movieApiInterface.fetchPopularMovies(currentPage);
        getMoviesFromApi(movieResponseCall, mPopularMovies, currentPage);
    }

    public void getUpComingMoviesFromApi(int currentPage) {
        Call<MovieResponse> movieResponseCall = movieApiInterface.fetchUpComingMovies(currentPage);
        getMoviesFromApi(movieResponseCall, mUpComingMovies, currentPage);
    }

    public void getMovieCastModelFromApi(int movie_id) {
        Call<CreditsResponse> movieCasts = movieApiInterface.fetchMovieCasts(movie_id);
        movieCasts.enqueue(new Callback<CreditsResponse>() {
            @Override
            public void onResponse(@NonNull Call<CreditsResponse> call, @NonNull Response<CreditsResponse> response) {
                if (response.isSuccessful()) {
                    CreditsResponse movieCastResponse = response.body();
                    if (movieCastResponse != null) {
                        List<MovieCastModel> movieCastModels = movieCastResponse.getCasts();
                        mCastModelMovies.setValue(movieCastModels);
                    }
                }
            }

            @Override
            public void onFailure(@NonNull Call<CreditsResponse> call, @NonNull Throwable t) {
            }
        });
    }

    public void getMovieVideosFromApi(int movie_id) {
        Call<VideosResponse> movieVideos = movieApiInterface.fetchMovieVideos(movie_id);
        movieVideos.enqueue(new Callback<VideosResponse>() {
            @Override
            public void onResponse(@NonNull Call<VideosResponse> call, @NonNull Response<VideosResponse> response) {
                if (response.isSuccessful()) {
                    VideosResponse movieVideosResponse = response.body();
                    if (movieVideosResponse != null) {
                        List<MovieVideoModel> movieVideoModels = movieVideosResponse.getVideos();
                        mVideoModelMovies.setValue(movieVideoModels);
                    }
                }
            }

            @Override
            public void onFailure(@NonNull Call<VideosResponse> call, @NonNull Throwable t) {
            }
        });
    }

    public static void getMoviesPersonsFromApi(int personId, final MovieApiCallback callback) {
        Call<MoviePerson> responseCall = movieApiInterface.fetchPersonDetails(personId);
        responseCall.enqueue(new Callback<MoviePerson>() {
            @Override
            public void onResponse(@NonNull Call<MoviePerson> call, @NonNull Response<MoviePerson> response) {
                if (response.isSuccessful()) {
                    MoviePerson moviePerson = response.body();
                    callback.onSuccess(moviePerson);
                } else {
                    callback.onFailure(new Throwable("Response unsuccessful"));
                }
            }

            @Override
            public void onFailure(@NonNull Call<MoviePerson> call, @NonNull Throwable t) {
                callback.onFailure(t);
            }
        });
    }

    public interface MovieApiCallback {
        void onSuccess(MoviePerson moviePerson);

        void onFailure(Throwable throwable);
    }

    public void getMoviePersonImages(int person_id) {
        Call<MoviePersonImagesResponse> moviePersonImagesResponseCall = movieApiInterface.fetchPersonImages(person_id);
        moviePersonImagesResponseCall.enqueue(new Callback<MoviePersonImagesResponse>() {
            @Override
            public void onResponse(@NonNull Call<MoviePersonImagesResponse> call, @NonNull Response<MoviePersonImagesResponse> response) {
                if (response.isSuccessful()) {
                    MoviePersonImagesResponse moviePersonImagesResponse = response.body();
                    if (moviePersonImagesResponse != null) {
                        List<MoviePersonImages> moviePersonImagesModels = moviePersonImagesResponse.getImages();
                        mPersonImagesModelMovies.setValue(moviePersonImagesModels);
                    }
                }
            }

            @Override
            public void onFailure(@NonNull Call<MoviePersonImagesResponse> call, @NonNull Throwable t) {
            }
        });
    }

    public void getMoviePersonCreditsFromApi(int person_id) {
        Call<MoviePersonCreditsResponse> moviePersonCreditsResponseCall = movieApiInterface.fetchPersonCredits(person_id);
        moviePersonCreditsResponseCall.enqueue(new Callback<MoviePersonCreditsResponse>() {
            @Override
            public void onResponse(@NonNull Call<MoviePersonCreditsResponse> call, @NonNull Response<MoviePersonCreditsResponse> response) {
                if (response.isSuccessful()) {
                    MoviePersonCreditsResponse moviePersonCreditsResponse = response.body();
                    if (moviePersonCreditsResponse != null) {
                        List<MoviePersonCredits> moviePersonImagesModels = moviePersonCreditsResponse.getCredits();
                        mPersonCreditsModelMovies.setValue(moviePersonImagesModels);
                    }
                }
            }

            @Override
            public void onFailure(@NonNull Call<MoviePersonCreditsResponse> call, @NonNull Throwable t) {
            }
        });
    }

    public void searchMoviesApi(String query, int pageNumber) {
        if (retrieveMoviesRunnable != null) {
            retrieveMoviesRunnable = null;
        }
        retrieveMoviesRunnable = new RetrieveMoviesRunnable(query, pageNumber);


        final Future<?> myHandler = AppExecutors.getInstance().netWorkIO().submit(retrieveMoviesRunnable);

        AppExecutors.getInstance().netWorkIO().schedule(() -> {
            // Cancelling the retrofit call
            myHandler.cancel(true);
        }, 5000, TimeUnit.MILLISECONDS);

    }

    private class RetrieveMoviesRunnable implements Runnable {
        private final String query;
        private final int pageNumber;
        boolean cancelRequest;

        public RetrieveMoviesRunnable(String query, int pageNumber) {
            this.query = query;
            this.pageNumber = pageNumber;
            cancelRequest = false;
        }

        public void run() {
            try {
                Response<MovieResponse> response = getMovies(query, pageNumber).execute();
                if (cancelRequest) {
                    return;
                }
                if (response.code() == 200) {
                    assert response.body() != null;
                    List<MovieModel> listTrending = new ArrayList<>(response.body().getMovies());
                    List<MovieModel> listPopular = new ArrayList<>(response.body().getMovies());
                    List<MovieModel> listUpcoming = new ArrayList<>(response.body().getMovies());
                    if (pageNumber == 1) {
                        mTrendingMovies.postValue(listTrending);
                        mPopularMovies.postValue(listPopular);
                        mUpComingMovies.postValue(listUpcoming);
                    } else {
                        List<MovieModel> currentMoviesTrending = mTrendingMovies.getValue();
                        List<MovieModel> currentMoviesPopular = mPopularMovies.getValue();
                        List<MovieModel> currentMoviesUpcoming = mUpComingMovies.getValue();
                        assert currentMoviesTrending != null;
                        currentMoviesTrending.addAll(listTrending);
                        assert currentMoviesPopular != null;
                        currentMoviesPopular.addAll(listPopular);
                        assert currentMoviesUpcoming != null;
                        currentMoviesUpcoming.addAll(listUpcoming);
                        mTrendingMovies.postValue(currentMoviesTrending);
                        mPopularMovies.postValue(currentMoviesPopular);
                        mUpComingMovies.postValue(currentMoviesUpcoming);
                    }
                } else {
                    assert response.errorBody() != null;
                    String error = response.errorBody().string();
                    Log.v("Tag", "Error " + error);
                }
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }

        private Call<MovieResponse> getMovies(String query, int pageNumber) {
            return MovieRetrofitBuilder.buildService(MovieApiInterface.class).searchMovie(
                    query,
                    pageNumber
            );
        }
    }
}


