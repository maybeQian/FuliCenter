package cn.ucai.fulicenter.controller.fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.ucai.fulicenter.R;
import cn.ucai.fulicenter.application.I;
import cn.ucai.fulicenter.controller.adapter.NewGoodsAdapter;
import cn.ucai.fulicenter.model.bean.NewGoodsBean;
import cn.ucai.fulicenter.model.net.IModelNewGoods;
import cn.ucai.fulicenter.model.net.ModelNewGoods;
import cn.ucai.fulicenter.model.net.OnCompleteListener;
import cn.ucai.fulicenter.model.utils.ConvertUtils;
import cn.ucai.fulicenter.model.utils.ImageLoader;
import cn.ucai.fulicenter.model.utils.L;
import cn.ucai.fulicenter.view.SpaceItemDecoration;

/**
 * A simple {@link Fragment} subclass.
 */
public class NewGoodsFragment extends Fragment {
    static final int ACTION_DOWNLOAD=0;
    static final int ACTION_PULL_DOWN=1;
    static final int ACTION_PULL_UP=2;

    @BindView(R.id.tvRefreshHint)
    TextView mtvRefreshHint;
    @BindView(R.id.rvGoods)
    RecyclerView mrvGoods;
    @BindView(R.id.srl)
    SwipeRefreshLayout msrl;

    NewGoodsAdapter mAdapter;
    ArrayList<NewGoodsBean> mList=new ArrayList<>();
    GridLayoutManager mLayoutManager;
    IModelNewGoods mModel;
    int mPageId;
    public NewGoodsFragment() {
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_new_goods, container, false);
        ButterKnife.bind(this, view);

        initView();
        mModel=new ModelNewGoods();
        initData();
        setListener();
        return view;
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
                    downloadData(ACTION_PULL_UP,mPageId);
                }
            }
        });
    }

    private void setPullDownListener() {
        msrl.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                msrl.setRefreshing(true);
                mtvRefreshHint.setVisibility(View.VISIBLE);
                mPageId=1;
                downloadData(ACTION_PULL_DOWN,mPageId);
            }
        });
    }

    private void initData() {
       mPageId=1;
        downloadData(ACTION_DOWNLOAD,mPageId);
    }

    private void downloadData(final int action, int pageId) {
        mModel.downData(getContext(), I.CAT_ID, pageId, new OnCompleteListener<NewGoodsBean[]>() {
            @Override
            public void onSuccess(NewGoodsBean[] result) {
                mAdapter.setMore(result!=null && result.length>0);
                if (!mAdapter.isMore()) {
                    if (action == ACTION_PULL_UP) {
                        mAdapter.setFooter("没有更多数据");
                    }
                    return;
                }
                mAdapter.setFooter("上拉加载更多数据");
                ArrayList<NewGoodsBean> list = ConvertUtils.array2List(result);
                switch (action) {
                    case ACTION_DOWNLOAD:
                        mAdapter.initData(list);
                        break;
                    case ACTION_PULL_DOWN:
                        ImageLoader.release();
                        msrl.setRefreshing(false);
                        mtvRefreshHint.setVisibility(View.GONE);
                        mAdapter.initData(list);
                        break;
                    case ACTION_PULL_UP:
                        mAdapter.addData(list);
                        break;

                }
            }

            @Override
            public void onError(String error) {
                Toast.makeText(getContext(), error, Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void initView() {
        msrl.setColorSchemeColors(
                getResources().getColor(R.color.google_blue),
                getResources().getColor(R.color.google_green),
                getResources().getColor(R.color.google_yellow),
                getResources().getColor(R.color.google_red) );
        mAdapter = new NewGoodsAdapter(getContext(), mList);
        mrvGoods.setAdapter(mAdapter);
        mLayoutManager = new GridLayoutManager(getContext(), I.COLUM_NUM);
        mrvGoods.setLayoutManager(mLayoutManager);
        mrvGoods.setHasFixedSize(true);
        mrvGoods.addItemDecoration(new SpaceItemDecoration(30));
    }

}
