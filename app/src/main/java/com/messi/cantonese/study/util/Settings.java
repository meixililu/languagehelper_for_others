package com.messi.cantonese.study.util;

import android.Manifest;
import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.support.v4.BuildConfig;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.text.ClipboardManager;
import android.view.View;

import com.avos.avoscloud.AVAnalytics;
import com.messi.cantonese.study.R;
import com.messi.cantonese.study.dialog.PopDialog;

import static android.support.v4.app.ActivityCompat.shouldShowRequestPermissionRationale;

public class Settings {

	private static final int RequestCode = 1;

	/**baidu translate api**/
	public static String baiduTranslateUrl = "https://fanyi-api.baidu.com/api/trans/vip/translate";
	
	/**baidu dictionary api**/
	public static String baiduDictionaryUrl = "http://openapi.baidu.com/public/2.0/translate/dict/simple";
	
	public static String baiduWebTranslateUrl = "http://fanyi.baidu.com/v2transapi";

	public static String IcibaTranslateUrl = "http://fy.iciba.com/api.php";

	public static String IcibaTranslateNewUrl = "http://fy.iciba.com/ajax.php?a=fy";
	
	public static String BaiduOCRUrl = "http://apis.baidu.com/idl_baidu/baiduocrpay/idlocrpaid";
	
	/**juhe dictionary api**/
	public static String JuheYoudaoApiUrl = "http://japi.juhe.cn/youdao/dictionary.from?key=a8edec6297be9b017a106aec955974f6&word=";
	/**youdao web translate for jsoup catch**/
	public static String YoudaoWeb = "http://dict.youdao.com/w/";
	
	public static String YoudaoWebEnd = "/#keyfrom=dict.index";
	
	public static String BingyingWeb = "http://cn.bing.com/dict/";
	
	/**showapi dictionary api**/
	public static String ShowApiDictionaryUrl = "http://route.showapi.com/32-9";
	
	/**showapi word list api**/
	public static String ShowApiWordListUrl = "http://route.showapi.com/8-11";
	
	/**showapi word detail list api**/
	public static String ShowApiWordDetailListUrl = "http://route.showapi.com/8-10";
	
	/**jinshan daily sentence api**/
	public static String DailySentenceUrl = "http://open.iciba.com/dsapi/";
	
	/**showapi budejie api**/
	public static String BudejieUrl = "http://route.showapi.com/255-1";
	
	/**showapi joke picture api**/
	public static String JokePictureUrl = "http://route.showapi.com/341-2";

	/**showapi joke gif api**/
	public static String JokeGifUrl = "http://route.showapi.com/341-3";

	/**showapi joke text api**/
	public static String JokeTextUrl = "http://route.showapi.com/341-1";
	
	public static final String CaiLingUrl = "http://api.openspeech.cn/kyls/NTBhYTEyMTM=";

	public static final String ChDicSearchUrl = "http://v.juhe.cn/xhzd/query";

	public static final String ChDicBushouUrl = "http://v.juhe.cn/xhzd/bushou";

	public static final String ChDicBushouListUrl = "http://v.juhe.cn/xhzd/querybs";

	public static final String ChDicPinyinUrl = "http://v.juhe.cn/xhzd/pinyin";

	public static final String ChDicPinyinListUrl = "http://v.juhe.cn/xhzd/querypy";

	public static final String ChDicIdiomSearchUrl = "http://v.juhe.cn/chengyu/query";

	public static final String WechatJXUrl = "http://api.tianapi.com/wxnew/?key=18f7f9dbd7dfcd8ab45efdcfbc33826d&rand=1&num=15&page=";

	public static final String ToutiaoNewsUrl = "http://v.juhe.cn/toutiao/index";

	public static final String SougoUrl = "http://weixin.sogou.com/weixinwap?ie=utf8&s_from=input&type=2&t=1488020521571&pg=webSearchList&_sug_=y&_sug_type_=&query=%E8%8B%B1%E8%AF%AD";

	/** BrainTwists **/
	public static final String TXBrainTwistsApi = "http://api.tianapi.com/txapi/naowan/?key=18f7f9dbd7dfcd8ab45efdcfbc33826d";

	public static final String TXYiZhanDaoDiApi = "http://api.tianapi.com/txapi/wenda/?key=18f7f9dbd7dfcd8ab45efdcfbc33826d";

	public static final String TXNewsApi = "http://api.tianapi.com/";

	public static final String TXNewsApiEnd = "?key=18f7f9dbd7dfcd8ab45efdcfbc33826d&num=15&page=";

	/**广阅通**/
	public static final String GuangyuetongUrl = "http://p.contx.cn/v1/access?id=f9136944-bc17-4cb1-9b14-ece9de91b39d&uid=#uid#&ud=#ud#";

	//应用静态常量：
	public static boolean isMainFragmentNeedRefresh;

	public static final String Email = "meixililulu@163.com";
	
	public static final String showapi_appid = "11619";	
	public static final String showapi_secret = "f27574671ec14eb4a97faacb2eee3ef2";	
	
	public static final int page_size = 12;
	public static final String baidu_appid = "20151111000005006";	
	public static final String baidu_secretkey = "91mGcsmdvX9HAaE8tXoI";	
	public static final String client_id = "vCV6TTGRTI5QrckdYSKHQIhq";	
	public static String from = "auto";	
	public static String to = "auto";	
	public static String q = "";	
	public static String role = "vimary";	
	public static final int offset = 100;

	public static String[] PERMISSIONS_STORAGE = {
			Manifest.permission.READ_EXTERNAL_STORAGE,
			Manifest.permission.WRITE_EXTERNAL_STORAGE
	};
	public static String[] PERMISSIONS_RECORD_AUDIO = {
			Manifest.permission.RECORD_AUDIO
	};
	
	/**is today already do something
	 * @param mSharedPreferences
	 * @return
	 */
	public static boolean isTodayShow(SharedPreferences mSharedPreferences){
		String today = TimeUtil.getTimeDate(System.currentTimeMillis());
		LogUtil.DefalutLog("---isTodayShow---today:"+today);
		String lastTime = mSharedPreferences.getString(KeyUtil.IsLoadingShowToday, "");
		if(today.equals(lastTime)){
			return true;
		}else{
			saveSharedPreferences(mSharedPreferences,KeyUtil.IsLoadingShowToday,today);
			return false;
		}
	}
	
	/**time interval
	 * @param mSharedPreferences
	 * @param interval
	 * @return
	 */
	public static boolean isEnoughTime(SharedPreferences mSharedPreferences, long interval){
		long now = System.currentTimeMillis();
		long lastTime = mSharedPreferences.getLong(KeyUtil.IsEnoughIntervalTime, 0);
		saveSharedPreferences(mSharedPreferences,KeyUtil.IsEnoughIntervalTime,now);
		if(now - lastTime > interval){
			return true;
		}else{
			return false;
		}
	}
	
	/**获取配置文件类
	 * @param context
	 * @return
	 */
	public static SharedPreferences getSharedPreferences(Context context){
		return context.getSharedPreferences(context.getPackageName(), Activity.MODE_PRIVATE);
	}
	
	/**
	 * 保存配置信息
	 * 
	 * @param context
	 * @param value
	 * @param key
	 */
	public static void saveSharedPreferences(SharedPreferences sharedPrefs, String key, String value) {
		LogUtil.DefalutLog("key-value:" + key + "-" + value);
		Editor editor = sharedPrefs.edit();
		editor.putString(key, value);
		editor.commit();
	}
	
	/**
	 * 保存配置信息
	 * 
	 * @param context
	 * @param value
	 * @param key
	 */
	public static void saveSharedPreferences(SharedPreferences sharedPrefs, String key, boolean value) {
		LogUtil.DefalutLog("key-value:" + key + "-" + value);
		Editor editor = sharedPrefs.edit();
		editor.putBoolean(key, value);
		editor.commit();
	}
	
	/**
	 * 保存配置信息
	 * 
	 * @param context
	 * @param value
	 * @param key
	 */
	public static void saveSharedPreferences(SharedPreferences sharedPrefs, String key, long value) {
		LogUtil.DefalutLog("key-value:" + key + "-" + value);
		Editor editor = sharedPrefs.edit();
		editor.putLong(key, value);
		editor.commit();
	}
	
	/**
	 * 保存配置信息
	 * 
	 * @param context
	 * @param value
	 * @param key
	 */
	public static void saveSharedPreferences(SharedPreferences sharedPrefs, String key, int value) {
		LogUtil.DefalutLog("key-value:" + key + "-" + value);
		Editor editor = sharedPrefs.edit();
		editor.putInt(key, value);
		editor.commit();
	}

	public static void contantUs(Context mContext){
		try {
			Intent emailIntent = new Intent(Intent.ACTION_SEND);
			emailIntent.setType("message/rfc822");
			emailIntent.putExtra(Intent.EXTRA_EMAIL,new String[] { Email });
			mContext.startActivity(emailIntent);
		} catch (Exception e) {
			copy(mContext,Email);
			e.printStackTrace();
		}
	}
	
	/**
	 * 复制按钮
	 */
	public static void copy(Context mContext,String dstString){
		try {
			// 得到剪贴板管理器
			ClipboardManager cmb = (ClipboardManager)mContext.getSystemService(Context.CLIPBOARD_SERVICE);
			cmb.setText(dstString);
			ToastUtil.diaplayMesShort(mContext, mContext.getResources().getString(R.string.copy_success));
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public static int getVersion(Context mContext) {
		try {
			PackageManager manager = mContext.getPackageManager();
			PackageInfo info = manager.getPackageInfo(mContext.getPackageName(), 0);
			return info.versionCode;
		} catch (Exception e) {
			e.printStackTrace();
			return 1;
		}
	}
	
	public static String getIpAddress(Context mContext){
		//获取wifi服务  
        WifiManager wifiManager = (WifiManager) mContext.getSystemService(Context.WIFI_SERVICE);  
        //判断wifi是否开启  
//        if (!wifiManager.isWifiEnabled()) {
//        	wifiManager.setWifiEnabled(true);
//        }
        WifiInfo wifiInfo = wifiManager.getConnectionInfo();       
        int ipAddress = wifiInfo.getIpAddress();   
        return intToIp(ipAddress);   
	}
	
	public static String intToIp(int i) {       
      return (i & 0xFF ) + "." +       
      ((i >> 8 ) & 0xFF) + "." +       
      ((i >> 16 ) & 0xFF) + "." +       
      ( i >> 24 & 0xFF) ;  
   }  
	
	/**
	 * 分享
	 */
	public static void share(final Context context,final String dstString){
		String[] tempText = new String[2];
		tempText[0] = context.getResources().getString(R.string.share_dialog_text_1);
		tempText[1] = context.getResources().getString(R.string.share_dialog_text_2);
		PopDialog mPopDialog = new PopDialog(context,tempText);
		mPopDialog.setCanceledOnTouchOutside(true);
		mPopDialog.setListener(new PopDialog.PopViewItemOnclickListener() {
			@Override
			public void onSecondClick(View v) {
				toShareImageActivity(context,dstString);
				AVAnalytics.onEvent(context, "share_img_btn");
			}
			@Override
			public void onFirstClick(View v) {
				toShareTextActivity(context,dstString);
				AVAnalytics.onEvent(context, "share_text_btn");
			}
		});
		mPopDialog.show();
	}
	
	public static void toShareTextActivity(Context context,String dstString){
		Intent intent = new Intent(Intent.ACTION_SEND);    
		intent.setType("text/plain"); // 纯文本     
		intent.putExtra(Intent.EXTRA_SUBJECT, context.getResources().getString(R.string.share));    
		intent.putExtra(Intent.EXTRA_TEXT, dstString);    
		intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);    
		context.startActivity(Intent.createChooser(intent, context.getResources().getString(R.string.share)));    
	}
	
	public static void toShareImageActivity(Context context,String dstString){
//		Intent intent = new Intent(context, ImgShareActivity.class);
//		intent.putExtra(KeyUtil.ShareContentKey, dstString);
//		context.startActivity(intent);
	}

	public static void verifyStoragePermissions(final Activity activity,final String[] permissions) {
		try{
			// Check if we have write permission
			int permission = ActivityCompat.checkSelfPermission(activity, permissions[0]);
			LogUtil.DefalutLog("verifyStoragePermissions---permission:"+permission);
			if (permission != PackageManager.PERMISSION_GRANTED) {
				if(shouldShowRequestPermissionRationale(activity,permissions[0])){
					AlertDialog.Builder builder = new AlertDialog.Builder(activity);
					builder.setTitle("温馨提示");
					builder.setMessage("软件需要SD卡和话筒的访问权限才能正常运行。");
					builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface dialog, int which) {
							ActivityCompat.requestPermissions(
									activity,
									permissions,
									RequestCode
							);
						}
					});
					AlertDialog dialog = builder.create();
					dialog.show();
				}else {
					ActivityCompat.requestPermissions(
							activity,
							permissions,
							RequestCode
					);
				}
			}
		}catch (Exception e){
			e.printStackTrace();
		}
	}

	public static void uninstall(Context mContext){
		Intent intent = new Intent();
		intent.setAction(Intent.ACTION_VIEW);
		intent.setData(Uri.parse("market://details?id=com.messi.languagehelper"));
		mContext.startActivity(intent);
	}
	
	public static void startPermissionManager(Context mContext){
		gotoMiuiPermission(mContext);
	}

	/**
	 * 跳转到miui的权限管理页面
	 */
	public static void gotoMiuiPermission(Context mContext) {
		Intent i = new Intent("miui.intent.action.APP_PERM_EDITOR");
		ComponentName componentName = new ComponentName("com.miui.securitycenter", "com.miui.permcenter.permissions.AppPermissionsEditorActivity");
		i.setComponent(componentName);
		i.putExtra("extra_pkgname", mContext.getPackageName());
		try {
			mContext.startActivity(i);
		} catch (Exception e) {
			e.printStackTrace();
			gotoMeizuPermission(mContext);
		}
	}

	public static void gotoMeizuPermission(Context mContext) {
		Intent intent = new Intent("com.meizu.safe.security.SHOW_APPSEC");
		intent.addCategory(Intent.CATEGORY_DEFAULT);
		intent.putExtra("packageName", BuildConfig.APPLICATION_ID);
		try {
			mContext.startActivity(intent);
		} catch (Exception e) {
			e.printStackTrace();
			gotoHuaweiPermission(mContext);
		}
	}

	public static void gotoHuaweiPermission(Context mContext) {
		try {
			Intent intent = new Intent();
			intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			ComponentName comp = new ComponentName("com.huawei.systemmanager", "com.huawei.permissionmanager.ui.MainActivity");//华为权限管理
			intent.setComponent(comp);
			mContext.startActivity(intent);
		} catch (Exception e) {
			e.printStackTrace();
			mContext.startActivity(getAppDetailSettingIntent(mContext));
		}
	}

	public static Intent getAppDetailSettingIntent(Context mContext) {
		Intent localIntent = new Intent();
		localIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		if (Build.VERSION.SDK_INT >= 9) {
			localIntent.setAction("android.settings.APPLICATION_DETAILS_SETTINGS");
			localIntent.setData(Uri.fromParts("package", mContext.getPackageName(), null));
		} else if (Build.VERSION.SDK_INT <= 8) {
			localIntent.setAction(Intent.ACTION_VIEW);
			localIntent.setClassName("com.android.settings", "com.android.settings.InstalledAppDetails");
			localIntent.putExtra("com.android.settings.ApplicationPkgName", mContext.getPackageName());
		}
		return localIntent;
	}
}
