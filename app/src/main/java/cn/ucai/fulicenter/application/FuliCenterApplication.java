package cn.ucai.fulicenter.application;

import android.app.Application;

import java.util.HashMap;

import cn.sharesdk.framework.ShareSDK;
import cn.ucai.fulicenter.model.bean.CartBean;
import cn.ucai.fulicenter.model.bean.User;

/**
 * Created by Administrator on 2017/1/10 0010.
 */
public class FuliCenterApplication extends Application {
    private static FuliCenterApplication instance;

    public static FuliCenterApplication getInstance() {
        return instance;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        instance=this;
        ShareSDK.initSDK(this);
    }


    private static User user;

    public static User getUser() {
        return user;
    }

    public static void setUser(User user) {
        FuliCenterApplication.user = user;
    }
}
