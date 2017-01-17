package cn.ucai.fulicenter.model.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import cn.ucai.fulicenter.model.bean.User;

/**
 * Created by Administrator on 2017/1/17 0017.
 */

public class DBManager {
    private static final String TAG=DBManager.class.getSimpleName();
    private static DBOpenHelper dbOpenHelper;
    static DBManager manager=new DBManager();

    public DBManager() {

    }

    public static void onInit(Context context) {
        dbOpenHelper = new DBOpenHelper(context);
    }

    public synchronized static DBManager getInstance() {
        if (dbOpenHelper == null) {
            Log.e(TAG, "没有调用onInit");
        }
        return manager;
    }

    public boolean saveUser(User user) {
        SQLiteDatabase db=dbOpenHelper.getWritableDatabase();
        if (db.isOpen()) {
            ContentValues values = new ContentValues();
            values.put(UserDao.USER_COLUMN_NAME,user.getMuserName());
            values.put(UserDao.USER_COLUMN_NICK,user.getMuserNick());
            values.put(UserDao.USER_COLUMN_AVATAR_ID,user.getMavatarId());
            values.put(UserDao.USER_COLUMN_AVATAR_PATH,user.getMavatarPath());
            values.put(UserDao.USER_COLUMN_AVATAR_SUFFIX,user.getMavatarSuffix());
            values.put(UserDao.USER_COLUMN_AVATAR_TYPE,user.getMavatarType());
            values.put(UserDao.USER_COLUMN_AVATAR_UPDATE_TIME,user.getMavatarLastUpdateTime());
            return  db.replace(UserDao.USER_TABLE_NAME, null, values)!=-1;
        }
        return false;
    }
}
