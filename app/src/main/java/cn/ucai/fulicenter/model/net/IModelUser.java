package cn.ucai.fulicenter.model.net;

import android.content.Context;

/**
 * Created by Administrator on 2017/1/16 0016.
 */

public interface IModelUser {
    void login(Context context, String userName, String password, OnCompleteListener<String> listener);
    void register(Context context, String userName,String nick, String password, OnCompleteListener<String> listener);
}
