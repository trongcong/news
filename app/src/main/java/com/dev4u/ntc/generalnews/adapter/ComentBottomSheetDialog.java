package com.dev4u.ntc.generalnews.adapter;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomSheetDialogFragment;
import android.support.v7.widget.AppCompatTextView;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.bumptech.glide.Glide;
import com.dev4u.ntc.generalnews.Constant;
import com.dev4u.ntc.generalnews.R;
import com.dev4u.ntc.generalnews.SaveUser;
import com.dev4u.ntc.generalnews.load.LoadNews;
import com.dev4u.ntc.generalnews.load.LoadNewsCallBack;
import com.dev4u.ntc.generalnews.model.Category;
import com.dev4u.ntc.generalnews.model.Comment;
import com.dev4u.ntc.generalnews.model.Post;
import com.dev4u.ntc.generalnews.model.User;
import com.dev4u.ntc.generalnews.view.MainActivity;
import com.dev4u.ntc.generalnews.widget.CircleImageView;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.android.volley.Request.Method.POST;
import static com.dev4u.ntc.generalnews.Constant.TAG;
import static com.dev4u.ntc.generalnews.Constant.showToast;

/**
 * IDE: Android Studio
 * Created by Nguyen Trong Cong  - 2DEV4U.COM
 * Name packge: com.dev4u.ntc.generalnews.adapter
 * Name project: GeneralNews
 * Date: 2/15/2017
 * Time: 23:01
 */

public class ComentBottomSheetDialog extends BottomSheetDialogFragment implements LoadNewsCallBack,
        GoogleApiClient.OnConnectionFailedListener {

    private static final int RC_SIGN_IN = 9001;
    int id_user, id_post;
    String action;
    @BindView(R.id.vClose)
    View vClose;
    @BindView(R.id.edComment)
    EditText edComment;
    @BindView(R.id.tvSendComment)
    TextView tvSendComment;
    @BindView(R.id.btnLoginGg)
    Button btnLoginGg;
    @BindView(R.id.btnLoginFb)
    Button btnLoginFb;
    @BindView(R.id.popupWindowLogin)
    LinearLayout popupWindowLogin;
    @BindView(R.id.popupWindowWriteComment)
    RelativeLayout popupWindowComment;
    @BindView(R.id.textView)
    TextView textView;
    @BindView(R.id.imgUser)
    CircleImageView imgUser;
    @BindView(R.id.edShowComment)
    EditText edShowComment;
    @BindView(R.id.popupWindowShowComment)
    RelativeLayout popupWindowShowComment;
    @BindView(R.id.mListViewComment)
    ListView mListViewComment;
    @BindView(R.id.vCloseL)
    View vCloseL;
    @BindView(R.id.mTVNoComment)
    AppCompatTextView mTVNoComment;

    /* Client used to interact with Google APIs. */
    private GoogleApiClient mGoogleApiClient;
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private CommentAdapter mCommentAdapter;
    private ArrayList<Comment> mArrComments;
    private LoadNews loadNews;

    public static ComentBottomSheetDialog newInstance(String action, int id_user, int id_post) {
        ComentBottomSheetDialog f = new ComentBottomSheetDialog();
        Bundle args = new Bundle();
        args.putInt("id_user", id_user);
        args.putInt("id_post", id_post);
        args.putString("action", action);
        f.setArguments(args);
        return f;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SaveUser.getInstance().init(getContext());

        loadNews = new LoadNews(this);
        mArrComments = new ArrayList<>();
        this.mGoogleApiClient = MainActivity.mGoogleApiClient;
        mAuth = FirebaseAuth.getInstance();
        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    Log.e("AUTH", "user logged in: " + user.getEmail());
                } else {
                    Log.e("AUTH", "user logged out ");
                }
            }
        };

        id_user = getArguments().getInt("id_user");
        id_post = getArguments().getInt("id_post");
        action = getArguments().getString("action");
        Log.e("action", action);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RC_SIGN_IN) {
            GoogleSignInResult signInResult = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            if (signInResult.isSuccess()) {
                // Google Sign In was successful, authenticate with Firebase
                GoogleSignInAccount account = signInResult.getSignInAccount();
                firebaseAuthWithGoogle(account);
                this.dismiss();
            } else {
                // Google Sign In failed, update UI appropriately
                Log.e(TAG, "Google Login Failed");
                Constant.showToast("Google Login Failed", Toast.LENGTH_LONG);
            }
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.bottom_sheet_form_login_comment, container, false);
        ButterKnife.bind(this, v);

        initListView();
        if (action.equals("Login")) {
            popupWindowComment.setVisibility(View.GONE);
            popupWindowShowComment.setVisibility(View.GONE);
        } else if (action.equals("Comment")) {
            textCommentChange();
        } else if (action.equals("ShowComment")) {
            popupWindowComment.setVisibility(View.GONE);
            popupWindowLogin.setVisibility(View.GONE);
            loadNews.getComment(id_post);
            Log.e("cmt1", mArrComments.size() + "");
            if (mArrComments.size() == 0) {
                mTVNoComment.setVisibility(View.VISIBLE);
            } else {
                mTVNoComment.setVisibility(View.GONE);
            }
            Glide.with(getContext()).load(SaveUser.getInstance().getUser().getAvatar())
                    .placeholder(R.drawable.ic_author)
                    .error(R.drawable.bg_no_img)
                    .into(imgUser);
            edShowComment.setFocusable(false);
        }
        return v;
    }

    @OnClick({R.id.btnLoginGg, R.id.btnLoginFb, R.id.vClose, R.id.vCloseL, R.id.edShowComment, R.id.tvSendComment})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btnLoginGg:
                signInGoogle();
                break;
            case R.id.btnLoginFb:
                break;
            case R.id.vClose:
                this.dismiss();
                break;
            case R.id.vCloseL:
                this.dismiss();
                break;
            case R.id.edShowComment:
                action = "Comment";
                textCommentChange();
                break;
            case R.id.tvSendComment:
                final String comment = edComment.getText().toString();
                if (TextUtils.isEmpty(comment)) {
                    showToast("Vui lòng nhập nội dung comment...", Toast.LENGTH_LONG);
                } else {
                    StringRequest sendCommentRequest = new StringRequest(Request.Method.POST, Constant.CATEGORY_URL, new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            if (response.length() > 0) {
                                showToast(response, Toast.LENGTH_LONG);
                                Log.e("comment", response.toString());
                                ComentBottomSheetDialog.this.dismiss();
                            }
                        }
                    }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Log.e(TAG, error.toString());
                        }
                    }) {
                        @Override
                        protected Map<String, String> getParams() throws AuthFailureError {
                            Map<String, String> params = new HashMap<>();
                            params.put("id_user", id_user + "");
                            params.put("id_post", id_post + "");
                            params.put("comment", comment);

                            return params;
                        }
                    };
                    Constant.getInstance().addToRequestQueue(sendCommentRequest);
                }
                break;
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(mAuthListener);
        if (mGoogleApiClient != null) {
            mGoogleApiClient.connect();
        }
    }

    @Override
    public void onStop() {
        if (mGoogleApiClient != null && mGoogleApiClient.isConnected()) {
            mGoogleApiClient.disconnect();
        }
        if (mAuthListener != null) {
            mAuth.removeAuthStateListener(mAuthListener);
        }
        super.onStop();

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mGoogleApiClient.stopAutoManage(getActivity());
        mGoogleApiClient.disconnect();
    }

    @Override
    public void onPause() {
        super.onPause();
        mGoogleApiClient.stopAutoManage(getActivity());
        mGoogleApiClient.disconnect();
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        Log.e(TAG, "onConnectionFailed: " + connectionResult.toString());
    }

    @Override
    public void updateDataListNews(Post post) {

    }

    @Override
    public void updateDataListCa(Category category) {

    }

    @Override
    public void updateDataListComment(Comment comment) {
        addDataList(comment);

        Log.e("cmt", mArrComments.size() + "");
        if (mArrComments.size() == 0) {
            mTVNoComment.setVisibility(View.VISIBLE);
        } else {
            mTVNoComment.setVisibility(View.GONE);
        }
    }

    @Override
    public void setRefreshingLayout(boolean b) {

    }

    private void textCommentChange() {
        popupWindowLogin.setVisibility(View.GONE);
        popupWindowShowComment.setVisibility(View.GONE);
        popupWindowComment.setVisibility(View.VISIBLE);
        edComment.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (charSequence.length() > 0)
                    tvSendComment.setTextColor(getResources().getColor(R.color.colorPrimary));
                else
                    tvSendComment.setTextColor(getResources().getColor(R.color.cardview_shadow_start_color));
            }

            @Override
            public void afterTextChanged(Editable editable) {
            }
        });
    }

    private void initListView() {
        mCommentAdapter = new CommentAdapter(getContext(), R.layout.item_list_comment, mArrComments);
        mListViewComment.setAdapter(mCommentAdapter);
    }

    private void addDataList(Comment comment) {
        mArrComments.add(comment);
        mCommentAdapter.notifyDataSetChanged();
    }

    private void signInGoogle() {
        Intent signIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
        startActivityForResult(signIntent, RC_SIGN_IN);
    }

    private void firebaseAuthWithGoogle(final GoogleSignInAccount account) {
        AuthCredential credential = GoogleAuthProvider.getCredential(account.getIdToken(), null);
        mAuth.signInWithCredential(credential).addOnCompleteListener(getActivity(), new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                Log.e("AUTH", "signInWithCredential: onComplete" + task.isSuccessful());
                Log.e("isLoggedIn", SaveUser.getInstance().isLoggedIn() + "");
                if (SaveUser.getInstance().isLoggedIn() == false) {
                    StringRequest stringReq = new StringRequest(Request.Method.GET,
                            Constant.CHECK_USER_BY_EMAIL + account.getEmail(),
                            new Response.Listener<String>() {
                                @Override
                                public void onResponse(String response) {
                                    Log.e(TAG, response.toString());
                                    if (!response.equals("User doesn't exist")) {
                                        // When user exists will return id_user
                                        Log.e(TAG, "User is exists: id= " + response);
                                        SaveUser.getInstance().setUser(new User(Integer.parseInt(response), account.getEmail(), account.getDisplayName(), account.getPhotoUrl().toString()));
                                    } else {
                                        insertUser(account.getDisplayName(), account.getEmail(), account.getPhotoUrl().toString());
                                    }
                                }
                            }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Log.e("Volley", error.getMessage());
                        }
                    });
                    Constant.getInstance().addToRequestQueue(stringReq);
                }
                showToast("Xin chào " + account.getDisplayName(), Toast.LENGTH_SHORT);
            }
        });
    }

    private void insertUser(final String name, final String email, final String avatar) {
        StringRequest insertRequest = new StringRequest(POST, Constant.CATEGORY_URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                if (response.length() > 0) {
                    try {
                        JSONObject jsonObject = new JSONObject(response.toString());
                        Log.e(TAG, jsonObject.toString());
                        String status = jsonObject.getString("status");
                        if (status.equals("Insert user success")) {
                            JSONArray userResponse = jsonObject.getJSONArray("response");
                            JSONObject user = userResponse.getJSONObject(0);
                            User u = new User(user);
                            SaveUser.getInstance().setUser(u);
                            Log.e(TAG, "Insert user success - Login: " + SaveUser.getInstance().isLoggedIn());
                        } else {
                            Log.e(TAG, "Insert user failed");
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(TAG, error.toString());
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("email", email);
                params.put("name", name);
                params.put("avatar", avatar);

                return params;
            }
        };
        Constant.getInstance().addToRequestQueue(insertRequest);
    }
}
