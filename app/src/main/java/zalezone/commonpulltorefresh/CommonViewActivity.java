package zalezone.commonpulltorefresh;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import zalezone.pullrefresh.pullrefreshview.PullContainer;
import zalezone.pullrefresh.pullrefreshview.RefreshCallBack;
import zalezone.pullrefresh.pullrefreshview.header.header.impl.SimpleHeader;

/**
 * Created by zale on 16/3/1.
 */
public class CommonViewActivity extends AppCompatActivity {

    public static void startActivity(Context context) {
        Intent intent = new Intent(context, CommonViewActivity.class);
        context.startActivity(intent);
    }

    PullContainer mPullContainer;
    SimpleHeader simpleHeader;
    Handler mHandler = new Handler(Looper.getMainLooper());


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pull_container);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        switch (id){
            case R.id.pull_refresh_view:
                CommonViewActivity.startActivity(this);
                break;
            case R.id.pull_refresh_listview:
                CommonListViewActivity.startActivity(this);
                break;
            case R.id.pull_scroll_listview:
                CommonViewScrollActivity.startActivity(this);
                break;
        }

        return super.onOptionsItemSelected(item);
    }
}
