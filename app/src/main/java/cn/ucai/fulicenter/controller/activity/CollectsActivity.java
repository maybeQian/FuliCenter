package cn.ucai.fulicenter.controller.activity;

import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.TextView;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.ucai.fulicenter.R;
import cn.ucai.fulicenter.application.FuliCenterApplication;
import cn.ucai.fulicenter.application.I;
import cn.ucai.fulicenter.model.bean.CollectBean;
import cn.ucai.fulicenter.model.bean.User;
import cn.ucai.fulicenter.model.net.IModelUser;
import cn.ucai.fulicenter.model.net.ModelUser;
import cn.ucai.fulicenter.model.net.OnCompleteListener;
import cn.ucai.fulicenter.model.utils.ConvertUtils;
import cn.ucai.fulicenter.view.MFGT;

public class CollectsActivity extends AppCompatActivity {

    @BindView(R.id.tvRefreshHint)
    TextView mtvRefreshHint;
    @BindView(R.id.rvGoods)
    RecyclerView mrvGoods;
    @BindView(R.id.srl)
    SwipeRefreshLayout mSrl;

    User user;
    IModelUser mModel;
    int pageId=1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_collects);
        ButterKnife.bind(this);
        user= FuliCenterApplication.getUser();
        if (user == null) {
            finish();
        } else {
            initData();
        }
    }

    private void initData() {
        mModel=new ModelUser();
        mModel.getCollects(this, user.getMuserName(), pageId, I.PAGE_SIZE_DEFAULT, new OnCompleteListener<CollectBean[]>() {
            @Override
            public void onSuccess(CollectBean[] result) {
                if (result != null) {
                    ArrayList<CollectBean> list = ConvertUtils.array2List(result);
                    Log.e("main", "collects.list=" + list.size());
                } else {

                }
            }

            @Override
            public void onError(String error) {

            }
        });
    }

    @OnClick(R.id.ivSettingBack)
    public void onClick() {
        MFGT.finishActivity(this);
    }
}
