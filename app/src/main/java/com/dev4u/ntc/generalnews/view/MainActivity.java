package com.dev4u.ntc.generalnews.view;

import android.app.Dialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
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
import com.dev4u.ntc.generalnews.adapter.ComentBottomSheetDialog;
import com.dev4u.ntc.generalnews.adapter.ViewPagerAdapter;
import com.dev4u.ntc.generalnews.model.User;
import com.dev4u.ntc.generalnews.utils.ConnectionUtils;
import com.dev4u.ntc.generalnews.widget.CircleImageView;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
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

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.dev4u.ntc.generalnews.Constant.TAG;
import static com.dev4u.ntc.generalnews.Constant.showToast;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, View.OnClickListener, GoogleApiClient.OnConnectionFailedListener {

    private static final int RC_SIGN_IN = 9001;
    //    /* Client used to interact with Google APIs. */
    public static GoogleApiClient mGoogleApiClient;
    @BindView(R.id.mToolBar)
    Toolbar mToolBar;
    @BindView(R.id.mTabs)
    TabLayout mTabs;
    @BindView(R.id.mViewPager)
    ViewPager mViewPager;
    @BindView(R.id.navView)
    NavigationView navView;
    @BindView(R.id.mDrawerLayout)
    DrawerLayout mDrawerLayout;
    TextView tvNavName;
    TextView tvLogin;
    TextView tvNavEmail;
    CircleImageView ivNavAvatar;
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        setSupportActionBar(mToolBar);
        SaveUser.getInstance().init(this);

        Log.e("connect", ConnectionUtils.isConnected(this) + "");
        if (ConnectionUtils.isConnected(this) == false) {
            View view = getLayoutInflater().inflate(R.layout.show_check_conection_form, null);
            Button btnThuLai = (Button) view.findViewById(R.id.btnThuLai);

            final Dialog mBottomSheetDialog = new Dialog(MainActivity.this, R.style.MaterialDialogSheet);
            mBottomSheetDialog.setContentView(view);
            mBottomSheetDialog.setCancelable(false);
            mBottomSheetDialog.getWindow().setGravity(Gravity.CENTER);
            mBottomSheetDialog.show();
            btnThuLai.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    finish();
                    startActivity(new Intent(MainActivity.this, MainActivity.class));
                    overridePendingTransition(R.anim.slide_out_left, R.anim.slide_out_right);
                }
            });
        }

        mAuth = FirebaseAuth.getInstance();
        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    Log.e("AUTH", "user logged in: " + user.getEmail());
                    showToast("Xin chào " + user.getDisplayName(), Toast.LENGTH_LONG);
                    tvNavName.setVisibility(View.VISIBLE);
                    tvNavEmail.setVisibility(View.VISIBLE);
                    tvLogin.setVisibility(View.GONE);

                    tvNavName.setText(user.getDisplayName() + "");
                    tvNavEmail.setText(user.getEmail() + "");
                    Glide.with(MainActivity.this).load(user.getPhotoUrl())
                            .placeholder(R.drawable.ic_author)
                            .error(R.drawable.ic_author)
                            .into(ivNavAvatar);
                } else {
                    Log.e("AUTH", "user logged out ");
                    tvNavEmail.setVisibility(View.GONE);
                    tvNavName.setVisibility(View.GONE);
                    tvLogin.setVisibility(View.VISIBLE);
                    ivNavAvatar.setImageResource(R.drawable.ic_author);
                }
            }
        };

        GoogleSignInOptions gSO = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this, this)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gSO)
                .build();

        setNavigationHeader();
        setupViewPager(mViewPager);
        mTabs.setupWithViewPager(mViewPager);
        setupTabIcons();

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, mDrawerLayout, mToolBar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        mDrawerLayout.setDrawerListener(toggle);
        toggle.syncState();

        navView.setNavigationItemSelectedListener(this);
        // showing dot next to notifications label
        navView.getMenu().getItem(0).getSubMenu().getItem(2).setActionView(R.layout.menu_dot);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.mDrawerLayout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_home) {
            // Handle the camera action
        } else if (id == R.id.nav_home) {

        } else if (id == R.id.nav_savenews) {
            startActivity(new Intent(this, NewsSaveActivity.class));
        } else if (id == R.id.nav_notification) {

        } else if (id == R.id.nav_manage) {

        } else if (id == R.id.nav_logout) {
            signOutGoogle();
//            mGoogleApiClient.disconnect();
        } else if (id == R.id.nav_rate) {
            final String appName = getApplicationContext().getPackageName();
            try {
                startActivity(new Intent(Intent.ACTION_VIEW,
                        Uri.parse("market://details?id=" + appName)));
            } catch (android.content.ActivityNotFoundException anfe) {
                startActivity(new Intent(
                        Intent.ACTION_VIEW,
                        Uri.parse("http://play.google.com/store/apps/details?id="
                                + appName)));
            }
        } else if (id == R.id.nav_share) {

        }

        mDrawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.llBtLogin) {
            mDrawerLayout.closeDrawer(GravityCompat.START);
            showSiginForm();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RC_SIGN_IN) {
            GoogleSignInResult signInResult = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            if (signInResult.isSuccess()) {
                // Google Sign In was successful, authenticate with Firebase
                GoogleSignInAccount account = signInResult.getSignInAccount();
                firebaseAuthWithGoogle(account);
            } else {
                // Google Sign In failed, update UI appropriately
                Log.e(TAG, "Google Login Failed");
                Constant.showToast("Google Login Failed", Toast.LENGTH_LONG);
            }
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
        super.onStop();
        if (mAuthListener != null) {
            mAuth.removeAuthStateListener(mAuthListener);
        }
//        if (mGoogleApiClient != null && mGoogleApiClient.isConnected()) {
//            mGoogleApiClient.stopAutoManage(this);
//            mGoogleApiClient.disconnect();
//        }
    }
//
//    @Override
//    public void onDestroy() {
//        super.onDestroy();
//        mGoogleApiClient.stopAutoManage(this);
//        mGoogleApiClient.disconnect();
//    }
//
//    @Override
//    public void onPause() {
//        super.onPause();
//        mGoogleApiClient.stopAutoManage(this);
//        mGoogleApiClient.disconnect();
//    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        Log.e(TAG, "onConnectionFailed: " + connectionResult.toString());
    }

    private void setNavigationHeader() {
        View view = navView.getHeaderView(0);
        LinearLayout llBtLogin = (LinearLayout) view.findViewById(R.id.llBtLogin);
        tvNavName = (TextView) view.findViewById(R.id.tvUserName);
        tvNavEmail = (TextView) view.findViewById(R.id.tvEmail);
        tvLogin = (TextView) view.findViewById(R.id.tvLogin);
        ivNavAvatar = (CircleImageView) view.findViewById(R.id.mImgAvatar);

        llBtLogin.setOnClickListener(this);
    }

    private void setupTabIcons() {
        int[] tabIcons = {
                R.drawable.ic_recent_news,
                R.drawable.ic_category,
        };

        mTabs.getTabAt(0).setIcon(tabIcons[0]);
        mTabs.getTabAt(1).setIcon(tabIcons[1]);
    }

    private void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        adapter.addFrag(new RecentNewsFragment(), "TIN MỚI");
        adapter.addFrag(new CategoryFragment(), "CHUYÊN MỤC");
        viewPager.setAdapter(adapter);
    }

    public void showSiginForm() {
        if (SaveUser.getInstance().isLoggedIn() == false) {
            ComentBottomSheetDialog mBottomSheet = ComentBottomSheetDialog.
                    newInstance("Login", 0, 0);

            mBottomSheet.show(getSupportFragmentManager(), mBottomSheet.getTag());
        }
    }

    private void firebaseAuthWithGoogle(final GoogleSignInAccount account) {
        AuthCredential credential = GoogleAuthProvider.getCredential(account.getIdToken(), null);
        mAuth.signInWithCredential(credential).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                Log.e("AUTH", "signInWithCredential: onComplete" + task.isSuccessful());
                setViewOnHeaderIsLogin(account);
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
        StringRequest insertRequest = new StringRequest(Request.Method.POST, Constant.CATEGORY_URL, new Response.Listener<String>() {
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
                            Log.e(TAG, "Insert user failed - Login: " + SaveUser.getInstance().isLoggedIn());
                        } else {
                            Log.e(TAG, "Insert user success");
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
                Map<String, String> params = new HashMap<String, String>();
                params.put("email", email);
                params.put("name", name);
                params.put("avatar", avatar);

                return params;
            }
        };
        Constant.getInstance().addToRequestQueue(insertRequest);
    }

    public void signOutGoogle() {
        mGoogleApiClient.connect();
        mGoogleApiClient.registerConnectionCallbacks(new GoogleApiClient.ConnectionCallbacks() {
            @Override
            public void onConnected(@Nullable Bundle bundle) {
                if (mGoogleApiClient.isConnected()) {
                    Auth.GoogleSignInApi.signOut(mGoogleApiClient).setResultCallback(new ResultCallback<Status>() {
                        @Override
                        public void onResult(@NonNull Status status) {
                            if (status.isSuccess()) {
                                Log.e("status logout ", status.toString());
                            }
                        }
                    });
                }
            }

            @Override
            public void onConnectionSuspended(int i) {
                Log.d(TAG, "Google API Client Connection Suspended");
            }
        });
        SaveUser.getInstance().setLogin(false);
        FirebaseAuth.getInstance().signOut();
        setViewOnHeaderIsLogout();
        showToast("Logout", Toast.LENGTH_SHORT);
    }


    private void setViewOnHeaderIsLogin(GoogleSignInAccount account) {
        tvNavName.setVisibility(View.VISIBLE);
        tvNavEmail.setVisibility(View.VISIBLE);
        tvLogin.setVisibility(View.GONE);

        tvNavName.setText(account.getDisplayName() + "");
        tvNavEmail.setText(account.getEmail() + "");
        Glide.with(this).load(account.getPhotoUrl())
                .placeholder(R.drawable.ic_author)
                .error(R.drawable.ic_author)
                .into(ivNavAvatar);
    }

    private void setViewOnHeaderIsLogout() {
        tvNavEmail.setVisibility(View.GONE);
        tvNavName.setVisibility(View.GONE);
        tvLogin.setVisibility(View.VISIBLE);
        ivNavAvatar.setImageResource(R.drawable.ic_author);
    }
}
