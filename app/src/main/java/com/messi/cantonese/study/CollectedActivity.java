package com.messi.cantonese.study;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;

import com.iflytek.cloud.SpeechSynthesizer;
import com.messi.cantonese.study.adapter.RcCollectTranslateListAdapter;
import com.messi.cantonese.study.dao.record;
import com.messi.cantonese.study.db.DataBaseUtil;
import com.messi.cantonese.study.util.Settings;
import com.messi.cantonese.study.views.DividerItemDecoration;

import java.util.ArrayList;
import java.util.List;

public class CollectedActivity extends BaseActivity {

    private RecyclerView recent_used_lv;
    private LayoutInflater mInflater;
    private RcCollectTranslateListAdapter mAdapter;
    private List<record> beans;
    // 缓存，保存当前的引擎参数到下一次启动应用程序使用.
    private SharedPreferences mSharedPreferences;
    //合成对象.
    private SpeechSynthesizer mSpeechSynthesizer;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.collected_main);
        init();
    }

    private void init() {
        getSupportActionBar().setTitle(getResources().getString(R.string.title_favorite));
        mSharedPreferences = getSharedPreferences(getPackageName(), Activity.MODE_PRIVATE);
        recent_used_lv = (RecyclerView) findViewById(R.id.collected_listview);
        mSpeechSynthesizer = SpeechSynthesizer.createSynthesizer(this, null);

        recent_used_lv.setHasFixedSize(true);
        recent_used_lv.setLayoutManager(new LinearLayoutManager(this));
        recent_used_lv.addItemDecoration(new DividerItemDecoration(getResources().getDrawable(R.drawable.abc_list_divider_mtrl_alpha)));

        beans = new ArrayList<record>();
        beans.addAll(DataBaseUtil.getInstance().getDataListCollected(0, Settings.offset));
        mAdapter = new RcCollectTranslateListAdapter(mSpeechSynthesizer, mSharedPreferences, beans);
        mAdapter.setItems(beans);
        recent_used_lv.setAdapter(mAdapter);
    }

}
