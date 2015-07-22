package net.kechol.udacity.android.popularmovies.utils;

import net.kechol.udacity.android.popularmovies.models.MovieResult;
import net.kechol.udacity.android.popularmovies.models.ReviewResult;
import net.kechol.udacity.android.popularmovies.models.VideoResult;

import retrofit.Callback;
import retrofit.http.GET;
import retrofit.http.Path;
import retrofit.http.Query;

/**
 * Created by ksuzuki on 7/22/15.
 */
public interface TmdbApi {

    public static final String API_BASE_URL = "http://api.themoviedb.org/3";
    public static final String API_KEY = "";

    @GET("/discover/movie")
    public void getDiscoverMovies(@Query("api_key") String apiKey, @Query("sort_by") String sortBy, Callback<MovieResult> cb);

    @GET("/movie/{id}/videos")
    public void getMovieVideos(@Path("id") int movieId, @Query("api_key") String apiKey, Callback<VideoResult> cb);

    @GET("/movie/{id}/reviews")
    public void getMovieReviews(@Path("id") int movieId, @Query("api_key") String apiKey, Callback<ReviewResult> cb);
}
