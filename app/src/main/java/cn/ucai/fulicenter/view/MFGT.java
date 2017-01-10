package cn.ucai.fulicenter.view;

import android.app.Activity;
import android.content.Intent;

import cn.ucai.fulicenter.R;

/**
 * Created by Administrator on 2017/1/10 0010.
 */
public class MFGT {
    public static void startActivity(Activity activity, Class<?> clz) {
        activity.startActivity(new Intent(activity,clz));
        activity.overridePendingTransition(R.anim.push_right_in,R.anim.push_right_out);
    }

    public static void finishActivity(Activity activity) {
        activity.finish();
        activity.overridePendingTransition(R.anim.push_left_in,R.anim.push_left_out);
    }
}
