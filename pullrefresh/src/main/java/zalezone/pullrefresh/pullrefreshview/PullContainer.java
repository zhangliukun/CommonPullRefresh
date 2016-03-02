package zalezone.pullrefresh.pullrefreshview;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.widget.Scroller;

import zalezone.pullrefresh.R;
import zalezone.pullrefresh.pullrefreshview.header.HeaderInterface;
import zalezone.pullrefresh.pullrefreshview.indicator.Indicator;
import zalezone.pullrefresh.pullrefreshview.util.ViewUtil;

/**
 * Created by zale on 2015/12/8.
 */
public class PullContainer extends ViewGroup {


    private View mContentView;
    private View mHeaderView;
    private static final boolean DEBUG_LAYOUT = false;
    public static boolean DEBUG = false;
    private static int ID = 1;
    protected final String LOG_TAG = "zale-frame-" + ++ID;
    protected final String TOUCH_TAG = "Touch-Event";

    private Indicator indicator;
    private Scroller mScroller;
    private HeaderInterface headerInterface;
    private RefreshCallBack refreshCallBack;

    private float pullResistance = 1.7f;
    private int pullCloseDuration = 200;
    private int headerCloseDuration = 1000;

    private boolean isOnTouch = false;

    int contentMarginTop = 0;


    private boolean mHasSendCancelEvent = false;//是否已经发送了取消事件
    private boolean mHasSendDownEvent = false;//是否已经发送了点击事件

    private MotionEvent mLastMotionEvent;

    public PullContainer(Context context) {
        this(context, null);
    }

    public PullContainer(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    private void init(Context context) {
        indicator = new Indicator();
        mScroller = new Scroller(context);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        if (mHeaderView != null) {
            measureChild(mHeaderView, widthMeasureSpec, heightMeasureSpec);
            indicator.setHeaderHeight(mHeaderView.getMeasuredHeight());
            Log.i("BaseContainer:", mHeaderView.getMeasuredHeight() + ":" + mHeaderView.getHeight());
        }

        if (mContentView != null) {
            measureChild(mContentView, widthMeasureSpec, heightMeasureSpec);
        }

    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        layoutChildren();
    }

    private void layoutChildren() {

        int offsetY = indicator.getCurrentY();

        int paddingLeft = getPaddingLeft();
        int paddingTop = getPaddingTop();

        if (mHeaderView != null) {
            PullContainer.MyLayoutParams lp = (MyLayoutParams) mHeaderView.getLayoutParams();
            //LayoutParams lp = mHeaderView.getLayoutParams();
            final int left = paddingLeft;
            final int top = paddingTop + offsetY - mHeaderView.getMeasuredHeight();
            final int right = left + mHeaderView.getMeasuredWidth();
            final int bottom = top + mHeaderView.getMeasuredHeight();
            if (DEBUG && DEBUG_LAYOUT) {
                Log.i(LOG_TAG, String.format("onLayout header: %s %s %s %s", left, top, right, bottom));
            }
            mHeaderView.layout(left, top, right, bottom);
        }

        if (mContentView != null) {
            PullContainer.MyLayoutParams lp = (MyLayoutParams) mContentView.getLayoutParams();
            final int left = paddingLeft;
            final int top = paddingTop + offsetY;
            final int right = left + mContentView.getMeasuredWidth();
            final int bottom = top + mContentView.getMeasuredHeight();
            if (DEBUG && DEBUG_LAYOUT) {
                Log.i(LOG_TAG, String.format("onLayout content: %s %s %s %s", left, top, right, bottom));
            }
            mContentView.layout(left, top, right, bottom);
        }
    }

    @Override
    protected void onFinishInflate() {
        final int childCount = getChildCount();
        if (childCount == 1) {
            mContentView = getChildAt(0);
            //set the contentView clickable that won't let the down event disappear
            mContentView.setClickable(true);
        }else {
            throw new IllegalStateException("the BaseContainer must contain one content view");
        }
        super.onFinishInflate();
    }

    @Override
    public void computeScroll() {
        super.computeScroll();
        if (mScroller.computeScrollOffset()) {
//            Log.i("getOffsetY,CurrY,diff", indicator.getOffsetY()+","+mScroller.getCurrY()+" "+(indicator.getOffsetY() - mScroller.getCurrY()));
            updateView(indicator.getOffsetY() - mScroller.getCurrY());
            indicator.setOffsetY(mScroller.getCurrY());
            invalidate();
        } else {

            if (indicator.getPullStatus() == Indicator.STATUS_REFRESH_COMPLETE) {

                if (mContentView.getTop() <= 0) {
                    Log.i(TOUCH_TAG, "更新完成，设置为initial");
                    indicator.setPullStatus(Indicator.STATUS_INITIAL);
                }
            }
        }
    }


        @Override
        public boolean dispatchTouchEvent (MotionEvent event){
            int action = event.getAction();
            switch (action) {
                case MotionEvent.ACTION_DOWN:


                    isOnTouch = true;
                    mScroller.forceFinished(true);
                    mLastMotionEvent = event;
                    Log.i("press down lo", event.getX() + " " + event.getY() + " ");

                    indicator.setCurrentY(mContentView.getTop());
                    indicator.setLastPos(event.getX(), event.getY());

                    if (checkIsBeingDraged()) {
                        Log.i("isBeingDraged", String.valueOf(checkIsBeingDraged()));
                        return true;
                    }

                    return super.dispatchTouchEvent(event);
                case MotionEvent.ACTION_MOVE:

                    if (!mScroller.isFinished()){
                        mScroller.forceFinished(true);
                    }

                    if (!mHasSendCancelEvent && indicator.getOffsetY() > 0 && checkIsBeingDraged()) {
                        Log.i("sendCancelEvent", String.valueOf(checkIsBeingDraged()));
                        sendCancelEvent();
                    }


                    indicator.onMove(event.getX(), event.getY());

                    Log.i("getOffsetY,CanPullDown", indicator.getOffsetY() + " " + ViewUtil.checkCanPullDown(mContentView));
                    if (indicator.getOffsetY() >= 0 && ViewUtil.checkCanPullDown(mContentView)) {
                        mHasSendDownEvent = false;
                        updateView(indicator.getOffsetY()/pullResistance);
                        headerInterface.onUIPositionChange(indicator, isOnTouch, refreshCallBack);
                        return true;
                    }
                    //最后直接到达顶部
                    if (indicator.getOffsetY() < 0 && checkIsBeingDraged()) {
                        if (-indicator.getOffsetY() > contentMarginTop) {
                            updateView(-contentMarginTop);
                            headerInterface.onUIPositionChange(indicator, isOnTouch, refreshCallBack);
                        } else {
                            updateView(indicator.getOffsetY());
                            headerInterface.onUIPositionChange(indicator, isOnTouch, refreshCallBack);
                        }
                        return true;
                    }
                    if (!mHasSendDownEvent && !checkIsBeingDraged()) {
                        Log.i("sendDownEvent", indicator.getOffsetY() + " " + checkIsBeingDraged());
                        mHasSendDownEvent = true;
                        sendDownEvent();
                    }
                    //Log.i("action_move_disp","dispatchevent");
                    return super.dispatchTouchEvent(event);

                case MotionEvent.ACTION_CANCEL:
                case MotionEvent.ACTION_UP:
                    Log.i(TOUCH_TAG, "touchevent up");

                    mHasSendCancelEvent = false;
                    mHasSendDownEvent = false;
                    isOnTouch = false;
                    indicator.setLastPos(event.getX(), event.getY());
                    headerInterface.onUIPositionChange(indicator, isOnTouch, refreshCallBack);
//                    Log.i(TOUCH_TAG, "touchup pullStatus " + indicator.getPullStatus());
                    if (indicator.getPullStatus() == Indicator.STATUS_REFRESH&&indicator.getCurrentY()>indicator.getHeaderHeight()) {
                        //立即刷新回原处
                        //updateView(-(indicator.getCurrentY() - indicator.getHeaderHeight()));
                        //缓慢刷新回原处
                        resetLocation(indicator.getCurrentY() - indicator.getHeaderHeight(),indicator.getHeaderHeight(),pullCloseDuration);
                        indicator.setCurrentY(mContentView.getTop());
                    }
                    if (indicator.getPullStatus() == Indicator.STATUS_REFRESH_COMPLETE){
                        refreshComplete();
                    }

                    if (indicator.getPullStatus() == Indicator.STATUS_PULL) {
                        resetLocation(indicator.getCurrentY(),0,headerCloseDuration);

                    }

//                    Log.i(TOUCH_TAG, "touchup pullStatus " + indicator.getPullStatus());
//                    if (indicator.getPullStatus() == Indicator.STATUS_REFRESH_COMPLETE) {
//                        refreshComplete();
//                    }
                    return super.dispatchTouchEvent(event);
            }

            return true;
        }



    public void refreshComplete() {
        resetLocation(indicator.getCurrentY(), 0,headerCloseDuration);
        indicator.setPullStatus(Indicator.STATUS_REFRESH_COMPLETE);
        headerInterface.onUIRefreshComplete(indicator);
    }

    private void resetLocation(int distance, int toPos,int millions) {
        mScroller.startScroll(0, toPos, 0,
                distance, millions);
        //indicator.setOffsetY(0);
        indicator.setOffsetY(toPos);
//        Log.i("Scroller-startY:", "" + mScroller.getStartY());
//        Log.i("Scroller-CurrY:", "" + mScroller.getCurrY());
//        Log.i("finalY", "" + mScroller.getFinalY());
//        Log.i("timePassed", "" + mScroller.timePassed());
//        Log.i("duration", "" + mScroller.getDuration());
        postInvalidate();

    }

    private void sendCancelEvent() {
        if (mLastMotionEvent == null) {
            return;
        }
        mHasSendCancelEvent = true;
        MotionEvent last = mLastMotionEvent;
        MotionEvent e = MotionEvent.obtain(last.getDownTime(), last.getEventTime() + ViewConfiguration.getLongPressTimeout(), MotionEvent.ACTION_CANCEL, last.getX(), last.getY(), last.getMetaState());
        super.dispatchTouchEvent(e);
    }

    private void sendDownEvent() {
        final MotionEvent last = mLastMotionEvent;
        MotionEvent e = MotionEvent.obtain(last.getDownTime(), last.getEventTime(), MotionEvent.ACTION_DOWN, last.getX(), last.getY(), last.getMetaState());
        super.dispatchTouchEvent(e);
    }

    private boolean checkIsBeingDraged() {
        contentMarginTop = indicator.getCurrentY();
        Log.i(TOUCH_TAG, "checkIsBeingDraged:" + "margintop:" + contentMarginTop);
        if (contentMarginTop <= 0) {
            return false;
        }
        return true;
    }

    private void updateView(float y) {
        mContentView.offsetTopAndBottom((int) y);
        mHeaderView.offsetTopAndBottom((int) y);
        invalidate();
        indicator.setCurrentY(mContentView.getTop());


    }


    public void setHeaderView(View headerView) {
        PullContainer.MyLayoutParams lp = (MyLayoutParams) headerView.getLayoutParams();
        if (lp == null) {
            lp = new MyLayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
            headerView.setLayoutParams(lp);
        }
        mHeaderView = headerView;
        addView(headerView);

        headerInterface = (HeaderInterface) headerView;
    }

    public void setRefreshCallBack(RefreshCallBack callBack) {
        this.refreshCallBack = callBack;
    }

    public void setPullResistance(float resistance){
        this.pullResistance = resistance;
    }

    public void setPullCloseDuration(int pullCloseDuration) {
        this.pullCloseDuration = pullCloseDuration;
    }

    public void setHeaderCloseDuration(int headerCloseDuration) {
        this.headerCloseDuration = headerCloseDuration;
    }

    public static class MyLayoutParams extends ViewGroup.LayoutParams {

        int marginLeft;
        int marginRight;

        public MyLayoutParams(Context c, AttributeSet attrs) {
            super(c, attrs);
            TypedArray typedArray = c.obtainStyledAttributes(attrs, R.styleable.PullContainer);
            marginLeft = typedArray.getDimensionPixelSize(R.styleable.PullContainer_layoutMarginLeft, 0);
            marginRight = typedArray.getDimensionPixelSize(R.styleable.PullContainer_layoutMarginRight, 0);
            Log.i("mylayoutparams", "marginLeft marginRight " + marginLeft + " " + marginRight);
            typedArray.recycle();
        }

        public MyLayoutParams(int width, int height) {
            super(width, height);
        }

        public MyLayoutParams(LayoutParams source) {
            super(source);
        }
    }

    @Override
    public LayoutParams generateLayoutParams(AttributeSet attrs) {
        return new MyLayoutParams(this.getContext(), attrs);
    }

    @Override
    protected LayoutParams generateLayoutParams(LayoutParams p) {
        return new MyLayoutParams(p);
    }

    @Override
    protected LayoutParams generateDefaultLayoutParams() {
        return new MyLayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
    }
}
