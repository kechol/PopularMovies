package net.kechol.udacity.android.popularmovies.loaders;


import android.content.Context;
import android.net.Uri;
import android.support.v4.content.AsyncTaskLoader;
import android.util.Log;

import net.kechol.udacity.android.popularmovies.models.Movie;

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
import java.util.List;

/**
 * Created by ksuzuki on 7/14/15.
 */
public class DiscoverMovieLoader extends AsyncTaskLoader<List<Movie>> {

    public DiscoverMovieLoader(Context context) {
        super(context);
    }

    @Override
    public boolean cancelLoad() {
        return super.cancelLoad();
    }

    @Override
    public List<Movie> loadInBackground() {
        List<Movie> movies = getMoviesFromJson(fetchMovieJson());
        return movies;
    }

    private String fetchMovieJson() {
        final String API_BASE_URL = "http://api.themoviedb.org/3/discover/movie";
        final String API_KEY = "";

        HttpURLConnection urlConn = null;
        BufferedReader reader = null;

        Uri buildUri = Uri.parse(API_BASE_URL).buildUpon()
                .appendQueryParameter("sort_by", "popularity.desc")
                .appendQueryParameter("api_key", API_KEY)
                .build();

        try {
            URL url = new URL(buildUri.toString());

            urlConn = (HttpURLConnection) url.openConnection();
            urlConn.setRequestMethod("GET");
            urlConn.connect();

            // Read the input stream into a String
            InputStream inputStream = urlConn.getInputStream();
            StringBuffer buffer = new StringBuffer();
            if (inputStream == null) {
                // Nothing to do.
                return null;
            }
            reader = new BufferedReader(new InputStreamReader(inputStream));

            String line;
            while ((line = reader.readLine()) != null) {
                // Since it's JSON, adding a newline isn't necessary (it won't affect parsing)
                // But it does make debugging a *lot* easier if you print out the completed
                // buffer for debugging.
                buffer.append(line + "\n");
            }

            if (buffer.length() == 0) {
                // Stream was empty.  No point in parsing.
                return null;
            }
            String jsonStr = buffer.toString();
            return jsonStr;

        } catch (IOException e) {
            Log.e("DiscoverMovieLoader", "IO Error ", e);
            // If the code didn't successfully get the weather data, there's no point in attempting
            // to parse it.
        } finally {
            if (urlConn != null) {
                urlConn.disconnect();
            }

            if (reader != null) {
                try {
                    reader.close();
                } catch (final IOException e) {
                    Log.e("DiscoverMovieLoader", "Error closing stream", e);
                }
            }
        }

        return null;
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

