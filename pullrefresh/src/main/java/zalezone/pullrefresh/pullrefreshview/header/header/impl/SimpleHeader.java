package zalezone.pullrefresh.pullrefreshview.header.header.impl;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;
import android.widget.ImageView;
import android.widget.TextView;

import zalezone.pullrefresh.R;
import zalezone.pullrefresh.pullrefreshview.header.BaseHeader;
import zalezone.pullrefresh.pullrefreshview.indicator.Indicator;


/**
 * Created by zale on 2015/12/9.
 */
public class SimpleHeader extends BaseHeader {


    private TextView stateTV;
    private ImageView arrowIV;

    private Animation rotateAn;


    public SimpleHeader(Context context) {
        this(context, null);
    }

    public SimpleHeader(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    private void init(Context context) {
        View headView = LayoutInflater.from(context).inflate(R.layout.item_simple_headview, this);
        stateTV = (TextView) headView.findViewById(R.id.state);
        arrowIV = (ImageView) headView.findViewById(R.id.arrow_image);

        rotateAn = AnimationUtils.loadAnimation(context,R.anim.pull_refresh_arrow_anim);
        LinearInterpolator linearInterpolator = new LinearInterpolator();
        rotateAn.setInterpolator(linearInterpolator);
    }


    @Override
    public void onUIReset(Indicator indicator) {
        stateTV.setText("下拉刷新");
    }

    @Override
    public void onUIRefreshPerpare(Indicator indicator) {
        stateTV.setText("释放以刷新");
    }

    @Override
    public void onUIRefreshBegin(Indicator indicator) {
        stateTV.setText("正在刷新");
        if (rotateAn!=null){
            arrowIV.startAnimation(rotateAn);
        }
    }

    @Override
    public void onUIRefreshComplete(Indicator indicator) {
        stateTV.setText("更新完成");
        arrowIV.clearAnimation();
    }

}
