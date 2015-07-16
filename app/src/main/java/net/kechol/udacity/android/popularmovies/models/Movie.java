package net.kechol.udacity.android.popularmovies.models;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.Date;

/**
 * Created by ksuzuki on 7/14/15.
 */
public class Movie implements Parcelable {
    private static final String BASE_URL = "http://image.tmdb.org/t/p/";
    private static final String DEFAULT_SIZE = "w185";

    public int id;
    public double popularity;
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
        dest.writeString(title);
        dest.writeString(poster_path);
        dest.writeString(overview);
        if (release_date != null)
            dest.writeLong(release_date.getTime());
    }
}
