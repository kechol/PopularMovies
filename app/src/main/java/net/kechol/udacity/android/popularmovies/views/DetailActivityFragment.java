package net.kechol.udacity.android.popularmovies.views;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.squareup.picasso.Picasso;

import net.kechol.udacity.android.popularmovies.R;
import net.kechol.udacity.android.popularmovies.models.Movie;

import java.text.SimpleDateFormat;

/**
 * A placeholder fragment containing a simple view.
 */
public class DetailActivityFragment extends Fragment {

    private Movie mMovie;

    private TextView mTitleView;
    private TextView mOverviewView;
    private TextView mReleaseDateView;
    private TextView mPopularityView;
    private ImageView mCoverImageView;
    private ToggleButton mFavoriteBtn;
    private ListView mTracksListView;
    private ListView mReviewsListView;

    public DetailActivityFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_detail, container, false);

        mTitleView = (TextView) rootView.findViewById(R.id.detail_title);
        mOverviewView = (TextView) rootView.findViewById(R.id.detail_overview);
        mReleaseDateView = (TextView) rootView.findViewById(R.id.detail_release_date);
        mPopularityView = (TextView) rootView.findViewById(R.id.detail_popularity);
        mCoverImageView = (ImageView) rootView.findViewById(R.id.detail_cover_image);
        mFavoriteBtn = (ToggleButton) rootView.findViewById(R.id.detail_favorite_btn);
        mTracksListView = (ListView) rootView.findViewById(R.id.detail_list_tracks);
        mReviewsListView = (ListView) rootView.findViewById(R.id.detail_list_reviews);

        Bundle args = getArguments();
        if (args != null) {
            mMovie = args.getParcelable("movie");
            SimpleDateFormat df = new SimpleDateFormat("yyyy/MM/dd");

            mTitleView.setText(mMovie.title);
            mOverviewView.setText(mMovie.overview);
            mReleaseDateView.setText(df.format(mMovie.release_date));
            mPopularityView.setText(String.valueOf(mMovie.popularity));
            Picasso.with(getActivity()).load(mMovie.getImageUrl()).into(mCoverImageView);
        }

        return rootView;
    }
}
