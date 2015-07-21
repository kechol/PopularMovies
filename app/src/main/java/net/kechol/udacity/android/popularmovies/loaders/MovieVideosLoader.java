package net.kechol.udacity.android.popularmovies.loaders;

import android.content.Context;
import android.support.v4.content.AsyncTaskLoader;
import android.util.Log;

import net.kechol.udacity.android.popularmovies.models.Video;
import net.kechol.udacity.android.popularmovies.utils.ApiConnectionUtil;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ksuzuki on 7/20/15.
 */
public class MovieVideosLoader extends AsyncTaskLoader<List<Video>> {
    private final String API_PATH = "movie/%d/videos";

    private int mMovieId;

    public MovieVideosLoader(Context context, int movieId) {
        super(context);
        mMovieId = movieId;
    }

    @Override
    public boolean cancelLoad() {
        return super.cancelLoad();
    }

    @Override
    public List<Video> loadInBackground() {
        return getVideosFromJson(ApiConnectionUtil.fetchJson(String.format(API_PATH, mMovieId), null));
    }

    private List<Video> getVideosFromJson(String jsonStr) {
        List<Video> videos = new ArrayList<Video>();

        Log.d("MovieVideosLoader", jsonStr);

        try {
            JSONObject json = new JSONObject(jsonStr);
            JSONArray results = json.getJSONArray("results");

            for(int i = 0; i < results.length(); i++) {
                Video v = new Video();

                JSONObject jv = results.getJSONObject(i);
                v.id = jv.getString("id");
                v.key = jv.getString("key");
                v.name = jv.getString("name");
                v.site = jv.getString("site");
                v.size = jv.getInt("size");
                v.type = jv.getString("type");

                videos.add(v);
            }
        } catch (JSONException e) {
            Log.e("MovieVideosLoader", e.getMessage(), e);
            e.printStackTrace();
        }

        return videos;
    }
}
