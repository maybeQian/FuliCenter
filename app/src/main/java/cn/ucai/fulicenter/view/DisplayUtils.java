package cn.ucai.fulicenter.view;

import android.app.Activity;
import android.view.View;
import android.widget.TextView;

import cn.ucai.fulicenter.R;

/**
 * Created by Administrator on 2017/1/17 0017.
 */

public class DisplayUtils {
    public static void initBack(final Activity activity) {
        activity.findViewById(R.id.ivBack).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                activity.finish();
            }
        });
    }

    public static void initBackWithTitle(Activity activity, String title) {
        TextView textView = (TextView) activity.findViewById(R.id.tvTitle);
        textView.setText(title);
        initBack(activity);
    }
}
