package com.dev4u.ntc.generalnews;

import android.app.Application;
import android.text.TextUtils;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import io.realm.Realm;
import io.realm.RealmConfiguration;

/**
 * IDE: Android Studio
 * Created by Nguyen Trong Cong  - 2DEV4U.COM
 * Name packge: com.dev4u.ntc.generalnews
 * Name project: GeneralNews
 * Date: 2/8/2017
 * Time: 15:40
 */

public class Constant extends Application {
    public static final String TAG = Constant.class.getSimpleName();
    //this url is used to get recent 20 news
    public static final String LATEST_NEWS_URL = "http://dev.2dev4u.com/MICNews/api.php?latest_news=";
    //    public static final String LATEST_NEWS_URL = "http://10.0.2.2/MICNews/api.php?latest_news=";
    //this url gives news by category
    public static final String NEWS_BY_CATEGORY_URL = "http://dev.2dev4u.com/MICNews/api.php?news_by_id_ca=";
    //    public static final String NEWS_BY_CATEGORY_URL = "http://10.0.2.2/MICNews/api.php?news_by_id_ca=";
    //this url gives list of category and Method _POST
    public static final String CATEGORY_URL = "http://dev.2dev4u.com/MICNews/api.php";
    //    public static final String CATEGORY_URL = "http://10.0.2.2/MICNews/api.php";
    //this url gives count comment by id_post of category
    public static final String COUNT_COMMENT_BY_ID_POST_URL = "http://dev.2dev4u.com/MICNews/api.php?count_comment_by_id_post=";
    //this url check user by email
    public static final String CHECK_USER_BY_EMAIL = "http://dev.2dev4u.com/MICNews/api.php?email=";
    //    public static final String COUNT_COMMENT_BY_ID_POST_URL = "http://10.0.2.2/MICNews/api.php?count_comment_by_id_post=";

    //this url gives list comment
    public static final String GET_COMMENT_BY_ID_POST_URL = "http://dev.2dev4u.com/MICNews/api.php?get_comment_by_id_post=";

    private static Constant mInstance;
    private RequestQueue mRequestQueue;

    public static synchronized Constant getInstance() {
        return mInstance;
    }

    public static void showToast(String msg, int timeToast) {
        Toast.makeText(getInstance().getApplicationContext(), msg, timeToast).show();
    }

    public static String FormatDateTime(String time) {
        SimpleDateFormat originFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        SimpleDateFormat resultFormat = new SimpleDateFormat("hh:mm dd/MM/yyyy");
        Date datetime = null;
        try {
            datetime = originFormat.parse(time);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return resultFormat.format(datetime);
    }

    public static String RemoveTag(String html, String i, String o) {
        html = html.replaceAll(i, o);
        return html;
    }

    public static String showDateTime() {

        return "";
    }

    @Override
    public void onCreate() {
        super.onCreate();
        mInstance = this;
        Realm.init(this);
        RealmConfiguration realmConfiguration = new RealmConfiguration.Builder()
                .name("news.realm")
                .schemaVersion(0)
                .deleteRealmIfMigrationNeeded()
                .build();
        Realm.setDefaultConfiguration(realmConfiguration);
    }

    public RequestQueue getRequestQueue() {
        if (mRequestQueue == null) {
            mRequestQueue = Volley.newRequestQueue(getApplicationContext());
        }
        return mRequestQueue;
    }

    public <T> void addToRequestQueue(Request<T> req, String tag) {
        req.setTag(TextUtils.isEmpty(tag) ? TAG : tag);
        getRequestQueue().add(req);
    }

    public <T> void addToRequestQueue(Request<T> req) {
        req.setTag(TAG);
        getRequestQueue().add(req);
    }

    public void cancelPendingRequests(Object tag) {
        if (mRequestQueue != null) {
            mRequestQueue.cancelAll(tag);
        }
    }
}
