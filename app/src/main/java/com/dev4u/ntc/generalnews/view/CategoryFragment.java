package com.dev4u.ntc.generalnews.view;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.dev4u.ntc.generalnews.Constant;
import com.dev4u.ntc.generalnews.R;
import com.dev4u.ntc.generalnews.adapter.CategoryAdapter;
import com.dev4u.ntc.generalnews.load.LoadNews;
import com.dev4u.ntc.generalnews.load.LoadNewsCallBack;
import com.dev4u.ntc.generalnews.model.Category;
import com.dev4u.ntc.generalnews.model.Comment;
import com.dev4u.ntc.generalnews.model.Post;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * IDE: Android Studio
 * Created by Nguyen Trong Cong  - 2DEV4U.COM
 * Name packge: com.dev4u.ntc.generalnews.view
 * Name project: GeneralNews
 * Date: 2/8/2017
 * Time: 13:38
 */

public class CategoryFragment extends Fragment implements LoadNewsCallBack, SwipeRefreshLayout.OnRefreshListener {
    @BindView(R.id.mRecyclerViewCategory)
    RecyclerView mRecyclerViewCategory;
    CategoryAdapter categoryAdapter;
    ArrayList<Category> arrCategory;
    @BindView(R.id.mContainerSwipeRefreshLayout)
    SwipeRefreshLayout mContainerSwipeRefreshLayout;
    LoadNews loadNews;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        loadNews = new LoadNews(this);
        arrCategory = new ArrayList<>();

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View layout = inflater.inflate(R.layout.fragment_category, container, false);
        ButterKnife.bind(this, layout);

        initGridView();
        mContainerSwipeRefreshLayout.setOnRefreshListener(this);
        mContainerSwipeRefreshLayout.setColorScheme(android.R.color.holo_blue_bright,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light);
        mContainerSwipeRefreshLayout.post(new Runnable() {
                                              @Override
                                              public void run() {
//                                                  getCategory();
                                                  loadNews.getCategory(arrCategory, Constant.CATEGORY_URL);
                                              }
                                          }
        );
        mRecyclerViewCategory.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(getContext(), 2);
        mRecyclerViewCategory.setLayoutManager(layoutManager);

//        getCategory();
        return layout;
    }

    @Override
    public void onRefresh() {
//        getCategory();
        loadNews.getCategory(arrCategory, Constant.CATEGORY_URL);
    }

//    private void getCategory() {
//        mContainerSwipeRefreshLayout.setRefreshing(true);
//        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET,
//                Constant.CATEGORY_URL, null, new Response.Listener<JSONArray>() {
//            @Override
//            public void onResponse(JSONArray response) {
//                if (response.length() > 0) {
//                    try {
//                        arrCategory.clear();
//                        for (int i = 0; i < response.length(); i++) {
//                            JSONObject ca = response.getJSONObject(i);
//                            //TODO: Fix
//                            Category p = new Category(ca.getInt("id_ca"), ca.getString("name_ca"));
//                            arrCategory.add(p);
//                        }
//                        initGridView();
//                    } catch (JSONException e) {
//                        Log.e("JSONException", "JSON Parsing error: " + e.getMessage());
//                    }
//                    categoryAdapter.notifyDataSetChanged();
//                }
//                mContainerSwipeRefreshLayout.setRefreshing(false);
//            }
//        }, new Response.ErrorListener() {
//            @Override
//            public void onErrorResponse(VolleyError error) {
//                Log.e("err", "Server Error: " + error.getMessage());
//                Toast.makeText(getContext(), error.getMessage(), Toast.LENGTH_LONG).show();
//                mContainerSwipeRefreshLayout.setRefreshing(false);
//            }
//        });
//        // Adding request to request queue
//        Constant.getInstance().addToRequestQueue(jsonArrayRequest);
//    }

    private void initGridView() {
        categoryAdapter = new CategoryAdapter(getContext(), arrCategory);
        mRecyclerViewCategory.setAdapter(categoryAdapter);
    }

    private void addDataList(Category ca) {
        arrCategory.add(ca);
        categoryAdapter.notifyDataSetChanged();
    }

    @Override
    public void updateDataListNews(Post post) {

    }

    @Override
    public void updateDataListCa(Category category) {
        addDataList(category);
    }

    @Override
    public void updateDataListComment(Comment comment) {

    }

    @Override
    public void setRefreshingLayout(boolean b) {
        mContainerSwipeRefreshLayout.setRefreshing(b);
    }
}
