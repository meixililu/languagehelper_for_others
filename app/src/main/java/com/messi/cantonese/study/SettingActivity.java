package com.messi.cantonese.study;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.CheckBox;
import android.widget.FrameLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import com.avos.avoscloud.AVAnalytics;
import com.messi.cantonese.study.db.DataBaseUtil;
import com.messi.cantonese.study.util.KeyUtil;
import com.messi.cantonese.study.util.SDCardUtil;
import com.messi.cantonese.study.util.Settings;
import com.messi.cantonese.study.util.ToastUtil;

public class SettingActivity extends BaseActivity implements OnClickListener,SeekBar.OnSeekBarChangeListener {

	private TextView seekbar_text;
	private SeekBar seekbar;
	private FrameLayout auto_play;
	private FrameLayout clear_all_except_favorite,clear_all;
	private CheckBox auto_play_cb;
	private SharedPreferences mSharedPreferences;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.setting);
		init();
		initData();
	}

	private void init() {
		getSupportActionBar().setTitle(getString(R.string.title_settings));
		mSharedPreferences = getSharedPreferences(this.getPackageName(), Activity.MODE_PRIVATE);
        seekbar_text = (TextView) findViewById(R.id.seekbar_text);
        seekbar = (SeekBar) findViewById(R.id.seekbar);
        auto_play = (FrameLayout) findViewById(R.id.setting_auto_play);
        auto_play_cb = (CheckBox) findViewById(R.id.setting_auto_play_cb);
        clear_all_except_favorite = (FrameLayout) findViewById(R.id.setting_clear_all_except_favorite);
        clear_all = (FrameLayout) findViewById(R.id.setting_clear_all);
        
        seekbar.setOnSeekBarChangeListener(this);
        clear_all_except_favorite.setOnClickListener(this);
        clear_all.setOnClickListener(this);
        auto_play.setOnClickListener(this);
	}
	
	private void initData(){
		seekbar_text.setText(this.getResources().getString(R.string.play_speed_text) +
				mSharedPreferences.getInt(getString(R.string.preference_key_tts_speed), 50));
		seekbar.setProgress(mSharedPreferences.getInt(getString(R.string.preference_key_tts_speed), 50));
		boolean autoplay = mSharedPreferences.getBoolean(KeyUtil.AutoPlayResult, false);
		auto_play_cb.setChecked(autoplay);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.setting_auto_play:
				auto_play_cb.setChecked(!auto_play_cb.isChecked());
				Settings.saveSharedPreferences(mSharedPreferences, KeyUtil.AutoPlayResult,auto_play_cb.isChecked());
				AVAnalytics.onEvent(this, "setting_pg_auto_play");
				break;
		case R.id.setting_clear_all_except_favorite:
			Settings.isMainFragmentNeedRefresh = true;
			DataBaseUtil.getInstance().clearExceptFavoriteTran();
			DataBaseUtil.getInstance().clearExceptFavoriteDic();
			ToastUtil.diaplayMesShort(SettingActivity.this, this.getResources().getString(R.string.clear_success));
			AVAnalytics.onEvent(this, "setting_pg_clear_all_except");
			break;
		case R.id.setting_clear_all:
			DataBaseUtil.getInstance().clearAllTran();
			DataBaseUtil.getInstance().clearAllDic();
			Settings.isMainFragmentNeedRefresh = true;
			SDCardUtil.deleteOldFile();
			ToastUtil.diaplayMesShort(SettingActivity.this, this.getResources().getString(R.string.clear_success));
			AVAnalytics.onEvent(this, "setting_pg_clear_all");
			break;
		default:
			break;
		}
	}

	@Override
	public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
		seekbar_text.setText(this.getResources().getString(R.string.play_speed_text) + progress);
	}

	@Override
	public void onStartTrackingTouch(SeekBar seekBar) {
	}

	@Override
	public void onStopTrackingTouch(SeekBar seekBar) {
		Settings.saveSharedPreferences(mSharedPreferences,
				getString(R.string.preference_key_tts_speed),
				seekBar.getProgress());
		AVAnalytics.onEvent(SettingActivity.this, "tran_tools_change_speed");
	}
}
