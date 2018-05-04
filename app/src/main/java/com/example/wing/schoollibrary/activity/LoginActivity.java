package com.example.wing.schoollibrary.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.InputType;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.wing.schoollibrary.MyApplication;
import com.example.wing.schoollibrary.R;
import com.example.wing.schoollibrary.bean.Student;
import com.example.wing.schoollibrary.util.MSG;
import com.example.wing.schoollibrary.util.Urls;
import com.google.gson.Gson;
import com.lzy.okhttputils.OkHttpUtils;
import com.lzy.okhttputils.callback.StringCallback;

import java.util.Map;

import okhttp3.Call;
import okhttp3.Request;
import okhttp3.Response;

/**
 * A login screen that offers login via email/password.
 */
public class LoginActivity extends Activity implements View.OnClickListener {

    /**
     * 注册请求码
     */
    public static final int REQUEST_REGISTER_CODE = 1;
    /**
     * 当前借阅情况请求码
     */
    public static final int REQUEST_CURRENTBORROW_CODE = 2;
    /**
     * 催还书请求码
     */
    public static final int REQUEST_RETURNBOOK_CODE = 3;
    /**
     * 新书通报请求码
     */
    public static final int REQUEST_NEWBOOK_CODE = 4;
    /**
     * 借书历史请求码
     */
    public static final int REQUEST_BORROWHISTORY_CODE = 5;

    private TextView tv_login_register;
    private EditText et_login_phone;
    private EditText et_login_pwd;
    private Button btn_login;
    private ProgressBar pb_loading;
    private ImageButton ib_login_visible;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        tv_login_register= (TextView) findViewById(R.id.tv_login_register);
        et_login_phone= (EditText) findViewById(R.id.et_login_phone);
        et_login_pwd= (EditText) findViewById(R.id.et_login_pwd);
        btn_login= (Button) findViewById(R.id.btn_login);
        pb_loading= (ProgressBar) findViewById(R.id.pb_loading);
        ib_login_visible= (ImageButton) findViewById(R.id.ib_login_visible);

        tv_login_register.setOnClickListener(this);
        btn_login.setOnClickListener(this);
        ib_login_visible.setOnClickListener(this);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.tv_login_register:
                startActivityForResult(new Intent(this,RegisterActivity.class),REQUEST_REGISTER_CODE);
                break;
            case R.id.btn_login:
                Toast.makeText(this,"正在登陆中",Toast.LENGTH_SHORT).show();
                login();
                break;
            case R.id.ib_login_visible:
                setPasswordVisiable();
                break;

        }
    }

    /**
     * 设置密码可见还是不可见
     */
    private void setPasswordVisiable() {
        //记住光标开始的位置
        int pos = et_login_pwd.getSelectionStart();
        if(et_login_pwd.getInputType()!= (InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD)){
            //隐藏密码
            et_login_pwd.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
            ib_login_visible.setBackgroundResource(R.drawable.new_password_drawable_invisible);
        }else{//显示密码
            et_login_pwd.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
            ib_login_visible.setBackgroundResource(R.drawable.new_password_drawable_visible);
        }
        et_login_pwd.setSelection(pos);
    }

    /**
     * 登录
     */
    private void login() {
        pb_loading.setVisibility(View.VISIBLE);
        String stuNum=et_login_phone.getText().toString().trim();
        String password=et_login_pwd.getText().toString().trim();
        if (!checkForLocal(stuNum,password)){
            checkForServer(stuNum,password);
        }
    }

    /**
     * 请求服务器判断密码是否成功，成功后把数据放到本地数据库，方便下次快速登录
     */
    private void checkForServer(String stuNum,String password) {
        OkHttpUtils.post(Urls.URL_LOGIN)
                .params("stuNum",stuNum)
                .params("password",password)
                .execute(new MyStringCallBack());
    }



    private class  MyStringCallBack extends StringCallback{

        @Override
        public void onResponse(boolean isFromCache, String s, Request request, @Nullable Response response) {
            processData(s);
        }

        @Override
        public void onError(boolean isFromCache, Call call, @Nullable Response response, @Nullable Exception e) {
            super.onError(isFromCache, call, response, e);
            Toast.makeText(LoginActivity.this,"登录失败："+e.getMessage(),Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onAfter(boolean isFromCache, @Nullable String s, Call call, @Nullable Response response, @Nullable Exception e) {
            super.onAfter(isFromCache, s, call, response, e);
            pb_loading.setVisibility(View.GONE);
        }
    }

    /**
     * 处理服务器返回数据
     * @param s
     */
    private void processData(String s) {
        Gson gson=new Gson();
        MSG msg=parseJson(s,gson);

        if (msg!=null){
            if (msg.getStatusCode()==100){//成功
                Map<String, Object> map=msg.getExtend();
                Object object=  map.get("student");

                String json=gson.toJson(object);
                Student student=gson.fromJson(json,Student.class);
                Intent intent=new Intent();
                intent.putExtra("student",student);
                //放入全局变量
                MyApplication app=(MyApplication) getApplicationContext();
                app.setStudent(student);
                this.setResult(RESULT_OK,intent);
                finish();
            }else if (msg.getStatusCode()==200){//失败
                Toast.makeText(this,msg.getExtend().get("result").toString(),Toast.LENGTH_SHORT).show();
//                finish();
            }
        }
    }

    /**
     * 解释json数据
     * @param s
     * @return
     */
    private MSG parseJson(String s,Gson gson) {
        MSG msg=null;
        if (s!=null){
            msg = gson.fromJson(s, MSG.class);
        }
        return msg;
    }

    /**
     * 本地查询数据库，如果存在即登录成功，否则请求服务器进行判断
     */
    private boolean checkForLocal(String stuNum,String password) {
        return false;
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode!=RESULT_CANCELED&&resultCode==RESULT_OK){
            String stuNum=data.getStringExtra("stuNum");
            if(stuNum!=null)
                 et_login_phone.setText(stuNum);
            String password=data.getStringExtra("password");
            if(password!=null)
                et_login_pwd.setText(password);
        }
    }
}

