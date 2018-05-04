package com.example.wing.schoollibrary.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.wing.schoollibrary.R;
import com.example.wing.schoollibrary.bean.Student;
import com.example.wing.schoollibrary.util.MD5Encoder;
import com.example.wing.schoollibrary.util.MSG;
import com.example.wing.schoollibrary.util.Urls;
import com.google.gson.Gson;
import com.lzy.okhttputils.OkHttpUtils;
import com.lzy.okhttputils.callback.StringCallback;

import okhttp3.Call;
import okhttp3.Request;
import okhttp3.Response;

public class RegisterActivity extends Activity implements View.OnClickListener {

    private EditText etRegisterNum;
    private EditText etRegisterName;
    private EditText etRegisterPwd;
    private EditText etRegisterType;
    private EditText etRegisterAcademy;
    private EditText etRegisterPhone;
    private EditText etRegisterEmail;
    private ProgressBar pb_loading;
    private Button btnRegister;
    private Long stuNum;
    private String name;
    private String MD5password;
    private String password;
    private String type;
    private String academy;
    private String phoneNum;
    private String email;
    /**
     * 注册结果码
     */
    public static final int RESULT_REGISTER_CODE = 1;

    /**
     * Find the Views in the layout<br />
     * <br />
     * Auto-created on 2018-03-10 10:39:19 by Android Layout Finder
     * (http://www.buzzingandroid.com/tools/android-layout-finder)
     */
    private void findViews() {
        etRegisterNum = (EditText)findViewById( R.id.et_register_num );
        etRegisterName = (EditText)findViewById( R.id.et_register_name );
        etRegisterPwd = (EditText)findViewById( R.id.et_register_pwd );
        etRegisterType = (EditText)findViewById( R.id.et_register_type );
        etRegisterAcademy = (EditText)findViewById( R.id.et_register_academy );
        etRegisterPhone = (EditText)findViewById( R.id.et_register_phone );
        etRegisterEmail = (EditText)findViewById( R.id.et_register_email );
        btnRegister = (Button)findViewById( R.id.btn_register );
        pb_loading = (ProgressBar) findViewById( R.id.pb_loading );

        btnRegister.setOnClickListener( this );
    }

    /**
     * Handle button click events<br />
     * <br />
     * Auto-created on 2018-03-10 10:39:19 by Android Layout Finder
     * (http://www.buzzingandroid.com/tools/android-layout-finder)
     */
    @Override
    public void onClick(View v) {
        if ( v == btnRegister ) {
           register();
        }
    }

    /**
     * 注册
     */
    private void register() {

        try {
            stuNum=Long.valueOf(etRegisterNum.getText().toString());
            name=etRegisterName.getText().toString();
            password=etRegisterPwd.getText().toString();
            MD5password= MD5Encoder.encode(etRegisterPwd.getText().toString());
            type=etRegisterType.getText().toString();
            academy=etRegisterAcademy.getText().toString();
            phoneNum=etRegisterPhone.getText().toString();
            email=etRegisterEmail.getText().toString();
            Student student=new Student(stuNum,name,MD5password,type,academy,phoneNum,email);
            String studentjson=new Gson().toJson(student);
            pb_loading.setVisibility(View.VISIBLE);
            OkHttpUtils.get(Urls.URL_REGISTER)
                        .params("student",studentjson)
                        .execute(new MyStringCallBack());
        } catch (Exception e) {
            e.printStackTrace();
        }

    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        findViews();
    }


    private class MyStringCallBack extends StringCallback{

        @Override
        public void onResponse(boolean isFromCache, String s, Request request, @Nullable Response response) {
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
        MSG msg=parseJson(s);

        if (msg!=null){
            if (msg.getStatusCode()==100){//成功
                if (stuNum!=null&&password!=null){
                    Intent intent=new Intent();
                    intent.putExtra("stuNum",stuNum+"");
                    intent.putExtra("password",password);
                    this.setResult(RESULT_OK,intent);
                }
                finish();
            }else if (msg.getStatusCode()==200){//失败
                Toast.makeText(this,msg.getExtend().get("result").toString(),Toast.LENGTH_SHORT).show();
                finish();
            }
        }
    }

    private MSG parseJson(String s) {
        MSG msg=null;
        if (s!=null){
            Gson gson=new Gson();
            msg = gson.fromJson(s, MSG.class);
        }
        return msg;
    }

}
