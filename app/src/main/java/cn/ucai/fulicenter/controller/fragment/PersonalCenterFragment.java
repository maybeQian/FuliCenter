package cn.ucai.fulicenter.controller.fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.ucai.fulicenter.R;
import cn.ucai.fulicenter.application.FuliCenterApplication;
import cn.ucai.fulicenter.model.bean.MessageBean;
import cn.ucai.fulicenter.model.bean.User;
import cn.ucai.fulicenter.model.net.IModelUser;
import cn.ucai.fulicenter.model.net.ModelUser;
import cn.ucai.fulicenter.model.net.OnCompleteListener;
import cn.ucai.fulicenter.model.utils.ImageLoader;
import cn.ucai.fulicenter.view.MFGT;

/**
 * A simple {@link Fragment} subclass.
 */
public class PersonalCenterFragment extends Fragment {


    @BindView(R.id.iv_user_avatar)
    ImageView mivUserAvatar;
    @BindView(R.id.tv_user_name)
    TextView mtvUserName;
    @BindView(R.id.tv_collect_count)
    TextView mtvCollectCount;

    IModelUser mModel;

    public PersonalCenterFragment() {
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_personal_center, container, false);
        ButterKnife.bind(this, view);
        initData();
        return view;
    }

    private void initData() {
        User user = FuliCenterApplication.getUser();
        if (user != null) {
            loadUserInfo(user);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        initData();
    }

    private void loadUserInfo(User user) {
        ImageLoader.setAvatar(ImageLoader.getAvatarUrl(user), getContext(), mivUserAvatar);
        mtvUserName.setText(user.getMuserNick());
        loadCollectCount();
    }

    private void loadCollectCount() {
        mModel = new ModelUser();
        mModel.getCollectCount(getContext(), FuliCenterApplication.getUser().getMuserName(), new OnCompleteListener<MessageBean>() {
            @Override
            public void onSuccess(MessageBean result) {
                if (result != null && result.isSuccess()) {
                    mtvCollectCount.setText(result.getMsg());
                } else {
                    mtvCollectCount.setText("0");
                }
            }

            @Override
            public void onError(String error) {
                mtvCollectCount.setText("0");
            }
        });
    }


    @OnClick({R.id.tv_center_settings, R.id.center_user_info})
    public void onClick() {
        MFGT.gotoSetting(getActivity());
    }

    @OnClick(R.id.layout_center_collect)
    public void getCollects() {
        MFGT.gotoCollects(getActivity());
    }
}
