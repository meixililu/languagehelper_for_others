package com.messi.cantonese.study.adapter;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.messi.cantonese.study.MainFragment;
import com.messi.cantonese.study.MainFragmentOld;
import com.messi.cantonese.study.R;
import com.messi.cantonese.study.util.KeyUtil;

public class MainPageAdapter extends FragmentPagerAdapter {

	public static String[] CONTENT;
	private Bundle bundle;
	private Activity mContext;
	private SharedPreferences mSharedPreferences;

    public MainPageAdapter(FragmentManager fm, Bundle bundle, Activity mContext, SharedPreferences mSharedPreferences) {
        super(fm);
        this.mContext = mContext;
        this.bundle = bundle;
        this.mSharedPreferences = mSharedPreferences;
        CONTENT = new String[] {
        		mContext.getResources().getString(R.string.title_translate),
        		mContext.getResources().getString(R.string.title_study),
        		mContext.getResources().getString(R.string.title_leisure)
        };
    }

    @Override
    public Fragment getItem(int position) {
        if( position == 0 ){
            return MainFragmentOld.getInstance(bundle,mContext);
        }else if( position == 1 ){
            return new Fragment();
//        	return StudyFragment.getInstance();
        }else if( position == 2 ){
            return new Fragment();
//        	return LeisureFragment.getInstance();
        }
        return null;
    }

    @Override
    public int getCount() {
        return CONTENT.length;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return CONTENT[position].toUpperCase();
    }
}