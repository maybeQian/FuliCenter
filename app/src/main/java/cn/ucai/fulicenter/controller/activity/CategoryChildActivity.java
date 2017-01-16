package cn.ucai.fulicenter.controller.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.ucai.fulicenter.R;
import cn.ucai.fulicenter.application.I;
import cn.ucai.fulicenter.controller.fragment.NewGoodsFragment;
import cn.ucai.fulicenter.view.CatFilterButton;
import cn.ucai.fulicenter.view.MFGT;

public class CategoryChildActivity extends AppCompatActivity {

    @BindView(R.id.ivBack)
    ImageView mivBack;
    @BindView(R.id.btnSortPrice)
    Button btnSortPrice;
    @BindView(R.id.ivPriceArrow)
    ImageView ivPriceArrow;
    @BindView(R.id.ivAddTimeArrow)
    ImageView ivAddTimeArrow;
    @BindView(R.id.btnSortAddTime)
    Button btnSortAddTime;
    @BindView(R.id.btnCategoryTitle)
    CatFilterButton mbtnCategoryTitle;
    NewGoodsFragment mNewGoodsFragment;
    boolean priceAsc = false;
    boolean addTimeAsc = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category_child);
        ButterKnife.bind(this);
        initView();
    }

    private void initView() {
        Intent intent = getIntent();
        String title = intent.getStringExtra("title");
        mbtnCategoryTitle.initCatFilterButton(title,null);

        mNewGoodsFragment = new NewGoodsFragment();
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.add(R.id.layout_content, mNewGoodsFragment).commit();
       
    }


    @OnClick(R.id.ivBack)
    public void onClick() {
        MFGT.finishActivity(this);
    }

    @OnClick({R.id.btnSortPrice, R.id.btnSortAddTime})
    public void onClick(View view) {
        int sortBy = I.SORT_BY_ADDTIME_DESC;
        switch (view.getId()) {
            case R.id.btnSortPrice:
                if (priceAsc) {
                    sortBy = I.SORT_BY_PRICE_ASC;
                } else {
                    sortBy = I.SORT_BY_PRICE_DESC;
                }
                priceAsc = !priceAsc;
                break;
            case R.id.btnSortAddTime:
                if (addTimeAsc) {
                    sortBy = I.SORT_BY_ADDTIME_ASC;
                } else {
                    sortBy = I.SORT_BY_ADDTIME_DESC;
                }
                addTimeAsc = !addTimeAsc;
                break;
        }
        mNewGoodsFragment.sortGoods(sortBy);
        ivPriceArrow.setImageResource(priceAsc ? R.drawable.arrow_order_down : R.drawable.arrow_order_up);
        ivAddTimeArrow.setImageResource(addTimeAsc ? R.drawable.arrow_order_down : R.drawable.arrow_order_up);
    }

}
