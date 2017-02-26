package com.dev4u.ntc.generalnews.view;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.RelativeLayout;

import com.dev4u.ntc.generalnews.R;
import com.dev4u.ntc.generalnews.adapter.NewsSaveBookMarkAdapter;
import com.dev4u.ntc.generalnews.model.Post;
import com.dev4u.ntc.generalnews.realm.RealmController;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.realm.Realm;

public class NewsSaveActivity extends AppCompatActivity implements AdapterView.OnItemClickListener {

    private static final String TAG = NewsSaveActivity.class.getSimpleName();
    @BindView(R.id.lvNewsSave)
    ListView lvNewsSave;
    @BindView(R.id.activity_news_save)
    RelativeLayout activityNewsSave;

    private NewsSaveBookMarkAdapter newsSaveBookMarkAdapter;
    private ArrayList<Post> arrPost;
    private Realm realm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news_save);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        ButterKnife.bind(this);
        //get realm instance
        this.realm = RealmController.with(this).getRealm();

        lvNewsSave.setOnItemClickListener(this);

        arrPost = new ArrayList<>();
        for (Post post : RealmController.getInstance().getPosts()) {
            // ... do something with the object ...
            arrPost.add(post);
        }
        loadListView();
    }

    private void loadListView() {
        newsSaveBookMarkAdapter = new NewsSaveBookMarkAdapter(this, R.layout.item_list_news, arrPost);
        lvNewsSave.setAdapter(newsSaveBookMarkAdapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.news_detail_menu, menu);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case android.R.id.home:
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        Intent intent = new Intent(this, NewsDetailActivity.class);
        intent.putExtra("arrPost", arrPost);
        Log.e(TAG, arrPost.size() + "");
        intent.putExtra("VT", i + "");
        intent.putExtra("Category", arrPost.get(i).getNameCategory());

        startActivity(intent);
    }
}
