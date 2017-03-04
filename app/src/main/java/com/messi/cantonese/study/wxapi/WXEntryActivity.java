package com.messi.cantonese.study.wxapi;


import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Toast;

import com.iflytek.cloud.SpeechConstant;
import com.iflytek.cloud.SpeechUtility;
import com.messi.cantonese.study.BaseActivity;
import com.messi.cantonese.study.R;
import com.messi.cantonese.study.adapter.MainPageAdapter;
import com.messi.cantonese.study.db.DataBaseUtil;
import com.messi.cantonese.study.impl.FragmentProgressbarListener;
import com.messi.cantonese.study.util.KeyUtil;
import com.messi.cantonese.study.util.LogUtil;
import com.messi.cantonese.study.util.Settings;

import java.util.HashMap;

public class WXEntryActivity extends BaseActivity implements OnClickListener,FragmentProgressbarListener {

	public static HashMap<String, Object> dataMap = new HashMap<String, Object>();
	private TabLayout tablayout;
	private ViewPager viewPager;
	private MainPageAdapter mAdapter;
	
	private long exitTime = 0;
	private Bundle bundle;
	private boolean isRespondWX;
	public static int currentIndex = 0;
	public static WXEntryActivity mInstance;
	private SharedPreferences mSharedPreferences;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		try {
			setContentView(R.layout.content_frame);
			mInstance = this;
			initDatas();
			initViews();
			Settings.verifyStoragePermissions(this, Settings.PERMISSIONS_STORAGE);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	private void initDatas(){
		bundle = getIntent().getExtras();
		SpeechUtility.createUtility(this, SpeechConstant.APPID + "=" +getString(R.string.app_id));
	}
	
	private void initViews(){
		mSharedPreferences = getSharedPreferences(this.getPackageName(), Activity.MODE_PRIVATE);
		if (toolbar != null) {
			getSupportActionBar().setDisplayHomeAsUpEnabled(false);
			getSupportActionBar().setTitle("");
		}
        
		viewPager = (ViewPager) findViewById(R.id.pager);
		tablayout = (TabLayout) findViewById(R.id.tablayout);
		mAdapter = new MainPageAdapter(this.getSupportFragmentManager(),bundle,this,mSharedPreferences);
		viewPager.setAdapter(mAdapter);
		viewPager.setOffscreenPageLimit(4);
		tablayout.setupWithViewPager(viewPager);
		
        setLastTimeSelectTab();
	}
	
	private void setLastTimeSelectTab(){
		int index = mSharedPreferences.getInt(KeyUtil.LastTimeSelectTab, 0);
		viewPager.setCurrentItem(index);
	}
	
	@Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.action_more:
			toMoreActivity();
			break;
		}
       return true;
	}
	
	private void toMoreActivity(){
//		Intent intent = new Intent(this, MoreActivity.class);
//		startActivity(intent);
//		AVAnalytics.onEvent(this, "index_pg_to_morepg");
	}
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		switch (keyCode) {
		case KeyEvent.KEYCODE_MENU:
			 toMoreActivity();
			 return true;
		}
		return super.onKeyDown(keyCode, event);
	}
	
	@Override
	public void onBackPressed() {
    	if ((System.currentTimeMillis() - exitTime) > 2000) {
			Toast.makeText(getApplicationContext(), this.getResources().getString(R.string.exit_program), Toast.LENGTH_SHORT).show();
			exitTime = System.currentTimeMillis();
		} else {
			finish();
		}
	}
	
	@Override
	public void onClick(View v) {
	}
	
	private void saveSelectTab(){
		int index = viewPager.getCurrentItem();
		LogUtil.DefalutLog("WXEntryActivity---onDestroy---saveSelectTab---index:"+index);
		Settings.saveSharedPreferences(mSharedPreferences, KeyUtil.LastTimeSelectTab,index);
	}
	
	@Override
	protected void onDestroy() {
		super.onDestroy();
		saveSelectTab();
		mInstance = null;
		if(mSharedPreferences.getBoolean(KeyUtil.AutoClearTran, false)){
			DataBaseUtil.getInstance().clearExceptFavoriteTran();
		}
	}

}
