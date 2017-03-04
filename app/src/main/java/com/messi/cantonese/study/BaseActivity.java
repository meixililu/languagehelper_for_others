package com.messi.cantonese.study;

import android.content.Intent;
import android.os.Build.VERSION;
import android.os.Build.VERSION_CODES;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v4.widget.SwipeRefreshLayout.OnRefreshListener;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.ProgressBar;

import com.avos.avoscloud.AVAnalytics;
import com.messi.cantonese.study.util.AudioTrackUtil;
import com.messi.cantonese.study.util.KeyUtil;
import com.messi.cantonese.study.util.ScreenUtil;

public class BaseActivity extends AppCompatActivity {

    public Toolbar toolbar;
    public ProgressBar mProgressbar;
    public SwipeRefreshLayout mSwipeRefreshLayout;
    private View mScrollable;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        TransparentStatusbar();
    }

    protected void TransparentStatusbar() {
        if (VERSION.SDK_INT >= VERSION_CODES.KITKAT && VERSION.SDK_INT <= VERSION_CODES.LOLLIPOP) {
            //透明状态栏
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        }
    }

    protected void setStatusbarColor(int color) {
        if (VERSION.SDK_INT >= 21) {
            getWindow().setStatusBarColor(this.getResources().getColor(color));
        }
    }

    @Override
    public void setContentView(int layoutResID) {
        super.setContentView(layoutResID);
        getActionBarToolbar();
        initProgressbar();
    }

    protected void getActionBarToolbar() {
        if (toolbar == null) {
            toolbar = (Toolbar) findViewById(R.id.my_awesome_toolbar);
            if (toolbar != null) {
                setSupportActionBar(toolbar);
                getSupportActionBar().setDisplayHomeAsUpEnabled(true);
                if (VERSION.SDK_INT >= VERSION_CODES.KITKAT && VERSION.SDK_INT <= VERSION_CODES.LOLLIPOP) {
                    toolbar.setPadding(0, ScreenUtil.dip2px(this, 10), 0, 0);
                }
            }
            String title = getIntent().getStringExtra(KeyUtil.ActionbarTitle);
            setActionBarTitle(title);
        }
    }

//	protected void startClipboardListener(){
//		final ClipboardManager cm = (ClipboardManager) getSystemService(CLIPBOARD_SERVICE);
//		cm.addPrimaryClipChangedListener(new ClipboardManager.OnPrimaryClipChangedListener() {
//			@Override
//			public void onPrimaryClipChanged() {
//				ClipData data = cm.getPrimaryClip();
//				ClipData.Item item = data.getItemAt(0);
//				LogUtil.DefalutLog("clipboard:"+item.getText().toString());
//			}
//		});
//	}

    protected void setToolbarBackground(int color) {
        if (toolbar != null) {
            toolbar.setBackgroundColor(this.getResources().getColor(color));
        }
    }

    protected void setActionBarTitle(String title) {
        if (!TextUtils.isEmpty(title) && getSupportActionBar() != null) {
            getSupportActionBar().setTitle(title);
        }
    }

    public void setCollTitle(String title) {
        BaseActivity.this.setTitle(title);
    }

    protected void toActivity(Class mClass, Bundle bundle) {
        Intent intent = new Intent(this, mClass);
        if (bundle != null) {
            intent.putExtra(KeyUtil.BundleKey, bundle);
        }
        startActivity(intent);
    }

    protected void initProgressbar() {
        if (mProgressbar == null) {
            mProgressbar = (ProgressBar) findViewById(R.id.progressBarCircularIndetermininate);
        }
    }

    /**
     * need init beford use
     */
    protected void initSwipeRefresh() {
        if (mSwipeRefreshLayout == null) {
//            mSwipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.mswiperefreshlayout);
//            mSwipeRefreshLayout.setColorSchemeResources(R.color.holo_blue_bright,
//                    R.color.holo_green_light,
//                    R.color.holo_orange_light,
//                    R.color.holo_red_light);
//            mSwipeRefreshLayout.setOnRefreshListener(new OnRefreshListener() {
//                @Override
//                public void onRefresh() {
//                    onSwipeRefreshLayoutRefresh();
//                }
//            });
        }
    }

    public void onSwipeRefreshLayoutFinish() {
        if (mSwipeRefreshLayout != null) {
            mSwipeRefreshLayout.setRefreshing(false);
        }
    }

    public void onSwipeRefreshLayoutRefresh() {
    }

    public void showProgressbar() {
        if (mProgressbar != null) {
            mProgressbar.setVisibility(View.VISIBLE);
        }
    }

    public void hideProgressbar() {
        if (mProgressbar != null) {
            mProgressbar.setVisibility(View.GONE);
        }
    }

    protected int getScreenHeight() {
        return findViewById(android.R.id.content).getHeight();
    }

    protected void setScrollable(View s) {
        mScrollable = s;
    }

    @Override
    protected void onResume() {
        super.onResume();
        AVAnalytics.onResume(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        AVAnalytics.onPause(this);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        switch (keyCode) {
            case KeyEvent.KEYCODE_VOLUME_UP:
                AudioTrackUtil.adjustStreamVolume(BaseActivity.this, keyCode);
                return true;
            case KeyEvent.KEYCODE_VOLUME_DOWN:
                AudioTrackUtil.adjustStreamVolume(BaseActivity.this, keyCode);
                return true;
        }
        return super.onKeyDown(keyCode, event);
    }

}
