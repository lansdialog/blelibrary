package com.ble.demo.view;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.ble.demo.R;
import com.ble.demo.utils.DensityUtils;

import java.util.Timer;
import java.util.TimerTask;

public class BannerView extends FrameLayout implements ViewPager.OnPageChangeListener {
    private Context context;
    private static final int MSG = 0X100;
    /**
     * 轮播图最大数
     */
    private int totalCount = Integer.MAX_VALUE;
    /**
     * 当前banner需要显式的数量
     */
    private int showCount;
    private int currentPosition = 0;
    private ViewPager viewPager;
    private LinearLayout carouselLayout;
    private Adapter adapter;
    /**
     * 轮播切换小圆点宽度默认宽度
     */
    private static final int DOT_DEFAULT_W = 5;
    /**
     * 轮播切换小圆点宽度
     */
    private int IndicatorDotWidth = DOT_DEFAULT_W;
    /**
     * 用户是否干预
     */
    private boolean isUserTouched = false;
    /**
     * 默认的轮播时间
     */
    private static final int DEFAULT_TIME = 3000;
    /**
     * 设置轮播时间
     */
    private int switchTime = DEFAULT_TIME;
    /**
     * 轮播图定时器
     */
    private Timer mTimer = new Timer();

    public BannerView(Context context) {
        super(context);
        this.context = context;
    }
    public BannerView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
    }
    public BannerView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.context = context;
    }
    @SuppressLint("ClickableViewAccessibility")
    private void init() {
        viewPager.setAdapter(null);
        carouselLayout.removeAllViews();
        if (adapter.isEmpty()) {
            return;
        }
        int count = adapter.getCount();
        showCount = adapter.getCount();
        //绘制切换小圆点
        for (int i = 0; i < count; i++) {
            View view = new View(context);
            if (currentPosition == i) {
                view.setPressed(true);
                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                        IndicatorDotWidth + DensityUtils.dip2px( 3),
                        IndicatorDotWidth + DensityUtils.dip2px( 3));
                params.setMargins(IndicatorDotWidth, 0, 0, 0);
                view.setLayoutParams(params);
            } else {
                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(IndicatorDotWidth, IndicatorDotWidth);
                params.setMargins(IndicatorDotWidth, 0, 0, 0);
                view.setLayoutParams(params);
            }
            view.setBackgroundResource(R.drawable.carousel_layout_dot);
            carouselLayout.addView(view);
        }
        viewPager.setAdapter(new ViewPagerAdapter());
        viewPager.setCurrentItem(0);



        this.viewPager.setOnTouchListener((v, event) -> {
            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN: //down不会执行---因为vp中imageview设置了onclick
                   // Log.i("abc","ACTION_DOWN");
                case MotionEvent.ACTION_MOVE:
                    //有用户滑动事件发生
                    isUserTouched = true;
                  //  Log.i("abc","ACTION_MOVE");

                    break;
                case MotionEvent.ACTION_UP:
                    isUserTouched = false;
                    break;
            }
            return true;
        });
        //以指定周期和岩石开启一个定时任务
        mTimer.schedule(mTimerTask, switchTime, switchTime);
    }

    //设置adapter，这个方法需要再使用时设置
    public void setAdapter(Adapter adapter) {
        this.adapter = adapter;
        if (adapter != null) {
            init();
        }
    }
    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        View view = LayoutInflater.from(context).inflate(R.layout.carousel_layout, null);
        this.viewPager = (ViewPager) view.findViewById(R.id.gallery);
        this.carouselLayout = (LinearLayout) view.findViewById(R.id.CarouselLayoutPage);
        IndicatorDotWidth = DensityUtils.dip2px( IndicatorDotWidth);
        this.viewPager.addOnPageChangeListener(this);
        addView(view);

    }
    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }
    @Override
    public void onPageSelected(int position) {
      //  Log.i("abc","onPageSelected"+position);

        currentPosition = position;
        int count = carouselLayout.getChildCount();
        for (int i = 0; i < count; i++) {
            View view = carouselLayout.getChildAt(i);
            if (position % showCount == i) {
                view.setSelected(true);
                //当前位置的点要绘制的较大一点，高亮显示
                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                        IndicatorDotWidth + DensityUtils.dip2px( 3),
                        IndicatorDotWidth + DensityUtils.dip2px( 3));
                params.setMargins(IndicatorDotWidth, 0, 0, 0);
                view.setLayoutParams(params);
            } else {
                view.setSelected(false);
                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(IndicatorDotWidth, IndicatorDotWidth);
                params.setMargins(IndicatorDotWidth, 0, 0, 0);
                view.setLayoutParams(params);
            }
        }
    }
    @Override
    public void onPageScrollStateChanged(int state) {

    }
    class ViewPagerAdapter extends PagerAdapter {
        @Override
        public int getCount() {
            return totalCount;
        }
        //判断一个页面(View)是否与instantiateItem方法返回的Object一致
        //判断instantiateItem(ViewGroup, int)函数所返回来的Key与一个页面视图是否是 代表的同一个视图(即它俩是否是对应的，对应的表示同一个View),通常我们直接写 return view == object；就可以了,至于为什么要这样讲起来比较复杂,后面有机会进行了解吧 貌似是ViewPager中有个存储view状态信息的ArrayList,根据View取出对应信息的吧!
        @Override
        public boolean isViewFromObject(View view, Object object) {
          //  Log.i("abc","isViewFromObject");

            return view == object;
        }
        //创建一个页面
       // ①将给定位置的view添加到ViewGroup(容器)中,创建并显示出来 ②返回一个代表新增页面的Object(key),通常都是直接返回view本身就可以了, 当然你也可以自定义自己的key,但是key和每个view要一一对应的关系
        // 当要显示的图片可以进行缓存的时候，会调用这个方法进行显示图片的初始化，我们将要显示的ImageView加入到ViewGroup中，然后作为返回值返回即可
        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            position %= showCount;
            View view = adapter.getView(position);
            container.addView(view);
          //  Log.i("abc","instantiateItem"+position);

            return view;
        }
        //销毁一个页面
        //移除一个给定位置的页面。适配器有责任从容器中删除这个视图。这是为了确保 在finishUpdate(viewGroup)返回时视图能够被移除。
        // PagerAdapter只缓存三张要显示的图片，如果滑动的图片超出了缓存的范围，就会调用这个方法，将图片销
        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
           // Log.i("abc","destroyItem");

        }
        @Override
        public int getItemPosition(Object object) {
            return super.getItemPosition(object);
        }
       // 最后在更新的后期会调用finishUpdate---当一个页面被更改时
//        @Override
//        public void finishUpdate(ViewGroup container) {
//            super.finishUpdate(container);
//            Log.i("abc","finishUpdate");
//            int position = viewPager.getCurrentItem();
//            if (position == 0) {
//                position = showCount;
//                viewPager.setCurrentItem(position, false);
//            } else if (position == totalCount - 1) {
//                position = showCount - 1;
//                viewPager.setCurrentItem(position, false);
//            }
//        }
    }
    private TimerTask mTimerTask = new TimerTask() {
        @Override
        public void run() {
           // Log.i("abc","mTimerTask:"+isUserTouched);

            //用户滑动时，定时任务不响应
            if (!isUserTouched) {
                currentPosition = (currentPosition + 1) % totalCount;
                handler.sendEmptyMessage(MSG);
            }
        }
    };
    public void cancelTimer() {
        if (this.mTimer != null) {
            this.mTimer.cancel();
        }
    }
    private Handler handler = new Handler(Looper.myLooper()) {
        @Override
        public void handleMessage(Message msg) {
            if (msg.what == MSG) {
              //  Log.i("abc","handleMessage:"+isUserTouched);

             //   Log.e("Pos", "the position is " + currentPosition);
                if (currentPosition == totalCount - 1) {
                    viewPager.setCurrentItem(showCount - 1, false);
                } else {
                    viewPager.setCurrentItem(currentPosition);
                }
            }
        }
    };
    /**
     *可自定义设置轮播图切换时间，单位毫秒
     * @param switchTime millseconds
     */
    public void setSwitchTime(int switchTime) {
        this.switchTime = switchTime;
    }
    /**
     * @param indicatorDotWidth
     */
    public void setIndicatorDotWidth(int indicatorDotWidth) {
        IndicatorDotWidth = indicatorDotWidth;
    }
    public interface Adapter {
        boolean isEmpty();
        View getView(int position);
        int getCount();
    }
}