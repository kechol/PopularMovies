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
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.Toast;

import net.kechol.udacity.android.popularmovies.R;
import net.kechol.udacity.android.popularmovies.adapters.MainMoviesAdapter;
import net.kechol.udacity.android.popularmovies.models.Movie;
import net.kechol.udacity.android.popularmovies.models.MovieResult;
import net.kechol.udacity.android.popularmovies.utils.TmdbApi;

import java.util.ArrayList;
import java.util.List;

import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.Response;
import retrofit.converter.GsonConverter;


/**
 * A placeholder fragment containing a simple view.
 */
public class MainActivityFragment extends Fragment {

    private MainMoviesAdapter mMoviesAdapter;
    private List<Movie> mMoviesList;
    private List<Movie> mFavoriteMoviesList;

    private SharedPreferences mSharedPref;

    private static final int LOADER_DISCOVER_MOVIE_ID = 0;
    private static final String STATE_MAIN_MOVIES_LIST = "STATE_MAIN_MOVIES_LIST";


    public interface Callback {
        public void onItemSelected(Bundle args);
    }

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
            RestAdapter restAdapter = new RestAdapter.Builder()
                    .setEndpoint(TmdbApi.API_BASE_URL)
                    .setConverter(new GsonConverter(Movie.GSON_CONVERTER))
                    .build();
            TmdbApi api = restAdapter.create(TmdbApi.class);

            api.getDiscoverMovies(TmdbApi.API_KEY, "popularity.desc", new retrofit.Callback<MovieResult>() {
                @Override
                public void success(MovieResult apiResult, Response response) {
                    mMoviesList = apiResult.results;
                    mMoviesAdapter.addAll(mMoviesList);
                }

                @Override
                public void failure(RetrofitError error) {
                    Log.e("TmdbApi", error.getMessage());
                    Toast.makeText(getActivity(), "Cannot load movies...", Toast.LENGTH_LONG).show();
                }
            });
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
                    ((Callback) getActivity()).onItemSelected(args);
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
