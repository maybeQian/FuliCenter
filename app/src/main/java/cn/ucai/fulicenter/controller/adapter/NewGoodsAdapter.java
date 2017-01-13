package cn.ucai.fulicenter.controller.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.ucai.fulicenter.R;
import cn.ucai.fulicenter.application.I;
import cn.ucai.fulicenter.model.bean.NewGoodsBean;
import cn.ucai.fulicenter.model.utils.ImageLoader;
import cn.ucai.fulicenter.view.MFGT;

/**
 * Created by Administrator on 2017/1/11 0011.
 */

public class NewGoodsAdapter extends RecyclerView.Adapter {
    public static final int TYPE_GOODS = 0;
    public static final int TYPE_FOOTER = 1;
    Context mContext;
    ArrayList<NewGoodsBean> mList;
    String footer;
    boolean isMore;

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

    public void sortGoods(final int sortBy) {
        Collections.sort(mList, new Comparator<NewGoodsBean>() {
            @Override
            public int compare(NewGoodsBean leftBean, NewGoodsBean rightBean) {
                int result=0;
                switch (sortBy) {
                    case I.SORT_BY_ADDTIME_ASC:
                        result= (int) (leftBean.getAddTime()-rightBean.getAddTime());
                        break;
                    case I.SORT_BY_ADDTIME_DESC:
                        result= (int) (rightBean.getAddTime()-leftBean.getAddTime());
                        break;
                    case I.SORT_BY_PRICE_ASC:
                        result = parsePrice(leftBean.getCurrencyPrice()) - parsePrice(rightBean.getCurrencyPrice());
                        break;
                    case I.SORT_BY_PRICE_DESC:
                        result = parsePrice(rightBean.getCurrencyPrice()) - parsePrice(leftBean.getCurrencyPrice());
                        break;
                }
                return result;
            }
        });
        notifyDataSetChanged();
    }

    private int parsePrice(String price) {
        int p=0;
        p=Integer.valueOf(price.substring(price.indexOf("￥")+1));
        Log.i("adapter", "price>>>>>>>" + p);
        return p;
    }

    public NewGoodsAdapter(Context mContext, ArrayList<NewGoodsBean> list) {
        this.mContext = mContext;
        this.mList = list;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(mContext);
        View layout;
        switch (viewType) {
            case TYPE_GOODS:
                layout = inflater.inflate(R.layout.item_goods, null);
                return new GoodsViewHolder(layout);
            case TYPE_FOOTER:
                layout = inflater.inflate(R.layout.item_footer, null);
                return new FooterViewHolder(layout);
        }
        return null;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder parentHolder, int position) {
        if (getItemViewType(position) == TYPE_FOOTER) {
            FooterViewHolder holder= (FooterViewHolder) parentHolder;
            holder.tvFooter.setText(getFooter());
            return;
        }
        final GoodsViewHolder holder = (GoodsViewHolder) parentHolder;
        final NewGoodsBean goodsBean = mList.get(position);
        holder.tvGoodsName.setText(goodsBean.getGoodsName());
        holder.tvGoodsPrice.setText(goodsBean.getCurrencyPrice());
        ImageLoader.downloadImg(mContext, holder.ivGoodsThumb, goodsBean.getGoodsThumb());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MFGT.gotoGoodsDetail(mContext,goodsBean.getGoodsId());
            }
        });
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

    public void initData(ArrayList<NewGoodsBean> list) {
        if (list != null) {
            this.mList.clear();
        }
        addData(list);
    }

    public void addData(ArrayList<NewGoodsBean> list) {
        this.mList.addAll(list);
        notifyDataSetChanged();
    }

    static class GoodsViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.ivGoodsThumb)
        ImageView ivGoodsThumb;
        @BindView(R.id.tvGoodsName)
        TextView tvGoodsName;
        @BindView(R.id.tvGoodsPrice)
        TextView tvGoodsPrice;
        @BindView(R.id.layout_goods)
        LinearLayout layoutGoods;

        GoodsViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }

    static class FooterViewHolder extends RecyclerView.ViewHolder{
        @BindView(R.id.tvFooter)
        TextView tvFooter;

        FooterViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }
}
