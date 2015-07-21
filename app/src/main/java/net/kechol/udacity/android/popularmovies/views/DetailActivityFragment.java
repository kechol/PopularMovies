package net.kechol.udacity.android.popularmovies.views;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v4.content.Loader;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import net.kechol.udacity.android.popularmovies.R;
import net.kechol.udacity.android.popularmovies.adapters.DetailReviewsAdapter;
import net.kechol.udacity.android.popularmovies.adapters.DetailVideosAdapter;
import net.kechol.udacity.android.popularmovies.loaders.MovieReviewsLoader;
import net.kechol.udacity.android.popularmovies.loaders.MovieVideosLoader;
import net.kechol.udacity.android.popularmovies.models.Movie;
import net.kechol.udacity.android.popularmovies.models.Review;
import net.kechol.udacity.android.popularmovies.models.Video;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * A placeholder fragment containing a simple view.
 */
public class DetailActivityFragment extends Fragment implements LoaderManager.LoaderCallbacks<List<Parcelable>> {

    private Movie mMovie;
    private SharedPreferences mSharedPref;

    private TextView mTitleView;
    private TextView mOverviewView;
    private TextView mReleaseDateView;
    private TextView mPopularityView;
    private ImageView mCoverImageView;
    private ListView mVideosListView;
    private ListView mReviewsListView;

    private DetailVideosAdapter mVideosAdapter;
    private DetailReviewsAdapter mReviewsAdapter;
    private List<Video> mVideosList;
    private List<Review> mReviewsList;


    private static final int LOADER_MOVIE_VIDEOS_ID = 0;
    private static final int LOADER_MOVIE_REVIEWS_ID = 1;
    private static final String STATE_DETAIL_MOVIE = "STATE_DETAIL_MOVIE";
    private static final String STATE_DETAIL_VIDEOS_LIST = "STATE_DETAIL_VIDEOS_LIST";
    private static final String STATE_DETAIL_REVIEWS_LIST = "STATE_DETAIL_REVIEWS_LIST";

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
        mVideosListView = (ListView) rootView.findViewById(R.id.detail_list_videos);
        mReviewsListView = (ListView) rootView.findViewById(R.id.detail_list_reviews);

        mVideosAdapter = new DetailVideosAdapter(getActivity());
        mVideosListView.setAdapter(mVideosAdapter);

        mReviewsAdapter = new DetailReviewsAdapter(getActivity());
        mReviewsListView.setAdapter(mReviewsAdapter);

        Bundle args = getArguments();
        if (args != null && savedInstanceState == null) {
            mMovie = args.getParcelable("movie");
            getLoaderManager().initLoader(LOADER_MOVIE_VIDEOS_ID, args, this).forceLoad();
            getLoaderManager().initLoader(LOADER_MOVIE_REVIEWS_ID, args, this).forceLoad();
        } else if (savedInstanceState != null) {
            mMovie = savedInstanceState.getParcelable(STATE_DETAIL_MOVIE);
            mVideosList = savedInstanceState.getParcelableArrayList(STATE_DETAIL_VIDEOS_LIST);
            mVideosAdapter.addAll(mVideosList);
            mReviewsList = savedInstanceState.getParcelableArrayList(STATE_DETAIL_REVIEWS_LIST);
            mReviewsAdapter.addAll(mReviewsList);
        }

        SimpleDateFormat df = new SimpleDateFormat("yyyy/MM/dd");
        mSharedPref = getActivity().getSharedPreferences(Movie.PREF_FAVORITE_PREFIX, Context.MODE_PRIVATE);

        mTitleView.setText(mMovie.title);
        mOverviewView.setText(mMovie.overview);
        mReleaseDateView.setText(df.format(mMovie.release_date));
        mPopularityView.setText(String.valueOf(mMovie.vote_average) + " / 10");
        Picasso.with(getActivity()).load(mMovie.getImageUrl()).into(mCoverImageView);

        mVideosListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Video v = (Video) adapterView.getItemAtPosition(i);
                Intent youtubeIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(v.getVideoURL()));
                startActivity(youtubeIntent);
            }
        });

        return rootView;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putParcelable(STATE_DETAIL_MOVIE, mMovie);
        outState.putParcelableArrayList(STATE_DETAIL_VIDEOS_LIST, (ArrayList) mVideosList);
        outState.putParcelableArrayList(STATE_DETAIL_REVIEWS_LIST, (ArrayList) mReviewsList);
        super.onSaveInstanceState(outState);
    }

    @Override
    public Loader<List<Parcelable>> onCreateLoader(int id, Bundle args) {
        Movie movie = args.getParcelable("movie");

        if (id == LOADER_MOVIE_VIDEOS_ID) {
            return (AsyncTaskLoader) new MovieVideosLoader(getActivity(), movie.id);
        }

        if (id == LOADER_MOVIE_REVIEWS_ID) {
            return (AsyncTaskLoader) new MovieReviewsLoader(getActivity(), movie.id);
        }

        return null;
    }

    @Override
    public void onLoadFinished(Loader<List<Parcelable>> loader, List<Parcelable> data) {
        int id = loader.getId();

        if (id == LOADER_MOVIE_VIDEOS_ID) {
            mVideosList = (ArrayList) data;
            mVideosAdapter.addAll((ArrayList) data);
        }

        if (id == LOADER_MOVIE_REVIEWS_ID) {
            mReviewsList = (ArrayList) data;
            mReviewsAdapter.addAll((ArrayList) data);
        }
    }

    @Override
    public void onLoaderReset(Loader<List<Parcelable>> loader) {
        int id = loader.getId();

        if (id == LOADER_MOVIE_VIDEOS_ID) {
            mVideosList.clear();
            mVideosAdapter.clear();
        }

        if (id == LOADER_MOVIE_REVIEWS_ID) {
            mReviewsList.clear();
            mReviewsAdapter.clear();
        }
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
