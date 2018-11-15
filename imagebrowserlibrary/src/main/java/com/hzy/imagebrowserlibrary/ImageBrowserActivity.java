package com.hzy.imagebrowserlibrary;

import android.content.Context;
import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.github.chrisbanes.photoview.PhotoView;
import com.hzy.imagebrowserlibrary.config.ImageBrowserConfig;
import com.hzy.imagebrowserlibrary.custom.CustomViewPager;
import com.hzy.imagebrowserlibrary.custom.DotIndicator;
import com.hzy.imagebrowserlibrary.custom.GestureRelativeLayout;
import com.hzy.imagebrowserlibrary.listener.OnClickListener;
import com.hzy.imagebrowserlibrary.listener.OnLongClickListener;
import com.hzy.imagebrowserlibrary.listener.OnPageChangeListener;
import com.hzy.imagebrowserlibrary.transforms.DefaultTransformer;
import com.hzy.imagebrowserlibrary.transforms.DepthPageTransformer;
import com.hzy.imagebrowserlibrary.transforms.RotateDownTransformer;
import com.hzy.imagebrowserlibrary.transforms.RotateUpTransformer;
import com.hzy.imagebrowserlibrary.transforms.ZoomInTransformer;
import com.hzy.imagebrowserlibrary.transforms.ZoomOutSlideTransformer;
import com.hzy.imagebrowserlibrary.transforms.ZoomOutTransformer;
import com.hzy.imagebrowserlibrary.util.StatusBarUtil;

import java.lang.ref.WeakReference;
import java.util.ArrayList;



/**
 * 图片浏览的页面
 * Created by ziye_huang on 2018/11/14.
 */
public class ImageBrowserActivity extends AppCompatActivity {

    //用来保存当前Activity
    private static WeakReference<ImageBrowserActivity> sActivityRef;
    private Context mContext;

    private GestureRelativeLayout mGestureRelativeLayout;
    private CustomViewPager mCustomViewPager;
    private RelativeLayout mRlBlackBg;
    private RelativeLayout mRlIndicator;
    private TextView mNumberIndicator;
    private DotIndicator mDotIndicator;
    private LinearLayout mLlCustomView;

    //图片地址
    private ArrayList<String> mImageUrlList;
    //当前位置
    private int mCurrentPosition;
    //当前切换的动画
    private ImageBrowserConfig.TransformType mTransformType;
    //切换器
    private ImageBrowserConfig.IndicatorType mIndicatorType;
    //图片加载引擎
    public ImageLoader mImageLoader;
    //监听
    public OnLongClickListener mOnLongClickListener;
    public OnClickListener mOnClickListener;
    public OnPageChangeListener mOnPageChangeListener;
    //相关配置信息
    public static ImageBrowserConfig sImageBrowserConfig;
    private MyAdapter mImageBrowserAdapter;
    private ImageBrowserConfig.ScreenOrientationType mScreenOrientationType;
    //图片加载进度View的布局ID
    private int mProgressViewLayoutId = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setWindowFullScreen();
        setContentView(R.layout.activity_image_browser);
        sActivityRef = new WeakReference<>(this);
        mContext = this;

        initViews();
        initData();
        initViewPager();
    }

    private void setWindowFullScreen() {
        //设置全屏
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        if (sImageBrowserConfig.isFullScreenMode()) {
            //设置全屏
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        } else {
            //设置状态栏颜色
            StatusBarUtil.setColor(this, Color.BLACK);
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            // 虚拟导航栏透明
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION, WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
        }
    }

    private void initViews() {
        mCustomViewPager = findViewById(R.id.viewPagerBrowser);
        mGestureRelativeLayout = findViewById(R.id.mnGestureView);
        mRlBlackBg = findViewById(R.id.rl_black_bg);
        mRlIndicator = findViewById(R.id.rl_indicator);
        mDotIndicator = findViewById(R.id.circleIndicator);
        mNumberIndicator = findViewById(R.id.numberIndicator);
        mLlCustomView = findViewById(R.id.ll_custom_view);
        mDotIndicator.setVisibility(View.GONE);
        mNumberIndicator.setVisibility(View.GONE);
        mLlCustomView.setVisibility(View.GONE);
    }

    private void initData() {
        mImageUrlList = sImageBrowserConfig.getImageList();
        mCurrentPosition = sImageBrowserConfig.getPosition();
        mTransformType = sImageBrowserConfig.getTransformType();
        mImageLoader = sImageBrowserConfig.getImageLoader();
        mOnClickListener = sImageBrowserConfig.getOnClickListener();
        mOnLongClickListener = sImageBrowserConfig.getOnLongClickListener();
        mIndicatorType = sImageBrowserConfig.getIndicatorType();
        mScreenOrientationType = sImageBrowserConfig.getScreenOrientationType();
        mOnPageChangeListener = sImageBrowserConfig.getOnPageChangeListener();

        if (mImageUrlList.size() <= 1) {
            mRlIndicator.setVisibility(View.GONE);
        } else {
            mRlIndicator.setVisibility(View.VISIBLE);

            if (sImageBrowserConfig.isIndicatorHide()) {
                mRlIndicator.setVisibility(View.GONE);
            } else {
                mRlIndicator.setVisibility(View.VISIBLE);
            }
            if (mIndicatorType == ImageBrowserConfig.IndicatorType.Indicator_Number) {
                mNumberIndicator.setVisibility(View.VISIBLE);
                mNumberIndicator.setText(String.valueOf((mCurrentPosition + 1) + "/" + mImageUrlList.size()));
            } else {
                mDotIndicator.setVisibility(View.VISIBLE);
            }
        }

        //自定义View
        View customShadeView = sImageBrowserConfig.getCustomShadeView();
        if (customShadeView != null) {
            mLlCustomView.setVisibility(View.VISIBLE);
            mLlCustomView.removeAllViews();
            mLlCustomView.addView(customShadeView);
            mRlIndicator.setVisibility(View.GONE);
        }

        //横竖屏梳理
        if (mScreenOrientationType == ImageBrowserConfig.ScreenOrientationType.ScreenOrientation_Portrait) {
            //设置横竖屏
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        } else if (mScreenOrientationType == ImageBrowserConfig.ScreenOrientationType.ScreenOrientation_Landscape) {
            //设置横横屏
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        } else {
            //设置默认:由设备的物理方向传感器决定
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR);
        }

        //自定义ProgressView
        mProgressViewLayoutId = sImageBrowserConfig.getCustomProgressViewLayoutID();
    }

    private void initViewPager() {
        mImageBrowserAdapter = new MyAdapter();
        mCustomViewPager.setAdapter(mImageBrowserAdapter);
        mCustomViewPager.setCurrentItem(mCurrentPosition);
        setViewPagerTransforms();
        mDotIndicator.setViewPager(mCustomViewPager);
        mCustomViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                mCurrentPosition = position;
                mNumberIndicator.setText(String.valueOf((mCurrentPosition + 1) + "/" + mImageUrlList.size()));
                if (mOnPageChangeListener != null) {
                    mOnPageChangeListener.onPageSelected(position);
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        mGestureRelativeLayout.setOnGestureListener(new GestureRelativeLayout.OnCanSwipeListener() {
            @Override
            public boolean canSwipe() {
                View view = mImageBrowserAdapter.getPrimaryItem();
                PhotoView imageView = view.findViewById(R.id.imageView);
                if (imageView.getScale() != 1.0) {
                    return false;
                }
                return true;
            }
        });

        mGestureRelativeLayout.setOnSwipeListener(new GestureRelativeLayout.OnSwipeListener() {
            @Override
            public void downSwipe() {
                finishBrowser();
            }

            @Override
            public void onSwiping(float deltaY) {
                mRlIndicator.setVisibility(View.GONE);
                mLlCustomView.setVisibility(View.GONE);

                float mAlpha = 1 - deltaY / 500;
                if (mAlpha < 0.3) {
                    mAlpha = 0.3f;
                }
                if (mAlpha > 1) {
                    mAlpha = 1;
                }
                mRlBlackBg.setAlpha(mAlpha);
                if (!sImageBrowserConfig.isFullScreenMode()) {
                    StatusBarUtil.setTranslucent(ImageBrowserActivity.this, (int) (mAlpha * 255));
                }
            }

            @Override
            public void overSwipe() {
                if (mImageUrlList.size() <= 1) {
                    mRlIndicator.setVisibility(View.GONE);
                } else {
                    mRlIndicator.setVisibility(View.VISIBLE);

                    if (!sImageBrowserConfig.isIndicatorHide()) {
                        mRlIndicator.setVisibility(View.VISIBLE);
                    } else {
                        mRlIndicator.setVisibility(View.GONE);
                    }
                }
                //自定义View
                View customShadeView = sImageBrowserConfig.getCustomShadeView();
                if (customShadeView != null) {
                    mLlCustomView.setVisibility(View.VISIBLE);
                    mRlIndicator.setVisibility(View.GONE);
                } else {
                    mLlCustomView.setVisibility(View.GONE);
                }

                mRlBlackBg.setAlpha(1);
            }
        });
    }

    private void setViewPagerTransforms() {
        if (mTransformType == ImageBrowserConfig.TransformType.Transform_Default) {
            mCustomViewPager.setPageTransformer(true, new DefaultTransformer());
        } else if (mTransformType == ImageBrowserConfig.TransformType.Transform_DepthPage) {
            mCustomViewPager.setPageTransformer(true, new DepthPageTransformer());
        } else if (mTransformType == ImageBrowserConfig.TransformType.Transform_RotateDown) {
            mCustomViewPager.setPageTransformer(true, new RotateDownTransformer());
        } else if (mTransformType == ImageBrowserConfig.TransformType.Transform_RotateUp) {
            mCustomViewPager.setPageTransformer(true, new RotateUpTransformer());
        } else if (mTransformType == ImageBrowserConfig.TransformType.Transform_ZoomIn) {
            mCustomViewPager.setPageTransformer(true, new ZoomInTransformer());
        } else if (mTransformType == ImageBrowserConfig.TransformType.Transform_ZoomOutSlide) {
            mCustomViewPager.setPageTransformer(true, new ZoomOutSlideTransformer());
        } else if (mTransformType == ImageBrowserConfig.TransformType.Transform_ZoomOut) {
            mCustomViewPager.setPageTransformer(true, new ZoomOutTransformer());
        } else {
            mCustomViewPager.setPageTransformer(true, new DefaultTransformer());
        }
    }

    private void finishBrowser() {
        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        mLlCustomView.setVisibility(View.GONE);
        mRlIndicator.setVisibility(View.GONE);
        mRlBlackBg.setAlpha(0);
        finish();
        this.overridePendingTransition(0, R.anim.browser_exit_anim);
    }

    @Override
    public void onBackPressed() {
        finishBrowser();
    }


    private class MyAdapter extends PagerAdapter {

        private View mCurrentView;

        private LayoutInflater layoutInflater;

        public MyAdapter() {
            layoutInflater = LayoutInflater.from(mContext);
        }

        @Override
        public void setPrimaryItem(ViewGroup container, int position, Object object) {
            mCurrentView = (View) object;
        }

        public View getPrimaryItem() {
            return mCurrentView;
        }

        @Override
        public int getCount() {
            return mImageUrlList.size();
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
        }

        @Override
        public Object instantiateItem(ViewGroup container, final int position) {
            View inflate = layoutInflater.inflate(R.layout.image_browser_item_show_image, container, false);
            final PhotoView imageView = inflate.findViewById(R.id.imageView);
            final RelativeLayout rl_browser_root = inflate.findViewById(R.id.rl_browser_root);
            final RelativeLayout progress_view = inflate.findViewById(R.id.progress_view);

            final String url = mImageUrlList.get(position);

            rl_browser_root.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    finishBrowser();
                }
            });

            imageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    //单击事件
                    if (mOnClickListener != null) {
                        mOnClickListener.onClick(ImageBrowserActivity.this, imageView, position, url);
                    }
                    finishBrowser();
                }
            });

            imageView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View view) {
                    if (mOnLongClickListener != null) {
                        mOnLongClickListener.onLongClick(ImageBrowserActivity.this, imageView, position, url);
                    }
                    return false;
                }
            });

            //ProgressView
            if (mProgressViewLayoutId != 0) {
                View customProgressView = layoutInflater.inflate(mProgressViewLayoutId, null);
                if (customProgressView != null) {
                    progress_view.removeAllViews();
                    progress_view.addView(customProgressView);
                    progress_view.setVisibility(View.VISIBLE);
                } else {
                    progress_view.setVisibility(View.GONE);
                }
            } else {
                progress_view.setVisibility(View.GONE);
            }

            //图片加载
            mImageLoader.loadImage(mContext, url, imageView, progress_view);

            container.addView(inflate);
            return inflate;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        sActivityRef = null;
        sImageBrowserConfig = null;
    }

    /**
     * 获取当前Activity实例
     */
    public static FragmentActivity getCurrentActivity() {
        if (sActivityRef != null && sActivityRef.get() != null) {
            return sActivityRef.get();
        } else {
            return null;
        }
    }

    /**
     * 关闭当前Activity
     */
    public static void finishActivity() {
        if (sActivityRef != null && sActivityRef.get() != null) {
            sActivityRef.get().finishBrowser();
        }
    }

    /**
     * 获取ViewPager
     *
     * @return
     */
    public static ViewPager getViewPager() {
        if (sActivityRef != null && sActivityRef.get() != null) {
            return sActivityRef.get().mCustomViewPager;
        } else {
            return null;
        }
    }

    /**
     * 获取当前位置
     *
     * @return
     */
    public static int getCurrentPosition() {
        if (sActivityRef != null && sActivityRef.get() != null) {
            return sActivityRef.get().mCurrentPosition;
        } else {
            return -1;
        }
    }

    /**
     * 获取当前ImageView
     *
     * @return
     */
    public static ImageView getCurrentImageView() {
        if (sActivityRef != null && sActivityRef.get() != null) {
            MyAdapter imageBrowserAdapter = sActivityRef.get().mImageBrowserAdapter;
            if (imageBrowserAdapter == null) {
                return null;
            }
            View view = imageBrowserAdapter.getPrimaryItem();
            if (view == null) {
                return null;
            }
            PhotoView imageView = view.findViewById(R.id.imageView);
            return imageView;
        } else {
            return null;
        }
    }

    /**
     * 删除一张图片
     *
     * @param position
     * @return
     */
    public static void removeImage(int position) {
        if (sActivityRef != null && sActivityRef.get() != null) {
            if (sActivityRef.get().mImageUrlList.size() > 1) {
                sActivityRef.get().mImageUrlList.remove(position);
                //更新当前位置
                if (sActivityRef.get().mCurrentPosition >= sActivityRef.get().mImageUrlList.size() && sActivityRef.get().mCurrentPosition >= 1) {
                    sActivityRef.get().mCurrentPosition--;
                }
                if (sActivityRef.get().mCurrentPosition >= sActivityRef.get().mImageUrlList.size()) {
                    sActivityRef.get().mCurrentPosition = sActivityRef.get().mImageUrlList.size() - 1;
                }
                sActivityRef.get().initViewPager();
                sActivityRef.get().mImageBrowserAdapter.notifyDataSetChanged();
            }
        }
    }

    /**
     * 删除一张图片
     *
     * @return
     */
    public static void removeCurrentImage() {
        removeImage(getCurrentPosition());
    }

    /**
     * 图片资源列表
     *
     * @return
     */
    public static ArrayList<String> getImageList() {
        if (sActivityRef != null && sActivityRef.get() != null) {
            return sActivityRef.get().mImageUrlList;
        }
        return new ArrayList<>();
    }
}
