package com.messi.cantonese.study.adapter;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.messi.cantonese.study.JokeBuDeJieFragment;
import com.messi.cantonese.study.JokeGifFragment;
import com.messi.cantonese.study.JokePictureFragment;
import com.messi.cantonese.study.JokeTextFragment;
import com.messi.cantonese.study.R;

public class JokePageAdapter extends FragmentPagerAdapter {

	public static String[] CONTENT;
	
    public JokePageAdapter(FragmentManager fm, Context mContext) {
        super(fm);
        CONTENT = new String[] { 
        		mContext.getResources().getString(R.string.title_duanzi_img),
        		mContext.getResources().getString(R.string.title_duanzi),
        		mContext.getResources().getString(R.string.title_duanzi_gif),
        		mContext.getResources().getString(R.string.title_duanzi_word)
        };
    }

    @Override
    public Fragment getItem(int position) {
        if( position == 0 ){
        	return new JokePictureFragment();
        }else if( position == 1 ){
        	return new JokeBuDeJieFragment();
        }else if( position == 2 ){
//            return new JokeTextFragment();
        	return new JokeGifFragment();
        }else if( position == 3 ){
            return new JokeTextFragment();
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