package com.dev4u.ntc.generalnews.model;

import android.os.Parcel;
import android.os.Parcelable;

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

public class Comment implements Parcelable {

    public static final Creator<Comment> CREATOR = new Creator<Comment>() {
        @Override
        public Comment createFromParcel(Parcel in) {
            return new Comment(in);
        }

        @Override
        public Comment[] newArray(int size) {
            return new Comment[size];
        }
    };
    int id_comment;
    int id_user;
    int id_post;
    String comment;
    String time_cmt;
    String name;
    String avatar;

    protected Comment(Parcel in) {
        id_comment = in.readInt();
        id_user = in.readInt();
        id_post = in.readInt();
        comment = in.readString();
        time_cmt = in.readString();
        name = in.readString();
        avatar = in.readString();
    }

    public Comment(JSONObject jPost) {
        try {
            id_comment = jPost.getInt("id_comment");
            id_user = jPost.getInt("id_user");
            id_post = jPost.getInt("id_post");
            comment = jPost.getString("comment");
            time_cmt = jPost.getString("time_cmt");
            name = jPost.getString("name");
            avatar = jPost.getString("avatar");
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public Comment(int id_comment, int id_user, int id_post, String comment, String time_cmt, String name, String avatar) {
        this.id_comment = id_comment;
        this.id_user = id_user;
        this.id_post = id_post;
        this.comment = comment;
        this.time_cmt = time_cmt;
        this.name = name;
        this.avatar = avatar;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(id_comment);
        parcel.writeInt(id_user);
        parcel.writeInt(id_post);
        parcel.writeString(comment);
        parcel.writeString(time_cmt);
        parcel.writeString(name);
        parcel.writeString(avatar);
    }

    public int getId_comment() {
        return id_comment;
    }

    public void setId_comment(int id_comment) {
        this.id_comment = id_comment;
    }

    public int getId_user() {
        return id_user;
    }

    public void setId_user(int id_user) {
        this.id_user = id_user;
    }

    public int getId_post() {
        return id_post;
    }

    public void setId_post(int id_post) {
        this.id_post = id_post;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getTime_cmt() {
        return time_cmt;
    }

    public void setTime_cmt(String time_cmt) {
        this.time_cmt = time_cmt;
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
}
