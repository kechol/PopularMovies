package net.kechol.udacity.android.popularmovies.loaders;


import android.content.Context;
import android.support.v4.content.AsyncTaskLoader;

import net.kechol.udacity.android.popularmovies.models.Movie;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by ksuzuki on 7/14/15.
 */
public class DiscoverMovieLoader extends AsyncTaskLoader<List<Movie>> {

    private int mCol = 2;

    public DiscoverMovieLoader(Context context) {
        super(context);
    }

    @Override
    public boolean cancelLoad() {
        return super.cancelLoad();
    }

    @Override
    public List<Movie> loadInBackground() {
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        Movie movie0 = new Movie();
        movie0.id = 135397;
        movie0.title = "Jurassic World";
        movie0.overview = "Twenty-two years after the events of Jurassic Park, Isla Nublar now features a fully functioning dinosaur theme park, Jurassic World, as originally envisioned by John Hammond.";
        movie0.popularity = 72.610666;
        movie0.poster_path = "/uXZYawqUsChGSj54wcuBtEdUJbh.jpg";
        try {
            movie0.release_date = df.parse("2015-06-12");
        } catch (ParseException e) {
            e.printStackTrace();
        }

        Movie movie1 = new Movie();
        movie1.id = 87101;
        movie1.title = "Terminator Genisys";
        movie1.overview = "The year is 2029. John Connor, leader of the resistance continues the war against the machines. At the Los Angeles offensive, John's fears of the unknown future begin to emerge when TECOM spies reveal a new plot by SkyNet that will attack him from both fronts; past and future, and will ultimately change warfare forever.";
        movie1.popularity = 57.294745;
        movie1.poster_path = "/5JU9ytZJyR3zmClGmVm9q4Geqbd.jpg";
        try {
            movie1.release_date = df.parse("2015-07-01");
        } catch (ParseException e) {
            e.printStackTrace();
        }

        List<Movie> movies = new ArrayList<Movie>();
        movies.add(movie0);
        movies.add(movie1);

        return movies;
    }
}

