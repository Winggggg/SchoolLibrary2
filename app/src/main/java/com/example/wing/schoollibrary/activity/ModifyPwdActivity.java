package com.example.wing.schoollibrary.activity;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.wing.schoollibrary.R;
import com.example.wing.schoollibrary.util.MSG;
import com.example.wing.schoollibrary.util.Urls;
import com.google.gson.Gson;
import com.lzy.okhttputils.OkHttpUtils;
import com.lzy.okhttputils.callback.StringCallback;

import okhttp3.Call;
import okhttp3.Request;
import okhttp3.Response;

public class ModifyPwdActivity extends Activity implements View.OnClickListener {

    private EditText et_num;
    private EditText et_before_pwd;
    private EditText et_after_pwd;
    private TextView tv_title;
    private ImageButton ib_back;
    private Button btn_modify;
    private ProgressBar pb_loading;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_modify_pwd);
        initView();
    }

    private void initView() {
        et_num= (EditText) findViewById(R.id.et_num);
        et_before_pwd= (EditText) findViewById(R.id.et_before_pwd);
        et_after_pwd= (EditText) findViewById(R.id.et_after_pwd);
        tv_title= (TextView) findViewById(R.id.tv_title);
        ib_back= (ImageButton) findViewById(R.id.ib_back);
        btn_modify= (Button) findViewById(R.id.btn_modify);
        pb_loading= (ProgressBar) findViewById(R.id.pb_loading);

        tv_title.setText("修改密码");
        ib_back.setOnClickListener(this);
        btn_modify.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.ib_back:
                finish();
                break;
            case R.id.btn_modify:
                modifyPassword();
                break;
        }
    }

    /**
     * 修改密码
     */
    private void modifyPassword() {
        pb_loading.setVisibility(View.VISIBLE);
        String stuNum=et_num.getText().toString();
        String before_pwd=et_before_pwd.getText().toString().trim();
        String after_pwd=et_after_pwd.getText().toString().trim();
        OkHttpUtils.post(Urls.URL_MODIFY)
                .params("stuNum",stuNum)
                .params("beforePwd",before_pwd)
                .params("afterPwd",after_pwd)
                .execute(new MyStringCallBack());
    }

    private class  MyStringCallBack extends StringCallback{

        @Override
        public void onResponse(boolean isFromCache, String s, Request request, @Nullable Response response) {
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
            Toast.makeText(ModifyPwdActivity.this,"修改失败"+e.getMessage(),Toast.LENGTH_SHORT).show();
        }
    }

    private void processData(String s) {
        MSG msg=praseJson(s);
        if (msg.getStatusCode()==100){
            Toast.makeText(this,"修改成功",Toast.LENGTH_SHORT).show();
            finish();
        }else if(msg.getStatusCode()==200){
            Toast.makeText(this,msg.getExtend().get("result").toString(),Toast.LENGTH_SHORT).show();
        }
    }

    /***
     * 解析json
     * @param s
     */
    private MSG praseJson(String s) {
        MSG msg=null;
        if (s!=null){
            msg = new Gson().fromJson(s, MSG.class);
        }
        return msg;
    }
}
