package com.messi.cantonese.study.adapter;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.messi.cantonese.study.ChineseDictionaryFragment;
import com.messi.cantonese.study.ChineseDictionaryIdiomFragment;
import com.messi.cantonese.study.R;

public class ChineseDictionaryAdapter extends FragmentPagerAdapter {

	public static String[] CONTENT;

    public ChineseDictionaryAdapter(FragmentManager fm, Context mContext) {
        super(fm);
        CONTENT = new String[] {
        		mContext.getResources().getString(R.string.title_ch_dictionary),
        		mContext.getResources().getString(R.string.title_dictionary_idiom)
        };
    }

    @Override
    public Fragment getItem(int position) {
        if( position == 0 ){
        	return ChineseDictionaryFragment.getInstance();
        }else if( position == 1 ){
        	return ChineseDictionaryIdiomFragment.getInstance();
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