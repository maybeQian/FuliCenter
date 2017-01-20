package cn.ucai.fulicenter.controller.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnCheckedChanged;
import butterknife.OnClick;
import cn.ucai.fulicenter.R;
import cn.ucai.fulicenter.application.FuliCenterApplication;
import cn.ucai.fulicenter.application.I;
import cn.ucai.fulicenter.model.bean.CartBean;
import cn.ucai.fulicenter.model.bean.GoodsDetailsBean;
import cn.ucai.fulicenter.model.bean.MessageBean;
import cn.ucai.fulicenter.model.bean.User;
import cn.ucai.fulicenter.model.net.IModelUser;
import cn.ucai.fulicenter.model.net.ModelUser;
import cn.ucai.fulicenter.model.net.OnCompleteListener;
import cn.ucai.fulicenter.model.utils.ImageLoader;

/**
 * Created by Administrator on 2017/1/11 0011.
 */

public class CartAdapter extends RecyclerView.Adapter {
    Context mContext;
    ArrayList<CartBean> mList;
    IModelUser mModel;
    User user;

    public CartAdapter(final Context mContext, ArrayList<CartBean> list) {
        this.mContext = mContext;
        this.mList = list;
        user= FuliCenterApplication.getUser();
        mModel=new ModelUser();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(mContext);
        View layout = inflater.inflate(R.layout.item_cart, null);
        return new CartViewHolder(layout);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder parentHolder, int position) {
        CartViewHolder holder = (CartViewHolder) parentHolder;
        holder.bind(position);
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }


    public void initData(ArrayList<CartBean> list) {
        if (list != null) {
            this.mList.clear();
        }
        this.mList.addAll(list);
        notifyDataSetChanged();
    }



    class CartViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.ivGoodsThumb)
        ImageView ivGoodsThumb;
        @BindView(R.id.tvGoodsName)
        TextView tvGoodsName;
        @BindView(R.id.tvCartCount)
        TextView tvCartCount;
        @BindView(R.id.tvGoodsPrice)
        TextView tvGoodsPrice;
        @BindView(R.id.chkSelect)
        CheckBox checkBox;

        int itemPosition;

        CartViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }

        public void bind(int position) {
            itemPosition = position;
            GoodsDetailsBean goodsDetails = mList.get(position).getGoods();
            if (goodsDetails != null) {
                ImageLoader.downloadImg(mContext, ivGoodsThumb, goodsDetails.getGoodsThumb());
                tvGoodsName.setText(goodsDetails.getGoodsName());
                tvGoodsPrice.setText(goodsDetails.getCurrencyPrice());
            }
            tvCartCount.setText("(" + mList.get(position).getCount() + ")");
            checkBox.setChecked(mList.get(itemPosition).isChecked());
        }

        @OnCheckedChanged(R.id.chkSelect)
        public void checkedListener(boolean checked) {
            mList.get(itemPosition).setChecked(checked);
            mContext.sendBroadcast(new Intent(I.BROADCAST_UPDATA_CART));
        }

        @OnClick(R.id.ivAddCart)
        public void addCart() {
            mModel.addCart(mContext, user.getMuserName(), mList.get(itemPosition).getGoodsId(), 1, new OnCompleteListener<MessageBean>() {
                @Override
                public void onSuccess(MessageBean result) {
                    if (result != null && result.isSuccess()) {
                        mList.get(itemPosition).setCount(mList.get(itemPosition).getCount()+1);
                        mContext.sendBroadcast(new Intent(I.BROADCAST_UPDATA_CART));
                    }
                }

                @Override
                public void onError(String error) {

                }
            });
        }
        @OnClick(R.id.ivReduceCart)
        public void reduceCart() {
            final int count=mList.get(itemPosition).getCount();
            if (count <= 1) {
                mModel.delCart(mContext, mList.get(itemPosition).getId(), new OnCompleteListener<MessageBean>() {
                    @Override
                    public void onSuccess(MessageBean result) {
                        if (result!=null && result.isSuccess())
                            mList.remove(itemPosition);
                            mContext.sendBroadcast(new Intent(I.BROADCAST_UPDATA_CART));

                    }

                    @Override
                    public void onError(String error) {

                    }
                });
            }else {
                mModel.updateCart(mContext, mList.get(itemPosition).getId(), count - 1, new OnCompleteListener<MessageBean>() {
                    @Override
                    public void onSuccess(MessageBean result) {
                        mList.get(itemPosition).setCount(count - 1);
                        mContext.sendBroadcast(new Intent(I.BROADCAST_UPDATA_CART));
                    }

                    @Override
                    public void onError(String error) {

                    }
                });
            }
        }
    }
}
