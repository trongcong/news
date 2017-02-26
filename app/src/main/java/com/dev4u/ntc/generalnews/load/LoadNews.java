package com.dev4u.ntc.generalnews.load;

import android.content.ContentValues;
import android.content.Context;
import android.util.Log;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.StringRequest;
import com.dev4u.ntc.generalnews.Constant;
import com.dev4u.ntc.generalnews.model.Category;
import com.dev4u.ntc.generalnews.model.Comment;
import com.dev4u.ntc.generalnews.model.Post;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import static com.dev4u.ntc.generalnews.Constant.COUNT_COMMENT_BY_ID_POST_URL;
import static com.dev4u.ntc.generalnews.Constant.TAG;

/**
 * IDE: Android Studio
 * Created by Nguyen Trong Cong  - 2DEV4U.COM
 * Name packge: com.dev4u.ntc.generalnews
 * Name project: GeneralNews
 * Date: 2/12/2017
 * Time: 16:46
 */

public class LoadNews {
    LoadNewsCallBack loadNewsCallBack;
    Context context;

    public LoadNews(Context context) {
        this.context = context;
    }

    public LoadNews(LoadNewsCallBack newsCallBack) {
        this.loadNewsCallBack = newsCallBack;
    }

    /*
    * actionNews is : ex: number load news or load new by id_ca
    */
    public void getNews(int actionNews, final ArrayList<Post> arrPost, String urlNews) {
        // showing refresh animation before making http call
        loadNewsCallBack.setRefreshingLayout(true);
        Log.e(TAG + " link", Constant.LATEST_NEWS_URL + actionNews);
        // Volley's json array request object
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, urlNews + actionNews,
                null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                if (response.length() > 0) {
                    try {
                        arrPost.clear();
                        for (int i = 0; i < response.length(); i++) {
                            JSONObject post = response.getJSONObject(i);
                            Post p = new Post(post);
                            loadNewsCallBack.updateDataListNews(p);
                        }
                    } catch (JSONException e) {
                        Log.e(TAG, "JSON Parsing error: " + e.getMessage());
                    }
                }
                // stopping swipe refresh
                loadNewsCallBack.setRefreshingLayout(false);
                // swipeRefreshLayout.setRefreshing(false);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(TAG, "Server Error: " + error.getMessage());
                // stopping swipe refresh
                loadNewsCallBack.setRefreshingLayout(false);
                // swipeRefreshLayout.setRefreshing(false);
            }
        });
        // Adding request to request queue
        Constant.getInstance().addToRequestQueue(jsonArrayRequest);
    }

    public void getCategory(final ArrayList<Category> arrCategory, String urlNews) {
        loadNewsCallBack.setRefreshingLayout(true);
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET,
                urlNews, null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                if (response.length() > 0) {
                    try {
                        arrCategory.clear();
                        for (int i = 0; i < response.length(); i++) {
                            JSONObject ca = response.getJSONObject(i);
                            Category category = new Category(ca);
                            loadNewsCallBack.updateDataListCa(category);
                        }
                    } catch (JSONException e) {
                        Log.e("JSONException", "JSON Parsing error: " + e.getMessage());
                    }
                }
                loadNewsCallBack.setRefreshingLayout(false);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("err", "Server Error: " + error.getMessage());
                loadNewsCallBack.setRefreshingLayout(false);
            }
        });
        // Adding request to request queue
        Constant.getInstance().addToRequestQueue(jsonArrayRequest);
    }

    public void getCountComment(int idPost, final TextView tvCommentH) {
        StringRequest stringRequest = new StringRequest(Request.Method.GET,
                COUNT_COMMENT_BY_ID_POST_URL + idPost, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                if (!response.equals("Count comment failure")) {
                    tvCommentH.setText(response);
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(ContentValues.TAG, error.toString());
            }
        });
        Constant.getInstance().addToRequestQueue(stringRequest);
    }

    public void getComment(int idPost) {
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET,
                Constant.GET_COMMENT_BY_ID_POST_URL + idPost, null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                if (response.length() > 0) {
                    try {
                        for (int i = 0; i < response.length(); i++) {
                            JSONObject c = response.getJSONObject(i);
                            Comment comment = new Comment(c);
                            loadNewsCallBack.updateDataListComment(comment);
                        }
                    } catch (JSONException e) {
                        Log.e("JSONException", "JSON Parsing error: " + e.getMessage());
                    }
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("err", "Server Error: " + error.getMessage());
            }
        });
        // Adding request to request queue
        Constant.getInstance().addToRequestQueue(jsonArrayRequest);
    }
}

