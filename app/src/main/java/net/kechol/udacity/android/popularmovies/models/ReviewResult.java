package net.kechol.udacity.android.popularmovies.models;

import java.util.List;

/**
 * Created by ksuzuki on 7/22/15.
 */
public class ReviewResult {
    public int id;
    public int page;
    public List<Review> results;
    public int total_pages;
    public int total_results;
}
