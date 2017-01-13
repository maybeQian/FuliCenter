package cn.ucai.fulicenter.model.net;

import android.content.Context;

import cn.ucai.fulicenter.model.bean.CategoryChildBean;
import cn.ucai.fulicenter.model.bean.CategoryGroupBean;

/**
 * Created by Administrator on 2017/1/13 0013.
 */

public interface IModelCategory {
    void downData(Context context, OnCompleteListener<CategoryGroupBean[]> listener);
    void downData(Context context, int groupId, OnCompleteListener<CategoryChildBean[]> listener);
}
