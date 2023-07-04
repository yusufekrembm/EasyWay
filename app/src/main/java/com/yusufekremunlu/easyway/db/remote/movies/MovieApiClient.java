package com.yusufekremunlu.easyway.db.remote.movies;

import androidx.lifecycle.MutableLiveData;
import com.yusufekremunlu.easyway.model.entity.movies.MovieModel;
import com.yusufekremunlu.easyway.model.network.movies.MovieResponse;
import com.yusufekremunlu.easyway.utils.Credentials;
import com.yusufekremunlu.easyway.utils.builders.MovieRetrofitBuilder;
import java.util.List;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MovieApiClient {
    private final MutableLiveData<List<MovieModel>> mTrendingMovies;
    private final MutableLiveData<List<MovieModel>> mPopularMovies;
    private final MutableLiveData<List<MovieModel>> mUpComingMovies;
    private static MovieApiClient instance;
    private static final MovieApiInterface movieApiInterface = MovieRetrofitBuilder.getMovieApiInterface();
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
}



