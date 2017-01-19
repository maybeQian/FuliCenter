package cn.ucai.fulicenter.controller.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.File;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.ucai.fulicenter.R;
import cn.ucai.fulicenter.application.FuliCenterApplication;
import cn.ucai.fulicenter.application.I;
import cn.ucai.fulicenter.model.bean.Result;
import cn.ucai.fulicenter.model.bean.User;
import cn.ucai.fulicenter.model.dao.UserDao;
import cn.ucai.fulicenter.model.net.IModelUser;
import cn.ucai.fulicenter.model.net.ModelUser;
import cn.ucai.fulicenter.model.net.OnCompleteListener;
import cn.ucai.fulicenter.model.net.SharePreferenceUtils;
import cn.ucai.fulicenter.model.utils.CommonUtils;
import cn.ucai.fulicenter.model.utils.ImageLoader;
import cn.ucai.fulicenter.model.utils.L;
import cn.ucai.fulicenter.model.utils.OnSetAvatarListener;
import cn.ucai.fulicenter.model.utils.ResultUtils;
import cn.ucai.fulicenter.view.MFGT;

public class SettingActivity extends AppCompatActivity {

    @BindView(R.id.iv_avatar)
    ImageView mivAvatar;
    @BindView(R.id.tvUsername)
    TextView mtvUsername;
    @BindView(R.id.tvNick)
    TextView mtvNick;

    OnSetAvatarListener mOnSetAvatarListener;
    IModelUser mModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        ButterKnife.bind(this);
        initData();
    }

    private void initData() {
        User user = FuliCenterApplication.getUser();
        if (user != null) {
            loadUserInfo(user);
        } else {
            MFGT.gotoLogin(this);
        }
    }

    private void loadUserInfo(User user) {
        ImageLoader.setAvatar(ImageLoader.getAvatarUrl(user), this, mivAvatar);
        mtvUsername.setText(user.getMuserName());
        mtvNick.setText(user.getMuserNick());
    }

    @OnClick(R.id.btnLogout)
    public void logout() {
        FuliCenterApplication.setUser(null);
        SharePreferenceUtils.getInstance(this).removeUser();
        MFGT.gotoLogin(this);
        finish();
    }


    @OnClick({R.id.ivSettingBack, R.id.layout_user_nick})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.ivSettingBack:
                MFGT.finishActivity(this);
                break;
            case R.id.layout_user_nick:
                MFGT.gotoUpdateNick(this);
                break;
        }
    }

    @OnClick(R.id.layout_user_avatar)
    public void onClickAvatar() {
        mOnSetAvatarListener = new OnSetAvatarListener(this,
                R.id.layout_user_avatar,
                FuliCenterApplication.getUser().getMuserName(),
                I.AVATAR_TYPE_USER_PATH);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != RESULT_OK) {
            return;
        }
        if (requestCode == I.REQUEST_CODE_NICK) {
            mtvNick.setText(FuliCenterApplication.getUser().getMuserNick());
        } else if (requestCode == OnSetAvatarListener.REQUEST_CROP_PHOTO) {
            uploadAvatar();
        } else {
            mOnSetAvatarListener.setAvatar(requestCode,data,mivAvatar);
        }
    }

    private void uploadAvatar() {
        User user=FuliCenterApplication.getUser();
        final ProgressDialog dialog = new ProgressDialog(this);
        dialog.setMessage(getString(R.string.update_user_avatar));
        dialog.show();
        File file = new File(String.valueOf(OnSetAvatarListener.getAvatarFile(this,
                "/"+user.getMavatarPath()+"/"+user.getMuserName()+user.getMavatarSuffix()
                )));
        Log.e("main", "uploadAvatar,file=>>>>>>>>>" + file.getAbsolutePath());
        mModel=new ModelUser();
        mModel.uploadAvatar(this,
                user.getMuserName(),
                file,
                new OnCompleteListener<String>() {
                    @Override
                    public void onSuccess(String s) {
                        int msg=R.string.update_user_avatar_fail;
                        if (s != null) {
                            Result result = ResultUtils.getResultFromJson(s, User.class);
                            Log.e("main", "uploadAvatar,result=>>>>>>>>" + result);
                            if (result != null) {
                                if (result.isRetMsg()) {
                                    msg=R.string.update_user_avatar_success;
                                }
                            }
                        }
                        CommonUtils.showLongToast(msg);
                        dialog.dismiss();
                    }

                    @Override
                    public void onError(String error) {
                        CommonUtils.showLongToast(error);
                        dialog.dismiss();
                        Log.e("main", "uploadAvatar,error=" + error);
                    }
                });
    }




}
