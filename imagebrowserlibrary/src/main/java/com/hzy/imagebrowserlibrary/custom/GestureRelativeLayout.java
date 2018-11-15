package com.hzy.imagebrowserlibrary.custom;

import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.ViewConfiguration;
import android.widget.RelativeLayout;

/**
 * Created by ziye_huang on 2018/11/14.
 */
public class GestureRelativeLayout extends RelativeLayout {

    private static final int mHeight = 500;
    private float mDisplacementX;
    private float mDisplacementY;
    private float mInitialTy;
    private float mInitialTx;
    private boolean mTracking;

    private OnSwipeListener mOnSwipeListener;
    private OnCanSwipeListener mOnCanSwipeListener;

    public GestureRelativeLayout(Context context) {
        this(context, null);
    }

    public GestureRelativeLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public GestureRelativeLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        return true;
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent event) {
        switch (event.getActionMasked()) {
            case MotionEvent.ACTION_DOWN:
                //获取在屏幕内的X坐标
                mDisplacementX = event.getRawX();
                //获取在屏幕内的Y坐标
                mDisplacementY = event.getRawY();
                //获取X坐标的偏移量
                mInitialTx = getTranslationX();
                //获取Y坐标的偏移量
                mInitialTy = getTranslationY();
                break;
            case MotionEvent.ACTION_MOVE:
                // get the delta distance in X and Y direction
                float deltaX = event.getRawX() - mDisplacementX;
                float deltaY = event.getRawY() - mDisplacementY;

                //只有在不缩放的状态才能下滑
                if (mOnCanSwipeListener != null) {
                    if (!mOnCanSwipeListener.canSwipe()) {
                        break;
                    }
                }

                // set the touch and cancel event
                if (deltaY > 0 && (Math.abs(deltaY) > ViewConfiguration.get(getContext()).getScaledTouchSlop() * 2 && Math.abs(deltaX) < Math.abs(deltaY) / 2) || mTracking) {
                    mOnSwipeListener.onSwiping(deltaY);
                    setBackgroundColor(Color.TRANSPARENT);
                    mTracking = true;
                    setTranslationY(mInitialTy + deltaY);
                    setTranslationX(mInitialTx + deltaX);
                    float mScale = 1 - deltaY / mHeight;
                    if (mScale < 0.3) {
                        mScale = 0.3f;
                    }
                    setScaleX(mScale);
                    setScaleY(mScale);
                }
                if (deltaY < 0) {
                    setViewDefault();
                }
                break;
            case MotionEvent.ACTION_UP:
                if (mTracking) {
                    mTracking = false;
                    float currentTranslateY = getTranslationY();
                    if (currentTranslateY > mHeight / 3) {
                        mOnSwipeListener.downSwipe();
                        break;
                    }
                }
                setViewDefault();
                mOnSwipeListener.overSwipe();
                break;
        }
        return false;
    }

    private void setViewDefault() {
        //恢复默认
        setAlpha(1);
        setTranslationX(0);
        setTranslationY(0);
        setScaleX(1);
        setScaleY(1);
        setBackgroundColor(Color.BLACK);
    }

    public interface OnSwipeListener {
        /**
         * 向下滑动
         */
        void downSwipe();

        /**
         * 结束
         */
        void overSwipe();

        /**
         * 正在滑动
         */
        void onSwiping(float y);
    }


    public void setOnSwipeListener(OnSwipeListener swipeListener) {
        this.mOnSwipeListener = swipeListener;
    }

    public void setOnGestureListener(OnCanSwipeListener onCanSwipeListener) {
        this.mOnCanSwipeListener = onCanSwipeListener;
    }

    /**
     * 是否可以滑动监听
     */
    public interface OnCanSwipeListener {
        //可不可以下滑关闭
        boolean canSwipe();
    }
}
