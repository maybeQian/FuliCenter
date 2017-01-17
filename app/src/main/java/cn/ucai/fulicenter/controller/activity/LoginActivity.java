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
import cn.ucai.fulicenter.model.net.SharePreferenceUtils;
import cn.ucai.fulicenter.model.utils.CommonUtils;
import cn.ucai.fulicenter.model.utils.ResultUtils;
import cn.ucai.fulicenter.view.MFGT;

public class LoginActivity extends AppCompatActivity {

    @BindView(R.id.etUserName)
    EditText metUserName;
    @BindView(R.id.etPassword)
    EditText metPassword;

    IModelUser mModel;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);
    }

    @OnClick({R.id.btnLogin, R.id.btnRegister,R.id.ivReturn})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.ivReturn:
                MFGT.finishActivity(this);
                break;
            case R.id.btnLogin:
                checkInput();
                break;
            case R.id.btnRegister:
                MFGT.gotoRegister(this);
                break;
        }
    }

    private void checkInput() {
        String username = metUserName.getText().toString().trim();
        String password = metPassword.getText().toString().trim();
        if (TextUtils.isEmpty(username)) {
            metUserName.setError(getResources().getString(R.string.user_name_connot_be_empty));
            metUserName.requestFocus();
        } else if (TextUtils.isEmpty(password)) {
            metPassword.setError(getResources().getString(R.string.password_connot_be_empty));
            metPassword.requestFocus();
        } else {
            login(username, password);
        } 
    }

    private void login(final String username, String password) {
        final ProgressDialog dialog = new ProgressDialog(this);
        dialog.setMessage(getString(R.string.logining));
        dialog.show();
        mModel=new ModelUser();
        mModel.login(this, username, password, new OnCompleteListener<String>() {
            @Override
            public void onSuccess(String s) {
                if (s != null) {
                    Result result = ResultUtils.getResultFromJson(s, User.class);
                    Log.i("main", "result=" + result);
                    if (result != null) {
                        if (result.isRetMsg()) {
                            User user= (User) result.getRetData();
                            UserDao.getInstance().saveUser(user);
                            SharePreferenceUtils.getInstance(LoginActivity.this).saveUser(user.getMuserName());
                            FuliCenterApplication.setUser(user);
                            MFGT.finishActivity(LoginActivity.this);
                        } else {
                            if (result.getRetCode() == I.MSG_LOGIN_UNKNOW_USER) {
                                CommonUtils.showLongToast(R.string.login_fail_unknow_user);
                            }
                            if (result.getRetCode() == I.MSG_LOGIN_ERROR_PASSWORD) {
                                CommonUtils.showLongToast(R.string.login_fail_error_password);
                            }
                        }
                    } else {
                        CommonUtils.showLongToast(R.string.login_fail);
                    }

                } else {
                    CommonUtils.showLongToast(R.string.login_fail);
                }
                dialog.dismiss();
            }

            @Override
            public void onError(String error) {
                dialog.dismiss();
                CommonUtils.showLongToast(error);
            }
        });
    }


}
