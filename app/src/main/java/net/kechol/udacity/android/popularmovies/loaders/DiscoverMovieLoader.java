package net.kechol.udacity.android.popularmovies.loaders;


import android.content.Context;
import android.net.Uri;
import android.support.v4.content.AsyncTaskLoader;
import android.util.Log;

import net.kechol.udacity.android.popularmovies.models.Movie;
import net.kechol.udacity.android.popularmovies.utils.ApiConnectionUtil;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by ksuzuki on 7/14/15.
 */
public class DiscoverMovieLoader extends AsyncTaskLoader<List<Movie>> {

    private final String API_PATH = "discover/movie";

    public DiscoverMovieLoader(Context context) {
        super(context);
    }

    @Override
    public boolean cancelLoad() {
        return super.cancelLoad();
    }

    @Override
    public List<Movie> loadInBackground() {
        Map<String, String> params = new HashMap<String, String>();
        params.put("sort_by", "popularity.desc");
        List<Movie> movies = getMoviesFromJson(ApiConnectionUtil.fetchJson(API_PATH, params));
        return movies;
    }

    private List<Movie> getMoviesFromJson(String jsonStr) {
        List<Movie> movies = new ArrayList<Movie>();

        try {
            JSONObject json = new JSONObject(jsonStr);
            JSONArray results = json.getJSONArray("results");

            DateFormat df = new SimpleDateFormat("yyyy-MM-dd");

            for(int i = 0; i < results.length(); i++) {
                Movie m = new Movie();

                JSONObject jm = results.getJSONObject(i);
                m.id = jm.getInt("id");
                m.title = jm.getString("title");
                m.poster_path = jm.getString("poster_path");
                m.popularity = jm.getDouble("popularity");
                m.vote_average = jm.getDouble("vote_average");
                m.overview = jm.getString("overview");
                m.release_date = df.parse(jm.getString("release_date"));

                movies.add(m);
            }
        } catch (JSONException e) {
            Log.e("DiscoverMovieLoader", e.getMessage(), e);
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return movies;
    }
}

