package net.kechol.udacity.android.popularmovies.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import net.kechol.udacity.android.popularmovies.R;
import net.kechol.udacity.android.popularmovies.models.Movie;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * Created by ksuzuki on 7/16/15.
 */
public class MainMoviesAdapter extends BaseAdapter {
    public static final int SORT_POPULARITY = 0;
    public static final int SORT_RATING = 1;

    private Context mContext;
    private List<Movie> mList;

    public MainMoviesAdapter(Context context) {
        mContext = context;
        mList = new ArrayList<Movie>();
    }

    public MainMoviesAdapter(Context context, List<Movie> data) {
        mContext = context;
        mList = data;
    }

    public void addAll(List<Movie> data) {
        mList.addAll(data);
        notifyDataSetChanged();
    }

    public void clear() {
        mList.clear();
        notifyDataSetChanged();
    }

    public void sort(int sortId) {
        if (sortId == SORT_POPULARITY) {
            Collections.sort(mList, new Comparator<Movie>() {
                @Override
                public int compare(Movie t0, Movie t1) {
                    return Double.valueOf(t1.popularity).compareTo(Double.valueOf(t0.popularity));
                }
            });
            notifyDataSetChanged();
        }

        if (sortId == SORT_RATING) {
            Collections.sort(mList, new Comparator<Movie>() {
                @Override
                public int compare(Movie t0, Movie t1) {
                    return Double.valueOf(t1.vote_average).compareTo(Double.valueOf(t0.vote_average));
                }
            });
            notifyDataSetChanged();
        }
    }

    @Override
    public int getCount() {
        return mList.size();
    }

    @Override
    public Object getItem(int i) {
        return mList.get(i);
    }

    @Override
    public long getItemId(int i) {
        Movie m = mList.get(i);
        if(m != null) return m.id;
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        View gridView;
        LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        ImageView imageView;
        Movie m = mList.get(i);

        if (view == null) {
            gridView = inflater.inflate(R.layout.grid_item_movie_main, null);
        } else {
            gridView = (View) view;
        }

        imageView = (ImageView) gridView.findViewById(R.id.main_grid_item_movie);
        Picasso.with(mContext).load(m.getImageUrl()).into(imageView);

        return gridView;
    }
}
