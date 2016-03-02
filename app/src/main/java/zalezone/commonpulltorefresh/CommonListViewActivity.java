package zalezone.commonpulltorefresh;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

import zalezone.pullrefresh.pullrefreshview.PullContainer;
import zalezone.pullrefresh.pullrefreshview.RefreshCallBack;
import zalezone.pullrefresh.pullrefreshview.header.header.impl.SimpleHeader;

/**
 * Created by zale on 16/3/1.
 */
public class CommonListViewActivity extends AppCompatActivity{

    public static void startActivity(Context context) {
        Intent intent = new Intent(context, CommonListViewActivity.class);
        context.startActivity(intent);
    }

    PullContainer mPullContainer;
    ListView mListView;
    List<String> dataList = new ArrayList<>();
    SimpleHeader simpleHeader;
    ArrayAdapter<String> arrayAdapter;
    Handler mHandler = new Handler(Looper.getMainLooper());

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_refresh_list);

        initView();
        initData();
    }

    private void initView() {
        mPullContainer = (PullContainer) findViewById(R.id.base_container);
        mListView = (ListView) findViewById(R.id.myListView);
        simpleHeader = new SimpleHeader(this);
        mPullContainer.setHeaderView(simpleHeader);
        mPullContainer.setRefreshCallBack(new RefreshCallBack() {

            @Override
            public void onRefreshBegin() {

                Log.i("network", "Refresh Start");
                mHandler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        for (int i = 0; i < 4; i++) {
                            dataList.add("new data");
                        }
                        arrayAdapter.notifyDataSetChanged();

                        mPullContainer.refreshComplete();
                        Log.i("network", "Refresh end");
                    }
                }, 3000);
            }
        });
        mPullContainer.setPullResistance(2.8f);
    }

    private void initData() {
        for (int i=0;i<10;i++){
            dataList.add("test data");
        }
        arrayAdapter = new ArrayAdapter<>(this,android.R.layout.simple_list_item_1,dataList);
        mListView.setAdapter(arrayAdapter);
    }
}
