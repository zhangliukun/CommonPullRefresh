# common-pull-refresh

A widget that can achieve the function of pulling to refresh. It's like a container which can help developer to manager the sub view.

This widget supports almost all of the android view such as listview,webview,scrollview or more.What's more,It's extremely flexible and extensible.You can user your own view for the pull-refresh head.I support some
callback to you in the headview for your own animation.you just need to follow the steps below.


## Gradle dependency
I am studying how to publish this widget to the Jcenter and the mavenCenter now,so you will see the dependency soon or later.

## Screenshots
Though the view of the widget is not very beautiful,this widget functions well.

## Usage

Only these simple code you need to write that you get what you want.


### code in your activity
```java

PullContainer mPullContainer = (PullContainer) findViewById(R.id.base_container);
SimpleHeader simpleHeader = new SimpleHeader(this);
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
```
### code in your contentView xml

```xml

<zalezone.pullrefresh.pullrefreshview.PullContainer
        android:id="@+id/base_container"
        android:layout_width="match_parent"
        android:layout_height="match_parent">

        <zalezone.pullrefresh.pullrefreshview.MyContent
            android:id="@+id/my_textview"
            android:layout_width="match_parent"
            android:layout_height="match_parent"
            android_custom:layoutMarginLeft="40dp"
            android_custom:layoutMarginRight="40dp"/>

</zalezone.pullrefresh.pullrefreshview.PullContainer>
```
Look,how easy it is.

## Customizing
I guess you are wondering to know how to use your own head view with your favourite style.Of course,I also
support the most flexible and extandable way for you.

### draw your own head view
For convenience,I offer a simple header view for you in the sample code.You just need to draw your own head view like me.
Here is the code of my SimpleHeader

```java
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
        stateTV.setText("pull to refresh");
    }

    @Override
    public void onUIRefreshPerpare(Indicator indicator) {
        stateTV.setText("release to refresh");
    }

    @Override
    public void onUIRefreshBegin(Indicator indicator) {
        stateTV.setText("refreshing");
        if (rotateAn!=null){
            arrowIV.startAnimation(rotateAn);
        }
    }

    @Override
    public void onUIRefreshComplete(Indicator indicator) {
        stateTV.setText("refresh complete");
        arrowIV.clearAnimation();
    }
}
```

All you need to do is to subclass the BaseHeader and override methods in the code.

**warning:you should not override the onUIPositionChange method int your head view because this method is used to control the UI** 

### other custom styles
Except write the head view by yourself,you can also drive the widget performs like what you want.You are able to use these methods to 
control the behaviour.

Class: **zalezone.pullrefresh.pullrefreshview.PullContainer**

Public Methods

- **void setPullResistance(float resistance)**
    set the resistance of pulling the view.

- **void setPullCloseDuration(int millisecond)**
    set the duration of close the content.

- **void set headerCloseDuration(int millisecond)**
    set the duration of close the header. 

more?to to continued..

## Communication
If you have some suggestions or want to join me to prefect this widget,welcome to contact me.
email:1433317488@qq.com,zalecoder@gmail.com

