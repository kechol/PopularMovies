package net.kechol.udacity.android.popularmovies.views;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;

import net.kechol.udacity.android.popularmovies.R;
import net.kechol.udacity.android.popularmovies.adapters.MainMoviesAdapter;
import net.kechol.udacity.android.popularmovies.loaders.DiscoverMovieLoader;
import net.kechol.udacity.android.popularmovies.models.Movie;

import java.util.ArrayList;
import java.util.List;


/**
 * A placeholder fragment containing a simple view.
 */
public class MainActivityFragment extends Fragment implements LoaderManager.LoaderCallbacks<List<Movie>> {

    private MainMoviesAdapter mMoviesAdapter;
    private List<Movie> mMoviesList;
    private List<Movie> mFavoriteMoviesList;

    private SharedPreferences mSharedPref;

    private static final int LOADER_DISCOVER_MOVIE_ID = 0;
    private static final String STATE_MAIN_MOVIES_LIST = "STATE_MAIN_MOVIES_LIST";

    public MainActivityFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_main, container, false);
        setHasOptionsMenu(true);

        mMoviesAdapter = new MainMoviesAdapter(getActivity());

        if (savedInstanceState != null) {
            mMoviesList = savedInstanceState.getParcelableArrayList(STATE_MAIN_MOVIES_LIST);
            mMoviesAdapter.addAll(mMoviesList);
        } else {
            Bundle args = new Bundle();
            getLoaderManager().initLoader(LOADER_DISCOVER_MOVIE_ID, args, MainActivityFragment.this).forceLoad();
        }

        GridView moviesGridView = (GridView) rootView.findViewById(R.id.main_grid_movies);
        moviesGridView.setAdapter(mMoviesAdapter);

        moviesGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Movie m = (Movie) adapterView.getItemAtPosition(i);
                if (m != null) {
                    Bundle args = new Bundle();
                    args.putParcelable("movie", m);

                    Intent intent = new Intent(getActivity(), DetailActivity.class);
                    intent.putExtras(args);
                    startActivity(intent);
                }
            }
        });

        mSharedPref = getActivity().getSharedPreferences(Movie.PREF_FAVORITE_PREFIX, Context.MODE_PRIVATE);

        return rootView;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putParcelableArrayList(STATE_MAIN_MOVIES_LIST, (ArrayList) mMoviesList);
        super.onSaveInstanceState(outState);
    }

    @Override
    public Loader<List<Movie>> onCreateLoader(int id, Bundle args) {
        return new DiscoverMovieLoader(getActivity());
    }

    @Override
    public void onLoadFinished(Loader<List<Movie>> loader, List<Movie> data) {
        mMoviesList = data;
        mMoviesAdapter.addAll(mMoviesList);
    }

    @Override
    public void onLoaderReset(Loader<List<Movie>> loader) {
        mMoviesList.clear();
        mMoviesAdapter.clear();
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.menu_main_fragment, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_sort_popularity) {
            // FIXME: not efficient
            mMoviesAdapter.clear();
            mMoviesAdapter.addAll(mMoviesList);
            mMoviesAdapter.sort(MainMoviesAdapter.SORT_POPULARITY);
            return true;
        }

        if (id == R.id.action_sort_rating) {
            // FIXME: not efficient
            mMoviesAdapter.clear();
            mMoviesAdapter.addAll(mMoviesList);
            mMoviesAdapter.sort(MainMoviesAdapter.SORT_RATING);
            return true;
        }

        if (id == R.id.action_sort_favorite) {
            if (mFavoriteMoviesList == null) {
                mFavoriteMoviesList = new ArrayList<Movie>();
                for (Movie m : mMoviesList) {
                    if (checkFavorite(m.id)) mFavoriteMoviesList.add(m);
                }
            }
            // FIXME: not efficient
            mMoviesAdapter.clear();
            mMoviesAdapter.addAll(mFavoriteMoviesList);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private boolean checkFavorite(int id) {
        Log.d("MainActivityFragment", "favorite?:" + mSharedPref.getBoolean(Movie.PREF_FAVORITE_PREFIX + id, false));
        return mSharedPref.getBoolean(Movie.PREF_FAVORITE_PREFIX + id, false);
    }

}
