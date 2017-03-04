package com.messi.cantonese.study;


import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;


import com.messi.cantonese.study.adapter.ChineseDictionaryAdapter;
import com.messi.cantonese.study.impl.FragmentProgressbarListener;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ChineseDictionaryActivity extends BaseActivity implements FragmentProgressbarListener {

    @BindView(R.id.tablayout)
    TabLayout tablayout;
    @BindView(R.id.pager)
    ViewPager viewPager;
    private ChineseDictionaryAdapter mAdapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.zh_dic_activity);
        ButterKnife.bind(this);
        setStatusbarColor(R.color.style6_color1);
        getSupportActionBar().setTitle(getResources().getString(R.string.leisuer_two));
        mAdapter = new ChineseDictionaryAdapter(this.getSupportFragmentManager(), this);
        viewPager.setAdapter(mAdapter);
        viewPager.setOffscreenPageLimit(2);
        tablayout.setupWithViewPager(viewPager);
    }

}
