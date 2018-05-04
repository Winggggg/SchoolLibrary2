package com.example.wing.schoollibrary.pager.search.picture;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.example.wing.schoollibrary.R;

import java.io.File;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * 选择相册，手机所有的图片列表
 * Created by hupei on 2016/7/7.
 */
public class PickPictureTotalActivity extends Activity {
    public static final int REQUEST_CODE_SELECT_PICTURE = 102;
    public static final int REQUEST_CODE_SELECT_ALBUM = 104;
    public static final String EXTRA_PICTURE_PATH = "picture_path";
    public static final String EXTRA_PICTURE = "picture";

//    private HashMap<String, List<String>> mGroupMap = new HashMap<>();
    private HashMap<String, List<Picture>> mGroupMap = new HashMap<>();
    private List<PictureGroup> list = new ArrayList<>();
    private final static int SCAN_OK = 1;
    private final static int SCAN_ERROR = 2;
    private ProgressDialog mProgressDialog;
    private PickPictureTotalAdapter mAdapter;
    private ListView mListView;
    private final MyHandler myHandler = new MyHandler(this);

    private static class MyHandler extends Handler {
        private final WeakReference<PickPictureTotalActivity> mActivity;

        public MyHandler(PickPictureTotalActivity activity) {
            mActivity = new WeakReference<>(activity);
        }

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            PickPictureTotalActivity activity = mActivity.get();
            if (activity != null) {
                switch (msg.what) {
                    case SCAN_OK:
                        //关闭进度条
                        activity.mProgressDialog.dismiss();
                        activity.mAdapter = new PickPictureTotalAdapter(activity,
                                activity.list = activity.subGroupOfPicture(activity.mGroupMap));
                        activity.mListView.setAdapter(activity.mAdapter);
                        break;
                    case SCAN_ERROR:
                        activity.mProgressDialog.dismiss();
                        break;
                }
            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pick_picture_total);
        mListView = (ListView) findViewById(R.id.pick_picture_total_listView);
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                List<Picture> childList = mGroupMap.get(list.get(position).getFolderName());
                PickPictureActivity.gotoActivity(PickPictureTotalActivity.this, (ArrayList<Picture>) childList);
            }
        });
        getPicture();
    }

    @Override
    protected void onPause() {
        mProgressDialog.dismiss();
        super.onPause();
    }

    /**
     * 利用ContentProvider扫描手机中的图片，此方法在运行在子线程中
     */
    private void getPicture() {
        //显示进度条
        mProgressDialog = ProgressDialog.show(this, null, "正在加载");

        new Thread(new Runnable() {

            @Override
            public void run() {
                Uri pictureUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
                ContentResolver contentResolver = PickPictureTotalActivity.this.getContentResolver();

                //只查询jpeg和png的图片
                List<String> parameters = new ArrayList<>();
                parameters.add("image/jpeg");
                parameters.add("image/gif");
                parameters.add("image/png");
                Cursor cursor = contentResolver.query(pictureUri, null,
                        MediaStore.Images.Media.MIME_TYPE + " in("+"\'"+ TextUtils.join("\',\'",parameters)+"\'"+")",
                        null, MediaStore.Images.Media.DATE_MODIFIED);
                if (cursor == null || cursor.getCount() == 0) {
                    myHandler.sendEmptyMessage(SCAN_ERROR);
                } else {
                    while (cursor.moveToNext()) {
                        //获取图片的路径
                        String path = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA));
                        int width = cursor.getInt(cursor.getColumnIndex(MediaStore.Images.Media.WIDTH));
                        int height = cursor.getInt(cursor.getColumnIndex(MediaStore.Images.Media.HEIGHT));

//                        LogUtil.e("宽="+width+"高="+height);
                        Picture picture=new Picture(path,width,height);
                        try {
                            //获取该图片的父路径名
                            String parentName = new File(path).getParentFile().getName();
                            //根据父路径名将图片放入到groupMap中
                            if (!mGroupMap.containsKey(parentName)) {

                                List<Picture> chileList = new ArrayList<>();
//                                List<String> chileList = new ArrayList<>();
                                chileList.add(picture);
                                mGroupMap.put(parentName, chileList);
                            } else {
                                mGroupMap.get(parentName).add(picture);
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                    cursor.close();
                    //通知Handler扫描图片完成
                    myHandler.sendEmptyMessage(SCAN_OK);
                }
            }
        }).start();
    }


    /**
     * 组装分组界面GridView的数据源，因为我们扫描手机的时候将图片信息放在HashMap中
     * 所以需要遍历HashMap将数据组装成List
     *
     * @param groupMap
     * @return
     */
    private List<PictureGroup> subGroupOfPicture(HashMap<String, List<Picture>> groupMap) {
        if (groupMap.size() == 0) {
            return null;
        }
        List<PictureGroup> list = new ArrayList<>();
        Iterator<Map.Entry<String, List<Picture>>> it = groupMap.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry<String, List<Picture>> entry = it.next();
            PictureGroup pictureGroup = new PictureGroup();
            String key = entry.getKey();
            List<Picture> value = entry.getValue();
            SortPictureList sortList = new SortPictureList();
            //按修改时间排序
            Collections.sort(value, sortList);
            pictureGroup.setFolderName(key);
            pictureGroup.setPictureCount(value.size());
            pictureGroup.setTopPicturePath(value.get(0).getPath());//获取该组的第一张图片
            list.add(pictureGroup);
        }
        return list;

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != Activity.RESULT_CANCELED && resultCode == Activity.RESULT_OK) {
            if (requestCode == PickPictureTotalActivity.REQUEST_CODE_SELECT_ALBUM) {
                setResult(Activity.RESULT_OK, data);
                finish();
            }
        }
    }

    public static void gotoActivity(Activity activity) {
        Intent intent = new Intent(activity, PickPictureTotalActivity.class);
        activity.startActivityForResult(intent, REQUEST_CODE_SELECT_PICTURE);
    }
}

