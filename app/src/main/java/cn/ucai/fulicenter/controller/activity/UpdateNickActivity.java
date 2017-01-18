package cn.ucai.fulicenter.controller.activity;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

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
import cn.ucai.fulicenter.model.utils.CommonUtils;
import cn.ucai.fulicenter.model.utils.ResultUtils;
import cn.ucai.fulicenter.view.MFGT;

public class UpdateNickActivity extends AppCompatActivity {

    @BindView(R.id.etUpdateNick)
    EditText metUpdateNick;

    User user;
    IModelUser mModel;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_nick);
        ButterKnife.bind(this);
        initData();
    }

    private void initData() {
        user= FuliCenterApplication.getUser();
        if (user == null) {
            MFGT.finishActivity(this);
        } else {
            metUpdateNick.setText(user.getMuserNick());
        }
    }

    @OnClick({R.id.ivSettingBack, R.id.btnSaveNick})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.ivSettingBack:
                MFGT.finishActivity(this);
                break;
            case R.id.btnSaveNick:
                checkInput();
                break;
        }
    }

    private void checkInput() {
        String nick=metUpdateNick.getText().toString().trim();
        if (TextUtils.isEmpty(nick)) {
            CommonUtils.showLongToast(R.string.nick_name_connot_be_empty);
        } else if (nick.equals(user.getMuserNick())) {
            CommonUtils.showLongToast(R.string.update_nick_fail_unmodify);
        } else {
            updateNick(nick);
        }
    }

    private void updateNick(String nick) {
        final ProgressDialog dialog = new ProgressDialog(this);
        dialog.setMessage(getString(R.string.update_user_nick));
        dialog.show();
        mModel=new ModelUser();
        mModel.updateNick(this, user.getMuserName(), nick, new OnCompleteListener<String>() {
            @Override
            public void onSuccess(String s) {
                int msg=R.string.update_fail;
                if (s != null) {
                    Result result = ResultUtils.getResultFromJson(s, User.class);
                    if (result != null) {
                        if (result.isRetMsg()) {
                            msg = R.string.update_user_nick_success;
                            User user= (User) result.getRetData();
                            Log.e("main", "update success,user=" + user);
                            saveNewUser(user);
                            setResult(RESULT_OK);
                            MFGT.finishActivity(UpdateNickActivity.this);
                        } else {
                            if (result.getRetCode() == I.MSG_USER_SAME_NICK ||
                                    result.getRetCode()==I.MSG_USER_UPDATE_NICK_FAIL) {
                                msg=R.string.update_nick_fail_unmodify;
                            }
                        }
                    }
                }
                CommonUtils.showLongToast(msg);
                dialog.dismiss();
            }

            @Override
            public void onError(String error) {
                CommonUtils.showLongToast(R.string.update_fail);
                dialog.dismiss();
            }
        });
    }

    private void saveNewUser(User user) {
        FuliCenterApplication.setUser(user);
        UserDao.getInstance().saveUser(user);
    }
}
