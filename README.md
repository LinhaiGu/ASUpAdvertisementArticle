# ASUpAdvertisementArticle
仿京东首页的京东快报，自动向上滚动的广告条
前言

上次在京东APP上买东西时，发现首页中间有块叫“京东快报”的栏目，其中广告条能自动向上滚动，效果还不错，看到这个效果，第一个念头就是我能不能实现，于是就诞生了这篇文章。

我们看看实现后的效果：

http://img.blog.csdn.net/20160330124340853

向上滚动的广告条

实现原理

起初看到这个效果时，第一个想法就是向上移动动画+定时器，但当我准备写时发现，滚动时上下文字都是逐渐出来的（有点像滑动的），如果用动画的话不行，如果用滑动的话，那就是ListView了，但在看效果图，界面上明明只有一个ITEM，于是，我进行了以下改动：

设置ListView的高度与Item高度一致，这样界面中就只显示一个Item。
自动滚动，可以使用ListView的smoothScrollBy(int distance, int duration)方法，第一个参数是滚动的距离，第二个是滚动时间。
自动滚动可以通过定时器，使用Handler类自带的postDelyed。
ListView的无限向上滚动，可以通过继承BaseAdapter类中重写的getCount()方法时返回Integer.MAX_VALUE使其接近无限大。
触摸事件的拦截，可以通过onInterceptTouchEvent来实现，返回false。
注意：通过smoothScrollBy方法滚动时，由于布局中的高度时dp，但这里面的第一个参数是sp，因此需要将dp转sp，转换完毕后的是float，但方法中是需要int，这样的话有可能导致小数位的丢失，自动滚动产生偏差，可以通过setSelection(int position)方法纠正位置，在smoothScrollBy方法调用后执行setSelection方法。
控件的使用

根据业务场景不同，如果是主页的广告条，可以创建MainScrollUpAdvertisementView类继承自BaseAutoScrollUpTextView，BaseAutoScrollUpTextView后尖括号中是广告的数据类型，这里假设是AdvertisementObject。

1、创建我们的广告类

package com.example.autoscrollup.entity;

import java.io.Serializable;

/**
 * 获取的首页广告条数据
 * 
 * @author 顾林海
 * 
 */
public class AdvertisementObject implements Serializable {
    public String title;
    public String info;
}

2、创建MainScrollUpAdvertisementView类继承自BaseAutoScrollUpTextView：

package com.example.autoscrollup.view;

import android.content.Context;
import android.util.AttributeSet;

import com.example.autoscrollup.entity.AdvertisementObject;
import com.example.autoscrollup.view.base.BaseAutoScrollUpTextView;

/**
 * <pre>
 * 制作主页的向上广告滚动条
 * AdvertisementObject是主页的数据源，假如通过GSON或FastJson获取的实体类
 * 
 * <pre>
 * @author 顾林海
 * 
 */
public class MainScrollUpAdvertisementView extends
        BaseAutoScrollUpTextView<AdvertisementObject> {

    public MainScrollUpAdvertisementView(Context context, AttributeSet attrs,
            int defStyle) {
        super(context, attrs, defStyle);
    }

    public MainScrollUpAdvertisementView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public MainScrollUpAdvertisementView(Context context) {
        super(context);
    }

    @Override
    public String getTextTitle(AdvertisementObject data) {
        return data.title;
    }

    @Override
    public String getTextInfo(AdvertisementObject data) {
        return data.info;
    }

    /**
     * 这里面的高度应该和你的xml里设置的高度一致
     */
    @Override
    protected int getAdertisementHeight() {
        return 40;
    }

}

通过getTextTitle、getTextInfo、getAdertisementHeight方法获取标题、内容、以及整个广告条的高度。 
这里面的getAdertisementHeight方法返回的高度必须与你将要创建的xml文件中MainScrollUpAdvertisementView 控件高度一致，比如上面是40，那我们创建的xml是这样的：
<RelativeLayout xmlns:android="http://schemas.android.com/apk/res/android"
    xmlns:tools="http://schemas.android.com/tools"
    android:layout_width="match_parent"
    android:layout_height="match_parent" >

    <RelativeLayout
        android:layout_width="match_parent"
        android:layout_height="wrap_content"
        android:layout_centerVertical="true"
        android:layout_marginLeft="10dp"
        android:layout_marginRight="10dp" >

        <ImageView
            android:id="@+id/iv_icon"
            android:layout_width="wrap_content"
            android:layout_height="40dp"
            android:layout_centerVertical="true"
            android:src="@drawable/icon" />

        <com.example.autoscrollup.view.MainScrollUpAdvertisementView
            android:id="@+id/main_advertisement_view"
            android:layout_width="match_parent"
            android:layout_height="40dp"
            android:layout_centerVertical="true"
            android:layout_marginLeft="10dp"
            android:layout_toRightOf="@id/iv_icon" >
        </com.example.autoscrollup.view.MainScrollUpAdvertisementView>
    </RelativeLayout>

</RelativeLayout>

这里android:layout_height=”40dp”设置为40，与我们定义的MainScrollUpAdvertisementView 类中的getAdertisementHeight方法返回的高度一致。
3、在我们的Activity中使用定义的MainScrollUpAdvertisementView 控件：

package com.example.autoscrollup;

import java.util.ArrayList;

import android.app.Activity;
import android.os.Bundle;
import android.widget.Toast;

import com.example.autoscrollup.entity.AdvertisementObject;
import com.example.autoscrollup.view.MainScrollUpAdvertisementView;
import com.example.autoscrollup.view.base.BaseAutoScrollUpTextView.OnItemClickListener;

public class MainActivity extends Activity {

    private MainScrollUpAdvertisementView mMainScrollUpAdvertisementView;
    private ArrayList<AdvertisementObject> mDataList = new ArrayList<AdvertisementObject>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initData();
        initViews();
    }

    /**
     * 模拟网络获取数据
     */
    private void initData() {
        AdvertisementObject advertisementObject = new AdvertisementObject();
        advertisementObject.title = "爆";
        advertisementObject.info = "踏青零食上京东，百万零食1元秒";
        mDataList.add(advertisementObject);
        advertisementObject = new AdvertisementObject();
        advertisementObject.title = "公告";
        advertisementObject.info = "看老刘中国行，满129减50！";
        mDataList.add(advertisementObject);
        advertisementObject = new AdvertisementObject();
        advertisementObject.title = "爆";
        advertisementObject.info = "高姿CC霜全渠道新品首发，领券199减50，点击查看";
        mDataList.add(advertisementObject);
    }

    private void initViews() {
        mMainScrollUpAdvertisementView = (MainScrollUpAdvertisementView) findViewById(R.id.main_advertisement_view);
        mMainScrollUpAdvertisementView.setData(mDataList);
        mMainScrollUpAdvertisementView.setTextSize(15);
        mMainScrollUpAdvertisementView
                .setOnItemClickListener(new OnItemClickListener() {

                    @Override
                    public void onItemClick(int position) {
                        Toast.makeText(MainActivity.this,
                                "点击了第" + position + "个广告条", Toast.LENGTH_SHORT)
                                .show();
                    }
                });
        mMainScrollUpAdvertisementView.setTimer(2000);
        mMainScrollUpAdvertisementView.start();


    }

}

注册我们的控件，为控件添加数据通过setData方法添加网络获取到的数据。通过setTextSize设置广告条中文字的大小，通过setOnItemClickListener方法设置监听事件，通过setTimer方法设置滚动的间隔时间，最后通过start方法开启滚动。其中stop方法用于在Activity暂停或销毁时调用，上面暂未写出，请大家自行添加。
至此使用完毕，整体使用还是挺简单的。

代码讲解

以下是对BaseAutoScrollUpTextView这个基类的说明，先贴出完整的源代码：

package com.example.autoscrollup.view.base;

import java.util.ArrayList;

import android.content.Context;
import android.graphics.Color;
import android.os.Handler;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.AbsListView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.autoscrollup.R;

/**
 * <pre>
 * 京东快报 自动向上滚动的广告基类
 * 内部包含TextView的自动向上滚动
 * 
 * <pre>
 * @author 顾林海
 * 
 * @param <T>
 */
public abstract class BaseAutoScrollUpTextView<T> extends ListView implements
        AutoScrollData<T> {

    /**
     * 数据源
     */
    private ArrayList<T> mDataList = new ArrayList<T>();

    /**
     * 字体大小
     */
    private float mSize=16;

    /**
     * 数据总数
     */
    private int mMax;

    private int position = -1;

    /**
     * 向上滚动距离
     */
    private int scroll_Y;

    private int mScrollY;

    /**
     * 适配器
     */
    private AutoScrollAdapter mAutoScrollAdapter = new AutoScrollAdapter();

    /**
     * 监听器
     */
    private OnItemClickListener mOnItemClickListener;

    private long mTimer = 1000;

    private Context mContext;

    /**
     * 获取高度
     * 
     * @return
     */
    protected abstract int getAdertisementHeight();

    private Handler handler = new Handler();
    Runnable runnable = new Runnable() {
        @Override
        public void run() {
            // 开启轮播
            switchItem();
            handler.postDelayed(this, mTimer);
        }
    };

    public interface OnItemClickListener {
        public void onItemClick(int position);
    }

    public BaseAutoScrollUpTextView(Context context, AttributeSet attrs,
            int defStyle) {
        super(context, attrs, defStyle);
        this.mContext = context;
        mScrollY = dip2px(getAdertisementHeight());
        init();

    }

    public BaseAutoScrollUpTextView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public BaseAutoScrollUpTextView(Context context) {
        this(context, null);
    }

    private void init() {
        this.setDivider(null);
        this.setFastScrollEnabled(false);
        this.setDividerHeight(0);
        this.setEnabled(false);
    }

    /**
     * dp-->px
     * 
     * @param dipValue
     * @return
     */
    private int dip2px(float dipValue) {
        final float scale = mContext.getResources().getDisplayMetrics().density;
        return (int) (dipValue * scale + 0.5f);
    }

    /**
     * 开始轮播
     */
    private void switchItem() {
        if (position == -1) {
            scroll_Y = 0;
        } else {
            scroll_Y = mScrollY;
        }
        smoothScrollBy(scroll_Y, 2000);
        setSelection(position);
        position++;
    }

    /**
     * 广告条适配器
     * 
     * @author 顾林海
     * 
     */
    private class AutoScrollAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            final int count = mDataList == null ? 0 : mDataList.size();
            return count > 1 ? Integer.MAX_VALUE : count;
        }

        @Override
        public Object getItem(int position) {
            return mDataList.get(position % mMax);
        }

        @Override
        public long getItemId(int position) {
            return position % mMax;
        }

        @Override
        public View getView(final int position, View convertView,
                ViewGroup parent) {
            ViewHolder viewHolder;
            if (null == convertView) {
                viewHolder = new ViewHolder();
                convertView = LayoutInflater.from(mContext).inflate(
                        R.layout.item_layout, null);
                viewHolder.mTitleView = (TextView) convertView
                        .findViewById(R.id.tv_title);
                viewHolder.mInfoView = (TextView) convertView
                        .findViewById(R.id.tv_info);
                convertView.setTag(viewHolder);
            } else {
                viewHolder = (ViewHolder) convertView.getTag();
            }
            T data = mDataList.get(position % mMax);
            viewHolder.mTitleView
                    .setLayoutParams(new RelativeLayout.LayoutParams(
                            RelativeLayout.LayoutParams.WRAP_CONTENT,
                            dip2px(getAdertisementHeight())));
            viewHolder.mTitleView.setTextSize(mSize);
            viewHolder.mInfoView.setTextSize(mSize);
            viewHolder.mTitleView.setText(getTextTitle(data));
            viewHolder.mInfoView.setText(getTextInfo(data));
            convertView.setOnClickListener(new OnClickListener() {

                @Override
                public void onClick(View v) {
                    mOnItemClickListener.onItemClick(position % mMax);
                }
            });
            return convertView;
        }
    }

    static class ViewHolder {
        TextView mTitleView;// 标题
        TextView mInfoView;// 内容
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        return false;
    }

    /**
     * 添加数据
     * 
     * @param _datas
     */
    public void setData(ArrayList<T> _datas) {
        mDataList.clear();
        mDataList.addAll(_datas);
        mMax = mDataList == null ? 0 : mDataList.size();
        this.setAdapter(mAutoScrollAdapter);
        mAutoScrollAdapter.notifyDataSetChanged();
    }

    /**
     * 设置文字大小
     * @param size
     */
    public void setTextSize(float _size){
        this.mSize=_size;
    }

    /**
     * 设置监听事件
     */
    public void setOnItemClickListener(OnItemClickListener _listener) {
        this.mOnItemClickListener = _listener;
    }

    /**
     * 设置轮播间隔时间
     * 
     * @param _time
     *            毫秒单位
     */
    public void setTimer(long _time) {
        this.mTimer = _time;
    }

    /**
     * 开启轮播
     */
    public void start() {
        handler.postDelayed(runnable, 1000);
    }

    /**
     * 关闭轮播
     */
    public void stop() {
        handler.removeCallbacks(runnable);
    }

}

以上主要是通过Handler类自带的postDelyed实现一个定时的轮播，轮播时调用了ListView的：

smoothScrollBy(scroll_Y, 2000);
setSelection(position);

数据的回调主要是定义了一个范型接口，范型接口的具体实现延迟到子类实现，这样方便大家格局不同场景下定义不同的广告条。

最后的最后大家使用时一定要注意在xml中使用你自己定义的广告条（继承BaseAutoScrollUpTextView），高度一定要与子类getAdertisementHeight方法返回的值一致
