package cn.ucai.fulicenter.controller.activity;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.ucai.fulicenter.R;
import cn.ucai.fulicenter.model.bean.Result;
import cn.ucai.fulicenter.model.net.IModelUser;
import cn.ucai.fulicenter.model.net.ModelUser;
import cn.ucai.fulicenter.model.net.OnCompleteListener;
import cn.ucai.fulicenter.model.utils.CommonUtils;
import cn.ucai.fulicenter.model.utils.ResultUtils;
import cn.ucai.fulicenter.view.MFGT;

public class RegisterActivity extends AppCompatActivity {

    @BindView(R.id.etUserName)
    EditText metUserName;
    @BindView(R.id.etNick)
    EditText metNick;
    @BindView(R.id.etPassword)
    EditText metPassword;
    @BindView(R.id.etConfirmPassword)
    EditText metConfirmPassword;

    IModelUser mModel;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        ButterKnife.bind(this);
    }

    @OnClick({R.id.ivReturn, R.id.btnRegister})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.ivReturn:
                MFGT.finishActivity(this);
                break;
            case R.id.btnRegister:
                checkInput();
                break;
        }
    }

    private void checkInput() {
        String userName = metUserName.getText().toString().trim();
        String nick = metNick.getText().toString().trim();
        String password = metPassword.getText().toString().trim();
        String confirmPwd = metConfirmPassword.getText().toString().trim();
        if (TextUtils.isEmpty(userName)) {
            metUserName.setError(getResources().getString(R.string.user_name_connot_be_empty));
            metUserName.requestFocus();
        } else if (!userName.matches("[a-zA-Z]\\w{5,15}")) {
            metUserName.setError(getResources().getString(R.string.illegal_user_name));
            metUserName.requestFocus();
        } else if (TextUtils.isEmpty(nick)) {
            metNick.setError(getResources().getString(R.string.nick_name_connot_be_empty));
            metNick.requestFocus();
        } else if (TextUtils.isEmpty(password)) {
            metPassword.setError(getResources().getString(R.string.password_connot_be_empty));
            metPassword.requestFocus();
        } else if (TextUtils.isEmpty(confirmPwd)) {
            metConfirmPassword.setError(getResources().getString(R.string.confirm_password_connot_be_empty));
            metConfirmPassword.requestFocus();
        } else if (!confirmPwd.equals(password)) {
            metConfirmPassword.setError(getResources().getString(R.string.two_input_password));
            metConfirmPassword.requestFocus();
        } else {
            register(userName, nick, password);
        }
    }

    private void register(String userName, String nick, String password) {
        final ProgressDialog dialog = new ProgressDialog(this);
        dialog.setMessage(getString(R.string.registering));
        mModel=new ModelUser();
        mModel.register(this, userName, nick, password, new OnCompleteListener<String>() {
            @Override
            public void onSuccess(String s) {
                if (s != null) {
                    Result result = ResultUtils.getResultFromJson(s, Result.class);
                    if (result != null) {
                        if (result.isRetMsg()) {
                            CommonUtils.showLongToast(R.string.register_success);
                            MFGT.finishActivity(RegisterActivity.this);
                        } else {
                            CommonUtils.showLongToast(R.string.register_fail_exists);
                        }
                    } else {
                        CommonUtils.showLongToast(R.string.register_fail);
                    }
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
