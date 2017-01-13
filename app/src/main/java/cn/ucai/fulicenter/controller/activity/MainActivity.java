package cn.ucai.fulicenter.controller.activity;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.RadioButton;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.ucai.fulicenter.R;
import cn.ucai.fulicenter.controller.fragment.BoutiqueFragment;
import cn.ucai.fulicenter.controller.fragment.CategoryFragment;
import cn.ucai.fulicenter.controller.fragment.NewGoodsFragment;

public class MainActivity extends AppCompatActivity {
    int index, currentIndex;
    RadioButton[] rbs = new RadioButton[5];
    Fragment[] fragments=new Fragment[5];
    @BindView(R.id.rbNewGoods)
    RadioButton mrbNewGoods;
    @BindView(R.id.rbBoutique)
    RadioButton mrbBoutique;
    @BindView(R.id.rbCategory)
    RadioButton mrbCategory;
    @BindView(R.id.rbCart)
    RadioButton mrbCart;
    @BindView(R.id.rbPersonal)
    RadioButton mrbPersonal;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        initView();
    }

    private void initView() {
        rbs[0] = mrbNewGoods;
        rbs[1] = mrbBoutique;
        rbs[2] = mrbCategory;
        rbs[3] = mrbCart;
        rbs[4] = mrbPersonal;

        fragments[0]=new NewGoodsFragment();
        fragments[1]=new BoutiqueFragment();
        fragments[2]=new CategoryFragment();

        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.add(R.id.layout_content,fragments[0]).show(fragments[0]).commit();
    }

    public void onCheckedChange(View view) {
        switch (view.getId()) {
            case R.id.rbNewGoods:
                index = 0;
                break;
            case R.id.rbBoutique:
                index = 1;
                break;
            case R.id.rbCategory:
                index = 2;
                break;
            case R.id.rbCart:
                index = 3;
                break;
            case R.id.rbPersonal:
                index = 4;
                break;
        }
        if (index != currentIndex) {
            setFragment();
            setRadioStatus();
        }
    }

    private void setFragment() {
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        Fragment fragment = fragments[index];
        if (!fragment.isAdded()) {
            ft.add(R.id.layout_content, fragment);
        }
        ft.show(fragment).hide(fragments[currentIndex]).commit();
    }

    private void setRadioStatus() {
        for (int i = 0; i < rbs.length; i++) {
            if (index != i) {
                rbs[i].setChecked(false);
            } else {
                rbs[i].setChecked(true);
            }
        }
        currentIndex = index;
    }

}
