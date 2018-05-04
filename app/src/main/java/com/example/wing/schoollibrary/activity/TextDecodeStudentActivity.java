package com.example.wing.schoollibrary.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import com.example.wing.schoollibrary.R;
import com.example.wing.schoollibrary.bean.Student;
import com.google.gson.Gson;
import com.mylhyl.zxing.scanner.common.Scanner;

public class TextDecodeStudentActivity extends Activity {

    private TextView tv_num;
    private TextView tv_name;
    private TextView tv_type;
    private TextView tv_academy;
    private TextView tv_phone;
    private TextView tv_email;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_text_decode_student);
        initView();
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            Student student = new Gson().fromJson(extras.getString(Scanner.Scan.RESULT), Student.class);
            if (student!=null)
                setData(student);
        }
    }

    private void setData(Student student) {
        tv_num.setText(student.getStuNum()+"");
        tv_name.setText(student.getStuName());
        tv_type.setText(student.getType());
        tv_academy.setText(student.getCollege());
        tv_phone.setText(student.getPhonenum());
        tv_email.setText(student.getEmail());
    }

    private void initView() {
        tv_num= (TextView) findViewById(R.id.tv_num);
        tv_name= (TextView) findViewById(R.id.tv_name);
        tv_type= (TextView) findViewById(R.id.tv_type);
        tv_academy= (TextView) findViewById(R.id.tv_academy);
        tv_phone= (TextView) findViewById(R.id.tv_phone);
        tv_email= (TextView) findViewById(R.id.tv_email);
    }

    public static void gotoActivity(Activity activity, Bundle bundle) {
        activity.startActivity(new Intent(activity, TextDecodeStudentActivity.class).putExtras(bundle));
    }
}
