package zalezone.pullrefresh.pullrefreshview.header;


import zalezone.pullrefresh.pullrefreshview.RefreshCallBack;
import zalezone.pullrefresh.pullrefreshview.indicator.Indicator;

/**
 * Created by zale on 2015/12/14.
 */
public interface HeaderInterface {

    public void onUIReset(Indicator indicator);

    public void onUIRefreshPerpare(Indicator indicator);

    public void onUIRefreshBegin(Indicator indicator);

    public void onUIRefreshComplete(Indicator indicator);

    public void onUIPositionChange(Indicator indicator, boolean isOnTouch, RefreshCallBack callBack);

}