package com.hzy.imagebrowserlibrary.config;

import android.support.annotation.LayoutRes;
import android.view.View;

import com.hzy.imagebrowserlibrary.ImageLoader;
import com.hzy.imagebrowserlibrary.listener.OnClickListener;
import com.hzy.imagebrowserlibrary.listener.OnLongClickListener;
import com.hzy.imagebrowserlibrary.listener.OnPageChangeListener;

import java.util.ArrayList;



/**
 * 相关配置信息
 * Created by ziye_huang on 2018/11/14.
 */
public class ImageBrowserConfig {

    //枚举类型：切换动画类型
    public enum TransformType {
        Transform_Default, Transform_DepthPage, Transform_RotateDown, Transform_RotateUp, Transform_ZoomIn, Transform_ZoomOutSlide, Transform_ZoomOut,
    }

    //枚举类型：指示器类型
    public enum IndicatorType {
        Indicator_Circle, Indicator_Number,
    }

    //枚举类型：屏幕方向
    public enum ScreenOrientationType {
        //默认：横竖屏全部支持
        ScreenOrientation_Default, //竖屏
        ScreenOrientation_Portrait, //横屏
        ScreenOrientation_Landscape,
    }

    //当前位置
    private int mPosition;
    //切换效果
    private TransformType mTransformType = TransformType.Transform_Default;
    //指示器类型
    private IndicatorType mIndicatorType = IndicatorType.Indicator_Number;
    //图片源
    private ArrayList<String> mImageList;
    //图片加载引擎
    private ImageLoader mImageLoader;
    //单击监听
    private OnClickListener mOnClickListener;
    //长按监听
    private OnLongClickListener mOnLongClickListener;
    //页面切换监听
    private OnPageChangeListener mOnPageChangeListener;
    //设置屏幕的方向
    private ScreenOrientationType mScreenOrientationType = ScreenOrientationType.ScreenOrientation_Default;
    //是否隐藏指示器
    private boolean mIndicatorHide = false;
    //自定义View
    private View mCustomShadeView;
    //自定义ProgressView
    private int mCustomProgressViewLayoutID;
    //全部模式：默认true
    private boolean mIsFullScreenMode = true;

    public boolean isFullScreenMode() {
        return mIsFullScreenMode;
    }

    public void setFullScreenMode(boolean fullScreenMode) {
        mIsFullScreenMode = fullScreenMode;
    }

    public int getCustomProgressViewLayoutID() {
        return mCustomProgressViewLayoutID;
    }

    public void setCustomProgressViewLayoutID(@LayoutRes int customProgressViewLayoutID) {
        this.mCustomProgressViewLayoutID = customProgressViewLayoutID;
    }

    public View getCustomShadeView() {
        return mCustomShadeView;
    }

    public void setCustomShadeView(View customView) {
        this.mCustomShadeView = customView;
    }

    public boolean isIndicatorHide() {
        return mIndicatorHide;
    }

    public void setIndicatorHide(boolean indicatorHide) {
        this.mIndicatorHide = indicatorHide;
    }

    public ScreenOrientationType getScreenOrientationType() {
        return mScreenOrientationType;
    }

    public void setScreenOrientationType(ScreenOrientationType screenOrientationType) {
        this.mScreenOrientationType = screenOrientationType;
    }

    public OnPageChangeListener getOnPageChangeListener() {
        return mOnPageChangeListener;
    }

    public void setOnPageChangeListener(OnPageChangeListener onPageChangeListener) {
        this.mOnPageChangeListener = onPageChangeListener;
    }

    public IndicatorType getIndicatorType() {
        return mIndicatorType;
    }

    public void setIndicatorType(IndicatorType indicatorType) {
        this.mIndicatorType = indicatorType;
    }

    public OnClickListener getOnClickListener() {
        return mOnClickListener;
    }

    public void setOnClickListener(OnClickListener onClickListener) {
        this.mOnClickListener = onClickListener;
    }

    public OnLongClickListener getOnLongClickListener() {
        return mOnLongClickListener;
    }

    public void setOnLongClickListener(OnLongClickListener onLongClickListener) {
        this.mOnLongClickListener = onLongClickListener;
    }

    public ArrayList<String> getImageList() {
        return mImageList;
    }

    public void setImageList(ArrayList<String> imageList) {
        this.mImageList = imageList;
    }

    public int getPosition() {
        return mPosition;
    }

    public void setPosition(int position) {
        this.mPosition = position;
    }

    public ImageLoader getImageLoader() {
        return mImageLoader;
    }

    public void setImageLoader(ImageLoader imageLoader) {
        mImageLoader = imageLoader;
    }

    public TransformType getTransformType() {
        return mTransformType;
    }

    public void setTransformType(TransformType transformType) {
        this.mTransformType = transformType;
    }
}
