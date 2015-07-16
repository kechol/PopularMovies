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
import java.util.List;

/**
 * Created by ksuzuki on 7/16/15.
 */
public class MoviesAdapter extends BaseAdapter {
    private Context mContext;
    private List<Movie> mList;

    public MoviesAdapter(Context context) {
        mContext = context;
        mList = new ArrayList<Movie>();
    }

    public MoviesAdapter(Context context, List<Movie> data) {
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
