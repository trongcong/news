package com.dev4u.ntc.generalnews.view;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.dev4u.ntc.generalnews.Constant;
import com.dev4u.ntc.generalnews.R;
import com.dev4u.ntc.generalnews.adapter.NewsByCategoryAdapter;
import com.dev4u.ntc.generalnews.load.LoadNews;
import com.dev4u.ntc.generalnews.load.LoadNewsCallBack;
import com.dev4u.ntc.generalnews.model.Category;
import com.dev4u.ntc.generalnews.model.Comment;
import com.dev4u.ntc.generalnews.model.Post;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class NewsByCategoryActivity extends AppCompatActivity
        implements LoadNewsCallBack, AdapterView.OnItemClickListener, SwipeRefreshLayout.OnRefreshListener {

    private static final String TAG = NewsByCategoryActivity.class.getSimpleName();
    @BindView(R.id.lvPostMain)
    ListView lvPostMain;
    LoadNews loadNews;
    @BindView(R.id.mContainerSwipeRefreshLayout)
    SwipeRefreshLayout swipeRefreshLayout;
    private String idCa, nameCa;
    private NewsByCategoryAdapter newsByCategoryAdapter;
    private ArrayList<Post> arrPost;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setContentView(R.layout.activity_list_news_by_category);
        ButterKnife.bind(this);
        arrPost = new ArrayList<>();
        loadNews = new LoadNews((LoadNewsCallBack) this);

        lvPostMain.setOnItemClickListener(this);
        swipeRefreshLayout.setOnRefreshListener(this);
        swipeRefreshLayout.setColorScheme(android.R.color.holo_blue_bright,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light);
        initListViewByCategory();

        idCa = getIntent().getStringExtra("idCa");
        nameCa = getIntent().getStringExtra("nameCa");
        Log.e("Ca", idCa + nameCa.toString());

        if (idCa.isEmpty() || nameCa.isEmpty()) {
            Toast.makeText(this, "Lỗi hiển thị..!!", Toast.LENGTH_SHORT).show();
            finish();
        } else {
            getSupportActionBar().setTitle(nameCa);
            swipeRefreshLayout.post(new Runnable() {
                                        @Override
                                        public void run() {
//                                            getNewsByCategory();
                                            loadNews.getNews(Integer.parseInt(idCa), arrPost, Constant.NEWS_BY_CATEGORY_URL);
                                        }
                                    }
            );
        }
    }

    @Override
    public void onRefresh() {
//        getNewsByCategory();
        loadNews.getNews(Integer.parseInt(idCa), arrPost, Constant.NEWS_BY_CATEGORY_URL);
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
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        Intent intent = new Intent(this, NewsDetailActivity.class);
        intent.putExtra("arrPost", arrPost);
        Log.e(TAG, arrPost.size() + "");
        intent.putExtra("VT", i + "");
        intent.putExtra("Category", nameCa);

        startActivity(intent);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case android.R.id.home:
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void initListViewByCategory() {
        newsByCategoryAdapter = new NewsByCategoryAdapter(this, R.layout.item_list_news, arrPost, loadNews);
        lvPostMain.setAdapter(newsByCategoryAdapter);
    }

    private void addDataList(Post post) {
        arrPost.add(post);
        newsByCategoryAdapter.notifyDataSetChanged();
    }

//    private void getNewsByCategory() {
//        // showing refresh animation before making http call
//        swipeRefreshLayout.setRefreshing(true);
//        Log.e("link", Constant.NEWS_BY_CATEGORY_URL + idCa);
//        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET,
//                Constant.NEWS_BY_CATEGORY_URL + idCa, null, new Response.Listener<JSONArray>() {
//            @Override
//            public void onResponse(JSONArray response) {
//                if (response.length() > 0) {
//                    try {
//                        arrPost.clear();
//                        for (int i = 0; i < response.length(); i++) {
//                            JSONObject post = response.getJSONObject(i);
//                            Post p = new Post(post);
//                            addDataList(p);
//                        }
//                    } catch (JSONException e) {
//                        Log.e("JSONException", "JSON Parsing error: " + e.getMessage());
//                    }
//                }
//                // stopping swipe refresh
//                swipeRefreshLayout.setRefreshing(false);
//            }
//        }, new Response.ErrorListener() {
//            @Override
//            public void onErrorResponse(VolleyError error) {
//                Log.e("err", "Server Error: " + error.getMessage());
//                // stopping swipe refresh
//                swipeRefreshLayout.setRefreshing(false);
//            }
//        });
//        // Adding request to request queue
//        Constant.getInstance().addToRequestQueue(jsonArrayRequest);
//    }
}
