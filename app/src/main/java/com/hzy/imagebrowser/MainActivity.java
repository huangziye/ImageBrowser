package com.hzy.imagebrowser;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.hzy.imagebrowserlibrary.ImageBrowser;
import com.hzy.imagebrowserlibrary.ImageLoader;
import com.hzy.imagebrowserlibrary.config.ImageBrowserConfig;
import com.hzy.imagebrowserlibrary.listener.OnClickListener;
import com.hzy.imagebrowserlibrary.listener.OnLongClickListener;
import com.hzy.imagebrowserlibrary.listener.OnPageChangeListener;

import java.util.ArrayList;


public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";
    protected GridView mGridView;
    private ArrayList<String> mImageList;
    private Context mContext;

    public ImageBrowserConfig.TransformType mTransformType = ImageBrowserConfig.TransformType.Transform_Default;
    public ImageBrowserConfig.IndicatorType mIndicatorType = ImageBrowserConfig.IndicatorType.Indicator_Number;
    public ImageBrowserConfig.ScreenOrientationType mScreenOrientationType = ImageBrowserConfig.ScreenOrientationType.ScreenOrientation_Default;
    private ImageLoader mImageLoader = new GlideImageLoader();

    //显示自定义遮盖层
    private boolean mShowCustomShadeView = false;
    //显示ProgressView
    private boolean mShowCustomProgressView = false;
    //是不是全屏模式
    private boolean mIsFulScreenMode = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mContext = this;

        mGridView = findViewById(R.id.gridView);
        mImageList = DataUtil.getDatas();
        mGridView.setAdapter(new NineGridAdapter());
    }

    private class NineGridAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return mImageList.size();
        }

        @Override
        public Object getItem(int position) {
            return mImageList.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup viewGroup) {
            final ViewHolder viewHolder;
            if (convertView == null) {
                viewHolder = new ViewHolder();
                convertView = LayoutInflater.from(mContext).inflate(R.layout.image_item, null);
                viewHolder.imageView = convertView.findViewById(R.id.imageView);
                convertView.setTag(viewHolder);
            } else {
                viewHolder = (ViewHolder) convertView.getTag();
            }

            GlideApp.with(mContext).load(mImageList.get(position)).placeholder(R.drawable.default_placeholder).error(R.mipmap.ic_launcher).into(viewHolder.imageView);

            viewHolder.imageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    //获取一个自定义遮盖层View
                    View customView = LayoutInflater.from(mContext).inflate(R.layout.layout_custom_view, null);
                    ImageView ic_close = customView.findViewById(R.id.iv_close);
                    ImageView iv_more = customView.findViewById(R.id.iv_more);
                    ImageView iv_comment = customView.findViewById(R.id.iv_comment);
                    ImageView iv_zan = customView.findViewById(R.id.iv_zan);
                    ImageView iv_delete = customView.findViewById(R.id.iv_delete);
                    final TextView tv_number_indicator = customView.findViewById(R.id.tv_number_indicator);
                    ic_close.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            //关闭图片浏览
                            ImageBrowser.finishImageBrowser();
                        }
                    });
                    iv_zan.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Toast.makeText(mContext, "点赞" + ImageBrowser.getCurrentPosition(), Toast.LENGTH_SHORT).show();
                        }
                    });
                    iv_more.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            FragmentActivity currentActivity = ImageBrowser.getCurrentActivity();
                            ImageView currentImageView = ImageBrowser.getCurrentImageView();
                            if (currentImageView != null && currentActivity != null) {
                            }
                        }
                    });
                    iv_comment.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Toast.makeText(mContext, "评论" + ImageBrowser.getCurrentPosition(), Toast.LENGTH_SHORT).show();
                        }
                    });
                    iv_delete.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            //删除当前位置的图片
                            ImageBrowser.removeCurrentImage();
                            //刷新指示器
                            tv_number_indicator.setText((ImageBrowser.getCurrentPosition() + 1) + "/" + ImageBrowser.getImageList().size());
                        }
                    });
                    tv_number_indicator.setText((position + 1) + "/" + mImageList.size());


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
                }
            });
            return convertView;
        }

        public final class ViewHolder {
            ImageView imageView;
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_01:
                mTransformType = ImageBrowserConfig.TransformType.Transform_Default;
                break;
            case R.id.menu_02:
                mTransformType = ImageBrowserConfig.TransformType.Transform_DepthPage;
                break;
            case R.id.menu_03:
                mTransformType = ImageBrowserConfig.TransformType.Transform_RotateDown;
                break;
            case R.id.menu_04:
                mTransformType = ImageBrowserConfig.TransformType.Transform_RotateUp;
                break;
            case R.id.menu_05:
                mTransformType = ImageBrowserConfig.TransformType.Transform_ZoomIn;
                break;
            case R.id.menu_06:
                mTransformType = ImageBrowserConfig.TransformType.Transform_ZoomOutSlide;
                break;
            case R.id.menu_07:
                mTransformType = ImageBrowserConfig.TransformType.Transform_ZoomOut;
                break;
            case R.id.menu_08:
                mIndicatorType = ImageBrowserConfig.IndicatorType.Indicator_Number;
                break;
            case R.id.menu_09:
                mIndicatorType = ImageBrowserConfig.IndicatorType.Indicator_Circle;
                break;
            case R.id.menu_10:
                mImageLoader = new GlideImageLoader();
                break;
            case R.id.menu_11:
                //                mImageLoader = new PicassoImageEngine();
                break;
            case R.id.menu_12:
                mScreenOrientationType = ImageBrowserConfig.ScreenOrientationType.ScreenOrientation_Default;
                break;
            case R.id.menu_13:
                mScreenOrientationType = ImageBrowserConfig.ScreenOrientationType.ScreenOrientation_Portrait;
                break;
            case R.id.menu_14:
                mScreenOrientationType = ImageBrowserConfig.ScreenOrientationType.ScreenOrientation_Landscape;
                break;
            case R.id.menu_15:
                mShowCustomShadeView = true;
                break;
            case R.id.menu_16:
                mShowCustomShadeView = false;
                break;
            case R.id.menu_17:
                mShowCustomProgressView = false;
                break;
            case R.id.menu_18:
                mShowCustomProgressView = true;
                break;
            case R.id.menu_19:
                mIsFulScreenMode = true;
                break;
            case R.id.menu_20:
                mIsFulScreenMode = false;
                break;
            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}
