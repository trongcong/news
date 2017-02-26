package com.dev4u.ntc.generalnews.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.dev4u.ntc.generalnews.R;
import com.dev4u.ntc.generalnews.load.LoadNews;
import com.dev4u.ntc.generalnews.model.Post;

import java.util.ArrayList;

/**
 * IDE: Android Studio
 * Created by Nguyen Trong Cong  - 2DEV4U.COM
 * Name packge: com.dev4u.ntc.generalnews.adapter
 * Name project: GeneralNews
 * Date: 2/8/2017
 * Time: 14:59
 */

public class RecentNewsAdapter extends ArrayAdapter {
    ArrayList<Post> arrPost;
    LayoutInflater mInflater;
    Context context;

    LoadNews loadNews;

    private ImageView imgThumbnail;
    private TextView tvTitle, tvCategory, tvComment;

    public RecentNewsAdapter(Context context, int resource, ArrayList<Post> arrPost, LoadNews loadNews) {
        super(context, resource, arrPost);
        this.arrPost = arrPost;
        this.context = context;
        this.loadNews = loadNews;
        mInflater = LayoutInflater.from(context);
    }

    public void addListItemToAdapter(ArrayList<Post> listPost) {
        //Add list to current array list of data
        arrPost.addAll(listPost);
        //Notify UI
        this.notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return arrPost.size();
    }

    @Override
    public Object getItem(int location) {
        return arrPost.get(location);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (mInflater == null || convertView == null) {
            mInflater = LayoutInflater.from(parent.getContext());
            convertView = mInflater.inflate(R.layout.item_list_news, parent, false);
        }
        imgThumbnail = (ImageView) convertView.findViewById(R.id.imgThumbnail);
        tvTitle = (TextView) convertView.findViewById(R.id.tvTitle);
        tvCategory = (TextView) convertView.findViewById(R.id.tvCategory);
        tvComment = (TextView) convertView.findViewById(R.id.tvComment);

        tvTitle.setText(arrPost.get(position).getTitle());
        //tvComment.setText("0");
        tvCategory.setText(arrPost.get(position).getNameCategory());
        loadNews.getCountComment(arrPost.get(position).getId_post(), tvComment);

        Glide.with(context).load(arrPost.get(position).getImage())
                .placeholder(R.drawable.bg_no_img)
                .error(R.drawable.bg_no_img)
                .crossFade(3000)
                .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                .into(imgThumbnail);

        return convertView;
    }
}
