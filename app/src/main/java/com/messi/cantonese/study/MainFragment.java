package com.messi.cantonese.study;

import java.util.List;
import android.app.Activity;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioButton;
import com.iflytek.cloud.RecognizerListener;
import com.iflytek.cloud.RecognizerResult;
import com.iflytek.cloud.SpeechError;
import com.iflytek.cloud.SpeechRecognizer;
import com.iflytek.cloud.SpeechSynthesizer;
import com.messi.cantonese.study.dao.record;
import com.messi.cantonese.study.db.DataBaseUtil;
import com.messi.cantonese.study.http.LanguagehelperHttpClient;
import com.messi.cantonese.study.impl.FragmentProgressbarListener;
import com.messi.cantonese.study.util.HtmlParseUtil;
import com.messi.cantonese.study.util.JsonParser;
import com.messi.cantonese.study.util.KeyUtil;
import com.messi.cantonese.study.util.LogUtil;
import com.messi.cantonese.study.util.Settings;
import com.messi.cantonese.study.util.ToastUtil;
import com.messi.cantonese.study.util.ViewUtil;
import com.messi.cantonese.study.util.XFUtil;

public class MainFragment extends Fragment{

//	private EditText input_et;
//	private ButtonRectangle submit_btn;
//	private LinearLayout baidu_translate;
//	private FrameLayout clear_btn_layout;
//	private Button voice_btn;
//	private LinearLayout speak_round_layout;
//	private RadioButton cb_speak_language_ch,cb_speak_language_en;
//	private ListView recent_used_lv;
//	/**record**/
//	private LinearLayout record_layout;
//	private ImageView record_anim_img;
//	private record currentDialogBean;
//
//	private RadioButton rb_to_yue,rb_to_zh;
//	private LayoutInflater mInflater;
//	private CollectedListItemAdapter mAdapter;
//	private List<record> beans;
//	private String dstString = "";
//	private Animation fade_in,fade_out;
//
//	// 识别对象
//	private SpeechRecognizer recognizer;
//	// 缓存，保存当前的引擎参数到下一次启动应用程序使用.
//	private SharedPreferences mSharedPreferences;
//	//合成对象.
//	private SpeechSynthesizer mSpeechSynthesizer;
//
	public static int speed;
//	public static boolean isSpeakYueyuNeedUpdate;
//	private Bundle bundle;
//	public static boolean isRefresh;
//	private View view;
//	private FragmentProgressbarListener mProgressbarListener;
//	public static MainFragment mMainFragment;
//	private boolean AutoClearInputAfterFinish;
//
//	public static MainFragment getInstance(Bundle bundle){
//		if(mMainFragment == null){
//			mMainFragment = new MainFragment();
//			mMainFragment.bundle = bundle;
//		}
//		return mMainFragment;
//	}
//
//	@Override
//	public void onAttach(Activity activity) {
//		super.onAttach(activity);
//		try {
//			mProgressbarListener = (FragmentProgressbarListener) activity;
//        } catch (ClassCastException e) {
//            throw new ClassCastException(activity.toString() + " must implement FragmentProgressbarListener");
//        }
//	}
//
//	@Override
//	public void onCreate(Bundle savedInstanceState) {
//		super.onCreate(savedInstanceState);
//		LogUtil.DefalutLog("MainFragment-onCreate");
//	}
//
//	@Override
//	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
//		LogUtil.DefalutLog("MainFragment-onCreateView");
//		view = inflater.inflate(R.layout.fragment_translate, null);
//		init();
//		return view;
//	}
//
//	private void init() {
//		mInflater = LayoutInflater.from(getActivity());
//		mSharedPreferences = getActivity().getSharedPreferences(getActivity().getPackageName(), Activity.MODE_PRIVATE);
//
//		mSpeechSynthesizer = SpeechSynthesizer.createSynthesizer(getActivity(), null);
//		recognizer = SpeechRecognizer.createRecognizer(getActivity(), null);
//		beans = DataBaseUtil.getInstance().getDataListRecord(0, Settings.offset);
//		mAdapter = new CollectedListItemAdapter(getActivity(), mInflater, beans,
//				mSpeechSynthesizer, mSharedPreferences, bundle);
//
//		recent_used_lv = (ListView) view.findViewById(R.id.recent_used_lv);
//		input_et = (EditText) view.findViewById(R.id.input_et);
//		submit_btn = (ButtonRectangle) view.findViewById(R.id.submit_btn);
//		cb_speak_language_ch = (RadioButton) view.findViewById(R.id.cb_speak_language_ch);
//		cb_speak_language_en = (RadioButton) view.findViewById(R.id.cb_speak_language_en);
//		speak_round_layout = (LinearLayout) view.findViewById(R.id.speak_round_layout);
//		clear_btn_layout = (FrameLayout) view.findViewById(R.id.clear_btn_layout);
//		record_layout = (LinearLayout) view.findViewById(R.id.record_layout);
//		record_anim_img = (ImageView) view.findViewById(R.id.record_anim_img);
//		fade_in = AnimationUtils.loadAnimation(getActivity(), R.anim.fade_in);
//		fade_out = AnimationUtils.loadAnimation(getActivity(), R.anim.fade_out);
//		voice_btn = (Button) view.findViewById(R.id.voice_btn);
//		rb_to_yue = (RadioButton) view.findViewById(R.id.rb_to_yue);
//		rb_to_zh = (RadioButton) view.findViewById(R.id.rb_to_zh);
//
//		AutoClearInputAfterFinish = mSharedPreferences.getBoolean(KeyUtil.AutoClearInputAfterFinish, true);
//		boolean IsHasShowBaiduMessage = mSharedPreferences.getBoolean(KeyUtil.IsHasShowBaiduMessage, false);
//		if(!IsHasShowBaiduMessage){
//			initSample();
//			Settings.saveSharedPreferences(mSharedPreferences, KeyUtil.IsHasShowBaiduMessage, true);
//		}
//
//		submit_btn.setOnClickListener(this);
//		cb_speak_language_ch.setOnClickListener(this);
//		cb_speak_language_en.setOnClickListener(this);
//		speak_round_layout.setOnClickListener(this);
//		clear_btn_layout.setOnClickListener(this);
//		rb_to_yue.setOnClickListener(this);
//		rb_to_zh.setOnClickListener(this);
//		recent_used_lv.setAdapter(mAdapter);
//		speed = mSharedPreferences.getInt(getString(R.string.preference_key_tts_speed), 50);
//		initLanguage();
//		initTranslateSelected();
//	}
//
//	private void initTranslateSelected(){
//		boolean IsTranslateYue = mSharedPreferences.getBoolean(KeyUtil.IsTranslateYueKey, true);
//		if(IsTranslateYue){
//			rb_to_yue.setChecked(true);
//			rb_to_zh.setChecked(false);
//		}else{
//			rb_to_yue.setChecked(false);
//			rb_to_zh.setChecked(true);
//		}
//	}
//
//	private void initSample(){
//		record sampleBean = new record("点击咪讲嘢", "点击话筒说话", "yue");
//		DataBaseUtil.getInstance().insert(sampleBean);
//		beans.add(0,sampleBean);
//	}
//
//	private void initLanguage(){
//		if(mSharedPreferences != null){
//			if(getSpeakLanguage().equals(XFUtil.VoiceEngineCH)){
//				XFUtil.setSpeakLanguage(getActivity(),mSharedPreferences,XFUtil.VoiceEngineCH);
//				cb_speak_language_ch.setChecked(true);
//				cb_speak_language_en.setChecked(false);
//			}else{
//				XFUtil.setSpeakLanguage(getActivity(),mSharedPreferences,XFUtil.VoiceEngineHK);
//				cb_speak_language_ch.setChecked(false);
//				cb_speak_language_en.setChecked(true);
//			}
//		}
//	}
//
//	private void resetLanguage(){
//		if(mSharedPreferences != null && cb_speak_language_ch != null){
//			if(cb_speak_language_ch.isChecked()){
//				XFUtil.setSpeakLanguage(getActivity(),mSharedPreferences,XFUtil.VoiceEngineCH);
//			}else{
//				XFUtil.setSpeakLanguage(getActivity(),mSharedPreferences,XFUtil.VoiceEngineHK);
//			}
//			AutoClearInputAfterFinish = mSharedPreferences.getBoolean(KeyUtil.AutoClearInputAfterFinish, true);
//		}
//	}
//
//	private void JudgeBtnTranslateLan(){
//		if(rb_to_yue.isChecked()){
//			LanguagehelperHttpClient.setTranslateLan(true);
//		}else{
//			LanguagehelperHttpClient.setTranslateLan(false);
//		}
//	}
//
//	@Override
//	public void onClick(View v) {
//		if (v.getId() == R.id.submit_btn) {
//			hideIME();
//			JudgeBtnTranslateLan();
//			submit();
//			StatService.onEvent(getActivity(), "tab_tran_submit", "首页翻译页提交按钮", 1);
//		}else if (v.getId() == R.id.speak_round_layout) {
//			showIatDialog();
//			StatService.onEvent(getActivity(), "tab_tran_speak_voice", "首页翻译页说话按钮", 1);
//		}else if (v.getId() == R.id.clear_btn_layout) {
//			input_et.setText("");
//			StatService.onEvent(getActivity(), "tab_tran_clearbtn", "首页翻译页清空按钮", 1);
//		}else if (v.getId() == R.id.rb_to_zh) {
//			rb_to_yue.setChecked(false);
//			Settings.saveSharedPreferences(mSharedPreferences, KeyUtil.IsTranslateYueKey, false);
//		}else if (v.getId() == R.id.rb_to_yue) {
//			rb_to_zh.setChecked(false);
//			Settings.saveSharedPreferences(mSharedPreferences, KeyUtil.IsTranslateYueKey, true);
//		}else if (v.getId() == R.id.cb_speak_language_ch) {
//			cb_speak_language_en.setChecked(false);
//			XFUtil.setSpeakLanguage(getActivity(),mSharedPreferences,XFUtil.VoiceEngineCH);
//			ToastUtil.diaplayMesShort(getActivity(), getActivity().getResources().getString(R.string.speak_chinese));
//			StatService.onEvent(getActivity(), "tab_tran_putonghuabtn", "首页翻译页普通话按钮", 1);
//		}else if (v.getId() == R.id.cb_speak_language_en) {
//			cb_speak_language_ch.setChecked(false);
//			XFUtil.setSpeakLanguage(getActivity(),mSharedPreferences,XFUtil.VoiceEngineHK);
//			ToastUtil.diaplayMesShort(getActivity(), getActivity().getResources().getString(R.string.speak_Cantonese));
//			StatService.onEvent(getActivity(), "tab_tran_yingyubtn", "首页翻译页粤语按钮", 1);
//		}
//	}
//
//	@Override
//    public void setUserVisibleHint(boolean isVisibleToUser) {
//        super.setUserVisibleHint(isVisibleToUser);
//        LogUtil.DefalutLog("MainFragment-setUserVisibleHint");
//        if (isVisibleToUser) {
//        	if(isRefresh){
//        		isRefresh = false;
//        		new WaitTask().execute();
//        	}
//        	resetLanguage();
//        }
//	}
//
//	@Override
//	public void onResume() {
//		super.onResume();
//		LogUtil.DefalutLog("MainFragment-onResume");
//		setUserVisibleHint(true);
//	}
//
//	@Override
//	public void onPause() {
//		super.onPause();
//		LogUtil.DefalutLog("MainFragment-onPause");
//		if(mSharedPreferences != null && cb_speak_language_ch != null){
//			if(cb_speak_language_ch.isChecked()){
//				Settings.saveSharedPreferences(mSharedPreferences, KeyUtil.UserSelectLanguage, XFUtil.VoiceEngineCH);
//			}else{
//				Settings.saveSharedPreferences(mSharedPreferences, KeyUtil.UserSelectLanguage, XFUtil.VoiceEngineHK);
//			}
//		}
//	}
//
//	class WaitTask extends AsyncTask<Void, Void, Void>{
//		@Override
//		protected void onPreExecute() {
//			loadding();
//		}
//		@Override
//		protected Void doInBackground(Void... params) {
//			try {
//				beans = DataBaseUtil.getInstance().getDataListRecord(0, Settings.offset);
//			} catch (Exception e) {
//				e.printStackTrace();
//			}
//			return null;
//		}
//
//		@Override
//		protected void onPostExecute(Void result) {
//			finishLoadding();
//			mAdapter.notifyDataChange(beans,0);
//		}
//	}
//
//	/**get defalut speaker
//	 * @return
//	 */
//	private String getSpeakLanguage(){
//		return mSharedPreferences.getString(KeyUtil.UserSelectLanguage, XFUtil.VoiceEngineCH);
//	}
//
//	/**
//	 * send translate request
//	 */
//	private void RequestAsyncTask(){
//		loadding();
//		submit_btn.setEnabled(false);
//		LanguagehelperHttpClient.postBaidu( new TextHttpResponseHandler() {
//			@Override
//			public void onFinish() {
//				super.onFinish();
//				finishLoadding();
//				submit_btn.setEnabled(true);
//			}
//			@Override
//			public void onFailure(int statusCode, Header[] headers,String responseString, Throwable throwable) {
//				showToast("Error("+statusCode+")");
//			}
//			@Override
//			public void onSuccess(int statusCode, Header[] headers, String responseString) {
//				if (!TextUtils.isEmpty(responseString)) {
//					LogUtil.DefalutLog(responseString);
//					if(AutoClearInputAfterFinish){
//						input_et.setText("");
//					}
//					dstString = JsonParser.getTranslateResult(responseString);
//					if (dstString.contains("error_msg:")) {
//						showToast(dstString);
//					} else {
//						currentDialogBean = new record(dstString, Settings.q, Settings.to);
//						long newRowId = DataBaseUtil.getInstance().insert(currentDialogBean);
//						beans.add(0,currentDialogBean);
//						mAdapter.notifyDataSetChanged();
//						recent_used_lv.setSelection(0);
//						if(mSharedPreferences.getBoolean(KeyUtil.AutoPlayResult, false)){
//							new AutoPlayWaitTask().execute();
//						}
//						LogUtil.DefalutLog("mDataBaseUtil:"+currentDialogBean.toString());
//					}
//				} else {
//					showToast(getActivity().getResources().getString(R.string.network_error));
//				}
//			}
//		});
//	}
//
//	class AutoPlayWaitTask extends AsyncTask<Void, Void, Void>{
//		@Override
//		protected Void doInBackground(Void... params) {
//			try {
//				Thread.sleep(100);
//			} catch (InterruptedException e) {
//				e.printStackTrace();
//			}
//			return null;
//		}
//		@Override
//		protected void onPostExecute(Void result) {
//			autoPlay();
//		}
//	}
//
//
//	private void autoPlay(){
//		View mView = recent_used_lv.getChildAt(0);
//		FrameLayout record_answer_cover = (FrameLayout) mView.findViewById(R.id.record_answer_cover);
//		record_answer_cover.callOnClick();
//	}
//
//	/**toast message
//	 * @param toastString
//	 */
//	private void showToast(String toastString) {
//		ToastUtil.diaplayMesShort(getActivity(), toastString);
//	}
//
//	/**
//	 * 显示转写对话框.
//	 */
//	public void showIatDialog() {
//		if(!recognizer.isListening()){
//			record_layout.setVisibility(View.VISIBLE);
//			input_et.setText("");
//			voice_btn.setBackgroundColor(getActivity().getResources().getColor(R.color.none));
//			voice_btn.setText(getActivity().getResources().getString(R.string.finish));
//			speak_round_layout.setBackgroundResource(R.drawable.round_light_blue_bgl);
//			XFUtil.showSpeechRecognizer(getActivity(),mSharedPreferences,recognizer,recognizerListener);
//		}else{
//			finishRecord();
//			recognizer.stopListening();
//			loadding();
//		}
//	}
//
//	/**
//	 * finish record
//	 */
//	private void finishRecord(){
//		record_layout.setVisibility(View.GONE);
//		record_anim_img.setBackgroundResource(R.drawable.speak_voice_1);
//		voice_btn.setText("");
//		voice_btn.setBackgroundResource(R.drawable.ic_voice_padded_normal);
//		speak_round_layout.setBackgroundResource(R.drawable.round_gray_bgl);
//	}
//
//	RecognizerListener recognizerListener = new RecognizerListener() {
//
//		@Override
//		public void onBeginOfSpeech() {
//			LogUtil.DefalutLog("onBeginOfSpeech");
//		}
//
//		@Override
//		public void onError(SpeechError err) {
//			LogUtil.DefalutLog("onError:"+err.getErrorDescription());
//			finishRecord();
//			ToastUtil.diaplayMesShort(getActivity(), err.getErrorDescription());
//			finishLoadding();
//		}
//
//		@Override
//		public void onEndOfSpeech() {
//			LogUtil.DefalutLog("onEndOfSpeech");
//			loadding();
//			finishRecord();
//		}
//
//		@Override
//		public void onResult(RecognizerResult results, boolean isLast) {
//			LogUtil.DefalutLog("onResult");
//			String text = JsonParser.parseIatResult(results.getResultString());
//			input_et.append(text);
//			input_et.setSelection(input_et.length());
//			if(isLast) {
//				finishRecord();
//				JudgeSpeakTranslateLan();
//				submit();
//			}
//		}
//
//		@Override
//		public void onEvent(int arg0, int arg1, int arg2, Bundle arg3) {
//
//		}
//
//		@Override
//		public void onVolumeChanged(int volume, byte[] arg1) {
//			if(volume < 4){
//				record_anim_img.setBackgroundResource(R.drawable.speak_voice_1);
//			}else if(volume < 8){
//				record_anim_img.setBackgroundResource(R.drawable.speak_voice_2);
//			}else if(volume < 12){
//				record_anim_img.setBackgroundResource(R.drawable.speak_voice_3);
//			}else if(volume < 16){
//				record_anim_img.setBackgroundResource(R.drawable.speak_voice_4);
//			}else if(volume < 20){
//				record_anim_img.setBackgroundResource(R.drawable.speak_voice_5);
//			}else if(volume < 24){
//				record_anim_img.setBackgroundResource(R.drawable.speak_voice_6);
//			}else if(volume < 31){
//				record_anim_img.setBackgroundResource(R.drawable.speak_voice_7);
//			}
//		}
//
//	};
//
//	private void JudgeSpeakTranslateLan(){
//		if(cb_speak_language_ch.isChecked()){
//			LanguagehelperHttpClient.setTranslateLan(true);
//		}else{
//			LanguagehelperHttpClient.setTranslateLan(false);
//		}
//	}
//
//	/**
//	 * 点击翻译之后隐藏输入法
//	 */
//	private void hideIME(){
//		final InputMethodManager imm = (InputMethodManager)getActivity().getSystemService(Activity.INPUT_METHOD_SERVICE);
//		imm.hideSoftInputFromWindow(submit_btn.getWindowToken(), 0);
//	}
//
//	/**
//	 * 点击编辑之后显示输入法
//	 */
//	private void showIME(){
//		final InputMethodManager imm = (InputMethodManager)getActivity().getSystemService(Activity.INPUT_METHOD_SERVICE);
//		imm.toggleSoftInput(InputMethodManager.SHOW_IMPLICIT, InputMethodManager.HIDE_NOT_ALWAYS);
//	}
//
//	/**
//	 * 通过接口回调activity执行进度条显示控制
//	 */
//	private void loadding(){
//		if(mProgressbarListener != null){
//			mProgressbarListener.showProgressbar();
//		}
//	}
//
//	/**
//	 * 通过接口回调activity执行进度条显示控制
//	 */
//	private void finishLoadding(){
//		if(mProgressbarListener != null){
//			mProgressbarListener.hideProgressbar();
//		}
//	}
//
//	/**
//	 * submit request task
//	 */
//	private void submit(){
//		Settings.q = input_et.getText().toString().trim();
//		if (!TextUtils.isEmpty(Settings.q)) {
//			RequestAsyncTask();
//			StatService.onEvent(getActivity(), "tab_translate_submitbtn", "首页翻译页面翻译按钮", 1);
//		} else {
//			showToast(getActivity().getResources().getString(R.string.input_et_hint));
//			finishLoadding();
//		}
//	}
//
//	@Override
//	public void onDestroy() {
//		super.onDestroy();
//		if(mSpeechSynthesizer != null){
//			mSpeechSynthesizer.stopSpeaking();
//			mSpeechSynthesizer.destroy();
//			mSpeechSynthesizer = null;
//		}
//		if(recognizer != null){
//			recognizer.stopListening();
//			recognizer.destroy();
//			recognizer = null;
//		}
//		LogUtil.DefalutLog("MainFragment-onDestroy");
//	}
}
