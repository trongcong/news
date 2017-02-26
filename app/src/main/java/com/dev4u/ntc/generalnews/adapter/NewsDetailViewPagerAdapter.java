package com.dev4u.ntc.generalnews.adapter;

import android.content.Context;
import android.graphics.Color;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;

import com.dev4u.ntc.generalnews.Constant;
import com.dev4u.ntc.generalnews.R;
import com.dev4u.ntc.generalnews.model.Post;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * IDE: Android Studio
 * Created by Nguyen Trong Cong  - 2DEV4U.COM
 * Name packge: com.dev4u.ntc.generalnews.adapter
 * Name project: GeneralNews
 * Date: 2/8/2017
 * Time: 16:59
 */

public class NewsDetailViewPagerAdapter extends PagerAdapter {
    ArrayList<Post> arrPost;
    Context context;
    LayoutInflater layoutInflater;

    @BindView(R.id.webViewNews)
    WebView webViewNews;

    public NewsDetailViewPagerAdapter(Context context, ArrayList<Post> arrPost) {
        this.context = context;
        this.arrPost = arrPost;
        layoutInflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return arrPost.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view.equals(object);
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        View v = layoutInflater.inflate(R.layout.custom_item_pager_detail_news, container, false);
        ButterKnife.bind(this, v);
        assert v != null;

        showDetailPost(position);

        container.addView(v, 0);
        return v;
    }

    private void showDetailPost(int i) {
        webViewNews.setBackgroundColor(Color.WHITE);
        webViewNews.setFocusableInTouchMode(false);
        webViewNews.setFocusable(false);
        webViewNews.getSettings().setDefaultTextEncodingName("UTF-8");
        webViewNews.getSettings().setLoadWithOverviewMode(true);
        webViewNews.getSettings().setUseWideViewPort(true);

        String mimeType = "text/html; charset=UTF-8";
        String encoding = "utf-8";
        String htmlText = arrPost.get(i).getContent().toString();

        String text = "<html><head>"
                + "<meta name=\"viewport\" content=\"width=device-width, user-scalable=no\" />"
                + "<link rel=\"stylesheet\" type=\"text/css\" href=\"content_file.css\" /></head>"
                + "<body><h2>" + arrPost.get(i).getTitle().toString() + "</h2></br><span style=\"color:red;\">"
                + arrPost.get(i).getNameCategory().toString() + "</span> | <span style=\"color:red;\">"
                + Constant.FormatDateTime(arrPost.get(i).getDatetime().toString()) + "</span></i></br> <p><h3>"
                + arrPost.get(i).getDescription().toString() + "</h3></p>"
                + htmlText + "</body></html>";

        webViewNews.loadDataWithBaseURL("file:///android_asset/", text, mimeType, encoding, null);
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }
}
