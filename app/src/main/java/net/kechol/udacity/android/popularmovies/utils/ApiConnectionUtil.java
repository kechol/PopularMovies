package net.kechol.udacity.android.popularmovies.utils;

import android.net.Uri;
import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Map;

/**
 * Created by ksuzuki on 7/20/15.
 */
public class ApiConnectionUtil {

    public static final String API_BASE_URL = "http://api.themoviedb.org/3";
    public static final String API_KEY = "";


    public static String fetchJson(String path, Map<String, String> params) {

        HttpURLConnection urlConn = null;
        BufferedReader reader = null;

        Uri.Builder builder = Uri.parse(API_BASE_URL).buildUpon();

        for (String p : path.split("/")) {
            if (p.length() > 0) builder.appendPath(p);
        }

        if (params != null) {
            for (Map.Entry<String, String> entry : params.entrySet()) {
                builder.appendQueryParameter(entry.getKey(), entry.getValue());
            }
        }

        builder.appendQueryParameter("api_key", API_KEY);
        Uri buildUri = builder.build();

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
            Log.e("ApiConnectionUtil", "IO Error ", e);
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
                    Log.e("ApiConnectionUtil", "Error closing stream", e);
                }
            }
        }

        return null;
    }
}
