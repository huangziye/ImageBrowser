package com.hzy.imagebrowserlibrary.listener;

import android.support.v4.app.FragmentActivity;
import android.widget.ImageView;


/**
 * Created by ziye_huang on 2018/11/14.
 */
public interface OnClickListener {
    void onClick(FragmentActivity activity, ImageView imageView, int position, String url);
}
