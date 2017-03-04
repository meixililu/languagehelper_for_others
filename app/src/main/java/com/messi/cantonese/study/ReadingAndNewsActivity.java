package com.messi.cantonese.study;

import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;

import com.messi.cantonese.study.adapter.ReadingAndNewsAdapter;
import com.messi.cantonese.study.impl.FragmentProgressbarListener;


public class ReadingAndNewsActivity extends BaseActivity implements FragmentProgressbarListener {

	private TabLayout tablayout;
	private ViewPager viewpager;
	private ReadingAndNewsAdapter pageAdapter;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.joke_activity);
		initViews();
	}
	
	private void initViews(){
		getSupportActionBar().setTitle(getResources().getString(R.string.leisuer_reading));
		tablayout = (TabLayout) findViewById(R.id.tablayout);
		viewpager = (ViewPager) findViewById(R.id.viewpager);
		
		pageAdapter = new ReadingAndNewsAdapter(getSupportFragmentManager(),this);
		viewpager.setAdapter(pageAdapter);
		viewpager.setOffscreenPageLimit(9);
		tablayout.setupWithViewPager(viewpager);
	}

	@Override
	public void onSaveInstanceState(Bundle outState, PersistableBundle outPersistentState) {
//		super.onSaveInstanceState(outState, outPersistentState);
	}
	
	
}
