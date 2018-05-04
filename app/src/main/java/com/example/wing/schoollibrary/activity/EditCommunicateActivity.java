package com.example.wing.schoollibrary.activity;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.text.format.Formatter;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.wing.schoollibrary.MyApplication;
import com.example.wing.schoollibrary.R;
import com.example.wing.schoollibrary.bean.Communicate;
import com.example.wing.schoollibrary.bean.MediaBean;
import com.example.wing.schoollibrary.bean.Student;
import com.example.wing.schoollibrary.pager.search.picture.PickPictureTotalActivity;
import com.example.wing.schoollibrary.pager.search.picture.Picture;
import com.example.wing.schoollibrary.util.LogUtil;
import com.example.wing.schoollibrary.util.Urls;
import com.example.wing.schoollibrary.util.Utils;
import com.google.gson.Gson;
import com.lzy.okhttputils.OkHttpUtils;
import com.lzy.okhttputils.callback.StringCallback;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import okhttp3.Call;
import okhttp3.Request;
import okhttp3.Response;

import static com.example.wing.schoollibrary.activity.ScannerActivity.APPLY_READ_EXTERNAL_STORAGE;

public class EditCommunicateActivity extends Activity implements View.OnClickListener {
    /***
     * 文本类型
      */
    private static final  String  TEXT_TYPE="text";
    /**
     * 图片类型
     */
    private static final  String  IMAGE_TYPE="image";
    /**
     * 视频类型
     */
    private static final  String  VIDEO_TYPE="video";
    /**
     * gif
     */
    private static final  String  GIF_TYPE="gif";
    /**
     * 判断是哪种类型，做标识
     */
    private String flag;
    private ImageButton ib_back;
    private TextView tv_send;
    private ImageButton ib_select_image;
    private EditText et_text;
//    private String picturePath;
    private Picture picture;
    /***
     * 选中要上传的视频
     */
    private MediaBean mediaBean;
    private ProgressBar pb_loading;
    private List<MediaBean> mediaItems=new ArrayList<MediaBean>();
    private Utils utils;
    /**
     * 当前用户是否登录
     */
    private Student currentstudent;
    private static final  long MAX_UPLOAD=10485760;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_communicate);

        initView();
    }

    private void initView() {
        ib_back= (ImageButton) findViewById(R.id.ib_back);
        ib_select_image= (ImageButton) findViewById(R.id.ib_select_image);
        tv_send= (TextView) findViewById(R.id.tv_send);
        et_text= (EditText) findViewById(R.id.et_text);
        pb_loading= (ProgressBar) findViewById(R.id.pb_loading);

        tv_send.setVisibility(View.VISIBLE);

        ib_back.setOnClickListener(this);
        ib_select_image.setOnClickListener(this);
        tv_send.setOnClickListener(this);

        utils=new Utils();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.ib_back://回退
                finish();
                break;
            case R.id.tv_send://发送
                if (checkLogin()){
                    uploadImageAndText();
                }
                break;
            case R.id.ib_select_image://选择图片或视频
                registerForContextMenu(ib_select_image);
                break;
        }
    }




    /**
     * 检查是否已经登录
     * @return
     */
    private boolean checkLogin() {
        MyApplication app= (MyApplication) getApplicationContext();
        currentstudent=app.getStudent();
        if (currentstudent!=null){
            return true;
        }else {
            //跳去登录页面
            startActivity(new Intent(this,LoginActivity.class));
            return false;
        }

    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        menu.add(Menu.NONE, Menu.FIRST + 1, 5, "图片").setIcon(
                android.R.drawable.ic_menu_delete);
        // setIcon()方法设置菜单图标
        menu.add(Menu.NONE, Menu.FIRST + 2, 2, "视频").setIcon(
                android.R.drawable.ic_menu_save);
        super.onCreateContextMenu(menu, v, menuInfo);
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case Menu.FIRST+1:
                goTOSelectImage();
                break;
            case Menu.FIRST+2:
                goTOSelectVideo();
                Toast.makeText(EditCommunicateActivity.this,"视频",Toast.LENGTH_SHORT).show();
                break;
        }
        return super.onContextItemSelected(item);
    }

    /**
     * 选择视频
     */
    private void goTOSelectVideo() {

        if (ContextCompat.checkSelfPermission(EditCommunicateActivity.this, Manifest.permission
                .WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            //权限还没有授予，需要在这里写申请权限的代码
            ActivityCompat.requestPermissions(EditCommunicateActivity.this,
                    new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                    APPLY_READ_EXTERNAL_STORAGE);
        } else {
            Toast.makeText(this,"选择视频",Toast.LENGTH_LONG).show();
            VideoListActivity.gotoActivity(this);
        }
    }








    /**
     * 选择图片
     */
    private void goTOSelectImage() {
        if (ContextCompat.checkSelfPermission(EditCommunicateActivity.this, Manifest.permission
                .WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            //权限还没有授予，需要在这里写申请权限的代码
            ActivityCompat.requestPermissions(EditCommunicateActivity.this,
                    new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                    APPLY_READ_EXTERNAL_STORAGE);
        } else {
            Toast.makeText(this,"选择相册",Toast.LENGTH_LONG).show();
            PickPictureTotalActivity.gotoActivity(EditCommunicateActivity.this);

        }
    }

    /**
     * 上传图片和文字到服务器
     */
    private void uploadImageAndText() {
        pb_loading.setVisibility(View.VISIBLE);

        String text=et_text.getText().toString();

        Communicate communicate=new Communicate();
//        communicate.setText("测试上传");
        if (!TextUtils.isEmpty(text)){
            flag=TEXT_TYPE;
            communicate.setText(text);
            communicate.setType(TEXT_TYPE);
        }
        if (picture!=null&&!TextUtils.isEmpty(picture.getPath())){
            String extName = picture.getPath().substring(picture.getPath().lastIndexOf("."));
            if(extName.equals(".png")||extName.equals(".jpg")||extName.equals(".jpeg")){
                flag=IMAGE_TYPE;
            }else if (extName.equals(".gif")){
                flag=GIF_TYPE;
            }
        }

        if(mediaBean!=null&&mediaBean.getPath()!=null&&!TextUtils.isEmpty(mediaBean.getPath())){
            flag=VIDEO_TYPE;
        }





        Toast.makeText(this,"发送中。。",Toast.LENGTH_SHORT).show();

        //判断学生是否登陆了，先省略,
        switch (flag){
            case IMAGE_TYPE:
            case GIF_TYPE:
                sendImageOrGif(communicate);
                break;
            case VIDEO_TYPE:
                sendVideo(communicate);
                break;
            case TEXT_TYPE:
                sendText(communicate);
                break;
        }



    }

    /**
     * 纯文本发送
     */
    private void sendText(Communicate communicate) {
        communicate.setPublishTime(getTime());
        communicate.setStuid(currentstudent.getStuId());//判断学生登录后返回学生

        String jsonstr=new Gson().toJson(communicate);
        OkHttpUtils.post(Urls.URL_TEXT_UPLOAD)//
                .tag(this)//
                .params("jsonstr",jsonstr)
                .execute(new MyStringCallBack());
    }

    /**
     * 发送视频
     * @param communicate
     */
    private void sendVideo(Communicate communicate) {
        if (mediaBean!=null){
            communicate.setType(VIDEO_TYPE);
        }

        communicate.setPublishTime(getTime());
        communicate.setStuid(currentstudent.getStuId());//判断学生登录后返回学生

        String jsonstr=new Gson().toJson(communicate);

        File file=new File(mediaBean.getPath());
        LogUtil.e("视频w大小"+file.length());

        if (file.isFile()&&file.length()<MAX_UPLOAD){
            OkHttpUtils.post(Urls.URL_FORM_UPLOAD)//
                    .tag(this)//
                    .params("file",file)   //这种方式为一个key，对应一个文件
                    .params("jsonstr",jsonstr)
                    .execute(new MyStringCallBack());
        }else{
            Toast.makeText(this,"发布失败，上传小视频大小不能超过10m",Toast.LENGTH_SHORT).show();
        }

    }

    /**
     * 发送图片或者gif
     * @param communicate
     */
    private void sendImageOrGif(Communicate communicate) {

//        picturePath="/storage/emulated/0/DCIM/Camera/test.gif";
        if (picture!=null&&!TextUtils.isEmpty(picture.getPath())){
            if(flag.equals(IMAGE_TYPE)){
                communicate.setType(IMAGE_TYPE);
            }else if (flag.equals(GIF_TYPE)){
                communicate.setType(GIF_TYPE);
            }
        }


        communicate.setPublishTime(getTime());
        communicate.setStuid(currentstudent.getStuId());//判断学生登录后返回学生

        String jsonstr=new Gson().toJson(communicate);

        OkHttpUtils.post(Urls.URL_FORM_UPLOAD)//
                .tag(this)//
                .params("file",new File(picture.getPath()))   //这种方式为一个key，对应一个文件
                .params("jsonstr",jsonstr)
                .params("width",picture.getWidth()+"")
                .params("height",picture.getHeight()+"")
                .execute(new MyStringCallBack());

    }

    private class MyStringCallBack extends StringCallback {


        @Override
        public void onResponse(boolean isFromCache, String s, Request request, @Nullable Response response) {

        }

        @Override
        public void onAfter(boolean isFromCache, @Nullable String s, Call call, @Nullable Response response, @Nullable Exception e) {
            super.onAfter(isFromCache, s, call, response, e);
            pb_loading.setVisibility(View.GONE);
            EditCommunicateActivity.this.finish();
        }

        @Override
        public void onError(boolean isFromCache, Call call, @Nullable Response response, @Nullable Exception e) {
            super.onError(isFromCache, call, response, e);
            pb_loading.setVisibility(View.GONE);
            EditCommunicateActivity.this.finish();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != Activity.RESULT_CANCELED && resultCode == Activity.RESULT_OK) {
            if (requestCode == PickPictureTotalActivity.REQUEST_CODE_SELECT_PICTURE) {
//                picturePath = data.getStringExtra(PickPictureTotalActivity
//                        .EXTRA_PICTURE_PATH);
                picture= (Picture) data.getSerializableExtra(PickPictureTotalActivity.EXTRA_PICTURE);
                //得到选择图片的路径
                Toast.makeText(this,"图片路径=="+picture.getPath(),Toast.LENGTH_SHORT).show();
                LogUtil.e("图片路径=="+picture.getPath());
                setImageOrGifView(picture.getPath());

            }else if (requestCode==VideoListActivity.REQUEST_CODE_SELECT_VIDEO){
                mediaBean= (MediaBean) data.getSerializableExtra("media");
                //得到选择图片的路径
                Toast.makeText(this,"视频路径=="+mediaBean.getPath(),Toast.LENGTH_SHORT).show();
                LogUtil.e("视频路径=="+mediaBean.getPath());
                setVideoView(mediaBean);

            }
        }
    }

    /**
     * 设置image或者gifview
     */
    private void setImageOrGifView(String picturePath) {
        ImageView imageView= (ImageView) findViewById(R.id.iv_upload_image);
        imageView.setVisibility(View.VISIBLE);
        ib_select_image.setVisibility(View.GONE);
        if (picturePath!=null&&!TextUtils.isEmpty(picturePath)){
            Glide.with(this).load(picturePath).into(imageView);
        }
    }

    private void setVideoView(MediaBean mediaBean) {
        RelativeLayout relativeLayout= (RelativeLayout) findViewById(R.id.media_item);
        ImageView iv_mediaIcon= (ImageView) findViewById(R.id.iv_mediaIcon);
        TextView tv_mediaName= (TextView) findViewById(R.id.tv_mediaName);
        TextView tv_size= (TextView) findViewById(R.id.tv_size);
        TextView tv_time= (TextView) findViewById(R.id.tv_time);
        relativeLayout.setVisibility(View.VISIBLE);
        ib_select_image.setVisibility(View.GONE);

        if(mediaBean.getThumbPath()!=null){
            Toast.makeText(this,"视频缩略图不为空",Toast.LENGTH_SHORT).show();
            iv_mediaIcon.setImageURI(Uri.parse(mediaBean.getThumbPath()));
        }
        tv_mediaName.setText(mediaBean.getMediaName());
        tv_time.setText(utils.stringForTime((int) mediaBean.getMediaTime()));
        tv_size.setText(Formatter.formatFileSize(this,mediaBean.getSize()));


    }

    private String getTime() {
        return new SimpleDateFormat("MM-dd HH:mm", Locale.CHINA).format(new Date());
    }
}
