package com.example.wing.schoollibrary.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.wing.schoollibrary.MyApplication;
import com.example.wing.schoollibrary.R;
import com.example.wing.schoollibrary.adapter.BorrowBookAdapter;
import com.example.wing.schoollibrary.bean.BorrowInfo;
import com.example.wing.schoollibrary.bean.Student;
import com.example.wing.schoollibrary.util.LogUtil;
import com.example.wing.schoollibrary.util.MSG;
import com.example.wing.schoollibrary.util.Urls;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.lzy.okhttputils.OkHttpUtils;
import com.lzy.okhttputils.callback.StringCallback;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import okhttp3.Call;
import okhttp3.Request;
import okhttp3.Response;

public class CurrentBorrowActivity extends Activity {

    private MyApplication app;
    private ListView search_history_listview;
    private TextView tv_borrow_rule;
    private ProgressBar pb_loading;
    private TextView tv_nohistory;
    private Student currentStudent;
    private List<BorrowInfo> items;
    private BorrowBookAdapter adapter;
    private ImageButton ib_back;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_current_borrow);

        initView();

        getDataFromNet();
    }

    private void getDataFromNet() {
        if(checkForLogin()){
            pb_loading.setVisibility(View.VISIBLE);
            OkHttpUtils.get(Urls.URL_BORROWINFO)
                    .params("stuId",currentStudent.getStuId()+"")
                    .execute(new MystringCallBack());
        }
    }

    private class MystringCallBack extends StringCallback{

        @Override
        public void onResponse(boolean isFromCache, String s, Request request, @Nullable Response response) {
            LogUtil.e(s);
            processData(s);

        }

        @Override
        public void onAfter(boolean isFromCache, @Nullable String s, Call call, @Nullable Response response, @Nullable Exception e) {
            super.onAfter(isFromCache, s, call, response, e);
            pb_loading.setVisibility(View.GONE);
        }

        @Override
        public void onError(boolean isFromCache, Call call, @Nullable Response response, @Nullable Exception e) {
            super.onError(isFromCache, call, response, e);
        }
    }

    /**
     * 处理数据
     * @param s
     */
    private void processData(String s) {
        items=parseJson(s);
        showData(items);
    }

    private void showData(List<BorrowInfo> items) {
        if (items!=null&&items.size()>0){
            adapter=new BorrowBookAdapter(this,items);
            search_history_listview.setAdapter(adapter);
            tv_nohistory.setVisibility(View.GONE);
        }else{
            tv_nohistory.setVisibility(View.VISIBLE);
        }
    }

    private List<BorrowInfo> parseJson(String s) {
        List<BorrowInfo> items=new ArrayList<>();
        Gson gson=new Gson();
        MSG msg=gson.fromJson(s,MSG.class);
        if (msg.getStatusCode()==100){
            //数据是成功的
            Map<String, Object> map=msg.getExtend();
            Object object=  map.get("list");

            String json=gson.toJson(object);

            items=gson.fromJson(json,new TypeToken<List<BorrowInfo>>(){}.getType());

        }
        return  items;
    }

    /**
     * 检查是否登录
     * @return
     */
    private boolean checkForLogin() {
        currentStudent=app.getStudent();
        if (currentStudent!=null){
            return true;
        }else{
//            startActivity(new Intent(this,LoginActivity.class));
            startActivityForResult(new Intent(this,LoginActivity.class),LoginActivity.REQUEST_CURRENTBORROW_CODE);
            return false;
        }
    }


    private void initView() {
        search_history_listview= (ListView) findViewById(R.id.search_history_listview);
        tv_borrow_rule= (TextView) findViewById(R.id.tv_borrow_rule);
        pb_loading= (ProgressBar) findViewById(R.id.pb_loading);
        tv_nohistory= (TextView) findViewById(R.id.tv_nohistory);
        app= (MyApplication) getApplicationContext();
        ib_back= (ImageButton) findViewById(R.id.ib_back);
        ib_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode==LoginActivity.REQUEST_CURRENTBORROW_CODE&&resultCode!=RESULT_CANCELED&&
                resultCode==RESULT_OK){
            getDataFromNet();
        }
    }
}
