[![](https://jitpack.io/v/huangziye/ImageBrowser.svg)](https://jitpack.io/#huangziye/imageBrowser)

一个图片浏览框架,类似微信图片浏览,手势向下滑动关闭,图片加载引擎自定义,支持长按,单击监听,切换监听,自定义任意的遮罩层，实现各种效果,支持横竖屏切换,简单方便。


**效果图：**

![效果图](https://github.com/huangziye/ImageBrowser/blob/master/screenshot/imagebrowser.gif)



# Add ` imageBrowser ` to project

- Step 1：Add the JitPack repository to your build file

Add it in your root build.gradle at the end of repositories:

```android
allprojects {
    repositories {
        ...
        maven { url 'https://jitpack.io' }
    }
}
```

- Step 2：Add the dependency

The latest version shall prevail.

```android
dependencies {
        implementation 'com.github.huangziye:ImageBrowser:${latest_version}'
}
```


# 使用方式（详见demo）

```
ImageBrowser.with(mContext)
    //页面切换效果
    .setTransformType(mTransformType)
    //指示器效果
    .setIndicatorType(mIndicatorType)
    //设置隐藏指示器
    .setIndicatorHide(false)
    //设置自定义遮盖层，定制自己想要的效果，当设置遮盖层后，原本的指示器会被隐藏
    .setCustomShadeView(mShowCustomShadeView ? customView : null)
    //自定义ProgressView，不设置默认默认没有
    .setCustomProgressViewLayoutID(mShowCustomProgressView ? R.layout.layout_custom_progress_view : 0)
    //当前位置
    .setCurrentPosition(position)
    //图片引擎
    .setImageLoader(mImageLoader)
    //图片集合
    .setImageList(mImageList)
    //方向设置
    .setScreenOrientationType(mScreenOrientationType)
    //点击监听
    .setOnClickListener(new OnClickListener() {
        @Override
        public void onClick(FragmentActivity activity, ImageView imageView, int position, String url) {

        }
    })
    //长按监听
    .setOnLongClickListener(new OnLongClickListener() {
        @Override
        public void onLongClick(FragmentActivity activity, ImageView imageView, int position, String url) {

        }
    })
    //页面切换监听
    .setOnPageChangeListener(new OnPageChangeListener() {
        @Override
        public void onPageSelected(int position) {
            Log.i(TAG, "onPageSelected:" + position);
            if (tv_number_indicator != null) {
                tv_number_indicator.setText((position + 1) + "/" + ImageBrowser.getImageList().size());
            }
        }
    })
    //全屏模式
    .setFullScreenMode(mIsFulScreenMode)
    //显示：传入当前View
    .show(viewHolder.imageView);
```


# 图片加载器需要实现ImageLoader

# 动画效果（TransformType 切换效果提供7种效果）

```
ImageBrowserConfig：

//枚举类型：切换动画类型
public enum TransformType {
    Transform_Default,
    Transform_DepthPage,
    Transform_RotateDown,
    Transform_RotateUp,
    Transform_ZoomIn,
    Transform_ZoomOutSlide,
    Transform_ZoomOut,
}
```

# IndicatorType 指示器提供2种效果

```
ImageBrowserConfig：

//枚举类型：指示器类型
public enum IndicatorType {
    Indicator_Circle,
    Indicator_Number
}
```

# ScreenOrientationType 屏幕方向3种效果

```
ImageBrowserConfig：

//枚举类型：屏幕方向
public enum ScreenOrientationType {
    //默认：横竖屏全部支持
    Screenorientation_Default,
    //竖屏
    ScreenOrientation_Portrait,
    //横屏
    Screenorientation_Landscape,
}
```




# About me


- [简书](https://user-gold-cdn.xitu.io/2018/7/26/164d5709442f7342)

- [掘金](https://juejin.im/user/5ad93382518825671547306b)

- [Github](https://github.com/huangziye)


# License

```
Copyright 2018, huangziye

   Licensed under the Apache License, Version 2.0 (the "License");
   you may not use this file except in compliance with the License.
   You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

   Unless required by applicable law or agreed to in writing, software
   distributed under the License is distributed on an "AS IS" BASIS,
   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   See the License for the specific language governing permissions and
   limitations under the License.
```
