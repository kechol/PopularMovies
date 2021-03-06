package net.kechol.udacity.android.popularmovies.models;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.Date;

/**
 * Created by ksuzuki on 7/14/15.
 */
public class Movie implements Parcelable {
    private static final String BASE_URL = "http://image.tmdb.org/t/p/";
    private static final String DEFAULT_SIZE = "w185";

    public static final String PREF_FAVORITE_PREFIX = "movie_favorite_";

    public static final Gson GSON_CONVERTER = new GsonBuilder().setDateFormat("yyyy-MM-dd").create();

    public int id;
    public double popularity;
    public double vote_average;
    public String title;
    public String poster_path;
    public String overview;
    public Date release_date;

    public Movie() {
        super();
    }

    public Movie(Parcel src) {
        id = src.readInt();
        popularity = src.readDouble();
        vote_average = src.readDouble();
        title = src.readString();
        poster_path = src.readString();
        overview = src.readString();
        release_date = new Date(src.readLong());
    }

    public String getImageUrl() {
        return BASE_URL + DEFAULT_SIZE + poster_path;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int i) {
        dest.writeInt(id);
        dest.writeDouble(popularity);
        dest.writeDouble(vote_average);
        dest.writeString(title);
        dest.writeString(poster_path);
        dest.writeString(overview);
        if (release_date != null)
            dest.writeLong(release_date.getTime());
    }

    @Override
    public String toString() {
        return "Movie: [" + id + "] " + title;
    }

    public static final Creator<Movie> CREATOR = new Creator<Movie>() {
        @Override
        public Movie createFromParcel(Parcel source) {
            return new Movie(source);
        }

        public Movie[] newArray(int size) {
            return new Movie[size];
        }
    };
}
