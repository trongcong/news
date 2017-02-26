package com.dev4u.ntc.generalnews.model;

import android.os.Parcel;
import android.os.Parcelable;

import org.json.JSONException;
import org.json.JSONObject;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * IDE: Android Studio
 * Created by Nguyen Trong Cong  - 2DEV4U.COM
 * Name packge: com.dev4u.ntc.generalnews.model
 * Name project: GeneralNews
 * Date: 2/8/2017
 * Time: 15:01
 */

public class Post extends RealmObject implements Parcelable {

    public static final Creator<Post> CREATOR = new Creator<Post>() {
        @Override
        public Post createFromParcel(Parcel in) {
            return new Post(in);
        }

        @Override
        public Post[] newArray(int size) {
            return new Post[size];
        }
    };
    @PrimaryKey
    int id_post;
    int id_ca;
    String nameCategory;
    String title;
    String description;
    String content;
    String datetime;
    String image;

    public Post() {
    }

    public Post(JSONObject jPost) {
        try {
            id_post = jPost.getInt("id_post");
            id_ca = jPost.getInt("id_ca");
            nameCategory = jPost.getString("name_ca");
            title = jPost.getString("title");
            description = jPost.getString("description");
            content = jPost.getString("content");
            datetime = jPost.getString("time");
            image = jPost.getString("image");
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public Post(int id_post, int id_ca, String nameCategory, String title, String description, String content, String datetime, String image) {
        this.id_post = id_post;
        this.id_ca = id_ca;
        this.nameCategory = nameCategory;
        this.title = title;
        this.description = description;
        this.content = content;
        this.datetime = datetime;
        this.image = image;
    }

    protected Post(Parcel in) {
        id_post = in.readInt();
        id_ca = in.readInt();
        nameCategory = in.readString();
        title = in.readString();
        description = in.readString();
        content = in.readString();
        datetime = in.readString();
        image = in.readString();
    }

    public int getId_post() {
        return id_post;
    }

    public void setId_post(int id_post) {
        this.id_post = id_post;
    }

    public int getId_ca() {
        return id_ca;
    }

    public void setId_ca(int id_ca) {
        this.id_ca = id_ca;
    }

    public String getNameCategory() {
        return nameCategory;
    }

    public void setNameCategory(String nameCategory) {
        this.nameCategory = nameCategory;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getDatetime() {
        return datetime;
    }

    public void setDatetime(String datetime) {
        this.datetime = datetime;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(id_post);
        parcel.writeInt(id_ca);
        parcel.writeString(nameCategory);
        parcel.writeString(title);
        parcel.writeString(description);
        parcel.writeString(content);
        parcel.writeString(datetime);
        parcel.writeString(image);
    }
}
