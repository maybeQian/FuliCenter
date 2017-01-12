package cn.ucai.fulicenter.controller.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.ucai.fulicenter.R;
import cn.ucai.fulicenter.controller.fragment.NewGoodsFragment;

public class BoutiqueChildActivity extends AppCompatActivity {

    @BindView(R.id.tvBoutiqueTitle)
    TextView tvBoutiqueTitle;
    @BindView(R.id.ivBack)
    ImageView ivBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_boutique_child);
        ButterKnife.bind(this);
        initView();

    }

    private void initView() {
        Intent intent = getIntent();
        String title = intent.getStringExtra("title");
        tvBoutiqueTitle.setText(title);


        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.add(R.id.layout_content, new NewGoodsFragment()).commit();
    }

    @OnClick(R.id.ivBack)
    public void onClick() {
        onBackPressed();
    }
}
