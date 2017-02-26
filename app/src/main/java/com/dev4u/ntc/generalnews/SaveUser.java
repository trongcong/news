package com.dev4u.ntc.generalnews;

import android.content.Context;
import android.content.SharedPreferences;

import com.dev4u.ntc.generalnews.model.User;

/**
 * IDE: Android Studio
 * Created by Nguyen Trong Cong  - 2DEV4U.COM
 * Name packge: com.dev4u.ntc.generalnews
 * Name project: GeneralNews
 * Date: 2/13/2017
 * Time: 23:21
 */

public class SaveUser {
    public static final String SHARED_PREFERENCES_NAME = "preference_file_key";
    public static final String PREFERENCES_ID = "key_save_id";
    public static final String PREFERENCES_EMAIL = "key_save_email";
    public static final String PREFERENCES_NAME = "key_save_name";
    public static final String PREFERENCES_AVATAR = "key_save_avatar";
    // All Shared Preferences Keys
    private static final String IS_LOGIN = "IsLoggedIn";

    private static SaveUser suInstance;
    SharedPreferences sharedpreferences;
    SharedPreferences.Editor editor;
    private Context context;

    public static SaveUser getInstance() {
        if (suInstance == null) {
            suInstance = new SaveUser();
        }
        return suInstance;
    }

    public void init(Context context) {
        this.context = context;
        sharedpreferences = context.getSharedPreferences(SHARED_PREFERENCES_NAME, Context.MODE_PRIVATE);
        editor = sharedpreferences.edit();
    }

    // int id, String name, int age, String sex, String address, int phoneNumber
    // Get object doctor from preference file
    public User getUser() {
        int id_user = sharedpreferences.getInt(PREFERENCES_ID, 0);
        String email = sharedpreferences.getString(PREFERENCES_EMAIL, "");
        String name = sharedpreferences.getString(PREFERENCES_NAME, "");
        String avatar = sharedpreferences.getString(PREFERENCES_AVATAR, "");
        User user = new User(id_user, email, name, avatar);
        return user;
    }

    public void setUser(User user) {
        setLogin(true);
        editor.putInt(PREFERENCES_ID, user.getId_user());
        editor.putString(PREFERENCES_EMAIL, user.getEmail());
        editor.putString(PREFERENCES_NAME, user.getName());
        editor.putString(PREFERENCES_AVATAR, user.getAvatar());
        save();
    }

    public void setLogin(boolean b) {
        editor.putBoolean(IS_LOGIN, b);
        save();
    }

    // Get Login State
    public boolean isLoggedIn() {
        return sharedpreferences.getBoolean(IS_LOGIN, false);
    }

    private void save() {
        editor.commit();
    }
}
