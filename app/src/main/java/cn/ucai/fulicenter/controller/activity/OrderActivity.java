package cn.ucai.fulicenter.controller.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.widget.EditText;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.ucai.fulicenter.R;
import cn.ucai.fulicenter.application.I;
import cn.ucai.fulicenter.view.MFGT;

public class OrderActivity extends AppCompatActivity {

    @BindView(R.id.etReceiver)
    EditText metReceiver;
    @BindView(R.id.etPhoneNumber)
    EditText metPhoneNumber;
    @BindView(R.id.etStreet)
    EditText metStreet;
    @BindView(R.id.tv_order_price)
    TextView mtvOrderPrice;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order);
        ButterKnife.bind(this);
        setView();
    }

    private void setView() {
        int payPrice = getIntent().getIntExtra(I.Cart.PAY_PRICE, 0);
        mtvOrderPrice.setText("合计: ￥" + payPrice);
    }

    @OnClick(R.id.ivSettingBack)
    public void onClick() {
        MFGT.finishActivity(this);
    }

    @OnClick(R.id.tv_order_buy)
    public void checkOrder() {
        String receiverName = metReceiver.getText().toString();
        if (TextUtils.isEmpty(receiverName)) {
            metReceiver.setError("收货人姓名不能为空");
            metReceiver.requestFocus();
            return;
        }
        String phoneNumber = metPhoneNumber.getText().toString();
        if (TextUtils.isEmpty(phoneNumber)) {
            metPhoneNumber.setError("手机号码不能为空");
            metPhoneNumber.requestFocus();
            return;
        }
        if (!phoneNumber.matches("[\\d]{11}")) {
            metPhoneNumber.setError("手机号码格式错误");
            metPhoneNumber.requestFocus();
            return;
        }
        String street = metStreet.getText().toString();
        if (TextUtils.isEmpty(street)) {
            metStreet.setError("街道地址不能为空");
            metStreet.requestFocus();
            return;
        }
    }
}
