package com.dev4u.ntc.generalnews.model;

/**
 * IDE: Android Studio
 * Created by Nguyen Trong Cong  - 2DEV4U.COM
 * Name packge: com.dev4u.ntc.generalnews.model
 * Name project: GeneralNews
 * Date: 2/10/2017
 * Time: 18:32
 */

public class NewsManager {
    public static NewsManager sharedInstance = new NewsManager();
    CallbackNewsManager.NewsCallback newsCallback;

    NewsManager() {
//        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, Constant.LATEST_NEWS_URL + 10, null, new Response.Listener<JSONArray>() {
//            @Override
//            public void onResponse(JSONArray response) {
//                if (response.length() > 0) {
//                    try {
//                        return response;
//                        for (int i = 0; i < response.length(); i++) {
//                            JSONObject postJ = response.getJSONObject(i);
//                            //namCategory = postJ.getString("name_ca").toString();
//
//
//                        }
//                    } catch (JSONException e) {
//                        Log.e(TAG, "JSON Parsing error: " + e.getMessage());
//                    }
//                }
//            }
//        }, new Response.ErrorListener() {
//            @Override
//            public void onErrorResponse(VolleyError error) {
//                Log.e(TAG, "Server Error: " + error.getMessage());
//                showToast(error.getMessage());
//            }
//        });
//        // Adding request to request queue
//        Constant.getInstance().addToRequestQueue(jsonArrayRequest);
    }

    public void setQuerNews(String nameCategory, CallbackNewsManager.NewsCallback call) {

        newsCallback = call;
    }
}
