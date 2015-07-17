package net.kechol.udacity.android.popularmovies.views;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;

import net.kechol.udacity.android.popularmovies.R;
import net.kechol.udacity.android.popularmovies.adapters.MoviesAdapter;
import net.kechol.udacity.android.popularmovies.loaders.DiscoverMovieLoader;
import net.kechol.udacity.android.popularmovies.models.Movie;

import java.util.ArrayList;
import java.util.List;


/**
 * A placeholder fragment containing a simple view.
 */
public class MainActivityFragment extends Fragment implements LoaderManager.LoaderCallbacks<List<Movie>> {

    private MoviesAdapter mMoviesAdapter;
    private List<Movie> mMoviesList;

    private static final int LOADER_DISCOVER_MOVIE_ID = 0;
    private static final String STATE_MOVIES_LIST = "STATE_MOVIES_LIST";

    public MainActivityFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_main, container, false);

        mMoviesAdapter = new MoviesAdapter(getActivity());

        if (savedInstanceState != null) {
            mMoviesList = savedInstanceState.getParcelableArrayList(STATE_MOVIES_LIST);
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

        return rootView;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putParcelableArrayList(STATE_MOVIES_LIST, (ArrayList) mMoviesList);
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
}
