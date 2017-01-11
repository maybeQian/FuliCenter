package cn.ucai.fulicenter.application;

import android.app.Application;

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
    }
}
