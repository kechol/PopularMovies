package net.kechol.udacity.android.popularmovies.views;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import net.kechol.udacity.android.popularmovies.R;
import net.kechol.udacity.android.popularmovies.models.Movie;

import java.text.SimpleDateFormat;

/**
 * A placeholder fragment containing a simple view.
 */
public class DetailActivityFragment extends Fragment {

    private Movie mMovie;
    private SharedPreferences mSharedPref;

    private TextView mTitleView;
    private TextView mOverviewView;
    private TextView mReleaseDateView;
    private TextView mPopularityView;
    private ImageView mCoverImageView;
    private ListView mTracksListView;
    private ListView mReviewsListView;

    public DetailActivityFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_detail, container, false);
        setHasOptionsMenu(true);

        mTitleView = (TextView) rootView.findViewById(R.id.detail_title);
        mOverviewView = (TextView) rootView.findViewById(R.id.detail_overview);
        mReleaseDateView = (TextView) rootView.findViewById(R.id.detail_release_date);
        mPopularityView = (TextView) rootView.findViewById(R.id.detail_popularity);
        mCoverImageView = (ImageView) rootView.findViewById(R.id.detail_cover_image);
        mTracksListView = (ListView) rootView.findViewById(R.id.detail_list_tracks);
        mReviewsListView = (ListView) rootView.findViewById(R.id.detail_list_reviews);

        Bundle args = getArguments();
        if (args != null) {
            mMovie = args.getParcelable("movie");
            SimpleDateFormat df = new SimpleDateFormat("yyyy/MM/dd");

            mTitleView.setText(mMovie.title);
            mOverviewView.setText(mMovie.overview);
            mReleaseDateView.setText(df.format(mMovie.release_date));
            mPopularityView.setText(String.valueOf(mMovie.vote_average) + " / 10");
            Picasso.with(getActivity()).load(mMovie.getImageUrl()).into(mCoverImageView);
        }

        mSharedPref = getActivity().getSharedPreferences(Movie.PREF_FAVORITE_PREFIX, Context.MODE_PRIVATE);
        return rootView;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.menu_detail_fragment, menu);

        MenuItem favoriteBtn = (MenuItem) menu.findItem(R.id.action_favorite);
        if (checkFavorite()) {
            favoriteBtn.setIcon(R.drawable.ic_favorite_white_24dp);
        } else {
            favoriteBtn.setIcon(R.drawable.ic_favorite_border_white_24dp);
        }

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        Log.d("MainActivityFragment", "id: " + id);

        if (id == R.id.action_favorite) {
            if (toggleFavorite()) {
                Toast.makeText(getActivity(), "Favorited.", Toast.LENGTH_SHORT).show();
                item.setIcon(R.drawable.ic_favorite_white_24dp);
            } else {
                Toast.makeText(getActivity(), "Unfavorited.", Toast.LENGTH_SHORT).show();
                item.setIcon(R.drawable.ic_favorite_border_white_24dp);
            }
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private boolean checkFavorite() {
        return mSharedPref.getBoolean(Movie.PREF_FAVORITE_PREFIX + mMovie.id, false);
    }

    private boolean toggleFavorite() {
        boolean val = !checkFavorite();
        SharedPreferences.Editor editor = mSharedPref.edit();
        editor.putBoolean(Movie.PREF_FAVORITE_PREFIX + mMovie.id, val);
        editor.commit();
        return val;
    }
}
