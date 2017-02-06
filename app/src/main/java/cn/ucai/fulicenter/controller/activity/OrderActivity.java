package cn.ucai.fulicenter.controller.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.widget.EditText;
import android.widget.TextView;

import com.pingplusplus.android.PingppLog;
import com.pingplusplus.libone.PaymentHandler;
import com.pingplusplus.libone.PingppOne;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Date;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.ucai.fulicenter.R;
import cn.ucai.fulicenter.application.I;
import cn.ucai.fulicenter.view.MFGT;

public class OrderActivity extends AppCompatActivity implements PaymentHandler{

    @BindView(R.id.etReceiver)
    EditText metReceiver;
    @BindView(R.id.etPhoneNumber)
    EditText metPhoneNumber;
    @BindView(R.id.etStreet)
    EditText metStreet;
    @BindView(R.id.tv_order_price)
    TextView mtvOrderPrice;
    int payPrice;
    private static String URL = "http://218.244.151.190/demo/charge";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order);
        ButterKnife.bind(this);
        payPrice = getIntent().getIntExtra(I.Cart.PAY_PRICE, 0);
        initPingPP();
        setView();
    }

    private void initPingPP() {
        // 设置要使用的支付方式
        PingppOne.enableChannels(new String[]{"wx", "alipay", "upacp", "bfb"});
        //提交数据的格式，默认格式为json
        //PingppOne.CONTENT_TYPE = "application/x-www-form-urlencoded";
        PingppOne.CONTENT_TYPE = "application/json";
        //是否开启日志
        PingppLog.DEBUG = true;
    }

    private void setView() {
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
        showPay();
    }

    private void showPay() {
        // 产生个订单号
        String orderNo = new SimpleDateFormat("yyyyMMddhhmmss")
                .format(new Date());

        // 计算总金额（以分为单位）
        int amount = payPrice*100;
        JSONArray billList = new JSONArray();
//        for (Good good : mList) {
//            amount += good.getPrice() * good.getCount() * 100;
//            billList.put(good.getName() + " x " + good.getCount());
//        }
        // 构建账单json对象
        JSONObject bill = new JSONObject();

        // 自定义的额外信息 选填
        JSONObject extras = new JSONObject();
        try {
            extras.put("extra1", "extra1");
            extras.put("extra2", "extra2");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        try {
            bill.put("order_no", orderNo);
            bill.put("amount", amount);
            bill.put("extras", extras);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        //壹收款: 创建支付通道
        PingppOne.showPaymentChannels(getSupportFragmentManager(), bill.toString(), null, URL, new PaymentHandler() {

            // 返回支付结果
            // @param data

            @Override
            public void handlePaymentResult(Intent data) {
            }
        });
    }
    @Override
    public void handlePaymentResult(Intent data) {
        if (data != null) {
            /**
             * code：支付结果码  -2:服务端错误、 -1：失败、 0：取消、1：成功
             * error_msg：支付结果信息
             */
            int code = data.getExtras().getInt("code");
            String errorMsg = data.getExtras().getString("error_msg");
        }
    }
}
