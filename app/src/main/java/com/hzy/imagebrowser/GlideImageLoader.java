package com.hzy.imagebrowser;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.hzy.imagebrowserlibrary.ImageLoader;


/**
 * Created by ziye_huang on 2018/11/14.
 */
public class GlideImageLoader implements ImageLoader {
    @Override
    public void loadImage(Context context, String url, ImageView imageView, final View progressView) {
        GlideApp.with(context).asBitmap().load(url).fitCenter().placeholder(R.drawable.default_placeholder).error(R.mipmap.ic_launcher).listener(new RequestListener<Bitmap>() {
            @Override
            public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Bitmap> target, boolean isFirstResource) {
                progressView.setVisibility(View.GONE);
                return false;
            }

            @Override
            public boolean onResourceReady(Bitmap resource, Object model, Target<Bitmap> target, DataSource dataSource, boolean isFirstResource) {
                progressView.setVisibility(View.GONE);
                return false;
            }
        }).into(imageView);
    }
}
