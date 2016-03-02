package zalezone.pullrefresh.pullrefreshview.header;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.RelativeLayout;

import zalezone.pullrefresh.pullrefreshview.RefreshCallBack;
import zalezone.pullrefresh.pullrefreshview.indicator.Indicator;


/**
 * Created by zale on 15/12/20.
 */
public class BaseHeader extends RelativeLayout implements HeaderInterface{


    public BaseHeader(Context context) {
        super(context);
    }

    public BaseHeader(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public void onUIReset(Indicator indicator) {

    }

    @Override
    public void onUIRefreshPerpare(Indicator indicator) {

    }

    @Override
    public void onUIRefreshBegin(Indicator indicator) {

    }

    @Override
    public void onUIRefreshComplete(Indicator indicator) {

    }

    @Override
    public void onUIPositionChange(Indicator indicator, boolean isOnTouch, RefreshCallBack refreshCallBack) {
        Log.i("onUIPositionChange", indicator.getPullStatus() + " " + isOnTouch + indicator.getCurrentY());
        if (indicator.getPullStatus() == Indicator.STATUS_REFRESH||indicator.getPullStatus()==Indicator.STATUS_REFRESH_COMPLETE){
            return;
        }
        if (isOnTouch) {
            if (indicator.getCurrentY() < indicator.getHeaderHeight() && indicator.getPullStatus() != Indicator.STATUS_PULL) {
                indicator.setPullStatus(Indicator.STATUS_PULL);
                onUIReset(indicator);

            }
            if (indicator.getCurrentY()>=indicator.getHeaderHeight()&&indicator.getPullStatus() != Indicator.STATUS_PULL_PREPARE) {
                indicator.setPullStatus(Indicator.STATUS_PULL_PREPARE);
                onUIRefreshPerpare(indicator);
            }
        } else {
            if (indicator.getPullStatus() == Indicator.STATUS_PULL_PREPARE) {
                indicator.setPullStatus(Indicator.STATUS_REFRESH);
                onUIRefreshBegin(indicator);
                refreshCallBack.onRefreshBegin();
            }

        }
    }
}
