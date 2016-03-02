package zalezone.pullrefresh.pullrefreshview.indicator;

import android.util.Log;

/**
 * Created by zale on 2015/12/8.
 */
public class Indicator {



    public static final int STATUS_INITIAL = 0;//初始状态
    public static final int STATUS_PULL = 1;//下拉状态
    public static final int STATUS_PULL_PREPARE = 2;//下拉释放刷新状态
    public static final int STATUS_REFRESH = 3;//刷新状态
    public static final int STATUS_REFRESH_COMPLETE = 4;//刷新状态

    private int pullStatus;


    private float mOffsetX;
    private float mOffsetY;

    private int mLastPosX = 0;
    private int mLastPosY = 0;

    private int mCurrentY = 0;

    private boolean mIsUnderTouch = false;

    private int mHeaderHeight;

    public int getCurrentY() {
        return mCurrentY;
    }

    public void setCurrentY(int mCurrentY) {
        this.mCurrentY = mCurrentY;
    }

    private void setOffset(float x, float y) {
        mOffsetX = x - mLastPosX;
        mOffsetY = y - mLastPosY;
    }

    public void onMove(float x, float y) {
        Log.i("indicator", "move");
        setOffset(x, y);
        setLastPos(x, y);

    }

    public float getOffsetY() {
        return mOffsetY;
    }


    public void setOffsetY(float offsetY) {
        mOffsetY = offsetY;
    }

    public void setLastPos(float x, float y) {
        mLastPosX = (int) x;
        mLastPosY = (int) y;
    }

    public void setHeaderHeight(int height) {
        mHeaderHeight = height;
    }

    public int getHeaderHeight() {
        return mHeaderHeight;
    }


    public int getPullStatus() {
        return pullStatus;
    }

    public void setPullStatus(int pullStatus) {
        this.pullStatus = pullStatus;
    }
}
