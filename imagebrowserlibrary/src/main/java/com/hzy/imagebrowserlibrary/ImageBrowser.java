package com.hzy.imagebrowserlibrary;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.LayoutRes;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;

import com.hzy.imagebrowserlibrary.config.ImageBrowserConfig;
import com.hzy.imagebrowserlibrary.listener.OnClickListener;
import com.hzy.imagebrowserlibrary.listener.OnLongClickListener;
import com.hzy.imagebrowserlibrary.listener.OnPageChangeListener;

import java.util.ArrayList;


/**
 * Created by ziye_huang on 2018/11/14.
 */
public class ImageBrowser {

    private Context mContext;
    private ImageBrowserConfig mImageBrowserConfig;

    private ImageBrowser(Context context) {
        mContext = context;
        mImageBrowserConfig = new ImageBrowserConfig();
    }

    public static ImageBrowser with(Context context) {
        return new ImageBrowser(context);
    }

    public ImageBrowser setImageUrl(String imageUrl) {
        ArrayList<String> imageList = new ArrayList<>();
        imageList.add(imageUrl);
        mImageBrowserConfig.setImageList(imageList);
        return this;
    }

    public ImageBrowser setImageList(ArrayList<String> imageList) {
        ArrayList<String> newImageList = new ArrayList<>();
        newImageList.addAll(imageList);
        mImageBrowserConfig.setImageList(newImageList);
        return this;
    }

    public ImageBrowser setCurrentPosition(int position) {
        mImageBrowserConfig.setPosition(position);
        return this;
    }

    public ImageBrowser setTransformType(ImageBrowserConfig.TransformType transformType) {
        mImageBrowserConfig.setTransformType(transformType);
        return this;
    }

    public ImageBrowser setImageLoader(ImageLoader imageLoader) {
        mImageBrowserConfig.setImageLoader(imageLoader);
        return this;
    }

    public ImageBrowser setOnClickListener(OnClickListener onClickListener) {
        mImageBrowserConfig.setOnClickListener(onClickListener);
        return this;
    }

    public ImageBrowser setOnLongClickListener(OnLongClickListener onLongClickListener) {
        mImageBrowserConfig.setOnLongClickListener(onLongClickListener);
        return this;
    }

    public ImageBrowser setOnPageChangeListener(OnPageChangeListener onPageChangeListener) {
        mImageBrowserConfig.setOnPageChangeListener(onPageChangeListener);
        return this;
    }

    public ImageBrowser setIndicatorType(ImageBrowserConfig.IndicatorType indicatorType) {
        mImageBrowserConfig.setIndicatorType(indicatorType);
        return this;
    }

    public ImageBrowser setIndicatorHide(boolean indicatorHide) {
        mImageBrowserConfig.setIndicatorHide(indicatorHide);
        return this;
    }

    public ImageBrowser setCustomShadeView(View customView) {
        mImageBrowserConfig.setCustomShadeView(customView);
        return this;
    }

    public ImageBrowser setCustomProgressViewLayoutID(@LayoutRes int customViewID) {
        mImageBrowserConfig.setCustomProgressViewLayoutID(customViewID);
        return this;
    }

    public ImageBrowser setScreenOrientationType(ImageBrowserConfig.ScreenOrientationType screenOrientationType) {
        mImageBrowserConfig.setScreenOrientationType(screenOrientationType);
        return this;
    }

    public ImageBrowser setFullScreenMode(boolean fullScreenMode) {
        mImageBrowserConfig.setFullScreenMode(fullScreenMode);
        return this;
    }

    public void show(View view) {
        //判断是不是空
        if (mImageBrowserConfig.getImageList() == null || mImageBrowserConfig.getImageList().size() <= 0) {
            return;
        }
        if (mImageBrowserConfig.getImageLoader() == null) {
            return;
        }
        if (mImageBrowserConfig.getIndicatorType() == null) {
            mImageBrowserConfig.setIndicatorType(ImageBrowserConfig.IndicatorType.Indicator_Number);
        }
        ImageBrowserActivity.sImageBrowserConfig = mImageBrowserConfig;
        Intent intent = new Intent(mContext, ImageBrowserActivity.class);
        startBrowserAvtivity(mContext, view, intent);
    }

    private static void startBrowserAvtivity(Context context, View view, Intent intent) {
        //android V4包的类,用于两个activity转场时的缩放效果实现
        ActivityOptionsCompat optionsCompat = ActivityOptionsCompat.makeScaleUpAnimation(view, view.getWidth() / 2, view.getHeight() / 2, 0, 0);
        try {
            ActivityCompat.startActivity(context, intent, optionsCompat.toBundle());
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
            context.startActivity(intent);
            ((AppCompatActivity) context).overridePendingTransition(R.anim.browser_enter_anim, 0);
        }
    }

    /**
     * 获取当前Activity实例
     */
    public static FragmentActivity getCurrentActivity() {
        return ImageBrowserActivity.getCurrentActivity();
    }

    /**
     * 手动关闭图片浏览器
     */
    public static void finishImageBrowser() {
        ImageBrowserActivity.finishActivity();
    }

    /**
     * 获取当前ImageView
     */
    public static ImageView getCurrentImageView() {
        return ImageBrowserActivity.getCurrentImageView();
    }

    /**
     * 获取当前位置
     */
    public static int getCurrentPosition() {
        return ImageBrowserActivity.getCurrentPosition();
    }

    /**
     * 获取ViewPager
     */
    public static ViewPager getViewPager() {
        return ImageBrowserActivity.getViewPager();
    }

    /**
     * 删除图片
     *
     * @param position
     */
    public static void removeImage(int position) {
        ImageBrowserActivity.removeImage(position);
    }

    /**
     * 删除图片
     */
    public static void removeCurrentImage() {
        ImageBrowserActivity.removeCurrentImage();
    }

    /**
     * 获取图片集合
     */
    public static ArrayList<String> getImageList() {
        return ImageBrowserActivity.getImageList();
    }

}
