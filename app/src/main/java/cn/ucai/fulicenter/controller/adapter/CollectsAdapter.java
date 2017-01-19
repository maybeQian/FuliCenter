package cn.ucai.fulicenter.controller.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.ucai.fulicenter.R;
import cn.ucai.fulicenter.application.FuliCenterApplication;
import cn.ucai.fulicenter.application.I;
import cn.ucai.fulicenter.model.bean.CollectBean;
import cn.ucai.fulicenter.model.bean.MessageBean;
import cn.ucai.fulicenter.model.bean.User;
import cn.ucai.fulicenter.model.net.IModelGoodsDetail;
import cn.ucai.fulicenter.model.net.IModelUser;
import cn.ucai.fulicenter.model.net.ModelGoodsDetail;
import cn.ucai.fulicenter.model.net.OnCompleteListener;
import cn.ucai.fulicenter.model.utils.ImageLoader;
import cn.ucai.fulicenter.view.MFGT;

/**
 * Created by Administrator on 2017/1/19 0019.
 */

public class CollectsAdapter extends RecyclerView.Adapter {
    public static final int TYPE_GOODS = 0;
    public static final int TYPE_FOOTER = 1;
    Context mContext;
    ArrayList<CollectBean> mList;
    String footer;
    boolean isMore;

    IModelGoodsDetail mModel;
    User user;
    public String getFooter() {
        return footer;
    }

    public void setFooter(String footer) {
        this.footer = footer;
        notifyDataSetChanged();
    }

    public boolean isMore() {
        return isMore;
    }

    public void setMore(boolean more) {
        isMore = more;
        notifyDataSetChanged();
    }

    public CollectsAdapter(Context mContext, ArrayList<CollectBean> list) {
        this.mContext = mContext;
        this.mList = list;
        mModel=new ModelGoodsDetail();
        user= FuliCenterApplication.getUser();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(mContext);
        View layout;
        switch (viewType) {
            case TYPE_GOODS:
                layout = inflater.inflate(R.layout.item_collects, null);
                return new CollectsViewHolder(layout);
            case TYPE_FOOTER:
                layout = inflater.inflate(R.layout.item_footer, null);
                return new FooterViewHolder(layout);
        }
        return null;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder parentHolder, int position) {
        if (getItemViewType(position) == TYPE_FOOTER) {
            FooterViewHolder holder = (FooterViewHolder) parentHolder;
            holder.tvFooter.setText(getFooter());
            return;
        }
        final CollectsViewHolder holder = (CollectsViewHolder) parentHolder;
        holder.bind(position);
    }

    @Override
    public int getItemCount() {
        return mList.size() + 1;
    }

    @Override
    public int getItemViewType(int position) {
        if (position == getItemCount() - 1) {
            return TYPE_FOOTER;
        }
        return TYPE_GOODS;
    }

    public void initData(ArrayList<CollectBean> list) {
        if (list != null) {
            this.mList.clear();
        }
        addData(list);
    }

    public void addData(ArrayList<CollectBean> list) {
        this.mList.addAll(list);
        notifyDataSetChanged();
    }

    public void removeItem(int goodsId) {
        if (goodsId != 0) {
            mList.remove(new CollectBean(goodsId));
            notifyDataSetChanged();
        }
    }


    static class FooterViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.tvFooter)
        TextView tvFooter;

        FooterViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }

    class CollectsViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.ivGoodsThumb)
        ImageView ivGoodsThumb;
        @BindView(R.id.ivCollectsDelete)
        ImageView ivCollectsDelete;
        @BindView(R.id.tvGoodsName)
        TextView tvGoodsName;
        @BindView(R.id.layout_goods)
        LinearLayout layoutGoods;

        int itemPosition;

        CollectsViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }

        public void bind(int position) {
            final CollectBean collectBean = mList.get(position);
            tvGoodsName.setText(collectBean.getGoodsName());
            ImageLoader.downloadImg(mContext, ivGoodsThumb, collectBean.getGoodsThumb());
            itemPosition=position;
        }

        @OnClick({R.id.ivCollectsDelete, R.id.layout_goods})
        public void onClick(View view) {
            switch (view.getId()) {
                case R.id.ivCollectsDelete:
                    mModel.setCollect(mContext, mList.get(itemPosition).getGoodsId(),
                            user.getMuserName(), I.ACTION_DELETE_COLLECT, new OnCompleteListener<MessageBean>() {
                                @Override
                                public void onSuccess(MessageBean result) {
                                    if (result != null && result.isSuccess()) {
                                        mList.remove(itemPosition);
                                        notifyDataSetChanged();
                                    }
                                }

                                @Override
                                public void onError(String error) {

                                }
                            });
                    break;
                case R.id.layout_goods:
                    MFGT.gotoGoodsDetail(mContext,mList.get(itemPosition).getGoodsId());
                    break;
            }
        }
    }
}
