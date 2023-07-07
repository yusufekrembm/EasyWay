package com.yusufekremunlu.easyway.db.remote.movies;

import android.os.Bundle;
import android.util.Log;

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
import com.yusufekremunlu.easyway.utils.builders.MovieRetrofitBuilder;
import java.util.List;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MovieApiClient {
    private static MovieApiClient instance;
    private static final MovieApiInterface movieApiInterface = MovieRetrofitBuilder.getMovieApiInterface();
    private final MutableLiveData<List<MovieModel>> mTrendingMovies;
    private final MutableLiveData<List<MovieModel>> mPopularMovies;
    private final MutableLiveData<List<MovieModel>> mUpComingMovies;
    private final MutableLiveData<List<MovieCastModel>> mCastModelMovies;
    private final MutableLiveData<List<MovieVideoModel>> mVideoModelMovies;
    private final MutableLiveData<List<MoviePersonImages>> mPersonImagesModelMovies;
    private final MutableLiveData<List<MoviePersonCredits>> mPersonCreditsModelMovies;

    private int currentPageTrending = 1;
    private int currentPagePopular = 1;
    private int currentPageUpComing = 1;

    private boolean isFetchingTrendingMovies = false;
    private boolean isFetchingPopularMovies = false;
    private boolean isFetchingUpComingMovies = false;

    private boolean isLastPageTrending = false;
    private boolean isLastPagePopular = false;
    private boolean isLastPageUpComing = false;

    public static MovieApiClient getInstance(){
        if(instance==null){
            instance = new MovieApiClient();
        }
        return instance;
    }

    private MovieApiClient(){
        mTrendingMovies = new MutableLiveData<>();
        mPopularMovies = new MutableLiveData<>();
        mUpComingMovies = new MutableLiveData<>();
        mCastModelMovies = new MutableLiveData<>();
        mVideoModelMovies = new MutableLiveData<>();
        mPersonImagesModelMovies = new MutableLiveData<>();
        mPersonCreditsModelMovies = new MutableLiveData<>();

        getTrendingMoviesFromApi();
        getPopularMoviesFromApi();
        getUpComingMoviesFromApi();
    }

    public MutableLiveData<List<MovieModel>> getTrendingMovies() {
        return mTrendingMovies;
    }

    public MutableLiveData<List<MovieModel>> getPopularMovies() {
        return mPopularMovies;
    }

    public MutableLiveData<List<MovieModel>> getUpComingMovies() {
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


    public void loadMoreTrendingMovies() {
        if (!isFetchingTrendingMovies && !isLastPageTrending) {
            currentPageTrending++;
            getTrendingMoviesFromApi();
        }
    }

    public void loadMorePopularMovies() {
        if (!isFetchingPopularMovies && !isLastPagePopular) {
            currentPagePopular++;
            getPopularMoviesFromApi();
        }
    }
    public void loadMoreUpComingMovies() {
        if (!isFetchingUpComingMovies && !isLastPageUpComing) {
            currentPageUpComing++;
            getUpComingMoviesFromApi();
        }
    }

    private void getTrendingMoviesFromApi() {
        isFetchingTrendingMovies = true;
        Call<MovieResponse> movieResponse = movieApiInterface.fetchTrendingMovies(currentPageTrending);
        movieResponse.enqueue(new Callback<MovieResponse>() {
            @Override
            public void onResponse(Call<MovieResponse> call, Response<MovieResponse> response) {
                if (response.isSuccessful()) {
                    MovieResponse movieResponse = response.body();
                    if (movieResponse != null) {
                        List<MovieModel> movies = movieResponse.getMovies();
                        if (currentPageTrending == 1) {
                            mTrendingMovies.setValue(movies);
                        } else {
                            List<MovieModel> currentMovies = mTrendingMovies.getValue();
                            if (currentMovies != null) {
                                currentMovies.addAll(movies);
                                mTrendingMovies.setValue(currentMovies);
                            }
                        }
                        isLastPageTrending = currentPageTrending >= movieResponse.getTotal_pages();
                    }
                }
                isFetchingTrendingMovies = false;
            }

            @Override
            public void onFailure(Call<MovieResponse> call, Throwable t) {
                isFetchingTrendingMovies = false;
            }
        });
    }

    private void getPopularMoviesFromApi() {
        isFetchingPopularMovies = true;
        Call<MovieResponse> movieResponse = movieApiInterface.fetchPopularMovies(currentPagePopular);
        movieResponse.enqueue(new Callback<MovieResponse>() {
            @Override
            public void onResponse(Call<MovieResponse> call, Response<MovieResponse> response) {
                if (response.isSuccessful()) {
                    MovieResponse movieResponse = response.body();
                    if (movieResponse != null) {
                        List<MovieModel> movies = movieResponse.getMovies();
                        if (currentPagePopular == 1) {
                            mPopularMovies.setValue(movies);
                        } else {
                            List<MovieModel> currentMovies = mPopularMovies.getValue();
                            if (currentMovies != null) {
                                currentMovies.addAll(movies);
                                mPopularMovies.setValue(currentMovies);
                            }
                        }
                        isLastPagePopular = currentPagePopular >= movieResponse.getTotal_pages();
                    }
                }
                isFetchingPopularMovies = false;
            }

            @Override
            public void onFailure(Call<MovieResponse> call, Throwable t) {
                isFetchingPopularMovies = false;
            }
        });
    }

    private void getUpComingMoviesFromApi() {
        isFetchingUpComingMovies = true;
        Call<MovieResponse> movieResponse = movieApiInterface.fetchUpComingMovies(currentPageUpComing);
        movieResponse.enqueue(new Callback<MovieResponse>() {
            @Override
            public void onResponse(Call<MovieResponse> call, Response<MovieResponse> response) {
                if (response.isSuccessful()) {
                    MovieResponse movieResponse = response.body();
                    if (movieResponse != null) {
                        List<MovieModel> movies = movieResponse.getMovies();
                        if (currentPageUpComing == 1) {
                            mUpComingMovies.setValue(movies);
                        } else {
                            List<MovieModel> currentMovies = mUpComingMovies.getValue();
                            if (currentMovies != null) {
                                currentMovies.addAll(movies);
                                mUpComingMovies.setValue(currentMovies);
                            }
                        }
                        isLastPageUpComing = currentPageUpComing >= movieResponse.getTotal_pages();
                    }
                }
                isFetchingUpComingMovies = false;
            }

            @Override
            public void onFailure(Call<MovieResponse> call, Throwable t) {
                isFetchingUpComingMovies = false;
            }
        });
    }

    public void getMovieCastModelFromApi(int movie_id) {
        Call<CreditsResponse> movieCasts = movieApiInterface.fetchMovieCasts(movie_id);
        movieCasts.enqueue(new Callback<CreditsResponse>() {
            @Override
            public void onResponse(Call<CreditsResponse> call, Response<CreditsResponse> response) {
                if (response.isSuccessful()) {
                    CreditsResponse movieCastResponse = response.body();
                    if(movieCastResponse!=null){
                        List<MovieCastModel> movieCastModels = movieCastResponse.getCasts();
                        mCastModelMovies.setValue(movieCastModels);
                    }
                }
            }
            @Override
            public void onFailure(Call<CreditsResponse> call, Throwable t) {
            }
        });
    }
    public void getMovieVideosFromApi(int movie_id) {
        Call<VideosResponse> movieVideos = movieApiInterface.fetchMovieVideos(movie_id);
        movieVideos.enqueue(new Callback<VideosResponse>() {
            @Override
            public void onResponse(Call<VideosResponse> call, Response<VideosResponse> response) {
                if (response.isSuccessful()) {
                    VideosResponse movieVideosResponse = response.body();
                    if(movieVideosResponse!=null){
                        List<MovieVideoModel> movieVideoModels = movieVideosResponse.getVideos();
                        mVideoModelMovies.setValue(movieVideoModels);
                    }
                }
            }
            @Override
            public void onFailure(Call<VideosResponse> call, Throwable t) {
            }
        });
    }
    public void getMoviesPersonsFromApi(int personId, final MovieApiCallback callback) {
        Call<MoviePerson> responseCall = movieApiInterface.fetchPersonDetails(personId);
        responseCall.enqueue(new Callback<MoviePerson>() {
            @Override
            public void onResponse(Call<MoviePerson> call, Response<MoviePerson> response) {
                if (response.isSuccessful()) {
                    MoviePerson moviePerson = response.body();
                    callback.onSuccess(moviePerson);
                } else {
                    callback.onFailure(new Throwable("Response unsuccessful"));
                }
            }

            @Override
            public void onFailure(Call<MoviePerson> call, Throwable t) {
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
            public void onResponse(Call<MoviePersonImagesResponse> call, Response<MoviePersonImagesResponse> response) {
                if (response.isSuccessful()) {
                    MoviePersonImagesResponse moviePersonImagesResponse = response.body();
                    if(moviePersonImagesResponse!=null){
                        List<MoviePersonImages> moviePersonImagesModels = moviePersonImagesResponse.getImages();
                        mPersonImagesModelMovies.setValue(moviePersonImagesModels);
                    }
                }
            }
            @Override
            public void onFailure(Call<MoviePersonImagesResponse> call, Throwable t) {
            }
        });
    }

    public void getMoviePersonCreditsFromApi(int person_id) {
        Call<MoviePersonCreditsResponse> moviePersonCreditsResponseCall = movieApiInterface.fetchPersonCredits(person_id);
        moviePersonCreditsResponseCall.enqueue(new Callback<MoviePersonCreditsResponse>() {
            @Override
            public void onResponse(Call<MoviePersonCreditsResponse> call, Response<MoviePersonCreditsResponse> response) {
                if (response.isSuccessful()) {
                    MoviePersonCreditsResponse moviePersonCreditsResponse = response.body();
                    if(moviePersonCreditsResponse!=null){
                        List<MoviePersonCredits> moviePersonImagesModels = moviePersonCreditsResponse.getCredits();
                        mPersonCreditsModelMovies.setValue(moviePersonImagesModels);
                    }
                }
            }
            @Override
            public void onFailure(Call<MoviePersonCreditsResponse> call, Throwable t) {
            }
        });
    }
}



