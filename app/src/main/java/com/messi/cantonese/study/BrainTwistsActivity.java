package com.messi.cantonese.study;

import android.os.Bundle;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.messi.cantonese.study.dao.TwistaItem;
import com.messi.cantonese.study.dao.TwistaResult;
import com.messi.cantonese.study.http.LanguagehelperHttpClient;
import com.messi.cantonese.study.http.UICallback;
import com.messi.cantonese.study.util.JsonParser;
import com.messi.cantonese.study.util.Settings;
import com.messi.cantonese.study.util.ToastUtil;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class BrainTwistsActivity extends BaseActivity {

    @BindView(R.id.question)
    TextView question;
    @BindView(R.id.answer)
    TextView answer;
    @BindView(R.id.answer_cover)
    FrameLayout answerCover;
    private TwistaItem mTwistaItem;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.brain_twists_activity);
        ButterKnife.bind(this);
        setStatusbarColor(R.color.sky_blue);
        initSwipeRefresh();
        getSupportActionBar().setTitle(getResources().getString(R.string.leisuer_twists));
        requestData();
    }

    @Override
    public void onSwipeRefreshLayoutRefresh() {
        requestData();
    }

    private void requestData() {
        showProgressbar();
        LanguagehelperHttpClient.get(Settings.TXBrainTwistsApi, new UICallback(BrainTwistsActivity.this) {
            @Override
            public void onResponsed(String responseString) {
                try {
                    if (JsonParser.isJson(responseString)) {
                        TwistaResult mRoot = JSON.parseObject(responseString, TwistaResult.class);
                        if (mRoot.getCode() == 200) {
                            if (mRoot.getNewslist() != null && mRoot.getNewslist().size() > 0) {
                                mTwistaItem = mRoot.getNewslist().get(0);
                                question.setText(mTwistaItem.getQuest());
                                answer.setText("轻触看答案");
                            }

                        } else {
                            ToastUtil.diaplayMesShort(BrainTwistsActivity.this, mRoot.getMsg());
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailured() {
                ToastUtil.diaplayMesShort(BrainTwistsActivity.this, BrainTwistsActivity.this.getResources().getString(R.string.network_error));
            }

            @Override
            public void onFinished() {
                hideProgressbar();
                onSwipeRefreshLayoutFinish();
            }
        });
    }

    @OnClick(R.id.answer_cover)
    public void onClick() {
        if(mTwistaItem != null){
            if (!mTwistaItem.isShowResult()) {
                answer.setText(mTwistaItem.getResult() + "\n\n\n" + "(轻触更新下一条)");
                mTwistaItem.setShowResult(true);
            } else {
                requestData();
            }
        }
    }
}
