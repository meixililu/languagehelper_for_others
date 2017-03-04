package com.messi.cantonese.study.adapter;

import android.app.Activity;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.messi.cantonese.study.NewsFragment;
import com.messi.cantonese.study.ToutiaoNewsFragment;
import com.messi.cantonese.study.WechatJXFragment;

public class ReadingAndNewsAdapter extends FragmentPagerAdapter {

	public static String[] CONTENT;
    private Activity mContext;

    public ReadingAndNewsAdapter(FragmentManager fm, Activity mContext) {
        super(fm);
        this.mContext = mContext;
        CONTENT = new String[] { 
        		"头条",
        		"精选",
        		"社会",
        		"体育",
        		"科技",
        		"国内",
        		"娱乐",
        		"国际",
        		"生活"
        };
    }

    @Override
    public Fragment getItem(int position) {
        if( position == 0 ){
        	return ToutiaoNewsFragment.getInstance("top",mContext);
        }else if( position == 1 ){
        	return WechatJXFragment.getInstance(mContext);
        }else if( position == 2 ){
        	return NewsFragment.getInstance(NewsFragment.social, CONTENT[position], mContext);
        }else if( position == 3 ){
            return NewsFragment.getInstance(NewsFragment.tiyu, CONTENT[position], mContext);
        }else if( position == 4 ){
            return NewsFragment.getInstance(NewsFragment.keji, CONTENT[position], mContext);
        }else if( position == 5 ){
            return NewsFragment.getInstance(NewsFragment.guonei, CONTENT[position], mContext);
        }else if( position == 6 ){
            return NewsFragment.getInstance(NewsFragment.yule, CONTENT[position], mContext);
        }else if( position == 7 ){
            return NewsFragment.getInstance(NewsFragment.guoji, CONTENT[position], mContext);
        }else if( position == 8 ){
            return NewsFragment.getInstance(NewsFragment.health, CONTENT[position], mContext);
        }else if( position == 9 ){
            return NewsFragment.getInstance(NewsFragment.social, CONTENT[position], mContext);
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