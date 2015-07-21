package net.kechol.udacity.android.popularmovies.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import net.kechol.udacity.android.popularmovies.R;
import net.kechol.udacity.android.popularmovies.models.Review;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ksuzuki on 7/20/15.
 */
public class DetailReviewsAdapter extends BaseAdapter {
    private Context mContext;
    private List<Review> mList;

    public DetailReviewsAdapter(Context context) {
        mContext = context;
        mList = new ArrayList<Review>();
    }

    public void addAll(List<Review> data) {
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
    public Review getItem(int i) {
        return mList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        Review review = getItem(i);
        if (review == null) return view;

        if (view == null) {
            view = LayoutInflater.from(mContext).inflate(R.layout.list_item_review_detail, viewGroup, false);
        }

        TextView authorView = (TextView) view.findViewById(R.id.detail_list_reviews_author);
        authorView.setText(review.author);

        TextView contentView = (TextView) view.findViewById(R.id.detail_list_reviews_content);
        contentView.setText(review.content);

        return view;
    }
}
