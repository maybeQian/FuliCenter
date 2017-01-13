package cn.ucai.fulicenter.controller.fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.ucai.fulicenter.R;
import cn.ucai.fulicenter.controller.adapter.CategoryAdapter;
import cn.ucai.fulicenter.model.bean.CategoryChildBean;
import cn.ucai.fulicenter.model.bean.CategoryGroupBean;
import cn.ucai.fulicenter.model.net.IModelCategory;
import cn.ucai.fulicenter.model.net.ModelCategory;
import cn.ucai.fulicenter.model.net.OnCompleteListener;
import cn.ucai.fulicenter.model.utils.ConvertUtils;

/**
 * A simple {@link Fragment} subclass.
 */
public class CategoryFragment extends Fragment {


    @BindView(R.id.elv)
    ExpandableListView melv;
    @BindView(R.id.tvLoadMore)
    TextView mtvLoadMore;

    IModelCategory mModel;
    ArrayList<CategoryGroupBean> mGroupList;
    ArrayList<ArrayList<CategoryChildBean>> mChildList;
    CategoryAdapter mAdapter;
    int count = 0;

    public CategoryFragment() {
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View layout = inflater.inflate(R.layout.fragment_category, container, false);
        ButterKnife.bind(this, layout);
        initView();
        setView(false);
        initData();
        return layout;
    }

    private void initView() {
        mGroupList=new ArrayList<>();
        mChildList=new ArrayList<>();
        mAdapter = new CategoryAdapter(getContext(), mGroupList, mChildList);
        melv.setAdapter(mAdapter);
        melv.setGroupIndicator(null);
    }

    private void initData() {
        mModel = new ModelCategory();

        mModel.downData(getContext(), new OnCompleteListener<CategoryGroupBean[]>() {
            @Override
            public void onSuccess(CategoryGroupBean[] result) {
                if (result != null) {
                    setView(true);
                    ArrayList<CategoryGroupBean> list = ConvertUtils.array2List(result);
                    mGroupList.addAll(list);
                    for (int i = 0; i < list.size(); i++) {
                        mChildList.add(new ArrayList<CategoryChildBean>());
                        downloadChildData(list.get(i).getId(),i);
                    }
                } else {
                    setView(false);
                }
            }

            @Override
            public void onError(String error) {
                Toast.makeText(getContext(), error, Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void downloadChildData(int id, final int index) {
        mModel.downData(getContext(), id, new OnCompleteListener<CategoryChildBean[]>() {
            @Override
            public void onSuccess(CategoryChildBean[] result) {
                count++;
                if (result != null) {
                    ArrayList<CategoryChildBean> list = ConvertUtils.array2List(result);
//                    mChildList.add(list);
                    mChildList.set(index,list);
                }
                if (count == mGroupList.size()) {
                    mAdapter.initData(mGroupList, mChildList);
                }
            }

            @Override
            public void onError(String error) {
                Toast.makeText(getContext(), error, Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void setView(boolean hasData) {
        melv.setVisibility(hasData ? View.VISIBLE : View.GONE);
        mtvLoadMore.setVisibility(hasData ? View.GONE : View.VISIBLE);
    }

    @OnClick(R.id.tvLoadMore)
    public void onClick() {
        initData();
    }
}
