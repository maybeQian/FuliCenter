package cn.ucai.fulicenter.model.net;

import android.content.Context;

import cn.ucai.fulicenter.model.bean.GoodsDetailsBean;

/**
 * Created by Administrator on 2017/1/12 0012.
 */

public interface IModelGoodsDetail {
    void downData(Context context, int goodsId, OnCompleteListener<GoodsDetailsBean> listener);
}
