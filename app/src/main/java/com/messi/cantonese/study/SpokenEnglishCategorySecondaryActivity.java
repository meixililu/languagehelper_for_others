package com.messi.cantonese.study;

import android.content.SharedPreferences;
import android.graphics.drawable.AnimationDrawable;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.avos.avoscloud.AVAnalytics;
import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVObject;
import com.avos.avoscloud.AVQuery;
import com.facebook.drawee.view.SimpleDraweeView;
import com.iflytek.cloud.RecognizerListener;
import com.iflytek.cloud.RecognizerResult;
import com.iflytek.cloud.SpeechConstant;
import com.iflytek.cloud.SpeechError;
import com.iflytek.cloud.SpeechRecognizer;
import com.iflytek.cloud.SpeechSynthesizer;
import com.iflytek.cloud.SynthesizerListener;
import com.messi.cantonese.study.adapter.RcSpokenEndlishCategorySecondaryAdapter;
import com.messi.cantonese.study.bean.UserSpeakBean;
import com.messi.cantonese.study.impl.SpokenEnglishPlayListener;
import com.messi.cantonese.study.task.MyThread;
import com.messi.cantonese.study.util.AVOUtil;
import com.messi.cantonese.study.util.AudioTrackUtil;
import com.messi.cantonese.study.util.DownLoadUtil;
import com.messi.cantonese.study.util.JsonParser;
import com.messi.cantonese.study.util.KeyUtil;
import com.messi.cantonese.study.util.LogUtil;
import com.messi.cantonese.study.util.SDCardUtil;
import com.messi.cantonese.study.util.ScoreUtil;
import com.messi.cantonese.study.util.Settings;
import com.messi.cantonese.study.util.ToastUtil;
import com.messi.cantonese.study.util.XFUtil;
import com.yqritc.recyclerviewflexibledivider.HorizontalDividerItemDecoration;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class SpokenEnglishCategorySecondaryActivity extends BaseActivity implements View.OnClickListener, SpokenEnglishPlayListener {

    @BindView(R.id.listview)
    RecyclerView studylist_lv;
    @BindView(R.id.sentence_cb)
    RadioButton sentence_cb;
    @BindView(R.id.sentence_cover)
    FrameLayout sentence_cover;
    @BindView(R.id.continuity_cb)
    RadioButton continuity_cb;
    @BindView(R.id.continuity_cover)
    FrameLayout continuity_cover;
    @BindView(R.id.conversation_cb)
    RadioButton conversation_cb;
    @BindView(R.id.conversation_cover)
    FrameLayout conversation_cover;
    @BindView(R.id.speak_type)
    LinearLayout speak_type;
    @BindView(R.id.previous_btn)
    FrameLayout previous_btn;
    @BindView(R.id.start_btn)
    TextView start_btn;
    @BindView(R.id.start_btn_cover)
    FrameLayout start_btn_cover;
    @BindView(R.id.next_btn)
    FrameLayout next_btn;
    @BindView(R.id.record_anim_img)
    ImageView record_anim_img;
    @BindView(R.id.record_layout)
    LinearLayout record_layout;
    @BindView(R.id.voice_img)
    ImageButton voice_img;

    @BindView(R.id.conversation_layout)
    RelativeLayout conversationLayout;
    @BindView(R.id.speaker_content)
    TextView speakerContent;
    @BindView(R.id.user_content)
    TextView userContent;
    @BindView(R.id.speaker_img)
    SimpleDraweeView speakerImg;
    @BindView(R.id.user_img)
    TextView userImg;
    private RcSpokenEndlishCategorySecondaryAdapter mAdapter;
    private LinearLayoutManager mLinearLayoutManager;
    private List<AVObject> avObjects;
    private int position;
    private boolean isNewIn = true;
    private boolean isFollow;
    private StringBuilder sbResult = new StringBuilder();
    private String userPcmPath;

    private SpeechSynthesizer mSpeechSynthesizer;
    private SharedPreferences mSharedPreferences;
    private SpeechRecognizer recognizer;
    private MediaPlayer mPlayer;
    private AnimationDrawable animationDrawable;
    private Thread mThread;
    private MyThread mMyThread;
    private boolean userFirst;
    private int skip = 0;
    private String code;
    private boolean loading;
    private boolean hasMore = true;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.spoken_english_category_secondary_activity);
        ButterKnife.bind(this);
        initSwipeRefresh();
        initViews();
        getDataTask();
    }

    private void initViews() {
        setStatusbarColor(R.color.white_alph);
        code = getIntent().getStringExtra(AVOUtil.EvaluationDetail.ECCode);
        mSharedPreferences = Settings.getSharedPreferences(this);
        mSpeechSynthesizer = SpeechSynthesizer.createSynthesizer(this, null);
        recognizer = SpeechRecognizer.createRecognizer(this, null);
        mPlayer = new MediaPlayer();
        mMyThread = new MyThread();
        avObjects = new ArrayList<AVObject>();
        studylist_lv = (RecyclerView) findViewById(R.id.listview);
        mAdapter = new RcSpokenEndlishCategorySecondaryAdapter(avObjects, this);
        mAdapter.setItems(avObjects);
        mAdapter.setFooter(new Object());
        hideFooterview();
        mLinearLayoutManager = new LinearLayoutManager(this);
        studylist_lv.setLayoutManager(mLinearLayoutManager);
        studylist_lv.addItemDecoration(
                new HorizontalDividerItemDecoration.Builder(this)
                        .colorResId(R.color.text_tint)
                        .sizeResId(R.dimen.list_divider_size)
                        .marginResId(R.dimen.padding_margin, R.dimen.padding_margin)
                        .build());
        studylist_lv.setAdapter(mAdapter);

        selectedFlowType(mSharedPreferences.getInt(KeyUtil.ReadModelType, 0), false);
        sentence_cover.setOnClickListener(this);
        continuity_cover.setOnClickListener(this);
        conversation_cover.setOnClickListener(this);
        previous_btn.setOnClickListener(this);
        conversationLayout.setOnClickListener(this);
        next_btn.setOnClickListener(this);
        start_btn_cover.setOnClickListener(this);
        animationDrawable = (AnimationDrawable) voice_img.getBackground();
        setListOnScrollListener();
    }

    public void setListOnScrollListener() {
        studylist_lv.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                int visible = mLinearLayoutManager.getChildCount();
                int total = mLinearLayoutManager.getItemCount();
                int firstVisibleItem = mLinearLayoutManager.findFirstCompletelyVisibleItemPosition();
                if (!loading && hasMore) {
                    if ((visible + firstVisibleItem) >= total) {
                        getDataTask();
                    }
                }
            }
        });
    }

    private void startVoiceImgAnimation() {
        voice_img.setVisibility(View.VISIBLE);
        record_layout.setVisibility(View.GONE);
        if (!animationDrawable.isRunning()) {
            animationDrawable.setOneShot(false);
            animationDrawable.start();
        }
    }

    private void stopVoiceImgAnimation() {
        animationDrawable.setOneShot(true);
        animationDrawable.stop();
        animationDrawable.selectDrawable(0);
        voice_img.setVisibility(View.GONE);
        record_layout.setVisibility(View.VISIBLE);
    }

    private void selectedFlowType(int selectedNum, boolean isReset) {
        sentence_cb.setChecked(false);
        continuity_cb.setChecked(false);
        conversation_cb.setChecked(false);
        if (selectedNum == 0) {
            conversationLayout.setVisibility(View.GONE);
            sentence_cb.setChecked(true);
            setSelected(position);
        } else if (selectedNum == 1) {
            conversationLayout.setVisibility(View.GONE);
            continuity_cb.setChecked(true);
            setSelected(position);
        } else if (selectedNum == 2) {
            conversationLayout.setVisibility(View.VISIBLE);
            conversation_cb.setChecked(true);
            userFirst = false;
            position = 0;
            setConversationContent();
            changeConversationLayout(false);
        }
        Settings.saveSharedPreferences(mSharedPreferences, KeyUtil.ReadModelType, selectedNum);
    }

    private void changeConversationLayout(boolean isUserTurn) {
        if (isUserTurn) {
            userContent.setVisibility(View.VISIBLE);
            speakerContent.setVisibility(View.GONE);
            userImg.setVisibility(View.VISIBLE);
            speakerImg.setVisibility(View.GONE);
        } else {
            speakerContent.setVisibility(View.VISIBLE);
            userContent.setVisibility(View.GONE);
            speakerImg.setVisibility(View.VISIBLE);
            userImg.setVisibility(View.GONE);
        }
    }

    private void previousItem() {
        if (conversation_cb.isChecked() && position > 1) {
            position -= 2;
            setDatas();
        } else if (!conversation_cb.isChecked() && position > 0) {
            position--;
            setDatas();
        } else {
            ToastUtil.diaplayMesShort(SpokenEnglishCategorySecondaryActivity.this, "到头了");
        }
        AVAnalytics.onEvent(SpokenEnglishCategorySecondaryActivity.this, "evaluationdetail_pg_previous_btn");
    }

    private void nextItem() {
        if (conversation_cb.isChecked() && position < avObjects.size() - 2) {
            position += 2;
            setDatas();
        } else if (!conversation_cb.isChecked() && position < avObjects.size() - 1) {
            position++;
            setDatas();
        } else {
            if(conversation_cb.isChecked()){
                userContent.setText("");
                speakerContent.setText("");
            }else {
                ToastUtil.diaplayMesShort(SpokenEnglishCategorySecondaryActivity.this, "木有了");
            }
        }
        AVAnalytics.onEvent(SpokenEnglishCategorySecondaryActivity.this, "evaluationdetail_pg_next_btn");
    }

    private void setDatas() {
        if (conversation_cb.isChecked()) {
            setConversationContent();
        } else {
            playOrStop(position);
            studylist_lv.scrollToPosition(position);
        }
    }

    private void setConversationContent() {
        if (avObjects.size() > 0) {
            if (!userFirst) {
                speakerContent.setText(getEnglishContent(avObjects.get(position)));
                if ((position + 1) < avObjects.size()) {
                    userContent.setText(getEnglishContent(avObjects.get(position + 1)));
                } else {
                    userContent.setText("");
                }
            } else {
                userContent.setText(getEnglishContent(avObjects.get(position)));
                if ((position + 1) < avObjects.size()) {
                    speakerContent.setText(getEnglishContent(avObjects.get(position + 1)));
                } else {
                    speakerContent.setText("");
                }
            }
        }
    }

    public void showIatDialog() {
        try {
            if (recognizer != null) {
                if (!recognizer.isListening()) {
                    if (conversation_cb.isChecked()) {
                        if (userFirst) {
                            changeConversationLayout(true);
                            startToRecord();
                        } else {
                            if (isNewIn) {
                                changeConversationLayout(false);
                                startToPlaySpeaker(position);
                            } else {
                                changeConversationLayout(true);
                                startToRecord();
                            }
                        }
                    } else {
                        if (isNewIn) {
                            startToPlaySpeaker(position);
                        } else {
                            startToRecord();
                        }
                    }
                } else {
                    showProgressbar();
                    finishRecord();
                }
            }
            AVAnalytics.onEvent(SpokenEnglishCategorySecondaryActivity.this, "evaluationdetail_pg_speak_btn");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void startToPlaySpeaker(int index) {
        isNewIn = false;
        isFollow = true;
        start_btn.setText("");
        startVoiceImgAnimation();
        AVObject mBean = avObjects.get(index);
        mBean.put(KeyUtil.UserSpeakBean, new UserSpeakBean());
        mAdapter.notifyDataSetChanged();
        playItem(mBean);
    }

    private void startToRecord() {
        stopVoiceImgAnimation();
        start_btn.setText("");
        XFUtil.showSpeechRecognizer(this, mSharedPreferences, recognizer,
                recognizerListener, XFUtil.VoiceEngineHK);
//        String path = SDCardUtil.getDownloadPath(SDCardUtil.UserPracticePath);
//        userPcmPath = path + "/userpractice.pcm";
//        recognizer.setParameter(SpeechConstant.ASR_AUDIO_PATH, userPcmPath);
    }

    /**
     * finish record
     */
    private void finishRecord() {
        if (recognizer != null) {
            if (recognizer.isListening()) {
                recognizer.stopListening();
            }
            isNewIn = true;
            isFollow = false;
            record_layout.setVisibility(View.GONE);
            record_anim_img.setBackgroundResource(R.drawable.speak_voice_1);
            start_btn.setText(this.getResources().getString(R.string.start));
        }
    }

    private void onfinishPlay() {
        if (isFollow) {
            isFollow = false;
            if(userFirst && conversation_cb.isChecked()){
                if(position < avObjects.size()-2){
                    showNext();
                }else {
                    stopVoiceImgAnimation();
                    finishRecord();
                    ToastUtil.diaplayMesShort(SpokenEnglishCategorySecondaryActivity.this, "很好，本节已完成！");
                }
            }else if(!userFirst && conversation_cb.isChecked() && position == avObjects.size()-1){
                changeRoles();
            } else {
                showIatDialog();
            }
        }
    }

    @Override
    public void onSwipeRefreshLayoutRefresh() {
        super.onSwipeRefreshLayoutRefresh();
        skip = 0;
        hideFooterview();
        getDataTask();
    }

    private void getDataTask() {
        loading = true;
        showProgressbar();
        Observable.create(new ObservableOnSubscribe<String>() {
            @Override
            public void subscribe(ObservableEmitter<String> e) throws Exception {
                queryData();
                e.onComplete();
            }
        })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<String>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                    }
                    @Override
                    public void onNext(String s) {
                    }
                    @Override
                    public void onError(Throwable e) {
                    }
                    @Override
                    public void onComplete() {
                        onQueryDataFinish();
                        setConversationContent();
                    }
                });

    }

    private void queryData() {
        AVQuery<AVObject> query = new AVQuery<AVObject>(AVOUtil.EvaluationDetail.EvaluationDetail);
        query.whereEqualTo(AVOUtil.EvaluationDetail.EDIsValid, "1");
        query.whereEqualTo(AVOUtil.EvaluationDetail.ECCode, code);
        query.orderByAscending(AVOUtil.EvaluationDetail.EDCode);
        query.skip(skip);
        query.limit(20);
        try {
            List<AVObject> avObject = query.find();
            if (avObject != null) {
                if(avObject.size() == 0){
                    hasMore = false;
                }else if(avObject.size() > 0){
                    if (skip == 0) {
                        avObjects.clear();
                    }
                    if(avObject.size() == 20){
                        skip += 20;
                        hasMore = true;
                    }else if(avObject.size() < 20){
                        hasMore = false;
                    }
                    for (int i = 0; i < avObject.size(); i++) {
                        if (i == 0 && skip <= 20) {
                            avObject.get(i).put(KeyUtil.PracticeItemIndex, "1");
                        } else {
                            avObject.get(i).put(KeyUtil.PracticeItemIndex, "0");
                        }
                    }
                    avObjects.addAll(avObject);
                }
            }
        } catch (AVException e) {
            e.printStackTrace();
        }
    }

    private void onQueryDataFinish() {
        loading = false;
        hideProgressbar();
        if(hasMore){
            showFooterview();
        }else {
            hideFooterview();
        }
        onSwipeRefreshLayoutFinish();
        mAdapter.notifyDataSetChanged();
    }

    private void resetData() {
        for (AVObject item : avObjects) {
            if (item.get(KeyUtil.PracticeItemIndex).equals("1")) {
                item.put(KeyUtil.PracticeItemIndex, "0");
            }
        }
    }

    private void setSelected(int index) {
        if(avObjects.size() > position){
            position = index;
            resetData();
            avObjects.get(index).put(KeyUtil.PracticeItemIndex, "1");
            mAdapter.notifyDataSetChanged();
        }
    }

    private String getEnglishContent(AVObject avObject) {
        String temp = avObject.getString(AVOUtil.EvaluationDetail.EDContent);
        String[] str = temp.split("#");
        if (str.length > 1) {
            return temp.split("#")[0];
        } else {
            return temp;
        }
    }

    @Override
    public void playOrStop(int index) {
        setSelected(index);
        playItem(avObjects.get(index));
        AVAnalytics.onEvent(SpokenEnglishCategorySecondaryActivity.this, "evaluationdetail_pg_play_result");
    }

    public void playItem(AVObject avObject) {
        if (!TextUtils.isEmpty(avObject.getString(AVOUtil.EvaluationDetail.mp3url))) {
            playMp3(avObject.getString(AVOUtil.EvaluationDetail.mp3url),
                    SDCardUtil.SpokenEnglishPath,
                    (avObject.getObjectId() + ".mp3"));
        } else {
            String path = SDCardUtil.getDownloadPath(SDCardUtil.EvaluationPath);
            String filepath = path + avObject.getString(AVOUtil.EvaluationDetail.objectId) + ".pcm";
            mSpeechSynthesizer.setParameter(SpeechConstant.TTS_AUDIO_PATH, filepath);
            if (!AudioTrackUtil.isFileExists(filepath)) {
                showProgressbar();
                XFUtil.showSpeechSynthesizer(SpokenEnglishCategorySecondaryActivity.this, mSharedPreferences,
                        mSpeechSynthesizer, getEnglishContent(avObject), XFUtil.SpeakerHk,
                        new SynthesizerListener() {
                            @Override
                            public void onSpeakResumed() {
                            }

                            @Override
                            public void onSpeakProgress(int arg0, int arg1, int arg2) {
                            }

                            @Override
                            public void onSpeakPaused() {
                            }

                            @Override
                            public void onSpeakBegin() {
                                hideProgressbar();
                            }

                            @Override
                            public void onCompleted(SpeechError arg0) {
                                if (arg0 != null) {
                                    ToastUtil.diaplayMesShort(SpokenEnglishCategorySecondaryActivity.this, arg0.getErrorDescription());
                                }
                                onfinishPlay();
                            }

                            @Override
                            public void onBufferProgress(int arg0, int arg1, int arg2, String arg3) {
                            }

                            @Override
                            public void onEvent(int arg0, int arg1, int arg2, Bundle arg3) {
                            }
                        });
            } else {
                playLocalPcm(filepath);
            }
        }
    }

    private void playMp3(final String url, final String path, final String fileName) {
        Observable.create(new ObservableOnSubscribe<String>() {
            @Override
            public void subscribe(ObservableEmitter<String> e) throws Exception {
                String fullName = SDCardUtil.getDownloadPath(path) + fileName;
                if (!AudioTrackUtil.isFileExists(fullName)) {
                    e.onNext("showProgressbar");
                    DownLoadUtil.downloadFile(SpokenEnglishCategorySecondaryActivity.this, url, path, fileName);
                    e.onNext("hideProgressbar");
                }
                playMp3(fullName, e);
            }
        })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<String>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                    }
                    @Override
                    public void onNext(String s) {
                        if (s.equals("showProgressbar")) {
                            showProgressbar();
                        }
                        if (s.equals("hideProgressbar")) {
                            hideProgressbar();
                        }
                    }
                    @Override
                    public void onError(Throwable e) {
                    }
                    @Override
                    public void onComplete() {
                        onfinishPlay();
                    }
                });

    }

    private void playLocalPcm(final String path) {
        Observable.create(new ObservableOnSubscribe<String>() {
            @Override
            public void subscribe(ObservableEmitter<String> e) throws Exception {
                AudioTrackUtil.createAudioTrack(path);
                e.onComplete();
            }
        })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<String>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                    }
                    @Override
                    public void onNext(String s) {
                    }
                    @Override
                    public void onError(Throwable e) {
                    }
                    @Override
                    public void onComplete() {
                        onfinishPlay();
                    }
                });
    }

    private void playMp3(String uriPath, final ObservableEmitter<String> subscriber) {
        try {
            mPlayer.reset();
            LogUtil.DefalutLog("uriPath:" + uriPath);
            Uri uri = Uri.parse(uriPath);
            mPlayer.setDataSource(this, uri);
            mPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mp) {
                    subscriber.onComplete();
                }
            });
            mPlayer.prepare();
            mPlayer.start();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    RecognizerListener recognizerListener = new RecognizerListener() {

        @Override
        public void onResult(RecognizerResult results, boolean isLast) {
            String text = JsonParser.parseIatResult(results.getResultString());
            sbResult.append(text);
            if (isLast) {
                LogUtil.DefalutLog("isLast---onResult:" + sbResult.toString());
                hideProgressbar();
                finishRecord();
                if (!conversation_cb.isChecked()) {
                    AVObject mSpeakItem = avObjects.get(position);
                    UserSpeakBean bean = ScoreUtil.score(SpokenEnglishCategorySecondaryActivity.this, sbResult.toString(), getEnglishContent(mSpeakItem), 0);
                    mSpeakItem.put(KeyUtil.UserSpeakBean, bean);
                    mAdapter.notifyDataSetChanged();
                }
                sbResult.setLength(0);
                playNext();
//                mMyThread = AudioTrackUtil.getMyThread(userPcmPath);
            }
        }

        @Override
        public void onError(SpeechError error) {
            LogUtil.DefalutLog("onError:" + error.getErrorDescription());
            finishRecord();
            hideProgressbar();
            ToastUtil.diaplayMesShort(SpokenEnglishCategorySecondaryActivity.this, error.getErrorDescription());
        }

        @Override
        public void onBeginOfSpeech() {
            LogUtil.DefalutLog("evaluator begin");
        }

        @Override
        public void onEndOfSpeech() {
            LogUtil.DefalutLog("evaluator end");
            finishRecord();
            showProgressbar();
        }

        @Override
        public void onEvent(int arg0, int arg1, int arg2, Bundle arg3) {

        }

        @Override
        public void onVolumeChanged(int volume, byte[] arg1) {
            if (volume < 4) {
                record_anim_img.setBackgroundResource(R.drawable.speak_voice_1);
            } else if (volume < 8) {
                record_anim_img.setBackgroundResource(R.drawable.speak_voice_2);
            } else if (volume < 12) {
                record_anim_img.setBackgroundResource(R.drawable.speak_voice_3);
            } else if (volume < 16) {
                record_anim_img.setBackgroundResource(R.drawable.speak_voice_4);
            } else if (volume < 20) {
                record_anim_img.setBackgroundResource(R.drawable.speak_voice_5);
            } else if (volume < 24) {
                record_anim_img.setBackgroundResource(R.drawable.speak_voice_6);
            } else if (volume < 31) {
                record_anim_img.setBackgroundResource(R.drawable.speak_voice_7);
            }
        }
    };

    private void playNext() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (continuity_cb.isChecked()) {
                    if (position < avObjects.size() - 1) {
                        showNext();
                    } else {
                        ToastUtil.diaplayMesShort(SpokenEnglishCategorySecondaryActivity.this, "很好，本节已完成！");
                    }
                } else if (conversation_cb.isChecked()) {
                    if (position < avObjects.size() - 2) {
                        if(userFirst){
                            changeConversationLayout(false);
                            startToPlaySpeaker(position+1);
                        }else {
                            showNext();
                        }
                    } else {
                        if(userFirst && position < avObjects.size() - 1){
                            changeConversationLayout(false);
                            startToPlaySpeaker(position+1);
                        }else if (!userFirst) {
                            changeRoles();
                        } else {
                            ToastUtil.diaplayMesShort(SpokenEnglishCategorySecondaryActivity.this, "很好，本节已完成！");
                        }

                    }
                }
            }
        }, 800);
    }

    private void changeRoles(){
        stopVoiceImgAnimation();
        finishRecord();
        userFirst = true;
        position = 0;
        changeConversationLayout(true);
        setDatas();
        ToastUtil.diaplayMesLong(SpokenEnglishCategorySecondaryActivity.this, "交换角色");
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.start_btn_cover:
                showIatDialog();
                break;
            case R.id.sentence_cover:
                selectedFlowType(0, false);
                AVAnalytics.onEvent(SpokenEnglishCategorySecondaryActivity.this, "evaluationdetail_pg_sentence_btn");
                break;
            case R.id.continuity_cover:
                selectedFlowType(1, false);
                AVAnalytics.onEvent(SpokenEnglishCategorySecondaryActivity.this, "evaluationdetail_pg_continuity_btn");
                break;
            case R.id.conversation_cover:
                selectedFlowType(2, true);
                AVAnalytics.onEvent(SpokenEnglishCategorySecondaryActivity.this, "evaluationdetail_pg_conversation_btn");
                break;
            case R.id.previous_btn:
                previousItem();
                break;
            case R.id.next_btn:
                nextItem();
                break;
        }
    }

    private void showNext() {
        onClick(next_btn);
        onClick(start_btn_cover);
    }

    private void hideFooterview() {
        mAdapter.hideFooter();
    }

    private void showFooterview() {
        mAdapter.showFooter();
    }
}
