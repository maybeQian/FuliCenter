package cn.ucai.fulicenter.controller.activity;

import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import cn.ucai.fulicenter.R;
import cn.ucai.fulicenter.application.FuliCenterApplication;
import cn.ucai.fulicenter.model.bean.User;
import cn.ucai.fulicenter.model.dao.UserDao;
import cn.ucai.fulicenter.model.net.SharePreferenceUtils;
import cn.ucai.fulicenter.view.MFGT;

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
    }

    @Override
    protected void onStart() {
        super.onStart();

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                String username= SharePreferenceUtils.getInstance(SplashActivity.this).getUser();
                if (username != null) {
                    User user= UserDao.getInstance().getUser(username);
                    Log.i("main", "user->>>>>>>>>>>>>>>" + user);
                    if (user != null) {
                        FuliCenterApplication.setUser(user);
                    }
                }
                MFGT.startActivity(SplashActivity.this, MainActivity.class);
                MFGT.finishActivity(SplashActivity.this);
            }
        }, 2000);

    }
}
