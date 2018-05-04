package com.example.wing.schoollibrary.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.wing.schoollibrary.MyApplication;
import com.example.wing.schoollibrary.R;
import com.example.wing.schoollibrary.bean.Comment;
import com.example.wing.schoollibrary.bean.Student;
import com.example.wing.schoollibrary.util.CacheUtils;
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

public class CommentActivity extends Activity implements View.OnClickListener {

    private ListView comment_listview;
    private EditText et_respond_content;
    private Button btn_respond;
    private TextView tv_title;
    private ImageButton ib_back;
    private TextView tv_resp;
    private ProgressBar pb_loading;
    private List<Comment> items;
    private List<String> commentList=new ArrayList<>();
    private ArrayAdapter<String> adapter;
    /**
     * 当前用户
     */
    private Student currentStudent;
    /**
     * 树洞交流发布方
     */
    private Student publish_Student;
    /**
     * 被回复方
     */
    private Student oriStudent;
    private int comid;
    private MyApplication app;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comment);
        initView();
        getAndSetData();
    }

    private void initView() {
        comment_listview = (ListView) findViewById(R.id.comment_listview);
        et_respond_content = (EditText) findViewById(R.id.et_respond_content);
        btn_respond = (Button) findViewById(R.id.btn_respond);
        tv_title = (TextView) findViewById(R.id.tv_title);
        ib_back = (ImageButton) findViewById(R.id.ib_back);
        pb_loading = (ProgressBar) findViewById(R.id.pb_loading);
        tv_resp = (TextView) findViewById(R.id.tv_resp);

        app= (MyApplication) getApplicationContext();

        btn_respond.setOnClickListener(this);
        ib_back.setOnClickListener(this);

        tv_title.setText("评论区");



        comment_listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if(items!=null&&items.size()>position){
                     oriStudent=items.get(position).getRespstudent();
                    String str="@"+oriStudent.getStuName();
                    tv_resp.setText(str);
                }
            }
        });


    }


    private void getAndSetData() {
        comid=getIntent().getIntExtra("comid",-1);
        publish_Student= (Student) getIntent().getSerializableExtra("student");
        String str="@"+publish_Student.getStuName();
        tv_resp.setText(str);
        oriStudent=publish_Student;
        if (comid!=-1){
            OkHttpUtils.get(Urls.URL_GET_COMMENT)
                    .params("comid",comid+"")
                    .execute(new MyCommentstringCallBack());
        }
    }

    private class MyCommentstringCallBack extends StringCallback{

        @Override
        public void onResponse(boolean isFromCache, String s, Request request, @Nullable Response response) {
            ProcessData(s);
            //缓存
            if (!app.isCleanCache()){
                CacheUtils.putString(CommentActivity.this,"comment",s);
            }
        }

        @Override
        public void onError(boolean isFromCache, Call call, @Nullable Response response, @Nullable Exception e) {
            super.onError(isFromCache, call, response, e);
            String cacheString= CacheUtils.getString(CommentActivity.this,"comment");
            if (!TextUtils.isEmpty(cacheString)){
                ProcessData(cacheString);
            }
            pb_loading.setVisibility(View.GONE);
        }

        @Override
        public void onAfter(boolean isFromCache, @Nullable String s, Call call, @Nullable Response response, @Nullable Exception e) {
            super.onAfter(isFromCache, s, call, response, e);
            pb_loading.setVisibility(View.GONE);
        }
    }

    /**
     * 处理数据
     * @param result
     */
    private void ProcessData(String result) {
        items=parseJson(result);
        showData(items);
    }


    /**
     * 检查是否已经登录
     * @return
     */
    private boolean checkLogin() {

        currentStudent=app.getStudent();
        if (currentStudent!=null){
            return true;
        }else {
            //跳去登录页面
            startActivity(new Intent(this,LoginActivity.class));
            return false;
        }

    }

    /**
     * 显示数据
     * @param items
     */
    private void showData(List<Comment> items) {
        if (items!=null&&items.size()>0){
            for (int i=0;i<items.size();i++){
                Comment comment=items.get(i);
                String commentStr=comment.getRespstudent().getStuName()+"@"+comment.getOristudent().getStuName()+":"+comment.getRespContent();
                commentList.add(commentStr);
            }
            LogUtil.e("显示评论");
            adapter=new ArrayAdapter<String>(this,R.layout.comment_text,commentList);
            comment_listview.setAdapter(adapter);


        }
    }

    /**
     *
     * 解析数据
     * @param result
     * @return
     */
    private List<Comment> parseJson(String result) {
        List<Comment> items=new ArrayList<>();
        Gson gson=new Gson();
        MSG msg=gson.fromJson(result,MSG.class);
        if (msg.getStatusCode()==100){
            //数据是成功的
            Map<String, Object> map=msg.getExtend();
            Object object=  map.get("list");

            String json=gson.toJson(object);

            items=gson.fromJson(json,new TypeToken<List<Comment>>(){}.getType());

        }else if(msg.getStatusCode()==200){
            Object result1 = msg.getExtend().get("result");
            Toast.makeText(this,result1.toString(),Toast.LENGTH_SHORT).show();
        }
        return  items;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.ib_back:
                finish();
                break;
            case R.id.btn_respond:
                response();
                break;
        }
    }

    /***
     * 评论或者回复评论
     */
    private void response() {
        if(checkLogin()){
            String content=et_respond_content.getText().toString();
            Comment comment=new Comment(currentStudent.getStuId(),content,oriStudent.getStuId(),comid);
            String commentJson=new Gson().toJson(comment);
          OkHttpUtils.get(Urls.URL_RESP)
                  .params("commentJson",commentJson)
                  .execute(new MyStringCallBack());
      }
    }

    private class  MyStringCallBack extends StringCallback{

        @Override
        public void onResponse(boolean isFromCache, String s, Request request, @Nullable Response response) {
            LogUtil.e("处理数据");
            processResultData(s);
        }

        @Override
        public void onError(boolean isFromCache, Call call, @Nullable Response response, @Nullable Exception e) {
            super.onError(isFromCache, call, response, e);
        }
    }

    /**
     * 处理数据
     * @param result
     */
    private void processResultData(String result) {
        Gson gson=new Gson();
        MSG msg=gson.fromJson(result,MSG.class);
        if (msg.getStatusCode()==100){
            //数据是成功的
            //刷新
            Toast.makeText(this,currentStudent.getStuName()+tv_resp.getText().toString()+et_respond_content.getText().toString(),Toast.LENGTH_SHORT).show();
             String str=currentStudent.getStuName()+tv_resp.getText().toString()+et_respond_content.getText().toString();
            LogUtil.e(str);
            if (commentList!=null){
                if (commentList.size()==0){
                    commentList.add(str);
                    adapter=new ArrayAdapter<String>(this,R.layout.comment_text,commentList);
                    comment_listview.setAdapter(adapter);
                }else{
                    adapter.add(str);
                }
            }
            et_respond_content.setText("");
        }else if(msg.getStatusCode()==200){
            Object result1 = msg.getExtend().get("result");
            Toast.makeText(this,result1.toString(),Toast.LENGTH_SHORT).show();
        }
    }

}
