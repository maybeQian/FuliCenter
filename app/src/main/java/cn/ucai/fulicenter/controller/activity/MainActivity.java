package cn.ucai.fulicenter.controller.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.RadioButton;

import cn.ucai.fulicenter.R;

public class MainActivity extends AppCompatActivity {
    int index,currentIndex;
    RadioButton mrbNewGoods,mrbBoutique,mrbCategory,mrbCart,mrbPersonal;
    RadioButton[] rbs = new RadioButton[5];
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
    }

    private void initView() {
        mrbNewGoods= (RadioButton) findViewById(R.id.rbNewGoods);
        mrbBoutique= (RadioButton) findViewById(R.id.rbBoutique);
        mrbCategory= (RadioButton) findViewById(R.id.rbCategory);
        mrbCart= (RadioButton) findViewById(R.id.rbCart);
        mrbPersonal= (RadioButton) findViewById(R.id.rbPersonal);

        rbs[0]=mrbNewGoods;
        rbs[1]=mrbBoutique;
        rbs[2]=mrbCategory;
        rbs[3]=mrbCart;
        rbs[4]=mrbPersonal;
    }

    public void onCheckedChange(View view) {
        switch (view.getId()) {
            case R.id.rbNewGoods:
                index=0;
                break;
            case R.id.rbBoutique:
                index=1;
                break;
            case R.id.rbCategory:
                index=2;
                break;
            case R.id.rbCart:
                index=3;
                break;
            case R.id.rbPersonal:
                index=4;
                break;
        }
        if (index != currentIndex) {
            setRadioStatus();
        }
    }

    private void setRadioStatus() {
        for (int i=0;i<rbs.length;i++) {
            if (index != i) {
                rbs[i].setChecked(false);
            }else {
                rbs[i].setChecked(true);
            }
        }
        currentIndex=index;
    }

}
