package com.dev4u.ntc.generalnews.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.dev4u.ntc.generalnews.Constant;
import com.dev4u.ntc.generalnews.R;
import com.dev4u.ntc.generalnews.model.Post;

import java.util.ArrayList;

import static android.content.ContentValues.TAG;
import static com.dev4u.ntc.generalnews.Constant.COUNT_COMMENT_BY_ID_POST_URL;

/**
 * IDE: Android Studio
 * Created by Nguyen Trong Cong  - 2DEV4U.COM
 * Name packge: com.dev4u.ntc.generalnews.adapter
 * Name project: GeneralNews
 * Date: 2/11/2017
 * Time: 17:32
 */

public class NewsSaveBookMarkAdapter extends ArrayAdapter {
    ArrayList<Post> arrPost;
    LayoutInflater mInflater;
    Context context;

    private ImageView imgThumbnail;
    private TextView tvTitle, tvCategory, tvComment;

    public NewsSaveBookMarkAdapter(Context context, int resource, ArrayList<Post> arrPost) {
        super(context, resource, arrPost);
        this.arrPost = arrPost;
        this.context = context;
        mInflater = LayoutInflater.from(context);
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
            mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = mInflater.inflate(R.layout.item_list_news, parent, false);
        }
        imgThumbnail = (ImageView) convertView.findViewById(R.id.imgThumbnail);
        tvTitle = (TextView) convertView.findViewById(R.id.tvTitle);
        tvCategory = (TextView) convertView.findViewById(R.id.tvCategory);
        tvComment = (TextView) convertView.findViewById(R.id.tvComment);

        tvTitle.setText(arrPost.get(position).getTitle());
        //tvComment.setText("0");
        tvCategory.setText(arrPost.get(position).getNameCategory());
        countComment(arrPost.get(position).getId_post(), tvComment, convertView);

        Glide.with(context).load(arrPost.get(position).getImage())
                .placeholder(R.drawable.bg_no_img)
                .error(R.drawable.bg_no_img)
                .crossFade(3000)
                .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                .into(imgThumbnail);
        return convertView;
    }

    private void countComment(int idPost, final TextView tvCommentH, final View v) {
        StringRequest stringRequest = new StringRequest(Request.Method.GET,
                COUNT_COMMENT_BY_ID_POST_URL + idPost, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
//                Log.e(TAG, response.toString());
                if (!response.equals("Count comment failure")) {
                    tvCommentH.setText("0");
                    //Save post Count comment to tag
                    v.setTag(tvCommentH.getText().toString());
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(TAG, error.toString());
            }
        });
        Constant.getInstance().addToRequestQueue(stringRequest);
    }
}
