package net.kechol.udacity.android.popularmovies.views;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
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
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import net.kechol.udacity.android.popularmovies.R;
import net.kechol.udacity.android.popularmovies.models.Movie;
import net.kechol.udacity.android.popularmovies.models.Review;
import net.kechol.udacity.android.popularmovies.models.ReviewResult;
import net.kechol.udacity.android.popularmovies.models.Video;
import net.kechol.udacity.android.popularmovies.models.VideoResult;
import net.kechol.udacity.android.popularmovies.utils.TmdbApi;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.Response;

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
    private LinearLayout mLinearView;
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
    public View onCreateView(final LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_detail, container, false);
        setHasOptionsMenu(true);

        mVideosList = new ArrayList<Video>();
        mReviewsList = new ArrayList<Review>();

        mTitleView = (TextView) rootView.findViewById(R.id.detail_title);
        mOverviewView = (TextView) rootView.findViewById(R.id.detail_overview);
        mReleaseDateView = (TextView) rootView.findViewById(R.id.detail_release_date);
        mPopularityView = (TextView) rootView.findViewById(R.id.detail_popularity);
        mCoverImageView = (ImageView) rootView.findViewById(R.id.detail_cover_image);
        mLinearView = (LinearLayout) rootView.findViewById(R.id.detail_linear_view);

        Bundle args = getArguments();
        if (args != null && savedInstanceState == null) {
            mMovie = args.getParcelable("movie");

            RestAdapter restAdapter = new RestAdapter.Builder()
                    .setEndpoint(TmdbApi.API_BASE_URL)
                    .build();
            TmdbApi api = restAdapter.create(TmdbApi.class);

            api.getMovieVideos(mMovie.id, TmdbApi.API_KEY, new retrofit.Callback<VideoResult>() {
                @Override
                public void success(VideoResult apiResult, Response response) {
                    mVideosList = apiResult.results;
                    for (Video v : mVideosList) {
                        mLinearView.addView(getVideoView(v, inflater));
                    }
                }

                @Override
                public void failure(RetrofitError error) {
                    Log.e("TmdbApi", error.getMessage());
                    Toast.makeText(getActivity(), "Cannot load videos...", Toast.LENGTH_LONG).show();
                }
            });

            api.getMovieReviews(mMovie.id, TmdbApi.API_KEY, new retrofit.Callback<ReviewResult>() {
                @Override
                public void success(ReviewResult apiResult, Response response) {
                    mReviewsList = apiResult.results;
                    for (Review r : mReviewsList) {
                        mLinearView.addView(getReviewView(r, inflater));
                    }
                }

                @Override
                public void failure(RetrofitError error) {
                    Log.e("TmdbApi", error.getMessage());
                    Toast.makeText(getActivity(), "Cannot load reviews...", Toast.LENGTH_LONG).show();
                }
            });

        } else if (savedInstanceState != null) {
            mMovie = savedInstanceState.getParcelable(STATE_DETAIL_MOVIE);
            mVideosList = savedInstanceState.getParcelableArrayList(STATE_DETAIL_VIDEOS_LIST);
            mReviewsList = savedInstanceState.getParcelableArrayList(STATE_DETAIL_REVIEWS_LIST);

            for (Video v : mVideosList) {
                mLinearView.addView(getVideoView(v, inflater));
            }

            for (Review r : mReviewsList) {
                mLinearView.addView(getReviewView(r, inflater));
            }
        }

        if (mMovie != null) {
            SimpleDateFormat df = new SimpleDateFormat("yyyy/MM/dd");
            mSharedPref = getActivity().getSharedPreferences(Movie.PREF_FAVORITE_PREFIX, Context.MODE_PRIVATE);

            mTitleView.setText(mMovie.title);
            mOverviewView.setText(mMovie.overview);
            mReleaseDateView.setText(df.format(mMovie.release_date));
            mPopularityView.setText(String.valueOf(mMovie.vote_average) + " / 10");
            Picasso.with(getActivity()).load(mMovie.getImageUrl()).into(mCoverImageView);
        }

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
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        if (mMovie == null) return;

        inflater.inflate(R.menu.menu_detail_fragment, menu);

        MenuItem favoriteBtn = (MenuItem) menu.findItem(R.id.action_favorite);
        if (checkFavorite(mMovie.id)) {
            favoriteBtn.setIcon(R.drawable.ic_favorite_white_24dp);
        } else {
            favoriteBtn.setIcon(R.drawable.ic_favorite_border_white_24dp);
        }

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_favorite) {
            if (toggleFavorite(mMovie.id)) {
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

    private boolean checkFavorite(int id) {
        return mSharedPref.getBoolean(Movie.PREF_FAVORITE_PREFIX + id, false);
    }

    private boolean toggleFavorite(int id) {
        boolean val = !checkFavorite(id);
        SharedPreferences.Editor editor = mSharedPref.edit();
        editor.putBoolean(Movie.PREF_FAVORITE_PREFIX + id, val);
        editor.commit();
        return val;
    }

    public View getVideoView(Video video, LayoutInflater inflater) {
        View view = inflater.inflate(R.layout.list_item_video_detail, null);

        TextView nameView = (TextView) view.findViewById(R.id.detail_list_videos_name);
        nameView.setText(video.name);

        view.setTag(R.string.tag_video_item, video);

        view.setOnClickListener(new AdapterView.OnClickListener() {
            @Override
            public void onClick(View view) {
                Video v = (Video) view.getTag(R.string.tag_video_item);
                Intent youtubeIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(v.getVideoURL()));
                startActivity(youtubeIntent);
            }
        });

        return view;
    }

    public View getReviewView(Review review, LayoutInflater inflater) {
        View view = inflater.inflate(R.layout.list_item_review_detail, null);

        TextView authorView = (TextView) view.findViewById(R.id.detail_list_reviews_author);
        authorView.setText(review.author);

        TextView contentView = (TextView) view.findViewById(R.id.detail_list_reviews_content);
        contentView.setText(review.content);

        return view;
    }
}
