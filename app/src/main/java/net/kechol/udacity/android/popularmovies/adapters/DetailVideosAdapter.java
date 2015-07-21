package net.kechol.udacity.android.popularmovies.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import net.kechol.udacity.android.popularmovies.R;
import net.kechol.udacity.android.popularmovies.models.Video;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ksuzuki on 7/20/15.
 */
public class DetailVideosAdapter extends BaseAdapter {
    private Context mContext;
    private List<Video> mList;

    public DetailVideosAdapter(Context context) {
        mContext = context;
        mList = new ArrayList<Video>();
    }

    public void addAll(List<Video> data) {
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
    public Video getItem(int i) {
        return mList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        Video video = getItem(i);
        if (video == null) return view;

        if (view == null) {
            view = LayoutInflater.from(mContext).inflate(R.layout.list_item_video_detail, viewGroup, false);
        }

        TextView nameView = (TextView) view.findViewById(R.id.detail_list_videos_name);
        nameView.setText(video.name);

        return view;
    }
}
