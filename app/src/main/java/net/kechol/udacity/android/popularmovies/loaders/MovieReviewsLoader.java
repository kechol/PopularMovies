package net.kechol.udacity.android.popularmovies.loaders;

import android.content.Context;
import android.support.v4.content.AsyncTaskLoader;
import android.util.Log;

import net.kechol.udacity.android.popularmovies.models.Review;
import net.kechol.udacity.android.popularmovies.utils.ApiConnectionUtil;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ksuzuki on 7/20/15.
 */
public class MovieReviewsLoader extends AsyncTaskLoader<List<Review>> {
    private final String API_PATH = "movie/%d/reviews";

    private int mMovieId;

    public MovieReviewsLoader(Context context, int movieId) {
        super(context);
        mMovieId = movieId;
    }

    @Override
    public boolean cancelLoad() {
        return super.cancelLoad();
    }

    @Override
    public List<Review> loadInBackground() {
        return getReviewsFromJson(ApiConnectionUtil.fetchJson(String.format(API_PATH, mMovieId), null));
    }

    private List<Review> getReviewsFromJson(String jsonStr) {
        List<Review> Reviews = new ArrayList<Review>();

        try {
            JSONObject json = new JSONObject(jsonStr);
            JSONArray results = json.getJSONArray("results");

            for(int i = 0; i < results.length(); i++) {
                Review r = new Review();

                JSONObject jr = results.getJSONObject(i);
                r.id = jr.getString("id");
                r.author = jr.getString("author");
                r.content = jr.getString("content");
                r.url = jr.getString("url");

                Reviews.add(r);
            }
        } catch (JSONException e) {
            Log.e("MovieReviewsLoader", e.getMessage(), e);
            e.printStackTrace();
        }

        return Reviews;
    }
}
