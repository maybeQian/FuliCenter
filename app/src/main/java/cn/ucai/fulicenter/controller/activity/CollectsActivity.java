package cn.ucai.fulicenter.controller.activity;

import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.ucai.fulicenter.R;
import cn.ucai.fulicenter.application.FuliCenterApplication;
import cn.ucai.fulicenter.application.I;
import cn.ucai.fulicenter.controller.adapter.CollectsAdapter;
import cn.ucai.fulicenter.controller.adapter.NewGoodsAdapter;
import cn.ucai.fulicenter.model.bean.CollectBean;
import cn.ucai.fulicenter.model.bean.NewGoodsBean;
import cn.ucai.fulicenter.model.bean.User;
import cn.ucai.fulicenter.model.net.IModelUser;
import cn.ucai.fulicenter.model.net.ModelUser;
import cn.ucai.fulicenter.model.net.OnCompleteListener;
import cn.ucai.fulicenter.model.utils.CommonUtils;
import cn.ucai.fulicenter.model.utils.ConvertUtils;
import cn.ucai.fulicenter.model.utils.ImageLoader;
import cn.ucai.fulicenter.view.MFGT;
import cn.ucai.fulicenter.view.SpaceItemDecoration;

public class CollectsActivity extends AppCompatActivity {

    @BindView(R.id.tvRefreshHint)
    TextView mtvRefreshHint;
    @BindView(R.id.rvGoods)
    RecyclerView mrvGoods;
    @BindView(R.id.srl)
    SwipeRefreshLayout mSrl;

    CollectsAdapter mAdapter;
    ArrayList<CollectBean> mList;
    GridLayoutManager mLayoutManager;

    User user;
    IModelUser mModel;
    int mPageId;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_collects);
        ButterKnife.bind(this);
        user= FuliCenterApplication.getUser();
        if (user == null) {
            finish();
        } else {
            initView();
            initData();
            setListener();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        initData();
    }

    private void setListener() {
        setPullDownListener();
        setPullUpListener();
    }

    private void setPullUpListener() {
        mrvGoods.setOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                int lastPosition=mLayoutManager.findLastVisibleItemPosition();
                if (newState == RecyclerView.SCROLL_STATE_IDLE && mAdapter.isMore() && lastPosition == mAdapter.getItemCount() - 1) {
                    mPageId++;
                    downloadData(I.ACTION_PULL_UP,mPageId);
                }
            }
        });
    }

    private void setPullDownListener() {
        mSrl.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                mSrl.setRefreshing(true);
                mtvRefreshHint.setVisibility(View.VISIBLE);
                mPageId=1;
                downloadData(I.ACTION_PULL_DOWN,mPageId);
            }
        });
    }
    private void initView() {
        mSrl.setColorSchemeColors(
                getResources().getColor(R.color.google_blue),
                getResources().getColor(R.color.google_green),
                getResources().getColor(R.color.google_yellow),
                getResources().getColor(R.color.google_red) );
        mList=new ArrayList<>();
        mAdapter = new CollectsAdapter(this, mList);
        mrvGoods.setAdapter(mAdapter);
        mLayoutManager = new GridLayoutManager(this, I.COLUM_NUM);
        mrvGoods.setLayoutManager(mLayoutManager);
        mrvGoods.setHasFixedSize(true);
        mrvGoods.addItemDecoration(new SpaceItemDecoration(30));

        mLayoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
            @Override
            public int getSpanSize(int position) {
                if (position == mAdapter.getItemCount() - 1) {
                    return I.COLUM_NUM;
                }
                return 1;
            }
        });
    }

    private void initData() {
        mPageId=1;
        downloadData(I.ACTION_DOWNLOAD, mPageId);
    }

    private void downloadData(final int action, int mPageId) {
        mModel=new ModelUser();
        mModel.getCollects(this, user.getMuserName(), mPageId, I.PAGE_SIZE_DEFAULT, new OnCompleteListener<CollectBean[]>() {
            @Override
            public void onSuccess(CollectBean[] result) {
                mAdapter.setMore(result!=null && result.length>0);
                if (!mAdapter.isMore()) {
                    if (action == I.ACTION_PULL_UP) {
                        mAdapter.setFooter("没有更多数据");
                    }
                    return;
                }
                mAdapter.setFooter("上拉加载更多数据");
                ArrayList<CollectBean> list = ConvertUtils.array2List(result);
                switch (action) {
                    case I.ACTION_DOWNLOAD:
                        mAdapter.initData(list);
                        break;
                    case I.ACTION_PULL_DOWN:
                        ImageLoader.release();
                        mSrl.setRefreshing(false);
                        mtvRefreshHint.setVisibility(View.GONE);
                        mAdapter.initData(list);
                        break;
                    case I.ACTION_PULL_UP:
                        mAdapter.addData(list);
                        break;

                }
            }

            @Override
            public void onError(String error) {
                CommonUtils.showLongToast(error);
            }
        });
    }

    @OnClick(R.id.ivSettingBack)
    public void onClick() {
        MFGT.finishActivity(this);
    }
}
