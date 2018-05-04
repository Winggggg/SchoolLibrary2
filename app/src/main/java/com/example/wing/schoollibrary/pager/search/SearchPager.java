package com.example.wing.schoollibrary.pager.search;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;

import com.example.wing.schoollibrary.MyApplication;
import com.example.wing.schoollibrary.R;
import com.example.wing.schoollibrary.activity.LoginActivity;
import com.example.wing.schoollibrary.activity.ScannerActivity;
import com.example.wing.schoollibrary.activity.SearchContentActivity;
import com.example.wing.schoollibrary.base.BasePager;
import com.example.wing.schoollibrary.bean.Book;
import com.example.wing.schoollibrary.bean.Student;
import com.example.wing.schoollibrary.util.CacheUtils;
import com.example.wing.schoollibrary.util.LogUtil;
import com.example.wing.schoollibrary.util.MSG;
import com.example.wing.schoollibrary.util.Urls;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.lzy.okhttputils.OkHttpUtils;
import com.lzy.okhttputils.callback.StringCallback;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import okhttp3.Call;
import okhttp3.Request;
import okhttp3.Response;

import static com.example.wing.schoollibrary.R.id.btn_scan;


/**
 * Created by Administrator on 2017/12/15 0015.
 * 书目检索
 */

public class SearchPager extends BasePager implements View.OnClickListener {

    private static final String TAG = SearchPager.class.getSimpleName();

    private Button btnScan;
    private EditText etSearchName;
    private EditText etSearchAuthor;
    private EditText etSearchPublisher;
    private EditText etSearchType;
    private EditText etSearchAr;
    private EditText etSearchPlace;
    private EditText etSearchIsbn;
    private EditText etSearchIssn;
    private Button btnSearch;
    private Button btnClear;
    private ProgressBar pb_loading;
    private Map<String,String> cacheMap;
    private List<Book> bookList;
    private MyApplication app;

    /**
     * Find the Views in the layout<br />
     * <br />
     * Auto-created on 2018-03-13 11:43:58 by Android Layout Finder
     * (http://www.buzzingandroid.com/tools/android-layout-finder)
     */
    private void findViews(View view) {
        btnScan = (Button)view.findViewById( btn_scan );
        etSearchName = (EditText)view.findViewById( R.id.et_search_name );
        etSearchAuthor = (EditText)view.findViewById( R.id.et_search_author );
        etSearchPublisher = (EditText)view.findViewById( R.id.et_search_publisher );
        etSearchType = (EditText)view.findViewById( R.id.et_search_type );
        etSearchAr = (EditText)view.findViewById( R.id.et_search_ar );
        etSearchPlace = (EditText)view.findViewById( R.id.et_search_place );
        etSearchIsbn = (EditText)view.findViewById( R.id.et_search_isbn );
        etSearchIssn = (EditText)view.findViewById( R.id.et_search_issn );
        btnSearch = (Button)view.findViewById( R.id.btn_search );
        btnClear = (Button)view.findViewById( R.id.btn_clear );
        pb_loading = (ProgressBar) view.findViewById(R.id.pb_loading);

        btnScan.setOnClickListener( this );
        btnSearch.setOnClickListener( this );
        btnClear.setOnClickListener( this );
    }

    /**
     * Handle button click events<br />
     * <br />
     * Auto-created on 2018-03-13 11:43:58 by Android Layout Finder
     * (http://www.buzzingandroid.com/tools/android-layout-finder)
     */
    @Override
    public void onClick(View v) {
        if ( v == btnScan ) {
            // Handle clicks for btnScan
            if (checkLogin()){
                scan();
            }
        } else if ( v == btnSearch ) {
            // Handle clicks for btnSearch
            if(checkLogin()){
                search();
            }
        } else if ( v == btnClear ) {
            // Handle clicks for btnClear
            clearText();
        }
    }

    /**
     * 检查是否已经登录
     * @return
     */
    private boolean checkLogin() {
        app= (MyApplication) context.getApplicationContext();
        Student student=app.getStudent();
        if (student!=null){
            return true;
        }else {
            //跳去登录页面
            context.startActivity(new Intent(context,LoginActivity.class));
            return false;
        }

    }

    /**
     * 检索
     */
  private void search() {
      pb_loading.setVisibility(View.VISIBLE);
      String name=etSearchName.getText().toString().trim();
      String author=etSearchAuthor.getText().toString().trim();
      String publisher=etSearchPublisher.getText().toString().trim();
      String type=etSearchType.getText().toString().trim();
      String ar=etSearchAr.getText().toString().trim();
      String place=etSearchPlace.getText().toString().trim();
      String isbn=etSearchIsbn.getText().toString().trim();
      String issn=etSearchIssn.getText().toString().trim();

      Book book=new Book(name,author,publisher,ar,type,place,isbn,issn);
      String bookjson=new Gson().toJson(book);
      OkHttpUtils.get(Urls.URL_SEARCH)
              .params("bookjson",bookjson)
              .execute(new MyStringCallBack());
      //保存检索历史
      if(app!=null&&!app.isCleanCache()){
          saveCache(bookjson);
      }
  }

    /**
     * 保存检索历史
     */
    private void saveCache(String bookjson) {
       String json= CacheUtils.getString(context,"book_search");
        Gson gson= new GsonBuilder().enableComplexMapKeySerialization().create();
        if (json!=null&&!"".equals(json)){
            cacheMap=gson.fromJson(json,new TypeToken<HashMap<String,String>>(){}.getType());
        }else{
            cacheMap=new HashMap<>();
        }

        cacheMap.put(getTime(),bookjson);
        json=gson.toJson(cacheMap);
        CacheUtils.putString(context,"book_search",json);
    }

    private  class  MyStringCallBack extends StringCallback{

      @Override
      public void onResponse(boolean isFromCache, String s, Request request, @Nullable Response response) {
          LogUtil.e("返回条目："+s);
          processData(s);
      }

      @Override
      public void onError(boolean isFromCache, Call call, @Nullable Response response, @Nullable Exception e) {
          super.onError(isFromCache, call, response, e);
      }

      @Override
      public void onAfter(boolean isFromCache, @Nullable String s, Call call, @Nullable Response response, @Nullable Exception e) {
          super.onAfter(isFromCache, s, call, response, e);
          pb_loading.setVisibility(View.GONE);
      }
  }

    private void processData(String s) {
        bookList=PraseJson(s);
        Intent intent=new Intent(context, SearchContentActivity.class);
        Bundle bundle=new Bundle();
        bundle.putSerializable("list",(Serializable) bookList);
        intent.putExtras(bundle);
        context.startActivity(intent);
    }

    private List<Book> PraseJson(String result) {
        List<Book> items=new ArrayList<>();
        Gson gson=new Gson();
        MSG msg=gson.fromJson(result,MSG.class);
        if (msg.getStatusCode()==100){
            //数据是成功的
            Map<String, Object> map=msg.getExtend();
            Object object=  map.get("list");

            String json=gson.toJson(object);

            items=gson.fromJson(json,new TypeToken<List<Book>>(){}.getType());

        }
        return  items;
    }


    private String getTime() {
        return new SimpleDateFormat("MM-dd HH:mm:ss", Locale.CHINA).format(new Date());
    }

    /**
     * 清空搜索文本
     */
    private void clearText() {
        etSearchName.setText("");
        etSearchAuthor.setText("");
        etSearchPublisher.setText("");
        etSearchType.setText("");
        etSearchAr.setText("");
        etSearchPlace.setText("");
        etSearchIsbn.setText("");
        etSearchIssn.setText("");
    }


    @Override
    public View initView() {
        View view=View.inflate(context,R.layout.activity_searcher_pager,null);
        findViews(view);
        return view;
    }

    @Override
    public void initData() {
        super.initData();
        Log.e(TAG,"书目检索初始化中....");

    }



    /**
     * 扫一扫
     */
    private void scan() {
        if (ContextCompat.checkSelfPermission(context,
                Manifest.permission.CAMERA)
                != PackageManager.PERMISSION_GRANTED) {
            //权限还没有授予，需要在这里写申请权限的代码
            ActivityCompat.requestPermissions((Activity) context,
                    new String[]{Manifest.permission.CAMERA}, 60);
        } else {
            //权限已经被授予，在这里直接写要执行的相应方法即可
            ScannerActivity.gotoActivity((Activity) context, false, ScannerActivity.EXTRA_LASER_LINE_MODE_0, ScannerActivity.EXTRA_SCAN_MODE_0, false, false, false);
            // OptionsScannerActivity.gotoActivity(MainPushActivity.this);
        }
    }
}
