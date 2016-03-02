package zalezone.pullrefresh.pullrefreshview;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.RelativeLayout;

import zalezone.pullrefresh.R;

/**
 * Created by zale on 2015/12/8.
 */
public class MyContent extends RelativeLayout {

    private View mContent;

    public MyContent(Context context) {
        this(context, null);
    }

    public MyContent(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    private void init(Context context) {
        mContent = LayoutInflater.from(context).inflate(R.layout.item_content,this);
    }

}
