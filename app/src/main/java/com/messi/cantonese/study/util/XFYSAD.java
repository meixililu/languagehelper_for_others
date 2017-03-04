package com.messi.cantonese.study.util;

import android.content.Context;
import android.graphics.Rect;
import android.os.Handler;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

import com.bumptech.glide.Glide;
import com.iflytek.voiceads.AdError;
import com.iflytek.voiceads.AdKeys;
import com.iflytek.voiceads.IFLYNativeAd;
import com.iflytek.voiceads.IFLYNativeListener;
import com.iflytek.voiceads.NativeADDataRef;
import com.messi.cantonese.study.R;
import com.messi.cantonese.study.adapter.ViewPagerAdapter;
import com.messi.cantonese.study.views.ProportionalImageView;
import com.messi.cantonese.study.views.WrapContentHeightViewPager;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class XFYSAD {
	
	public static boolean isReverse = false;
	private Context mContext;
	private View parentView;
	private IFLYNativeAd nativeAd;
	private List<NativeADDataRef> adList;
	private WrapContentHeightViewPager auto_view_pager;
	private LinearLayout viewpager_dot_layout;
	private LayoutInflater mInflater;
	private Handler mHandler;
	private ArrayList<View> views;
	private ViewPagerAdapter vpAdapter;
	private int Index;
	private String adId;
	private int retryTime;
	private boolean isStopPlay;
	private long lastLoadAdTime;
	private boolean isStartPlay;
	private boolean isDirectExPosure = true;
	private boolean isFinishPlay = false;

	public XFYSAD(Context mContext,View parentView,String adId){
		this.mContext = mContext;
		this.parentView = parentView;
		this.adId = adId;
		mInflater = LayoutInflater.from(mContext);
		mHandler = new Handler();
		auto_view_pager = (WrapContentHeightViewPager)parentView.findViewById(R.id.auto_view_pager);
		viewpager_dot_layout = (LinearLayout)parentView.findViewById(R.id.viewpager_dot_layout);
		parentView.setVisibility(View.GONE);
		lastLoadAdTime = System.currentTimeMillis();
		
		views = new ArrayList<View>();
		vpAdapter = new ViewPagerAdapter(views);
		auto_view_pager.setAdapter(vpAdapter);
        auto_view_pager.setOnPageChangeListener(new MyOnPageChangeListener());
	}

	public XFYSAD(Context mContext,String adId){
		this.mContext = mContext;
		this.adId = adId;
		mInflater = LayoutInflater.from(mContext);
		mHandler = new Handler();
		lastLoadAdTime = System.currentTimeMillis();
		views = new ArrayList<View>();
	}

	public void setParentView(View parentView){
		this.parentView = parentView;
		auto_view_pager = (WrapContentHeightViewPager)parentView.findViewById(R.id.auto_view_pager);
		viewpager_dot_layout = (LinearLayout)parentView.findViewById(R.id.viewpager_dot_layout);
		parentView.setVisibility(View.GONE);
		vpAdapter = new ViewPagerAdapter(views);
		auto_view_pager.setAdapter(vpAdapter);
		auto_view_pager.setOnPageChangeListener(new MyOnPageChangeListener());
	}
	
	private Runnable mRunnable = new Runnable() {
		@Override
		public void run() {
			changeAd();
			mHandler.postDelayed(mRunnable, ADUtil.adInterval);
			LogUtil.DefalutLog("---mRunnable run---");
		}
	};
	
	private void changeAd(){
		if(auto_view_pager != null && adList != null){
			Index++;
			if(Index >= adList.size()){
				Index = 0;
				isFinishPlay = true;
				canclePlayImg();
			}else {
				if(!isFinishPlay){
					auto_view_pager.setCurrentItem(Index);
				}
			}
		}
	}
	
	public void startPlayImg(){
		if(ADUtil.adCount > 1 && !isStartPlay && !isFinishPlay){
			isStartPlay = true;
			if(mHandler != null){
				if(adList != null && adList.size() > 1){
					new Handler().postDelayed(new Runnable() {
						@Override
						public void run() {
							changeAd();
						}
					}, 400);
					LogUtil.DefalutLog("---start changeAd---");
				}
				mHandler.postDelayed(mRunnable, ADUtil.adInterval);
				if(System.currentTimeMillis() - lastLoadAdTime > 1000*35){
					isStopPlay = true;
					isFinishPlay = false;
					lastLoadAdTime = System.currentTimeMillis();
					showAD();
				}
			}
		}
	}

	public void canclePlayImg(){
		if(ADUtil.adCount > 1 && mHandler != null){
			isStartPlay = false;
			mHandler.removeCallbacks(mRunnable);
			LogUtil.DefalutLog("---cancle changeAd---");
		}
	}
	
	public void showAD(){
		if(ADUtil.isShowAd(mContext)){
			loadData();
		}else{
			parentView.setVisibility(View.GONE);
		}
	}
	
	private void loadData(){
		LogUtil.DefalutLog("---load ad Data---");
		nativeAd = new IFLYNativeAd(mContext, adId, new IFLYNativeListener() {
			@Override
			public void onConfirm() {
			}
			@Override
			public void onCancel() {
			}
			@Override
			public void onAdFailed(AdError arg0) {
				parentView.setVisibility(View.GONE);
				LogUtil.DefalutLog("onAdFailed---"+arg0.getErrorCode()+"---"+arg0.getErrorDescription());
				if(retryTime < 1){
					retryTime ++;
					showAD();
				}
			}
			@Override
			public void onADLoaded(List<NativeADDataRef> arg0) {
				if(arg0 != null && arg0.size() > 0){
					adList = arg0;
					LogUtil.DefalutLog("---onADLoaded---");
					try {
						setAdData();
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}
		});
		nativeAd.setParameter(AdKeys.DOWNLOAD_ALERT, "true");
		nativeAd.loadAd(ADUtil.adCount);
	}
	
	private void setAdData() throws Exception{
		parentView.setVisibility(View.VISIBLE);
		views.clear();
		if(isReverse){
			Collections.reverse(adList);
		}
		isReverse = !isReverse;
		for(int i=0; i < adList.size() ;i++){
			views.add(getAdItem(adList.get(i)));
		}
        if(adList.size() > 1){
        	viewpager_dot_layout.removeAllViews();
        	for(int i=0;i<adList.size();i++){
        		viewpager_dot_layout.addView( ViewUtil.getDot(mContext,i) );
        	}
        	viewpager_dot_layout.setVisibility(View.VISIBLE);
        	ViewUtil.changeState(viewpager_dot_layout, 0);
        }else{
        	viewpager_dot_layout.setVisibility(View.GONE);
        }

        auto_view_pager.setOnTouchListener(new OnTouchListener() {
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				canclePlayImg();
				return false;
			}
		});
        vpAdapter.notifyDataSetChanged();
        if(!isStopPlay){
        	startPlayImg();
        }
	}
	
	public View getAdItem(final NativeADDataRef mNativeADDataRef) throws Exception{
		View convertView = mInflater.inflate(R.layout.ad_viewpager_item, null);
		FrameLayout cover = (FrameLayout) convertView.findViewById(R.id.ad_layout);
		ProportionalImageView ad_img = (ProportionalImageView) convertView.findViewById(R.id.ad_img);
		Glide.with(mContext)
				.load(mNativeADDataRef.getImage())
				.into(ad_img);
		cover.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				mNativeADDataRef.onClicked(v);
			}
		});
		if(isDirectExPosure){
			mNativeADDataRef.onExposured(auto_view_pager);
		}
		return convertView;
	}
	
	public class MyOnPageChangeListener implements OnPageChangeListener {
        @Override
        public void onPageSelected(int position) {
        	Index = position;
        	if(adList != null && adList.size() > position){
				adList.get(position).onExposured(auto_view_pager);
        		ViewUtil.changeState(viewpager_dot_layout, position);
        	}
        }
        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {}
        @Override
        public void onPageScrollStateChanged(int arg0) {}
    }

	public void ExposureAD(){
		int pos = auto_view_pager.getCurrentItem();
		adList.get(pos).onExposured(auto_view_pager);
	}
	
	public boolean isStopPlay() {
		return isStopPlay;
	}

	public void setStopPlay(boolean isStopPlay) {
		this.isStopPlay = isStopPlay;
	}

	public void setDirectExPosure(boolean directExPosure) {
		isDirectExPosure = directExPosure;
	}

	/**
	 * 判断view是否在屏幕范围内（是否曝光）。
	 * @param context
	 * @param view
	 * @return true view矩形在屏幕矩形内或相交
	 */
	public static boolean isInScreen(Context context,View view) {
		//获取屏幕宽高（px）以及矩形
		Rect screenRect = getScreenRect(context);
		//获取view矩形
		Rect viewRect=getViewRect(view);
		int viewHight= (int) ((viewRect.bottom-viewRect.top)*0.3);
		screenRect=new Rect(screenRect.left, screenRect.top+viewHight, screenRect.right, screenRect.bottom-viewHight);
		//判断是否相交或包含
		boolean intersects=Rect.intersects(screenRect, viewRect);
		boolean contains=screenRect.contains(viewRect);
		return intersects||contains;

	}

	///Ad_Android_SDK_v1/src/com/iflytek/voiceads/utils/AppState.java
	/**
	 * 获取屏幕矩形
	 * @param context
	 * @return screenRect
	 */
	public static Rect getScreenRect(Context context) {
		WindowManager windowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
		Display display=windowManager.getDefaultDisplay();
		int screenW=display.getWidth();
		int screenH=display.getHeight();
		Rect screenRect=new Rect(0,0,screenW,screenH);
		return screenRect;
	}

///Ad_Android_SDK_v1/src/com/iflytek/voiceads/utils/AppState.java
	/**
	 * 获取view矩形
	 * @param view
	 * @return viewRect
	 */
	public static Rect getViewRect(View view){
		int[] location=new int[2];
		view.getLocationOnScreen(location);
		Rect viewRect=new Rect(location[0],location[1],location[0]+view.getWidth(),location[1]+view.getHeight());
		return viewRect;
	}

}
