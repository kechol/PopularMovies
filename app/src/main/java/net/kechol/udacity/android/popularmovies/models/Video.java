package net.kechol.udacity.android.popularmovies.models;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by ksuzuki on 7/20/15.
 */
public class Video implements Parcelable {

    public static final String YOUTUBE_BASE_URL = "https://www.youtube.com/watch?v=";

    public String id;
    public String key;
    public String name;
    public String site;
    public int size;
    public String type;


    public Video() {
        super();
    }

    public Video(Parcel src) {
        id = src.readString();
        key = src.readString();
        name = src.readString();
        site = src.readString();
        size = src.readInt();
        type = src.readString();
    }

    public String getYoutubeURL() {
        if (type != "YouTube") return null;
        return YOUTUBE_BASE_URL + key;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int i) {
        dest.writeString(id);
        dest.writeString(key);
        dest.writeString(name);
        dest.writeString(site);
        dest.writeInt(size);
        dest.writeString(type);
    }

    @Override
    public String toString() {
        return "Video: [" + id + "] " + key;
    }

    public static final Parcelable.Creator<Video> CREATOR = new Parcelable.Creator<Video>() {
        @Override
        public Video createFromParcel(Parcel source) {
            return new Video(source);
        }

        public Video[] newArray(int size) {
            return new Video[size];
        }
    };
}
