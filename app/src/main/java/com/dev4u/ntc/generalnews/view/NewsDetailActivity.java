package com.dev4u.ntc.generalnews.view;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.dev4u.ntc.generalnews.R;
import com.dev4u.ntc.generalnews.SaveUser;
import com.dev4u.ntc.generalnews.adapter.ComentBottomSheetDialog;
import com.dev4u.ntc.generalnews.adapter.NewsDetailViewPagerAdapter;
import com.dev4u.ntc.generalnews.load.LoadNews;
import com.dev4u.ntc.generalnews.model.Post;
import com.dev4u.ntc.generalnews.realm.RealmController;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.realm.Realm;

import static com.dev4u.ntc.generalnews.Constant.TAG;
import static com.dev4u.ntc.generalnews.Constant.showToast;

public class NewsDetailActivity extends AppCompatActivity {

    //    private static final int RC_SIGN_IN = 9001;
    ArrayList<Post> arrPost;
    int VT;
    String namCategory;
    boolean checkLike, checkBookMark = false;
    @BindView(R.id.viewPagerDetailNews)
    ViewPager viewPagerDetailNews;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.llShareDetailNews)
    LinearLayout llShareDetailNews;
    @BindView(R.id.imgLikeDetailNews)
    ImageView imgLikeDetailNews;
    @BindView(R.id.imgBookMarkDetailNews)
    ImageView imgBookMarkDetailNews;
    @BindView(R.id.imgWriteCmtDetailNews)
    ImageView imgWriteCmtDetailNews;
    @BindView(R.id.imgShowCommentDetailNews)
    ImageView imgCommentDetailNews;
    LoadNews loadNews;
    @BindView(R.id.tvCountCmtDetailNews)
    TextView tvCountCmtDetailNews;
    private Menu menu;
    private Realm realm;
    private int id_post;

//    /* Client used to interact with Google APIs. */
//    private GoogleApiClient mGoogleApiClient;
//    private FirebaseAuth mAuth;
//    private FirebaseAuth.AuthStateListener mAuthListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news_detail);
        ButterKnife.bind(this);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        //get realm instance
        this.realm = RealmController.with(this).getRealm();
        SaveUser.getInstance().init(this);

        initView();
        setViewPager();
    }

    @SuppressLint("LongLogTag")
    @OnClick({R.id.llShareDetailNews, R.id.imgLikeDetailNews, R.id.imgBookMarkDetailNews, R.id.imgWriteCmtDetailNews, R.id.imgShowCommentDetailNews})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.llShareDetailNews:
                String formattedString = Html
                        .fromHtml(arrPost.get(viewPagerDetailNews.getCurrentItem()).getDescription()).toString();
                Intent sendIntent = new Intent();
                sendIntent.setAction(Intent.ACTION_SEND);
                sendIntent.putExtra(Intent.EXTRA_TEXT, formattedString + "\n"
                        + "Application from PlayStore "
                        + "https://play.google.com/store/apps/details?id=" + getPackageName());
                sendIntent.setType("text/plain");
                startActivity(sendIntent);
                break;
            case R.id.imgLikeDetailNews:
                likeEvent();
                break;
            case R.id.imgBookMarkDetailNews:
                saveBookMarkEvent();
                break;
            case R.id.imgWriteCmtDetailNews:
                Log.e("imgWriteCmtDetailNews isLoggedIn", SaveUser.getInstance().isLoggedIn() + "");
                if (SaveUser.getInstance().isLoggedIn() == false) {
                    showSiginForm();
                } else {
                    showWriteCommentForm();
                }
                break;
            case R.id.imgShowCommentDetailNews:
                if (SaveUser.getInstance().isLoggedIn() == false) {
                    showSiginForm();
                } else {
                    showCommentForm();
                }
                break;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        this.menu = menu;
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
            case R.id.menu_back:
                VT = viewPagerDetailNews.getCurrentItem() - 1;
                viewPagerDetailNews.setCurrentItem(VT);
                return true;
            case R.id.menu_next:
                VT = viewPagerDetailNews.getCurrentItem() + 1;
                viewPagerDetailNews.setCurrentItem(VT);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void initView() {
        arrPost = new ArrayList<>();
        loadNews = new LoadNews(this);

        arrPost = getIntent().getParcelableArrayListExtra("arrPost");
        VT = Integer.parseInt(getIntent().getStringExtra("VT"));
        namCategory = getIntent().getStringExtra("Category");

        if (arrPost.isEmpty() || namCategory.isEmpty()) {
            finish();
            showToast("Lỗi hiển thị", Toast.LENGTH_SHORT);
        } else {
            id_post = arrPost.get(VT).getId_post();
            Log.d("VT", VT + " Id_post " + id_post + " arsize " + arrPost.size());
            loadNews.getCountComment(id_post, tvCountCmtDetailNews);
            checkSaveBookMark(id_post);
        }

//        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
//        getSupportActionBar().setDisplayShowCustomEnabled(true);
//        getSupportActionBar().setCustomView(R.layout.custom_action_bar_layout);
//        View view = getSupportActionBar().getCustomView();
//        TextView tvTitleCustom = (TextView) view.findViewById(R.id.tvTitleCustom);
//        tvTitleCustom.setText(post.getTitle().toString());
    }

    private void setViewPager() {
        viewPagerDetailNews.setAdapter(new NewsDetailViewPagerAdapter(this, arrPost));
        viewPagerDetailNews.setCurrentItem(VT);
        viewPagerDetailNews.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                VT = position;
                getSupportActionBar().setTitle(arrPost.get(VT).getTitle().toString());
            }

            @Override
            public void onPageSelected(int position) {
                namCategory = arrPost.get(position).getNameCategory();
                id_post = arrPost.get(position).getId_post();
                loadNews.getCountComment(id_post, tvCountCmtDetailNews);
                Log.e("id_post", arrPost.get(position).getId_post() + "");
                checkSaveBookMark(id_post);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    private void likeEvent() {
        if (checkLike == true) {
            imgLikeDetailNews.setImageResource(R.drawable.ic_unlike_grey);
            showToast("Hủy yêu thích", Toast.LENGTH_SHORT);
            checkLike = false;
        } else {
//            menu.getItem(0).setIcon(getResources().getDrawable(R.drawable.ic_detail_save_new_tick));
            imgLikeDetailNews.setImageResource(R.drawable.ic_like_grey);
            showToast("Yêu thích", Toast.LENGTH_SHORT);
            checkLike = true;
        }
    }

    private void saveBookMarkEvent() {
        if (checkBookMark == true) {
            imgBookMarkDetailNews.setImageResource(R.drawable.ic_unbookmark_grey);
            RealmController.getInstance().clearPost(id_post);

            showToast("Hủy lưu tin tức", Toast.LENGTH_SHORT);
            Log.e("realm", arrPost.size() + " - " + RealmController.getInstance().getPosts().size());
            checkBookMark = false;
        } else {
            imgBookMarkDetailNews.setImageResource(R.drawable.ic_bookmark_grey);
            RealmController.getInstance().savePost(arrPost.get(viewPagerDetailNews.getCurrentItem()));

            showToast("Lưu tin tức thành công", Toast.LENGTH_SHORT);
            Log.e("realm", arrPost.size() + " - " + RealmController.getInstance().getPosts().size());
            checkBookMark = true;
        }
    }

    public void checkSaveBookMark(int idPost) {
        if (RealmController.getInstance().hasPostById(idPost) == true) {
            imgBookMarkDetailNews.setImageResource(R.drawable.ic_bookmark_grey);
            Log.e("realm Check", RealmController.getInstance().getPosts().size() + "");
            checkBookMark = true;
        } else {
            imgBookMarkDetailNews.setImageResource(R.drawable.ic_unbookmark_grey);
            Log.e("realm Check", RealmController.getInstance().getPosts().size() + "");
            checkBookMark = false;
        }
    }

    public void checkLike(int idPost) {
        int postLike = isLike(idPost);
        if (postLike == 1) {
            imgLikeDetailNews.setImageResource(R.drawable.ic_like_grey);
//            lcYT = 0;
            checkLike = true;
        } else {
            imgLikeDetailNews.setImageResource(R.drawable.ic_unlike_grey);
//            lcYT = 1;
            checkLike = false;
        }
    }

    private int isLike(int idPost) {
        //TODO: check like
        if (true) {
            return 1;
        }
        return 0;
    }

    private void showWriteCommentForm() {
        Log.e(TAG, "id user: " + SaveUser.getInstance().getUser().getId_user() + " - id post: " + id_post);
        ComentBottomSheetDialog mBottomSheet = ComentBottomSheetDialog.
                newInstance("Comment", SaveUser.getInstance().getUser().getId_user(), id_post);

        mBottomSheet.show(getSupportFragmentManager(), mBottomSheet.getTag());
    }

    public void showSiginForm() {
        ComentBottomSheetDialog mBottomSheet = ComentBottomSheetDialog.
                newInstance("Login", 0, 0);

        mBottomSheet.show(getSupportFragmentManager(), mBottomSheet.getTag());
    }

    public void showCommentForm() {
        ComentBottomSheetDialog mBottomSheet = ComentBottomSheetDialog.
                newInstance("ShowComment", SaveUser.getInstance().getUser().getId_user(), id_post);
        mBottomSheet.show(getSupportFragmentManager(), mBottomSheet.getTag());
    }
}
