package com.messi.cantonese.study;

import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.widget.TextView;

import com.avos.avoscloud.AVAnalytics;
import com.messi.cantonese.study.util.Settings;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class AboutActivity extends BaseActivity{

	@BindView(R.id.email_layout) TextView email_layout;
	@BindView(R.id.app_version) TextView app_version;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.about);
		ButterKnife.bind(this);
		init();
	}

	private void init() {
		setActionBarTitle(getResources().getString(R.string.title_about));
		app_version.setText(getVersion());
	}

	@OnClick(R.id.email_layout)
	public void onClick() {
		Settings.contantUs(AboutActivity.this);
		AVAnalytics.onEvent(this, "about_pg_send_email");
	}
	
	public String getVersion() {
		try {
			PackageManager manager = this.getPackageManager();
			PackageInfo info = manager.getPackageInfo(this.getPackageName(), 0);
			String version = info.versionName;
			return version;
		} catch (Exception e) {
			e.printStackTrace();
			return "x.x";
		}
	}
	
}
