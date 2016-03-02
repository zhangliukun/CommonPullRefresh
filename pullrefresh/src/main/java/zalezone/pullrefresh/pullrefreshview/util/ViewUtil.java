package zalezone.pullrefresh.pullrefreshview.util;

import android.view.View;
import android.widget.AbsListView;

/**
 * Created by zale on 2015/12/10.
 */
public class ViewUtil {

    public static boolean canChildScrollUp(View view) {
        if (android.os.Build.VERSION.SDK_INT < 14) {
            if (view instanceof AbsListView) {
                final AbsListView absListView = (AbsListView) view;
                return absListView.getChildCount() > 0
                        && (absListView.getFirstVisiblePosition() > 0 || absListView.getChildAt(0)
                        .getTop() < absListView.getPaddingTop());
            } else {
                return view.getScrollY() > 0;
            }
        } else {
            return view.canScrollVertically(-1);
        }
    }

    public static boolean checkCanPullDown(View view){
        return !canChildScrollUp(view);
    }

}
