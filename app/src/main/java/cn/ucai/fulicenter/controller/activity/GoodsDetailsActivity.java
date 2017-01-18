package cn.ucai.fulicenter.controller.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.ucai.fulicenter.R;
import cn.ucai.fulicenter.application.FuliCenterApplication;
import cn.ucai.fulicenter.application.I;
import cn.ucai.fulicenter.model.bean.AlbumsBean;
import cn.ucai.fulicenter.model.bean.GoodsDetailsBean;
import cn.ucai.fulicenter.model.bean.MessageBean;
import cn.ucai.fulicenter.model.bean.User;
import cn.ucai.fulicenter.model.net.IModelGoodsDetail;
import cn.ucai.fulicenter.model.net.ModelGoodsDetail;
import cn.ucai.fulicenter.model.net.OnCompleteListener;
import cn.ucai.fulicenter.model.utils.CommonUtils;
import cn.ucai.fulicenter.view.FlowIndicator;
import cn.ucai.fulicenter.view.MFGT;
import cn.ucai.fulicenter.view.SlideAutoLoopView;

public class GoodsDetailsActivity extends AppCompatActivity {

    @BindView(R.id.ivBack)
    ImageView mivBack;
    @BindView(R.id.ivShare)
    ImageView mivShare;
    @BindView(R.id.ivCollect)
    ImageView mivCollect;
    @BindView(R.id.ivCart)
    ImageView mivCart;
    @BindView(R.id.goods_detail_English_name)
    TextView mtvGoodsEnglishName;
    @BindView(R.id.goods_detail_name)
    TextView mtvGoodsName;
    @BindView(R.id.goods_detail_price)
    TextView mtvGoodsCurrentPrice;
    @BindView(R.id.salv)
    SlideAutoLoopView mSalv;
    @BindView(R.id.indicator)
    FlowIndicator mIndicator;
    @BindView(R.id.webView)
    WebView mWebView;

    int goodsId;
    IModelGoodsDetail mModel;

    boolean isCollect;
    @BindView(R.id.layout_image)
    RelativeLayout layoutImage;
    @BindView(R.id.activity_goods_details)
    LinearLayout activityGoodsDetails;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_goods_details);
        ButterKnife.bind(this);

        goodsId = getIntent().getIntExtra("goodsId", 0);
        if (goodsId == 0) {
            MFGT.finishActivity(this);
        } else {
            initData();
        }
    }

    private void initData() {
        mModel = new ModelGoodsDetail();
        mModel.downData(this, goodsId, new OnCompleteListener<GoodsDetailsBean>() {
            @Override
            public void onSuccess(GoodsDetailsBean result) {
                showGoodsDetail(result);
            }

            @Override
            public void onError(String error) {
                Toast.makeText(GoodsDetailsActivity.this, error, Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void showGoodsDetail(GoodsDetailsBean goods) {
        mtvGoodsEnglishName.setText(goods.getGoodsEnglishName());
        mtvGoodsName.setText(goods.getGoodsName());
        mtvGoodsCurrentPrice.setText(goods.getCurrencyPrice());
        mSalv.startPlayLoop(mIndicator, getAlbumUrl(goods), getAlbumCount(goods));
        mWebView.loadDataWithBaseURL(null, goods.getGoodsBrief(), I.TEXT_HTML, I.UTF_8, null);
    }

    private int getAlbumCount(GoodsDetailsBean goods) {
        if (goods != null && goods.getProperties() != null && goods.getProperties().length > 0) {
            return goods.getProperties()[0].getAlbums().length;
        }
        return 0;
    }

    private String[] getAlbumUrl(GoodsDetailsBean goods) {
        if (goods != null && goods.getProperties() != null && goods.getProperties().length > 0) {
            AlbumsBean[] albums = goods.getProperties()[0].getAlbums();
            if (albums != null && albums.length > 0) {
                String[] urls = new String[albums.length];
                for (int i = 0; i < albums.length; i++) {
                    urls[i] = albums[i].getImgUrl();
                }
                return urls;
            }
        }
        return new String[0];
    }

    @Override
    protected void onResume() {
        super.onResume();
        initCollectStatus();
    }

    private void initCollectStatus() {
        User user = FuliCenterApplication.getUser();
        if (user != null) {
            mModel.isCollect(this, goodsId, user.getMuserName(), new OnCompleteListener<MessageBean>() {
                @Override
                public void onSuccess(MessageBean result) {
                    if (result != null && result.isSuccess()) {
                        isCollect = true;
                    } else {
                        isCollect = false;
                    }
                    setCollectStatus();
                }

                @Override
                public void onError(String error) {
                    isCollect = false;
                    setCollectStatus();
                }
            });

        }

    }

    private void setCollectStatus() {
        if (isCollect) {
            mivCollect.setImageResource(R.mipmap.bg_collect_out);
        } else {
            mivCollect.setImageResource(R.mipmap.bg_collect_in);
        }
    }

    @OnClick(R.id.ivBack)
    public void onClick() {
        MFGT.finishActivity(this);
    }

    @OnClick(R.id.ivCollect)
    public void setCollectListener() {
        User user=FuliCenterApplication.getUser();
        if (user != null) {
            setCollect(user);
        } else {
            MFGT.gotoLogin(this);
        }
    }

    private void setCollect(User user) {
        mModel.setCollect(this, goodsId, user.getMuserName(),
                isCollect ? I.ACTION_DELETE_COLLECT : I.ACTION_ADD_COLLECT, new OnCompleteListener<MessageBean>() {
                    @Override
                    public void onSuccess(MessageBean result) {
                        if (result != null && result.isSuccess()) {
                            isCollect=!isCollect;
                            setCollectStatus();
                            CommonUtils.showShortToast(result.getMsg());
                        }
                    }

                    @Override
                    public void onError(String error) {
                        CommonUtils.showLongToast(error);
                        Log.e("main", "setCollect,error=" + error);
                    }
                });
    }


}
