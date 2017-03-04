package com.messi.cantonese.study;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.avos.avoscloud.AVAnalytics;
import com.avos.avoscloud.okhttp.FormEncodingBuilder;
import com.avos.avoscloud.okhttp.RequestBody;
import com.iflytek.cloud.RecognizerListener;
import com.iflytek.cloud.RecognizerResult;
import com.iflytek.cloud.SpeechError;
import com.iflytek.cloud.SpeechRecognizer;
import com.iflytek.cloud.SpeechSynthesizer;
import com.messi.cantonese.study.dao.ChDicDao;
import com.messi.cantonese.study.http.LanguagehelperHttpClient;
import com.messi.cantonese.study.http.UICallback;
import com.messi.cantonese.study.impl.FragmentProgressbarListener;
import com.messi.cantonese.study.util.JsonParser;
import com.messi.cantonese.study.util.KeyUtil;
import com.messi.cantonese.study.util.LogUtil;
import com.messi.cantonese.study.util.Settings;
import com.messi.cantonese.study.util.ShareUtil;
import com.messi.cantonese.study.util.ToastUtil;
import com.messi.cantonese.study.util.XFUtil;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ChineseDictionaryFragment extends Fragment implements OnClickListener {

    @BindView(R.id.btn_bushou)
    TextView btnBushou;
    @BindView(R.id.btn_pinyin)
    TextView btnPinyin;
    private EditText input_et;
    private FrameLayout submit_btn;
    private FrameLayout photo_tran_btn, copy_btn, share_btn;
    private FrameLayout clear_btn_layout;
    private Button voice_btn;
    private LinearLayout speak_round_layout;
    private TextView result_tv;
    private View view;
    private String word;
    private ChDicDao mRoot;
    /**
     * record
     **/
    private LinearLayout record_layout;
    private ImageView record_anim_img;

    // 识别对象
    private SpeechRecognizer recognizer;
    // 缓存，保存当前的引擎参数到下一次启动应用程序使用.
    private SharedPreferences mSharedPreferences;
    //合成对象.
    private SpeechSynthesizer mSpeechSynthesizer;

    private FragmentProgressbarListener mProgressbarListener;
    RecognizerListener recognizerListener = new RecognizerListener() {

        @Override
        public void onBeginOfSpeech() {
            LogUtil.DefalutLog("onBeginOfSpeech");
        }

        @Override
        public void onError(SpeechError err) {
            LogUtil.DefalutLog("onError:" + err.getErrorDescription());
            finishRecord();
            ToastUtil.diaplayMesShort(getActivity(), err.getErrorDescription());
            finishLoadding();
        }

        @Override
        public void onEndOfSpeech() {
            LogUtil.DefalutLog("onEndOfSpeech");
            loadding();
            finishRecord();
        }

        @Override
        public void onResult(RecognizerResult results, boolean isLast) {
            LogUtil.DefalutLog("onResult");
            String text = JsonParser.parseIatResult(results.getResultString());
            input_et.append(text.toLowerCase());
            input_et.setSelection(input_et.length());
            if (isLast) {
                finishRecord();
                submit();
            }
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

    public static ChineseDictionaryFragment getInstance() {
        ChineseDictionaryFragment mMainFragment = new ChineseDictionaryFragment();
        return mMainFragment;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mProgressbarListener = (FragmentProgressbarListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString() + " must implement FragmentProgressbarListener");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        LogUtil.DefalutLog("MainFragment-onCreateView");
        view = inflater.inflate(R.layout.fragment_ch_dictionary, null);
        ButterKnife.bind(this, view);
        init();
        return view;
    }

    private void init() {
        mSharedPreferences = getActivity().getSharedPreferences(getActivity().getPackageName(), Activity.MODE_PRIVATE);
        mSpeechSynthesizer = SpeechSynthesizer.createSynthesizer(getActivity(), null);
        recognizer = SpeechRecognizer.createRecognizer(getActivity(), null);

        result_tv = (TextView) view.findViewById(R.id.result_tv);
        input_et = (EditText) view.findViewById(R.id.input_et);
        submit_btn = (FrameLayout) view.findViewById(R.id.submit_btn);
        photo_tran_btn = (FrameLayout) view.findViewById(R.id.photo_tran_btn);
        copy_btn = (FrameLayout) view.findViewById(R.id.copy_btn);
        share_btn = (FrameLayout) view.findViewById(R.id.share_btn);
        speak_round_layout = (LinearLayout) view.findViewById(R.id.speak_round_layout);
        clear_btn_layout = (FrameLayout) view.findViewById(R.id.clear_btn_layout);
        record_layout = (LinearLayout) view.findViewById(R.id.record_layout);
        record_anim_img = (ImageView) view.findViewById(R.id.record_anim_img);
        voice_btn = (Button) view.findViewById(R.id.voice_btn);

        photo_tran_btn.setOnClickListener(this);
        submit_btn.setOnClickListener(this);
        copy_btn.setOnClickListener(this);
        share_btn.setOnClickListener(this);
        speak_round_layout.setOnClickListener(this);
        clear_btn_layout.setOnClickListener(this);
        btnBushou.setOnClickListener(this);
        btnPinyin.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.submit_btn) {
            hideIME();
            submit();
            AVAnalytics.onEvent(getActivity(), "tab1_submit_btn");
        } else if (v.getId() == R.id.speak_round_layout) {
            showIatDialog();
            AVAnalytics.onEvent(getActivity(), "tab1_speak_btn");
        } else if (v.getId() == R.id.clear_btn_layout) {
            input_et.setText("");
            AVAnalytics.onEvent(getActivity(), "tab1_clear_btn");
        } else if (v.getId() == R.id.copy_btn) {
            copy();
        } else if (v.getId() == R.id.share_btn) {
            share();
        } else if (v.getId() == R.id.btn_bushou) {
            toBushouActivity();
        } else if (v.getId() == R.id.btn_pinyin) {
            toPinyinActivity();
        }
    }

    private void toPinyinActivity() {
        Intent intent = new Intent(getContext(), ChDicBushouPinyinActivity.class);
        intent.putExtra(KeyUtil.CHDicType, ChDicBushouPinyinActivity.pinyin);
        startActivity(intent);
    }

    private void toBushouActivity() {
        Intent intent = new Intent(getContext(), ChDicBushouPinyinActivity.class);
        intent.putExtra(KeyUtil.CHDicType, ChDicBushouPinyinActivity.bushou);
        startActivity(intent);
    }

    private void copy() {
        if (mRoot != null && mRoot.getError_code() == 0) {
            ShareUtil.copy(getContext(), mRoot.getResult().getResultForShow(word));
        }
    }

    private void share() {
        if (mRoot != null && mRoot.getError_code() == 0) {
            ShareUtil.shareText(getContext(), mRoot.getResult().getResultForShow(word));
        }
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
    }

    private void onFinishRequest() {
        finishLoadding();
        submit_btn.setEnabled(true);
    }

    private void RequestAsyncTask() {
        loadding();
        submit_btn.setEnabled(false);
        RequestBody formBody = new FormEncodingBuilder()
                .add("key", "59ef16d2ca4ee5b590bde3976a8bf45f")
                .add("word", word)
                .build();
        LanguagehelperHttpClient.post(Settings.ChDicSearchUrl, formBody, new UICallback(getActivity()) {
            @Override
            public void onFailured() {
                ToastUtil.diaplayMesShort(getActivity(), getActivity().getResources().getString(R.string.network_error));
            }

            @Override
            public void onFinished() {
                onFinishRequest();
            }

            @Override
            public void onResponsed(String responseString) {
                if (!TextUtils.isEmpty(responseString)) {
                    LogUtil.DefalutLog("responseString:" + responseString);
                    mRoot = JSON.parseObject(responseString, ChDicDao.class);
                    if (mRoot != null && mRoot.getError_code() == 0) {
                        mRoot.getResult().setResult();
                        result_tv.setText(mRoot.getResult().getResultForShow(word));
                    } else {
                        ToastUtil.diaplayMesShort(getActivity(), mRoot.getReason());
                    }
                } else {
                    ToastUtil.diaplayMesShort(getActivity(), getActivity().getResources().getString(
                            R.string.network_error));
                }
            }
        });
    }

    /**
     * 显示转写对话框.
     */
    public void showIatDialog() {
        if (!recognizer.isListening()) {
            record_layout.setVisibility(View.VISIBLE);
            input_et.setText("");
            voice_btn.setBackgroundColor(getActivity().getResources().getColor(R.color.none));
            voice_btn.setText(getActivity().getResources().getString(R.string.finish));
            speak_round_layout.setBackgroundResource(R.drawable.round_light_green_bgl);
            XFUtil.showSpeechRecognizer(getActivity(), mSharedPreferences, recognizer,
                    recognizerListener, XFUtil.VoiceEngineMD);
        } else {
            finishRecord();
            recognizer.stopListening();
            loadding();
        }
    }

    /**
     * finish record
     */
    private void finishRecord() {
        record_layout.setVisibility(View.GONE);
        record_anim_img.setBackgroundResource(R.drawable.speak_voice_1);
        voice_btn.setText("");
        voice_btn.setBackgroundResource(R.drawable.ic_voice_padded_normal);
        speak_round_layout.setBackgroundResource(R.drawable.round_gray_green_bgl);
    }

    /**
     * 点击翻译之后隐藏输入法
     */
    private void hideIME() {
        final InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Activity.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(submit_btn.getWindowToken(), 0);
    }

    /**
     * 点击编辑之后显示输入法
     */
    private void showIME() {
        final InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Activity.INPUT_METHOD_SERVICE);
        imm.toggleSoftInput(InputMethodManager.SHOW_IMPLICIT, InputMethodManager.HIDE_NOT_ALWAYS);
    }

    /**
     * 通过接口回调activity执行进度条显示控制
     */
    private void loadding() {
        if (mProgressbarListener != null) {
            mProgressbarListener.showProgressbar();
        }
    }

    /**
     * 通过接口回调activity执行进度条显示控制
     */
    private void finishLoadding() {
        if (mProgressbarListener != null) {
            mProgressbarListener.hideProgressbar();
        }
    }

    /**
     * submit request task
     */
    private void submit() {
        word = input_et.getText().toString().trim();
        if (!TextUtils.isEmpty(word)) {
            String last = word.substring(word.length() - 1);
            if (",.?!;:'，。？！‘；：".contains(last)) {
                word = word.substring(0, word.length() - 1);
            }
            RequestAsyncTask();
        } else {
            ToastUtil.diaplayMesShort(getActivity(), getActivity().getResources().getString(R.string.ch_dic_input_hint));
            finishLoadding();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mSpeechSynthesizer != null) {
            mSpeechSynthesizer.stopSpeaking();
            mSpeechSynthesizer = null;
        }
        if (recognizer != null) {
            recognizer.stopListening();
            recognizer = null;
        }
        LogUtil.DefalutLog("MainFragment-onDestroy");
    }
}
