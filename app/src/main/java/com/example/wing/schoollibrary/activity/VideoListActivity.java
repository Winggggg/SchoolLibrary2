package com.example.wing.schoollibrary.activity;

import android.Manifest;
import android.app.Activity;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.wing.schoollibrary.R;
import com.example.wing.schoollibrary.adapter.MediaAdapter;
import com.example.wing.schoollibrary.bean.MediaBean;

import java.util.ArrayList;
import java.util.List;

import static com.lzy.okhttputils.interceptor.LoggerInterceptor.TAG;

public class VideoListActivity extends Activity {

    private ListView listView;
    private TextView tv_nomedia;
    private ProgressBar pb_loading;
    private List<MediaBean> mediaItems=new ArrayList<MediaBean>();
    private MediaAdapter adapter;
    private TextView tv_title;
    /**
     * 视频请求码
     */
    public static final int REQUEST_CODE_SELECT_VIDEO = 3;

    private Handler handler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (mediaItems.size()>0){
                adapter=new MediaAdapter(VideoListActivity.this,mediaItems);
                listView.setAdapter(adapter);
            }else{
                //显示文本
                tv_nomedia.setVisibility(View.VISIBLE);
            }

            pb_loading.setVisibility(View.GONE);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_list);

        initView();

        initData();
    }

    private void initData() {
        getlocaldataFromSD();
    }


    /**
     * 从sd卡中获取视频数据
     */
    private void getlocaldataFromSD() {


        isGrantExternalRW(this);
        //查询视频数据
        Uri uri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
        String[] thumbColumns = {
                MediaStore.Video.Thumbnails._ID,
                MediaStore.Video.Thumbnails.DATA,//缩略图路径
        };
        String[] datas = {
                MediaStore.Video.Media.DISPLAY_NAME,//视频在sd卡名称
                MediaStore.Video.Media.DURATION,//视频总时长
                MediaStore.Video.Media.SIZE,//视频文件大小
                MediaStore.Video.Media.DATA,//视频绝对地址
                MediaStore.Video.Media.ARTIST, //歌曲的演唱者
                MediaStore.Video.Media._ID,
        };
        CursorLoader cursorLoader1=new CursorLoader(this,uri,datas,null,null,null);
        Cursor cursor=cursorLoader1.loadInBackground();
        if (cursor != null) {
            while (cursor.moveToNext()) {
                MediaBean mediaBean = new MediaBean();
                //封装数据
                String mediaName = cursor.getString(0);
                mediaBean.setMediaName(mediaName);
                long mediaTime = cursor.getLong(1);
                mediaBean.setMediaTime(mediaTime);
                long size = cursor.getLong(2);
                mediaBean.setSize(size);
                String path = cursor.getString(3);
                mediaBean.setPath(path);
                String author = cursor.getString(4);
                mediaBean.setAuthor(author);

                //获取当前video对应的id，然后根据id查询thumbPath
                int id = cursor.getInt(5);
                String selection = MediaStore.Video.Thumbnails._ID + "=?";
                String[] selectionArg = {
                        id + ""
                };
                CursorLoader cursorLoader2 = new CursorLoader(this, MediaStore.Video.Thumbnails.EXTERNAL_CONTENT_URI, thumbColumns, selection, selectionArg, null);
                Cursor cursor_thumb = cursorLoader2.loadInBackground();
                while (cursor_thumb.moveToNext()) {
                    String thumbpath = cursor_thumb.getString(1);
                    Log.e(TAG, "缩略图路径==" + thumbpath);
                    mediaBean.setThumbPath(thumbpath);
                }

                mediaItems.add(mediaBean);
            }
            cursor.close();
        }


        //发送消息到主线程处理
        handler.sendEmptyMessage(10);

    }


    /**
     * 解决安卓6.0以上版本不能读取外部存储权限的问题
     * @param activity
     * @return
     */
    public static boolean isGrantExternalRW(Activity activity) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && activity.checkSelfPermission(
                Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {

            activity.requestPermissions(new String[]{
                    Manifest.permission.READ_EXTERNAL_STORAGE,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE
            }, 1);

            return false;
        }

        return true;
    }

    private void initView() {
        listView=(ListView) findViewById(R.id.listView);
        tv_nomedia=(TextView) findViewById(R.id.tv_nomedia);
        pb_loading=(ProgressBar)findViewById(R.id.pb_loading);
        tv_title= (TextView) findViewById(R.id.tv_title);

        tv_title.setText("视频选择");
        listView.setOnItemClickListener(new MyOnItemClickListener());
    }


    private class MyOnItemClickListener implements android.widget.AdapterView.OnItemClickListener {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            Intent intent=new Intent();
            intent.putExtra("media",mediaItems.get(position));
            VideoListActivity.this.setResult(RESULT_OK,intent);
            finish();
        }
    }

    public static void gotoActivity(Activity activity) {
        Intent intent = new Intent(activity, VideoListActivity.class);
        activity.startActivityForResult(intent, REQUEST_CODE_SELECT_VIDEO);
    }
}
