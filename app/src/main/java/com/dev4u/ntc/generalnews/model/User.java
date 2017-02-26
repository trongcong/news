package com.dev4u.ntc.generalnews.model;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * IDE: Android Studio
 * Created by Nguyen Trong Cong  - 2DEV4U.COM
 * Name packge: com.mic.internship.micnews.Obj
 * Name project: MICNews
 * Date: 1/3/2017
 * Time: 08:09
 */

public class User {
    int id_user;
    String email;
    String name;
    String avatar;

    public User(int id_user, String email, String name, String avatar) {
        this.id_user = id_user;
        this.email = email;
        this.name = name;
        this.avatar = avatar;
    }

    public User(JSONObject jUser) {
        try {
            id_user = jUser.getInt("id_user");
            email = jUser.getString("email");
            name = jUser.getString("name");
            avatar = jUser.getString("avatar");
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public int getId_user() {
        return id_user;
    }

    public void setId_user(int id_user) {
        this.id_user = id_user;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
