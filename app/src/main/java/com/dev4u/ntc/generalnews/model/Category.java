package com.dev4u.ntc.generalnews.model;

import android.os.Parcel;
import android.os.Parcelable;

import org.json.JSONException;
import org.json.JSONObject;

import io.realm.RealmModel;
import io.realm.annotations.PrimaryKey;

/**
 * IDE: Android Studio
 * Created by Nguyen Trong Cong  - 2DEV4U.COM
 * Name packge: com.mic.internship.micnews.Obj
 * Name project: MICNews
 * Date: 1/3/2017
 * Time: 08:09
 */

public class Category implements Parcelable, RealmModel {
    public static final Creator<Category> CREATOR = new Creator<Category>() {
        @Override
        public Category createFromParcel(Parcel in) {
            return new Category(in);
        }

        @Override
        public Category[] newArray(int size) {
            return new Category[size];
        }
    };
    @PrimaryKey
    int id_ca;
    String nameCategory;

    public Category() {
    }

    public Category(JSONObject jCategory) {
        try {
            id_ca = jCategory.getInt("id_ca");
            nameCategory = jCategory.getString("name_ca");
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public Category(int id_ca, String nameCategory) {
        this.id_ca = id_ca;
        this.nameCategory = nameCategory;
    }

    protected Category(Parcel in) {
        id_ca = in.readInt();
        nameCategory = in.readString();
    }

    public String getNameCategory() {
        return nameCategory;
    }

    public void setNameCategory(String nameCategory) {
        this.nameCategory = nameCategory;
    }

    public int getId_ca() {
        return id_ca;
    }

    public void setId_ca(int id_ca) {
        this.id_ca = id_ca;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(id_ca);
        parcel.writeString(nameCategory);
    }
}
