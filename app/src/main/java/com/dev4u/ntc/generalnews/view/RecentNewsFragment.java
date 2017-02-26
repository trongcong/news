package com.dev4u.ntc.generalnews.view;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.dev4u.ntc.generalnews.Constant;
import com.dev4u.ntc.generalnews.R;
import com.dev4u.ntc.generalnews.adapter.RecentNewsAdapter;
import com.dev4u.ntc.generalnews.load.LoadNews;
import com.dev4u.ntc.generalnews.load.LoadNewsCallBack;
import com.dev4u.ntc.generalnews.model.Category;
import com.dev4u.ntc.generalnews.model.Comment;
import com.dev4u.ntc.generalnews.model.Post;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * IDE: Android Studio
 * Created by Nguyen Trong Cong  - 2DEV4U.COM
 * Name packge: com.dev4u.ntc.generalnews.view
 * Name project: GeneralNews
 * Date: 2/8/2017
 * Time: 13:36
 */

public class RecentNewsFragment extends Fragment implements LoadNewsCallBack,
        AdapterView.OnItemClickListener, AbsListView.OnScrollListener, SwipeRefreshLayout.OnRefreshListener {
    public Handler mHandler;
    public View ftView;
    public boolean isLoading = false;
    @BindView(R.id.lvPostMain)
    ListView lvPostMain;
    @BindView(R.id.containerSwipeRefreshLayout)
    SwipeRefreshLayout swipeRefreshLayout;
    LoadNews loadNews;
    private RecentNewsAdapter recentNewsAdapter;
    private ArrayList<Post> arrPost;
    private int newsNumber = 10;
    private String TAG = MainActivity.class.getSimpleName();

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        arrPost = new ArrayList<>();
        loadNews = new LoadNews(this);

        LayoutInflater li = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        ftView = li.inflate(R.layout.footer_view, null);
        mHandler = new MyHandler();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View layout = inflater.inflate(R.layout.fragment_recent, container, false);
        ButterKnife.bind(this, layout);

        lvPostMain.setOnItemClickListener(this);
        lvPostMain.setOnScrollListener(this);

        initListView();
        swipeRefreshLayout.setOnRefreshListener(this);
        swipeRefreshLayout.setColorScheme(android.R.color.holo_blue_bright,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light);
        swipeRefreshLayout.post(new Runnable() {
                                    @Override
                                    public void run() {
                                        loadNews.getNews(newsNumber, arrPost, Constant.LATEST_NEWS_URL);
                                    }
                                }
        );

        return layout;
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        Intent intent = new Intent(getContext(), NewsDetailActivity.class);
        intent.putExtra("arrPost", arrPost);
        Log.e(TAG, arrPost.size() + "");
        intent.putExtra("VT", i + "");
        intent.putExtra("Category", arrPost.get(i).getNameCategory());

        startActivity(intent);
    }

    @Override
    public void onRefresh() {
        loadNews.getNews(newsNumber, arrPost, Constant.LATEST_NEWS_URL);
    }

    @Override
    public void updateDataListNews(Post post) {
        addDataList(post);
    }

    @Override
    public void updateDataListCa(Category category) {

    }

    @Override
    public void updateDataListComment(Comment comment) {

    }

    @Override
    public void setRefreshingLayout(boolean b) {
        swipeRefreshLayout.setRefreshing(b);
    }

    @Override
    public void onScrollStateChanged(AbsListView absListView, int i) {
    }

    @Override
    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
        if (view.getLastVisiblePosition() == totalItemCount - 1 && lvPostMain.getCount() >= 10
                && isLoading == false && visibleItemCount + firstVisibleItem >= view.getLastVisiblePosition()) {
            isLoading = true;
            Log.e(TAG + " size4", arrPost.size() + " totalItemCount " + totalItemCount + " visibleItemCount " + visibleItemCount
                    + " LastVisiblePosition " + view.getLastVisiblePosition() + " lvPostMain.getCount " + lvPostMain.getCount());

            Log.e(TAG, "bf Thread " + arrPost.size() + " - " + newsNumber);
            Thread thread = new ThreadGetMoreData();
//            if (newsNumber <= arrPost.size()) {
            //Start thread
            thread.start();
            Log.e(TAG, "af Thread " + arrPost.size() + " - " + newsNumber);
//            } else {
//                newsNumber = arrPost.size();
//                Snackbar snackbarwellcom = Snackbar
//                        .make(swipeRefreshLayout, "Đã đến tin cuối cùng", Snackbar.LENGTH_LONG);
//                snackbarwellcom.show();
//            }
        }
    }

    private void initListView() {
        recentNewsAdapter = new RecentNewsAdapter(getContext(), R.layout.item_list_news, arrPost, loadNews);
        lvPostMain.setAdapter(recentNewsAdapter);
    }

    private void addDataList(Post post) {
        arrPost.add(post);
        recentNewsAdapter.notifyDataSetChanged();
    }

    public class MyHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 0:
                    //Add loading view during search processing
                    lvPostMain.addFooterView(ftView);
                    break;
                case 1:
                    Log.e(TAG + " size3", arrPost.size() + "");
                    //Update data adapter and UI
//                    viewAdapter.addListItemToAdapter((ArrayList<Post>) msg.obj);
                    recentNewsAdapter.notifyDataSetChanged();
                    //Remove loading view after update listview
                    lvPostMain.removeFooterView(ftView);
                    isLoading = false;
                    break;
                default:
                    break;
            }
        }
    }

    public class ThreadGetMoreData extends Thread {
        @Override
        public void run() {
            //Add footer view after get data
            mHandler.sendEmptyMessage(0);
            //Search more data
            newsNumber += 5;
            String url = Constant.LATEST_NEWS_URL + newsNumber;
            Log.e(TAG, url);
            JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, url, null,
                    new Response.Listener<JSONArray>() {
                        @Override
                        public void onResponse(JSONArray response) {
                            if (response.length() > 0) {
                                try {
                                    Log.e(TAG + " size", arrPost.size() + "");
                                    arrPost.clear();
                                    for (int i = 0; i < response.length(); i++) {
                                        JSONObject post = response.getJSONObject(i);
                                        Post p = new Post(post);
                                        addDataList(p);
                                    }
                                    Log.e(TAG + " size1", arrPost.size() + "");
                                } catch (JSONException e) {
                                    Log.e(TAG, "JSON Parsing error: " + e.getMessage());
                                }
                            }
                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Log.e(TAG, "Server Error: " + error.getMessage());
                    Toast.makeText(getContext(), error.getMessage(), Toast.LENGTH_LONG).show();
                }
            });
            // Adding request to request queue
            Constant.getInstance().addToRequestQueue(jsonArrayRequest);

            //Delay time to show loading footer when debug, remove it when release
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            Log.e(TAG + " size2", arrPost.size() + "");
            //Send the result to Handle
            Message msg = mHandler.obtainMessage(1, arrPost);
            mHandler.sendMessage(msg);
        }
    }
}
