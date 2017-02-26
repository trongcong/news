package com.dev4u.ntc.generalnews.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.AppCompatTextView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.dev4u.ntc.generalnews.Constant;
import com.dev4u.ntc.generalnews.R;
import com.dev4u.ntc.generalnews.model.Comment;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * IDE: Android Studio
 * Created by Nguyen Trong Cong  - 2DEV4U.COM
 * Name packge: com.dev4u.ntc.generalnews.adapter
 * Name project: GeneralNews
 * Date: 2/24/2017
 * Time: 03:48
 */

public class CommentAdapter extends ArrayAdapter {
    Context mContext;
    LayoutInflater mInflater;
    ArrayList<Comment> mArrComments;

    public CommentAdapter(Context context, int resource, ArrayList<Comment> mArrComments) {
        super(context, resource);
        this.mContext = context;
        this.mArrComments = mArrComments;
        mInflater = LayoutInflater.from(mContext);
    }

    @Override
    public int getCount() {
        return mArrComments.size();
    }

    @Override
    public Object getItem(int location) {
        return mArrComments.get(location);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if (mInflater == null || convertView == null) {
            mInflater = LayoutInflater.from(parent.getContext());
            convertView = mInflater.inflate(R.layout.item_list_comment, parent, false);
            holder = new ViewHolder(convertView);
        }
        if (mArrComments.get(position).getName() == null) {
            holder.mTvName.setText("Anonymous");
        } else {
            holder.mTvName.setText(mArrComments.get(position).getName());
        }
        holder.mTvTime.setText(Constant.FormatDateTime(mArrComments.get(position).getTime_cmt()));
        holder.mTvContentComment.setText(mArrComments.get(position).getComment());
        Glide.with(mContext).load(mArrComments.get(position).getAvatar())
                .placeholder(R.drawable.bg_no_img)
                .error(R.drawable.bg_no_img)
                .into(holder.mImgAvatar);

        return convertView;
    }

    static class ViewHolder {
        @BindView(R.id.mImgAvatar)
        ImageView mImgAvatar;
        @BindView(R.id.mTvName)
        AppCompatTextView mTvName;
        @BindView(R.id.mTvTime)
        AppCompatTextView mTvTime;
        @BindView(R.id.mTvContentComment)
        AppCompatTextView mTvContentComment;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
