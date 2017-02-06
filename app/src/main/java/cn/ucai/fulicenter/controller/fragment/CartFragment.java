package cn.ucai.fulicenter.controller.fragment;


import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.ucai.fulicenter.R;
import cn.ucai.fulicenter.application.FuliCenterApplication;
import cn.ucai.fulicenter.application.I;
import cn.ucai.fulicenter.controller.adapter.CartAdapter;
import cn.ucai.fulicenter.model.bean.CartBean;
import cn.ucai.fulicenter.model.bean.GoodsDetailsBean;
import cn.ucai.fulicenter.model.bean.User;
import cn.ucai.fulicenter.model.net.IModelUser;
import cn.ucai.fulicenter.model.net.ModelUser;
import cn.ucai.fulicenter.model.net.OnCompleteListener;
import cn.ucai.fulicenter.model.utils.CommonUtils;
import cn.ucai.fulicenter.model.utils.ConvertUtils;
import cn.ucai.fulicenter.model.utils.ImageLoader;
import cn.ucai.fulicenter.view.MFGT;
import cn.ucai.fulicenter.view.SpaceItemDecoration;

/**
 * A simple {@link Fragment} subclass.
 */
public class CartFragment extends Fragment {


    @BindView(R.id.tvRefreshHint)
    TextView mtvRefreshHint;
    @BindView(R.id.tv_nothing)
    TextView mtvNothing;
    @BindView(R.id.rv)
    RecyclerView mrv;
    @BindView(R.id.srl)
    SwipeRefreshLayout msrl;
    @BindView(R.id.tv_cart_buy)
    TextView mtvCartBuy;
    @BindView(R.id.tv_cart_sum_price)
    TextView mtvCartSumPrice;
    @BindView(R.id.tv_cart_save_price)
    TextView mtvCartSavePrice;

    CartAdapter mAdapter;
    ArrayList<CartBean> mList = new ArrayList<>();
    LinearLayoutManager mLayoutManager;
    IModelUser mModel;
    User user;
    UpdateCartReceiver mReceiver;
    int sumPrice;
    int payPrice;
    public CartFragment() {
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_cart, container, false);
        ButterKnife.bind(this, view);
        initView();
        initData(I.ACTION_DOWNLOAD);
        setListener();
        registerMyReceiver();
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        initData(I.ACTION_DOWNLOAD);
    }

    private void registerMyReceiver() {
        mReceiver = new UpdateCartReceiver();
        IntentFilter filter = new IntentFilter(I.BROADCAST_UPDATA_CART);
        getActivity().registerReceiver(mReceiver, filter);
    }

    private void setListener() {
        msrl.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                msrl.setRefreshing(true);
                mtvRefreshHint.setVisibility(View.VISIBLE);
                initData(I.ACTION_PULL_DOWN);
            }
        });
    }

    private void initData(final int action) {
        user = FuliCenterApplication.getUser();
        mModel = new ModelUser();
        if (user != null) {
            mModel.getCart(getContext(), user.getMuserName(), new OnCompleteListener<CartBean[]>() {
                @Override
                public void onSuccess(CartBean[] result) {
                    if (result != null && result.length > 0) {
                        mtvNothing.setVisibility(View.GONE);
                        ArrayList<CartBean> list = ConvertUtils.array2List(result);
                        mList.addAll(list);
                        switch (action) {
                            case I.ACTION_DOWNLOAD:
                                mAdapter.initData(list);
                                break;
                            case I.ACTION_PULL_DOWN:
                                ImageLoader.release();
                                msrl.setRefreshing(false);
                                mtvRefreshHint.setVisibility(View.GONE);
                                mAdapter.initData(list);
                                break;
                        }
                    }
                }

                @Override
                public void onError(String error) {

                }
            });
        }
    }

    private void initView() {
        msrl.setColorSchemeColors(
                getResources().getColor(R.color.google_blue),
                getResources().getColor(R.color.google_green),
                getResources().getColor(R.color.google_yellow),
                getResources().getColor(R.color.google_red)
        );
        mAdapter = new CartAdapter(getContext(), mList);
        mrv.setAdapter(mAdapter);
        mLayoutManager = new LinearLayoutManager(getContext());
        mrv.setLayoutManager(mLayoutManager);
        mrv.setHasFixedSize(true);
        mrv.addItemDecoration(new SpaceItemDecoration(15));
    }

    private void setPrice() {
        sumPrice = 0;
        payPrice=0;
        int savePrice = 0;
        if (mList != null && mList.size() > 0) {
            for (CartBean cartBean : mList) {
                GoodsDetailsBean goods = cartBean.getGoods();
                if (cartBean.isChecked() && goods != null) {
                    sumPrice += cartBean.getCount() * parsePrice(goods.getCurrencyPrice());
                    savePrice += cartBean.getCount() * (parsePrice(goods.getCurrencyPrice()) - parsePrice(goods.getRankPrice()));
                }
            }
        }
        payPrice=sumPrice-savePrice;
        mtvCartSumPrice.setText(sumPrice + "");
        mtvCartSavePrice.setText(savePrice + "");
        mAdapter.notifyDataSetChanged();
    }

    int parsePrice(String price) {
        int p = 0;
        p = Integer.valueOf(price.substring(price.indexOf("￥") + 1));
        return p;
    }

    @OnClick(R.id.tv_cart_buy)
    public void onOrderClick() {
        if (sumPrice > 0) {
            MFGT.gotoOrder(getActivity(), payPrice);
        } else {
            CommonUtils.showLongToast(R.string.order_nothing);
        }
    }

    class UpdateCartReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            setPrice();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        getActivity().unregisterReceiver(mReceiver);
    }
}
