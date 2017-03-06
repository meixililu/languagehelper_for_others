package com.messi.cantonese.study.db;

import android.content.Context;
import com.messi.cantonese.study.BaseApplication;
import com.messi.cantonese.study.R;
import com.messi.cantonese.study.dao.DaoSession;
import com.messi.cantonese.study.dao.Dictionary;
import com.messi.cantonese.study.dao.DictionaryDao;
import com.messi.cantonese.study.dao.EveryDaySentence;
import com.messi.cantonese.study.dao.EveryDaySentenceDao;
import com.messi.cantonese.study.dao.Means;
import com.messi.cantonese.study.dao.MeansDao;
import com.messi.cantonese.study.dao.Parts;
import com.messi.cantonese.study.dao.PartsDao;
import com.messi.cantonese.study.dao.SymbolListDao;
import com.messi.cantonese.study.dao.Tag;
import com.messi.cantonese.study.dao.record;
import com.messi.cantonese.study.dao.recordDao;
import com.messi.cantonese.study.dao.SymbolListDaoDao;
import com.messi.cantonese.study.dao.TagDao;
import com.messi.cantonese.study.util.LogUtil;
import com.messi.cantonese.study.util.Settings;

import java.util.List;

import de.greenrobot.dao.query.DeleteQuery;
import de.greenrobot.dao.query.QueryBuilder;

public class DataBaseUtil {

    private static DataBaseUtil instance;
    private static Context appContext;
    private DaoSession mDaoSession;
    private recordDao recordDao;
    private EveryDaySentenceDao mEveryDaySentenceDao;
    private DictionaryDao mDictionaryDao;
    private MeansDao MmeansDao;
    private PartsDao mPartsDao;
    private TagDao mTagDao;
    private SymbolListDaoDao mSymbolListDaoDao;

    public DataBaseUtil() {
    }

    public static DataBaseUtil getInstance() {
        if (instance == null) {
            instance = new DataBaseUtil();
            if (appContext == null) {
                appContext = BaseApplication.mInstance;
            }
            instance.mDaoSession = BaseApplication.getDaoSession(appContext);
            instance.recordDao = instance.mDaoSession.getRecordDao();
            instance.mDictionaryDao = instance.mDaoSession.getDictionaryDao();
            instance.mEveryDaySentenceDao = instance.mDaoSession.getEveryDaySentenceDao();
            instance.mPartsDao = instance.mDaoSession.getPartsDao();
            instance.MmeansDao = instance.mDaoSession.getMeansDao();
            instance.mTagDao = instance.mDaoSession.getTagDao();
            instance.mSymbolListDaoDao = instance.mDaoSession.getSymbolListDaoDao();
        }
        return instance;
    }

    public long insert(Dictionary bean) {
        bean.setIscollected("0");
        bean.setVisit_times(0);
        bean.setSpeak_speed(Settings.getSharedPreferences(appContext).getInt(appContext.getString(R.string.preference_key_tts_speed), 50));
        bean.setQuestionVoiceId(System.currentTimeMillis() + "");
        return mDictionaryDao.insert(bean);
    }

    public long insert(Parts bean) {
        return mPartsDao.insert(bean);
    }

    public long insert(Tag bean) {
        return mTagDao.insert(bean);
    }

    public long insert(Means bean) {
        return MmeansDao.insert(bean);
    }

    public void insert(List<SymbolListDao> beans) {
        for (SymbolListDao bean : beans) {
            mSymbolListDaoDao.insert(bean);
        }
    }

    public long getSymbolListSize() {
        return mSymbolListDaoDao.count();
    }

    public List<SymbolListDao> getSymbolList() {
        return mSymbolListDaoDao.loadAll();
    }

    public void update(SymbolListDao bean) {
        mSymbolListDaoDao.update(bean);
    }

    public long insert(record bean) {
        bean.setIscollected("0");
        bean.setVisit_times(0);
        bean.setSpeak_speed(Settings.getSharedPreferences(appContext).getInt(appContext.getString(R.string.preference_key_tts_speed), 50));
        bean.setQuestionVoiceId(System.currentTimeMillis() + "");
        bean.setResultVoiceId(System.currentTimeMillis() - 5 + "");
        return recordDao.insert(bean);
    }


    public void update(record bean) {
        recordDao.update(bean);
    }

    public void update(Dictionary bean) {
        mDictionaryDao.update(bean);
    }

    public List<record> getDataListRecord(int offset, int maxResult) {
        QueryBuilder<record> qb = recordDao.queryBuilder();
        qb.orderDesc(com.messi.cantonese.study.dao.recordDao.Properties.Id);
        qb.limit(maxResult);
        return qb.list();
    }

    public List<Dictionary> getDataListDictionary(int offset, int maxResult) {
        QueryBuilder<Dictionary> qb = mDictionaryDao.queryBuilder();
        qb.orderDesc(DictionaryDao.Properties.Id);
        qb.limit(maxResult);
        return qb.list();
    }

    public List<record> getDataListCollected(int offset, int maxResult) {
        QueryBuilder<record> qb = recordDao.queryBuilder();
        qb.where(com.messi.cantonese.study.dao.recordDao.Properties.Iscollected.eq("1"));
        qb.orderDesc(com.messi.cantonese.study.dao.recordDao.Properties.Id);
        qb.limit(maxResult);
        return qb.list();
    }

    public List<Dictionary> getDataListDictionaryCollected(int offset, int maxResult) {
        QueryBuilder<Dictionary> qb = mDictionaryDao.queryBuilder();
        qb.where(DictionaryDao.Properties.Iscollected.eq("1"));
        qb.orderDesc(DictionaryDao.Properties.Id);
        qb.limit(maxResult);
        return qb.list();
    }

    public void dele(record bean) {
        recordDao.delete(bean);
    }

    public void dele(Dictionary bean) {
        mDictionaryDao.delete(bean);
    }

    public void clearExceptFavoriteTran() {
        clearTranslateExceptFavorite();
    }

    public void clearExceptFavoriteDic() {
        clearDictionaryExceptFavorite();
    }

    public void clearSymbolList() {
        mSymbolListDaoDao.deleteAll();
    }

    public void clearTranslateExceptFavorite() {
        QueryBuilder<record> qb = recordDao.queryBuilder();
        DeleteQuery<record> bd = qb.where(com.messi.cantonese.study.dao.recordDao.Properties.Iscollected.eq("0")).buildDelete();
        bd.executeDeleteWithoutDetachingEntities();
    }

    public void clearDictionaryExceptFavorite() {
        QueryBuilder<Dictionary> qb = mDictionaryDao.queryBuilder();
        DeleteQuery<Dictionary> bd = qb.where(DictionaryDao.Properties.Iscollected.eq("0")).buildDelete();
        bd.executeDeleteWithoutDetachingEntities();
    }

    public void clearAllTran() {
        clearAllTranslate();
    }

    public void clearAllDic() {
        clearAllDictionary();
    }

    public void clearAllTranslate() {
        recordDao.deleteAll();
    }

    public void clearAllDictionary() {
        mDictionaryDao.deleteAll();
    }

    public long getRecordCount() {
        return recordDao.count();
    }

    public long getDictionaryCount() {
        return mDictionaryDao.count();
    }

    /**
     * Daily Sentence CURD
     **/
    public long insert(EveryDaySentence bean) {
        return mEveryDaySentenceDao.insert(bean);
    }

    public boolean isExist(long cid) {
        QueryBuilder<EveryDaySentence> qb = mEveryDaySentenceDao.queryBuilder();
        qb.where(EveryDaySentenceDao.Properties.Cid.eq(cid));
        int size = qb.list().size();
        LogUtil.DefalutLog("isExist---size:" + size);
        return size > 0;
    }

    public List<EveryDaySentence> getDailySentenceList(int limit) {
        QueryBuilder<EveryDaySentence> qb = mEveryDaySentenceDao.queryBuilder();
        qb.orderDesc(EveryDaySentenceDao.Properties.Dateline);
        qb.limit(limit);
        return qb.list();
    }

    public void saveEveryDaySentenceList(List<EveryDaySentence> beans) {
        for (EveryDaySentence item : beans) {
            if (!isExist(item.getCid())) {
                insert(item);
            }
        }
    }
    /**Daily Sentence CURD**/

}
