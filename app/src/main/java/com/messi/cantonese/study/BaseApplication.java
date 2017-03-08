package com.messi.cantonese.study;

import android.app.Application;
import android.content.Context;
import android.os.Process;

import com.avos.avoscloud.AVAnalytics;
import com.avos.avoscloud.AVOSCloud;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.messi.cantonese.study.dao.DaoMaster;
import com.messi.cantonese.study.dao.DaoSession;
import com.messi.cantonese.study.db.LHContract;

import java.util.HashMap;

public class BaseApplication extends Application {

	public static HashMap<String, Object> dataMap = new HashMap<String, Object>();
	private static DaoMaster daoMaster;
    private static DaoSession daoSession;
    public static BaseApplication mInstance;

    @Override  
    public void onCreate() {  
        super.onCreate();
        if(mInstance == null)  mInstance = this;
        initAVOS();
    }

    private void initAVOS(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                Process.setThreadPriority(Process.THREAD_PRIORITY_BACKGROUND);
                Fresco.initialize(BaseApplication.this);
                AVOSCloud.initialize(mInstance, "BW9UrdYCUw7MqHkyvehsP4vl-gzGzoHsz", "2wmWwlEpuzYEtvV17WwwUO5A");
                AVAnalytics.enableCrashReport(mInstance, true);
            }
        }).run();
    }

	/**
     * 取得DaoMaster
     * @param context
     * @return
     */
    public static DaoMaster getDaoMaster(Context context) {
        if (daoMaster == null) {
            DaoMaster.OpenHelper helper = new DaoMaster.DevOpenHelper(context, LHContract.DATABASE_NAME, null);
            daoMaster = new DaoMaster(helper.getWritableDatabase());
        }
        return daoMaster;
    }

    /**
     * 取得DaoSession
     * @param context
     * @return
     */
    public static DaoSession getDaoSession(Context context) {
        if (daoSession == null) {
            if (daoMaster == null) {
                daoMaster = getDaoMaster(context);
            }
            daoSession = daoMaster.newSession();
        }
        return daoSession;
    }

}
