package cn.ucai.fulicenter.controller.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.ucai.fulicenter.R;
import cn.ucai.fulicenter.application.I;
import cn.ucai.fulicenter.controller.activity.BoutiqueChildActivity;
import cn.ucai.fulicenter.model.bean.BoutiqueBean;
import cn.ucai.fulicenter.model.utils.ImageLoader;

/**
 * Created by Administrator on 2017/1/11 0011.
 */

public class BoutiqueAdapter extends RecyclerView.Adapter {
    public static final int TYPE_GOODS = 0;
    public static final int TYPE_FOOTER = 1;
    Context mContext;
    ArrayList<BoutiqueBean> mList;
    String footer;

    View.OnClickListener onItemCLickListener;

    public String getFooter() {
        return footer;
    }

    public void setFooter(String footer) {
        this.footer = footer;
        notifyDataSetChanged();
    }


    public BoutiqueAdapter(final Context mContext, ArrayList<BoutiqueBean> list) {
        this.mContext = mContext;
        this.mList = list;

        onItemCLickListener=new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int id = (int) v.getTag(R.id.tvDescription);
                String title= (String) v.getTag(R.id.tvBoutiqueTitle);
                Intent intent=new Intent(mContext, BoutiqueChildActivity.class);
                intent.putExtra("id",id);
                intent.putExtra("title",title);
                mContext.startActivity(intent);
            }
        };
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(mContext);
        View layout;
        switch (viewType) {
            case TYPE_GOODS:
                layout = inflater.inflate(R.layout.item_botique, null);
                layout.setOnClickListener(onItemCLickListener);
                return new BoutiqueViewHolder(layout);
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
        BoutiqueViewHolder holder = (BoutiqueViewHolder) parentHolder;
        BoutiqueBean boutiqueBean = mList.get(position);
        holder.tvTitle.setText(boutiqueBean.getTitle());
        holder.tvName.setText(boutiqueBean.getName());
        holder.tvDescription.setText(boutiqueBean.getDescription());
        holder.itemView.setTag(R.id.tvDescription,boutiqueBean.getId());
        holder.itemView.setTag(R.id.tvBoutiqueTitle,boutiqueBean.getName());
        ImageLoader.downloadImg(mContext, holder.ivBoutique, boutiqueBean.getImageurl());
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

    public void initData(ArrayList<BoutiqueBean> list) {
        if (list != null) {
            this.mList.clear();
        }
        addData(list);
    }

    public void addData(ArrayList<BoutiqueBean> list) {
        this.mList.addAll(list);
        notifyDataSetChanged();
    }


    static class FooterViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.tvFooter)
        TextView tvFooter;

        FooterViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }

    static class BoutiqueViewHolder extends RecyclerView.ViewHolder{
        @BindView(R.id.ivBoutique)
        ImageView ivBoutique;
        @BindView(R.id.tvTitle)
        TextView tvTitle;
        @BindView(R.id.tvName)
        TextView tvName;
        @BindView(R.id.tvDescription)
        TextView tvDescription;

        BoutiqueViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }
}
