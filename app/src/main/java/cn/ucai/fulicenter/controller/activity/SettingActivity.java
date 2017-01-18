package cn.ucai.fulicenter.controller.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.ucai.fulicenter.R;
import cn.ucai.fulicenter.application.FuliCenterApplication;
import cn.ucai.fulicenter.application.I;
import cn.ucai.fulicenter.model.bean.User;
import cn.ucai.fulicenter.model.net.SharePreferenceUtils;
import cn.ucai.fulicenter.model.utils.ImageLoader;
import cn.ucai.fulicenter.view.MFGT;

public class SettingActivity extends AppCompatActivity {

    @BindView(R.id.iv_avatar)
    ImageView mivAvatar;
    @BindView(R.id.tvUsername)
    TextView mtvUsername;
    @BindView(R.id.tvNick)
    TextView mtvNick;

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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && requestCode == I.REQUEST_CODE_NICK) {
            mtvNick.setText(FuliCenterApplication.getUser().getMuserNick());
        }
    }
}
