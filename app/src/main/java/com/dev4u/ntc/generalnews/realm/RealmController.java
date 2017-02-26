package com.dev4u.ntc.generalnews.realm;

import android.app.Activity;
import android.app.Application;
import android.support.v4.app.Fragment;

import com.dev4u.ntc.generalnews.model.Post;

import io.realm.Realm;
import io.realm.RealmResults;


public class RealmController {

    private static RealmController instance;
    private final Realm realm;

    public RealmController(Application application) {
        realm = Realm.getDefaultInstance();
    }

    public static RealmController with(Fragment fragment) {

        if (instance == null) {
            instance = new RealmController(fragment.getActivity().getApplication());
        }
        return instance;
    }

    public static RealmController with(Activity activity) {

        if (instance == null) {
            instance = new RealmController(activity.getApplication());
        }
        return instance;
    }

    public static RealmController with(Application application) {

        if (instance == null) {
            instance = new RealmController(application);
        }
        return instance;
    }

    public static RealmController getInstance() {

        return instance;
    }

    public Realm getRealm() {

        return realm;
    }

    //Refresh the realm istance
    public void refresh() {

        realm.isAutoRefresh();
    }

    //save  objects from Book.class
    public void savePost(Post post) {
        // Persist your data easily
        realm.beginTransaction();
        realm.copyToRealm(post);
        realm.commitTransaction();
    }

    //clear all objects from Book.class
    public void clearAll() {
        realm.beginTransaction();
        realm.deleteAll();
        realm.commitTransaction();
    }

    //clear objects by id_post from Book.class
    public void clearPost(int id_post) {
        realm.beginTransaction();
        getPost(id_post).deleteFromRealm();
        realm.commitTransaction();
        refresh();
    }

    //find all objects in the Book.class
    public RealmResults<Post> getPosts() {
        return realm.where(Post.class).findAll();
    }

    //query a single item with the given id
    public Post getPost(int id_post) {
        return realm.where(Post.class).equalTo("id_post", id_post).findFirst();
    }

    //check if Book.class is empty
    public boolean hasPosts() {
        return !realm.where(Post.class).findAll().isEmpty();
    }

    //check if Book.class by id_post is empty
    public boolean hasPostById(int id_post) {
        return !realm.where(Post.class).equalTo("id_post", id_post).findAll().isEmpty();
    }

    //query example
    public RealmResults<Post> queryedPosts() {

        return realm.where(Post.class)
                .contains("author", "Author 0")
                .or()
                .contains("title", "Realm")
                .findAll();

    }
}
