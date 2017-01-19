package cn.ucai.fulicenter.model.net;

import android.content.Context;

import java.io.File;

import cn.ucai.fulicenter.model.bean.CollectBean;
import cn.ucai.fulicenter.model.bean.MessageBean;

/**
 * Created by Administrator on 2017/1/16 0016.
 */

public interface IModelUser {
    void login(Context context, String userName, String password, OnCompleteListener<String> listener);
    void register(Context context, String userName,String nick, String password, OnCompleteListener<String> listener);
    void updateNick(Context context, String userName, String newNick, OnCompleteListener<String> listener);
    void uploadAvatar(Context context, String username, File file, OnCompleteListener<String> listener);
    void getCollectCount(Context context, String username, OnCompleteListener<MessageBean> listener);
    void getCollects(Context context, String username, int pageId, int pageSize, OnCompleteListener<CollectBean[]> listener);
}
