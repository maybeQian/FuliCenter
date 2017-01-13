package cn.ucai.fulicenter.controller.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.ucai.fulicenter.R;
import cn.ucai.fulicenter.application.I;
import cn.ucai.fulicenter.controller.fragment.NewGoodsFragment;
import cn.ucai.fulicenter.view.MFGT;

public class CategoryChildActivity extends AppCompatActivity {

    @BindView(R.id.ivBack)
    ImageView mivBack;
    @BindView(R.id.tvCategoryTitle)
    TextView mtvCategoryTitle;
    @BindView(R.id.btnSortPrice)
    Button btnSortPrice;
    @BindView(R.id.ivPriceArrow)
    ImageView ivPriceArrow;
    @BindView(R.id.ivAddTimeArrow)
    ImageView ivAddTimeArrow;
    @BindView(R.id.btnSortAddTime)
    Button btnSortAddTime;

    NewGoodsFragment mNewGoodsFragment;
    boolean priceAsc=false;
    boolean addTimeAsc=false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category_child);
        ButterKnife.bind(this);
        initView();
    }

    private void initView() {
        mNewGoodsFragment=new NewGoodsFragment();
        Intent intent = getIntent();
        String title = intent.getStringExtra("title");
        mtvCategoryTitle.setText(title);

        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.add(R.id.layout_content,mNewGoodsFragment).commit();
    }

    @OnClick(R.id.ivBack)
    public void onClick() {
        MFGT.finishActivity(this);
    }


}
