package com.example.wing.schoollibrary.pager.search.picture;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;

import com.example.wing.schoollibrary.R;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * 手机图片列表中的详细
 * Created by hupei on 2016/7/7.
 */
public class PickPictureActivity extends Activity {
    private GridView mGridView;
    private List<Picture> mList;//此相册下所有图片的路径集合
    private PickPictureAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pick_picture);
        mGridView = (GridView) findViewById(R.id.child_grid);
        mGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                setResult(mList.get(position));
            }
        });
        processExtraData();
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
        processExtraData();
    }

    private void processExtraData() {
        Bundle extras = getIntent().getExtras();
        if (extras == null) return;
        mList = (List<Picture>) extras.getSerializable("data");
        if (mList.size() > 1) {
            SortPictureList sortList = new SortPictureList();
            Collections.sort(mList, sortList);
        }
        mAdapter = new PickPictureAdapter(this, mList);
        mGridView.setAdapter(mAdapter);
    }

//    private void setResult(String picturePath) {
//        Intent intent = new Intent();
//        intent.putExtra(PickPictureTotalActivity.EXTRA_PICTURE_PATH, picturePath);
//        setResult(Activity.RESULT_OK, intent);
//        finish();
//    }
    private void setResult(Picture picture) {
        Intent intent = new Intent();
//        intent.putExtra(PickPictureTotalActivity.EXTRA_PICTURE_PATH, picture);
        intent.putExtra(PickPictureTotalActivity.EXTRA_PICTURE, picture);
        setResult(Activity.RESULT_OK, intent);
        finish();
    }

    public static void gotoActivity(Activity activity, ArrayList<Picture> childList) {
        Intent intent = new Intent(activity, PickPictureActivity.class);
        intent.putExtra("data", childList);
//        intent.putStringArrayListExtra("data", childList);
        activity.startActivityForResult(intent, PickPictureTotalActivity.REQUEST_CODE_SELECT_ALBUM);
    }
}

