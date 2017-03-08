package com.messi.cantonese.study.adapter;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.CheckBox;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.avos.avoscloud.AVAnalytics;
import com.iflytek.cloud.SpeechConstant;
import com.iflytek.cloud.SpeechError;
import com.iflytek.cloud.SpeechSynthesizer;
import com.iflytek.cloud.SynthesizerListener;
import com.messi.cantonese.study.PracticeActivity;
import com.messi.cantonese.study.R;
import com.messi.cantonese.study.dao.record;
import com.messi.cantonese.study.db.DataBaseUtil;
import com.messi.cantonese.study.task.MyThread;
import com.messi.cantonese.study.util.AudioTrackUtil;
import com.messi.cantonese.study.util.KeyUtil;
import com.messi.cantonese.study.util.LogUtil;
import com.messi.cantonese.study.util.SDCardUtil;
import com.messi.cantonese.study.util.Settings;
import com.messi.cantonese.study.util.ToastUtil;
import com.messi.cantonese.study.util.XFUtil;
import com.messi.cantonese.study.wxapi.WXEntryActivity;

import java.util.List;

/**
 * Created by luli on 10/23/16.
 */

public class RcTranslateLiatItemViewHolder extends RecyclerView.ViewHolder {

    private Context context;
    public TextView record_question;
    public TextView record_answer;
    public FrameLayout record_answer_cover;
    public FrameLayout record_to_practice;
    public FrameLayout record_question_cover;
    public FrameLayout delete_btn;
    public FrameLayout copy_btn;
    public FrameLayout collected_btn;
    public FrameLayout weixi_btn;
    public ImageButton voice_play;
    public ImageView unread_dot_answer;
    public ImageView unread_dot_question;
    public CheckBox collected_cb;
    public FrameLayout voice_play_layout;
    public ProgressBar play_content_btn_progressbar;

    private AnimationDrawable currentAnimationDrawable;
    private Thread mThread;
    private MyThread mMyThread;
    private Handler mHandler;

    private List<record> beans;
    private SpeechSynthesizer mSpeechSynthesizer;
    private SharedPreferences mSharedPreferences;
    private RcTranslateListAdapter mAdapter;

    public RcTranslateLiatItemViewHolder(View convertView,
                                         List<record> mBeans,
                                         SpeechSynthesizer mSpeechSynthesizer,
                                         SharedPreferences mSharedPreferences,
                                         RcTranslateListAdapter mAdapter) {
        super(convertView);
        this.context = convertView.getContext();
        this.beans = mBeans;
        this.mAdapter = mAdapter;
        this.mSpeechSynthesizer = mSpeechSynthesizer;
        this.mSharedPreferences = mSharedPreferences;
        record_question_cover = (FrameLayout) convertView.findViewById(R.id.record_question_cover);
        record_answer_cover = (FrameLayout) convertView.findViewById(R.id.record_answer_cover);
        record_to_practice = (FrameLayout) convertView.findViewById(R.id.record_to_practice);
        record_question = (TextView) convertView.findViewById(R.id.record_question);
        record_answer = (TextView) convertView.findViewById(R.id.record_answer);
        voice_play = (ImageButton) convertView.findViewById(R.id.voice_play);
        unread_dot_answer = (ImageView) convertView.findViewById(R.id.unread_dot_answer);
        unread_dot_question = (ImageView) convertView.findViewById(R.id.unread_dot_question);
        collected_cb = (CheckBox) convertView.findViewById(R.id.collected_cb);
        voice_play_layout = (FrameLayout) convertView.findViewById(R.id.voice_play_layout);
        delete_btn = (FrameLayout) convertView.findViewById(R.id.delete_btn);
        copy_btn = (FrameLayout) convertView.findViewById(R.id.copy_btn);
        collected_btn = (FrameLayout) convertView.findViewById(R.id.collected_btn);
        weixi_btn = (FrameLayout) convertView.findViewById(R.id.weixi_btn);
        play_content_btn_progressbar = (ProgressBar) convertView.findViewById(R.id.play_content_btn_progressbar);
        mHandler = new Handler() {
            public void handleMessage(Message message) {
                if (message.what == MyThread.EVENT_PLAY_OVER) {
                    mThread = null;
                    if (currentAnimationDrawable != null) {
                        currentAnimationDrawable.setOneShot(true);
                        currentAnimationDrawable.stop();
                        currentAnimationDrawable.selectDrawable(0);
                    }
                    resetStatus();
                }
            }
        };
        mMyThread = new MyThread(mHandler);
    }

    public void render(final record mBean) {
        AnimationDrawable animationDrawable = (AnimationDrawable) voice_play.getBackground();
        MyOnClickListener mMyOnClickListener = new MyOnClickListener(mBean,animationDrawable,voice_play,play_content_btn_progressbar,true);
        MyOnClickListener mQuestionOnClickListener = new MyOnClickListener(mBean,animationDrawable,voice_play,play_content_btn_progressbar,false);
        if(mBean.getIscollected().equals("0")){
            collected_cb.setChecked(false);
        }else{
            collected_cb.setChecked(true);
        }
        if (getLayoutPosition() == 0) {
            if (!mSharedPreferences.getBoolean(KeyUtil.IsShowAnswerUnread, false)) {
                unread_dot_answer.setVisibility(View.VISIBLE);
            } else {
                unread_dot_answer.setVisibility(View.GONE);
            }
            if (!mSharedPreferences.getBoolean(KeyUtil.IsShowQuestionUnread, false)) {
                unread_dot_question.setVisibility(View.VISIBLE);
            } else {
                unread_dot_question.setVisibility(View.GONE);
            }
        } else {
            unread_dot_answer.setVisibility(View.GONE);
            unread_dot_question.setVisibility(View.GONE);
        }
        record_question.setText(mBean.getChinese());
        record_answer.setText(mBean.getEnglish());

        record_question_cover.setOnClickListener(mQuestionOnClickListener);
        record_answer_cover.setOnClickListener(mMyOnClickListener);
        voice_play_layout.setOnClickListener(mMyOnClickListener);

        record_answer_cover.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                Settings.copy(context,mBean.getEnglish());
                return true;
            }
        });
        record_question_cover.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                Settings.copy(context,mBean.getChinese());
                return true;
            }
        });

        delete_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteEntity(getLayoutPosition());
            }
        });
        copy_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mBean.getEnglish().contains("英[") || mBean.getEnglish().contains("美[")){
                    Settings.copy(context,mBean.getChinese()+"\n"+mBean.getEnglish());
                }else {
                    Settings.copy(context,mBean.getEnglish());
                }
                AVAnalytics.onEvent(context, "tab1_tran_copy");
            }
        });
        weixi_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String text = "";
                if(mBean.getEnglish().contains("英[") || mBean.getEnglish().contains("美[")){
                    text = mBean.getChinese()+"\n"+mBean.getEnglish();
                }else{
                    text = mBean.getEnglish();
                }
                Settings.share(context,text);
                AVAnalytics.onEvent(context, "tab1_share_btn");
            }
        });
        collected_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateCollectedStatus(mBean);
                AVAnalytics.onEvent(context, "tab1_tran_collected");
            }
        });
        record_to_practice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context,PracticeActivity.class);
                WXEntryActivity.dataMap.put(KeyUtil.DialogBeanKey, mBean);
                context.startActivity(intent);
                AVAnalytics.onEvent(context, "tab1_tran_to_practicepg");
            }
        });
    }

    public void deleteEntity(int position) {
        try{
            record mBean = beans.remove(position);
            mAdapter.notifyItemRemoved(position);
            DataBaseUtil.getInstance().dele(mBean);
            ToastUtil.diaplayMesShort(context,context.getResources().getString(R.string.dele_success));
            AVAnalytics.onEvent(context, "tab1_tran_delete");
        } catch (Exception e){
            e.printStackTrace();
        }
    }

    private void updateCollectedStatus(record mBean){
        if (mBean.getIscollected().equals("0")) {
            mBean.setIscollected("1");
            ToastUtil.diaplayMesShort(context,context.getResources().getString(R.string.favorite_success));
        } else {
            mBean.setIscollected("0");
            ToastUtil.diaplayMesShort(context,context.getResources().getString(R.string.favorite_cancle));
        }
        mAdapter.notifyDataSetChanged();
        DataBaseUtil.getInstance().update(mBean);
    }

    public class MyOnClickListener implements View.OnClickListener {

        private record mBean;
        private ImageButton voice_play;
        private AnimationDrawable animationDrawable;
        private ProgressBar play_content_btn_progressbar;
        private boolean isPlayResult;
        boolean isNotify = false;

        private MyOnClickListener(record bean,AnimationDrawable mAnimationDrawable,ImageButton voice_play,
                                  ProgressBar progressbar, boolean isPlayResult){
            this.mBean = bean;
            this.voice_play = voice_play;
            this.animationDrawable = mAnimationDrawable;
            this.play_content_btn_progressbar = progressbar;
            this.isPlayResult = isPlayResult;
        }
        @Override
        public void onClick(final View v) {
            boolean isPlay = true;
            stopPlay();
            if (TextUtils.isEmpty(mBean.getBackup2())) {
                isPlay = true;
            } else if (isPlayResult && (mBean.getBackup2().equals(XFUtil.PlayResultOnline) || mBean.getBackup2().equals(XFUtil.PlayResultOffline))) {
                isPlay = false;
            } else if (!isPlayResult && (mBean.getBackup2().equals(XFUtil.PlayQueryOnline) || mBean.getBackup2().equals(XFUtil.PlayQueryOffline))) {
                isPlay = false;
            }
            resetStatus();
            if (isPlay) {
                String path = SDCardUtil.getDownloadPath(SDCardUtil.sdPath);
                if (TextUtils.isEmpty(mBean.getResultVoiceId()) || TextUtils.isEmpty(mBean.getQuestionVoiceId())) {
                    mBean.setQuestionVoiceId(System.currentTimeMillis() + "");
                    mBean.setResultVoiceId(System.currentTimeMillis() - 5 + "");
                }
                String filepath = "";
                String speakContent = "";

                if (!mSharedPreferences.getBoolean(KeyUtil.IsShowAnswerUnread, false)){
                    isNotify = true;
                }
                if (!mSharedPreferences.getBoolean(KeyUtil.IsShowQuestionUnread, false)) {
                    isNotify = true;
                }
                if (isPlayResult) {
                    Settings.saveSharedPreferences(mSharedPreferences, KeyUtil.IsShowAnswerUnread, true);
                    filepath = path + mBean.getResultVoiceId() + ".pcm";
                    mBean.setResultAudioPath(filepath);
                    speakContent = mBean.getEnglish();
                } else {
                    Settings.saveSharedPreferences(mSharedPreferences, KeyUtil.IsShowQuestionUnread, true);
                    filepath = path + mBean.getQuestionVoiceId() + ".pcm";
                    mBean.setQuestionAudioPath(filepath);
                    speakContent = mBean.getChinese();
                }
                if (mBean.getSpeak_speed() != mSharedPreferences.getInt(context.getString(R.string.preference_key_tts_speed), 50)) {
                    String filep1 = path + mBean.getResultVoiceId() + ".pcm";
                    String filep2 = path + mBean.getQuestionVoiceId() + ".pcm";
                    AudioTrackUtil.deleteFile(filep1);
                    AudioTrackUtil.deleteFile(filep2);
                    mBean.setSpeak_speed(mSharedPreferences.getInt(context.getString(R.string.preference_key_tts_speed), 50));
                }
                mSpeechSynthesizer.setParameter(SpeechConstant.TTS_AUDIO_PATH, filepath);
                if (!AudioTrackUtil.isFileExists(filepath)) {
                    String speaker = "";
                    if (isPlayResult) {
                        if(mBean.getBackup3().equals(Settings.yue)){
                            speaker = XFUtil.SpeakerHk;
                        }else{
                            speaker = XFUtil.SpeakerZh;
                        }
                        mBean.setBackup2(XFUtil.PlayResultOnline);
                    } else {
                        if(mBean.getBackup3().equals(Settings.yue)){
                            speaker = XFUtil.SpeakerZh;
                        }else{
                            speaker = XFUtil.SpeakerHk;
                        }
                        mBean.setBackup2(XFUtil.PlayQueryOnline);
                    }
                    play_content_btn_progressbar.setVisibility(View.VISIBLE);
                    voice_play.setVisibility(View.GONE);

                    XFUtil.showSpeechSynthesizer(context, mSharedPreferences, mSpeechSynthesizer, speakContent,
                            speaker,new SynthesizerListener() {
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
                                    play_content_btn_progressbar.setVisibility(View.GONE);
                                    voice_play.setVisibility(View.VISIBLE);
                                    if (!animationDrawable.isRunning()) {
                                        animationDrawable.setOneShot(false);
                                        animationDrawable.start();
                                    }
                                }

                                @Override
                                public void onCompleted(SpeechError arg0) {
                                    LogUtil.DefalutLog("---onCompleted");
                                    if (arg0 != null) {
                                        ToastUtil.diaplayMesShort(context, arg0.getErrorDescription());
                                    }
                                    DataBaseUtil.getInstance().update(mBean);
                                    animationDrawable.setOneShot(true);
                                    animationDrawable.stop();
                                    animationDrawable.selectDrawable(0);
                                    if(isNotify){
                                        mAdapter.notifyDataSetChanged();
                                    }
                                }

                                @Override
                                public void onBufferProgress(int arg0, int arg1, int arg2, String arg3) {
                                }

                                @Override
                                public void onEvent(int arg0, int arg1, int arg2, Bundle arg3) {
                                }
                            });
                } else {
                    if (isPlayResult) {
                        mBean.setBackup2(XFUtil.PlayResultOffline);
                    } else {
                        mBean.setBackup2(XFUtil.PlayQueryOffline);
                    }
                    if (!animationDrawable.isRunning()) {
                        animationDrawable.setOneShot(false);
                        animationDrawable.start();
                    }
                    currentAnimationDrawable = animationDrawable;
                    mMyThread.setDataUri(filepath);
                    mThread = AudioTrackUtil.startMyThread(mMyThread);
                    LogUtil.DefalutLog("mThread--start:" + mThread.getId());
                }
                if (v.getId() == R.id.record_question_cover) {
                    AVAnalytics.onEvent(context, "tab1_tran_play_question");
                } else if (v.getId() == R.id.record_answer_cover) {
                    AVAnalytics.onEvent(context, "tab1_tran_play_result");
                } else if (v.getId() == R.id.voice_play_layout) {
                    AVAnalytics.onEvent(context, "tab1_tran_play_voice");
                }
            }
        }
    }

    public void stopPlay(){
        AudioTrackUtil.stopPlayOnline(mSpeechSynthesizer);
        AudioTrackUtil.stopPlayPcm(mThread);
    }

    public void resetStatus() {
        for (record mBean : beans) {
            mBean.setBackup2("");
        }
    }

}
