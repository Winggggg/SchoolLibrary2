package com.example.wing.schoollibrary.activity;

import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;

import com.example.wing.schoollibrary.MyApplication;
import com.example.wing.schoollibrary.R;
import com.example.wing.schoollibrary.bean.Student;

public class PersonInformationActivity extends Activity {

    private TextView tv_num;
    private TextView tv_name;
    private TextView tv_type;
    private TextView tv_academy;
    private TextView tv_phone;
    private TextView tv_email;
    private TextView tv_title;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_person_information);
        initView();
        setData();
    }

    private void setData() {
        tv_title.setText("个人信息");
        MyApplication app= (MyApplication) getApplicationContext();
        Student student=app.getStudent();
        if (student!=null){
            tv_num.setText(student.getStuNum()+"");
            tv_name.setText(student.getStuName());
            tv_type.setText(student.getType());
            tv_academy.setText(student.getCollege());
            tv_phone.setText(student.getPhonenum());
            tv_email.setText(student.getEmail());
        }
    }

    private void initView() {
        tv_num= (TextView) findViewById(R.id.tv_num);
        tv_name= (TextView) findViewById(R.id.tv_name);
        tv_type= (TextView) findViewById(R.id.tv_type);
        tv_academy= (TextView) findViewById(R.id.tv_academy);
        tv_phone= (TextView) findViewById(R.id.tv_phone);
        tv_email= (TextView) findViewById(R.id.tv_email);
        tv_title= (TextView) findViewById(R.id.tv_title);
    }
}
