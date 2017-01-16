package cn.ucai.fulicenter.view;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;

import java.util.ArrayList;

import cn.ucai.fulicenter.R;
import cn.ucai.fulicenter.application.I;
import cn.ucai.fulicenter.controller.activity.BoutiqueChildActivity;
import cn.ucai.fulicenter.controller.activity.CategoryChildActivity;
import cn.ucai.fulicenter.controller.activity.GoodsDetailsActivity;
import cn.ucai.fulicenter.model.bean.BoutiqueBean;
import cn.ucai.fulicenter.model.bean.CategoryChildBean;

/**
 * Created by Administrator on 2017/1/10 0010.
 */
public class MFGT {
    public static void startActivity(Activity activity, Class<?> clz) {
        activity.startActivity(new Intent(activity,clz));
        activity.overridePendingTransition(R.anim.push_right_in,R.anim.push_right_out);
    }
    public static void startActivity(Activity activity, Intent intent) {
        activity.startActivity(intent);
        activity.overridePendingTransition(R.anim.push_right_in,R.anim.push_right_out);
    }

    public static void finishActivity(Activity activity) {
        activity.finish();
        activity.overridePendingTransition(R.anim.push_left_in,R.anim.push_left_out);
    }

    public static void gotoBoutiqueChild(Context mContext, BoutiqueBean boutiqueBean) {
        Intent intent = new Intent(mContext, BoutiqueChildActivity.class);
        intent.putExtra("id", boutiqueBean.getId());
        intent.putExtra("title", boutiqueBean.getName());
        startActivity((Activity) mContext,intent);
    }

    public static void gotoGoodsDetail(Context mContext, int goodsId) {
        Intent intent = new Intent(mContext, GoodsDetailsActivity.class);
        intent.putExtra("goodsId", goodsId);
        startActivity((Activity) mContext,intent);
    }

    public static void gotoCategoryChild(Context mContext, int catId, String groupName, ArrayList<CategoryChildBean> list) {
        Intent intent = new Intent(mContext, CategoryChildActivity.class);
        intent.putExtra("id", catId);
        intent.putExtra("title", groupName);
        intent.putExtra(I.CategoryChild.DATA,list);
        startActivity((Activity) mContext,intent);
    }
}
