package cn.ucai.fulicenter.model.net;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by Administrator on 2017/1/16 0016.
 */

public class SharePreferenceUtils {
    private static final String SHARE_PREFERENCE_NAME = "cn.ucai.fulicenter_user";
    private static final String SHARE_PREFERENCE_NAME_USERNAME = "cn.ucai.fulicenter_user_username";
    private static SharePreferenceUtils instance;
    private static SharedPreferences preferences;

    public SharePreferenceUtils(Context context) {
        preferences = context.getSharedPreferences(SHARE_PREFERENCE_NAME, Context.MODE_PRIVATE);
    }

    public static SharePreferenceUtils getInstance(Context context) {
        if (instance == null) {
            instance = new SharePreferenceUtils(context);
        }
        return instance;
    }

    public void saveUser(String username) {
        preferences.edit().putString(SHARE_PREFERENCE_NAME_USERNAME,username).commit();
    }

    public String getUser() {
        return preferences.getString(SHARE_PREFERENCE_NAME_USERNAME, null);
    }

    public void removeUser() {
        preferences.edit().remove(SHARE_PREFERENCE_NAME_USERNAME).commit();
    }
}
