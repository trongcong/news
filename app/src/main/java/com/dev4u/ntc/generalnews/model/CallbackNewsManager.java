package com.dev4u.ntc.generalnews.model;

/**
 * IDE: Android Studio
 * Created by Nguyen Trong Cong  - 2DEV4U.COM
 * Name packge: com.dev4u.ntc.generalnews.utils
 * Name project: GeneralNews
 * Date: 2/10/2017
 * Time: 18:20
 */

public interface CallbackNewsManager {
    interface NewsCallback {
        void getResultNews(Post post);

        void onError(String error);
    }
}
