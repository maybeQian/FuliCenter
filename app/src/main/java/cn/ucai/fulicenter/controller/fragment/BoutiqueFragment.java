package cn.ucai.fulicenter.controller.fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.ucai.fulicenter.R;
import cn.ucai.fulicenter.application.I;
import cn.ucai.fulicenter.controller.adapter.BoutiqueAdapter;
import cn.ucai.fulicenter.model.bean.BoutiqueBean;
import cn.ucai.fulicenter.model.net.IModelBoutique;
import cn.ucai.fulicenter.model.net.ModelBoutique;
import cn.ucai.fulicenter.model.net.OnCompleteListener;
import cn.ucai.fulicenter.model.utils.ConvertUtils;
import cn.ucai.fulicenter.model.utils.ImageLoader;
import cn.ucai.fulicenter.view.SpaceItemDecoration;

/**
 * A simple {@link Fragment} subclass.
 */
public class BoutiqueFragment extends Fragment {


    @BindView(R.id.tvRefreshHint)
    TextView mtvRefreshHint;
    @BindView(R.id.rvGoods)
    RecyclerView mrvBoutique;
    @BindView(R.id.srl)
    SwipeRefreshLayout mSrl;

    BoutiqueAdapter mAdapter;
    ArrayList<BoutiqueBean> mList;
    LinearLayoutManager mLayoutManager;
    IModelBoutique mModel;
    @BindView(R.id.tvLoadMore)
    TextView mtvLoadMore;

    public BoutiqueFragment() {
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_new_goods, container, false);
        ButterKnife.bind(this, view);
        initView();
        initData();
        setListener();
        return view;
    }

    private void setListener() {
        setPullDownListener();
    }

    private void setPullDownListener() {
        mSrl.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                mSrl.setRefreshing(true);
                mtvRefreshHint.setVisibility(View.VISIBLE);
                downloadData(I.ACTION_PULL_DOWN);
            }
        });
    }

    private void initData() {
        mModel = new ModelBoutique();
        downloadData(I.ACTION_DOWNLOAD);
    }

    private void downloadData(final int action) {
        mModel.downData(getContext(), new OnCompleteListener<BoutiqueBean[]>() {
            @Override
            public void onSuccess(BoutiqueBean[] result) {
                if (result != null && result.length > 0) {
                    mSrl.setVisibility(View.VISIBLE);
                    mtvLoadMore.setVisibility(View.GONE);
                    ArrayList<BoutiqueBean> list = ConvertUtils.array2List(result);
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

                    }
                } else {
                    mSrl.setVisibility(View.GONE);
                    mtvLoadMore.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onError(String error) {
                Toast.makeText(getContext(), error, Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void initView() {
        mSrl.setColorSchemeColors(
                getResources().getColor(R.color.google_blue),
                getResources().getColor(R.color.google_green),
                getResources().getColor(R.color.google_yellow),
                getResources().getColor(R.color.google_red)
        );
        mList = new ArrayList<>();
        mAdapter = new BoutiqueAdapter(getContext(), mList);
        mrvBoutique.setAdapter(mAdapter);
        mLayoutManager = new LinearLayoutManager(getContext());
        mrvBoutique.setLayoutManager(mLayoutManager);
        mrvBoutique.setHasFixedSize(true);
        mrvBoutique.addItemDecoration(new SpaceItemDecoration(15));

        mSrl.setVisibility(View.GONE);
        mtvLoadMore.setVisibility(View.GONE);
    }

    @OnClick(R.id.tvLoadMore)
    public void onClick() {
        initData();
    }
}
