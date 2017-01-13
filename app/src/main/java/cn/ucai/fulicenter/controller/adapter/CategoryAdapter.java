package cn.ucai.fulicenter.controller.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.ucai.fulicenter.R;
import cn.ucai.fulicenter.model.bean.CategoryChildBean;
import cn.ucai.fulicenter.model.bean.CategoryGroupBean;
import cn.ucai.fulicenter.model.utils.ImageLoader;

/**
 * Created by Administrator on 2017/1/13 0013.
 */

public class CategoryAdapter extends BaseExpandableListAdapter {
    Context mContext;
    ArrayList<CategoryGroupBean> mGroupList;
    ArrayList<ArrayList<CategoryChildBean>> mChildList;

    public CategoryAdapter(Context mContext, ArrayList<CategoryGroupBean> groupList, ArrayList<ArrayList<CategoryChildBean>> childList) {
        this.mContext = mContext;
        mGroupList=new ArrayList<>();
        mGroupList.addAll(groupList);
        mChildList=new ArrayList<>();
        mChildList.addAll(childList);

    }

    @Override
    public int getGroupCount() {
        return mGroupList != null ? mGroupList.size() : 0;
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return mGroupList != null && mChildList.get(groupPosition) != null ? mChildList.get(groupPosition).size() : 0;
    }

    @Override
    public Object getGroup(int groupPosition) {
        return mGroupList.get(groupPosition);
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return mChildList.get(groupPosition).get(childPosition);
    }

    @Override
    public long getGroupId(int groupPosition) {
        return 0;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return 0;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        GroupViewHolder holder = null;
        if (convertView == null) {
            convertView = View.inflate(mContext, R.layout.item_group, null);
            holder = new GroupViewHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (GroupViewHolder) convertView.getTag();
        }
        ImageLoader.downloadImg(mContext, holder.mivGroup, mGroupList.get(groupPosition).getImageUrl());
        holder.mtvGroupName.setText(mGroupList.get(groupPosition).getName());
        holder.mivExpand.setImageResource(isExpanded ? R.mipmap.expand_off : R.mipmap.expand_on);
        return convertView;
    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        ChildViewHolder holder=null;
        if (convertView == null) {
            convertView = View.inflate(mContext, R.layout.item_child, null);
            holder = new ChildViewHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder= (ChildViewHolder) convertView.getTag();
        }
        ImageLoader.downloadImg(mContext,holder.mivChild, mChildList.get(groupPosition).get(childPosition).getImageUrl());
        holder.mtvChildName.setText(mChildList.get(groupPosition).get(childPosition).getName());
        return convertView;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return false;
    }

    public void initData(ArrayList<CategoryGroupBean> mGroupList, ArrayList<ArrayList<CategoryChildBean>> mChildList) {
        if (this.mGroupList != null ) {
            this.mGroupList.clear();
        }
        if (this.mChildList != null) {

            this.mChildList.clear();
        }
        this.mGroupList.addAll(mGroupList);
        this.mChildList.addAll(mChildList);
        notifyDataSetChanged();
    }


    static class GroupViewHolder {
        @BindView(R.id.ivGroup)
        ImageView mivGroup;
        @BindView(R.id.tvGroupName)
        TextView mtvGroupName;
        @BindView(R.id.ivExpand)
        ImageView mivExpand;

        GroupViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }

    static class ChildViewHolder {
        @BindView(R.id.ivChild)
        ImageView mivChild;
        @BindView(R.id.tvChildName)
        TextView mtvChildName;

        ChildViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
