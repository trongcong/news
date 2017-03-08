package com.dev4u.ntc.generalnews.view;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.dev4u.ntc.generalnews.R;
import com.dev4u.ntc.generalnews.adapter.NewsSaveBookMarkAdapter;
import com.dev4u.ntc.generalnews.load.LoadNews;
import com.dev4u.ntc.generalnews.load.LoadNewsCallBack;
import com.dev4u.ntc.generalnews.model.Category;
import com.dev4u.ntc.generalnews.model.Comment;
import com.dev4u.ntc.generalnews.model.Post;
import com.dev4u.ntc.generalnews.realm.RealmController;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.realm.Realm;
import io.realm.RealmResults;

import static com.dev4u.ntc.generalnews.Constant.showToast;

public class NewsSaveActivity extends AppCompatActivity implements AdapterView.OnItemClickListener,
        AdapterView.OnItemLongClickListener, LoadNewsCallBack {

    private static final String TAG = NewsSaveActivity.class.getSimpleName();
    @BindView(R.id.lvNewsSave)
    ListView lvNewsSave;
    @BindView(R.id.activity_news_save)
    RelativeLayout activityNewsSave;
    RealmResults<Post> posts;
    LoadNews loadNews;
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

        arrPost = new ArrayList<>();
        loadNews = new LoadNews(this);
        loadListView();
        lvNewsSave.setOnItemClickListener(this);
        lvNewsSave.setOnItemLongClickListener(this);
        posts = RealmController.getInstance().getPosts();
        for (Post post : posts) {
            addDataList(post);
        }
    }

    private void loadListView() {
        newsSaveBookMarkAdapter = new NewsSaveBookMarkAdapter(this, R.layout.item_list_news, arrPost, loadNews);
        lvNewsSave.setAdapter(newsSaveBookMarkAdapter);
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

    @Override
    public boolean onItemLongClick(final AdapterView<?> adapterView, View view, final int i, long l) {
        view = getLayoutInflater().inflate(R.layout.show_check_conection_form, null);
        Button btnThuLai = (Button) view.findViewById(R.id.btnThuLai);
        TextView tvMessage = (TextView) view.findViewById(R.id.tvMessage);
        tvMessage.setText("Xóa bài viết " + RealmController.getInstance().getPost(arrPost.get(i).getId_post()).getTitle());
        btnThuLai.setText("Ok");

        AlertDialog.Builder alert = new AlertDialog.Builder(this);
        alert.setView(view);
        final AlertDialog show = alert.show();
        btnThuLai.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.e("realm clear post ", RealmController.getInstance().getPost(arrPost.get(i).getId_post()).getId_post() + "");

                showToast("Đã xóa " + posts.get(i).getTitle(), Toast.LENGTH_LONG);

                realm.executeTransaction(new Realm.Transaction() {
                    @Override
                    public void execute(Realm realm) {
//                        RealmController.getInstance().clearPost(arrPost.get(i).getId_post());
                        RealmController.getInstance().getPost(arrPost.get(i).getId_post()).deleteFromRealm();
                        posts = RealmController.getInstance().getPosts();
                        arrPost.clear();
                        for (Post post : posts) {
                            addDataList(post);
                        }
                    }
                });
                show.dismiss();
            }
        });
        return true;
    }

    private void addDataList(Post post) {
        arrPost.add(post);
        newsSaveBookMarkAdapter.notifyDataSetChanged();
    }

    @Override
    public void updateDataListNews(Post post) {

    }

    @Override
    public void updateDataListCa(Category category) {

    }

    @Override
    public void updateDataListComment(Comment comment) {

    }

    @Override
    public void setRefreshingLayout(boolean b) {

    }
}
