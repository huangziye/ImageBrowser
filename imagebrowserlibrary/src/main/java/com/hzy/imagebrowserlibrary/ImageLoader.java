package com.hzy.imagebrowserlibrary;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;

/**
 * Created by ziye_huang on 2018/11/14.
 */
public interface ImageLoader {
    /**
     * 加载图片方法
     *
     * @param context      上下文
     * @param url          图片地址
     * @param imageView    ImageView
     * @param progressView 进度View
     */
    void loadImage(Context context, String url, ImageView imageView, View progressView);
}
