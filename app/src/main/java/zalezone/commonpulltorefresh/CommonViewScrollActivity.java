package zalezone.commonpulltorefresh;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.v7.app.AppCompatActivity;

import zalezone.pullrefresh.pullrefreshview.PullContainer;
import zalezone.pullrefresh.pullrefreshview.RefreshCallBack;
import zalezone.pullrefresh.pullrefreshview.header.header.impl.SimpleHeader;

/**
 * Created by zale on 16/3/2.
 */
public class CommonViewScrollActivity extends AppCompatActivity{
    public static void startActivity(Context context) {
        Intent intent = new Intent(context, CommonViewScrollActivity.class);
        context.startActivity(intent);
    }

    PullContainer mPullContainer;
    SimpleHeader simpleHeader;
    Handler mHandler = new Handler(Looper.getMainLooper());


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scroll_list);
        initView();
    }

    private void initView() {
        mPullContainer = (PullContainer) findViewById(R.id.base_container);
        simpleHeader = new SimpleHeader(this);
        mPullContainer.setHeaderView(simpleHeader);
        mPullContainer.setRefreshCallBack(new RefreshCallBack() {

            @Override
            public void onRefreshBegin() {
                mHandler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        mPullContainer.refreshComplete();
                    }
                }, 2000);
            }
        });
        mPullContainer.setPullResistance(2.8f);
    }
}
