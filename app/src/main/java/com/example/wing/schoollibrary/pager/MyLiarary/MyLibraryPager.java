package com.example.wing.schoollibrary.pager.MyLiarary;

import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.wing.schoollibrary.MyApplication;
import com.example.wing.schoollibrary.R;
import com.example.wing.schoollibrary.activity.BorrowHistoryActivity;
import com.example.wing.schoollibrary.activity.CurrentBorrowActivity;
import com.example.wing.schoollibrary.activity.CustomerServiceActivity;
import com.example.wing.schoollibrary.activity.LoginActivity;
import com.example.wing.schoollibrary.activity.ModifyPwdActivity;
import com.example.wing.schoollibrary.activity.MySearchHistoryActivity;
import com.example.wing.schoollibrary.activity.NewBookInformationActivity;
import com.example.wing.schoollibrary.activity.PersonInformationActivity;
import com.example.wing.schoollibrary.activity.ReturnBookActivity;
import com.example.wing.schoollibrary.activity.SettingActivity;
import com.example.wing.schoollibrary.activity.StudentCertificateActivity;
import com.example.wing.schoollibrary.base.BasePager;
import com.example.wing.schoollibrary.bean.Student;

import static android.app.Activity.RESULT_CANCELED;
import static android.app.Activity.RESULT_OK;

/**
 * Created by Administrator on 2017/12/15 0015.
 * 我的图书馆
 */

public class MyLibraryPager extends BasePager implements View.OnClickListener {

    private static final String TAG = MyLibraryPager.class.getSimpleName();
    /***
     * 登录请求码
     */
    private static final int LOGIN_REQUEST_CODE = 1;
    private ImageButton ibUserIconAvator;
    private TextView tvUsername;
    private TextView tvPersonalInformation;
    private TextView tvModifyPassword;
    private TextView tvCurrentBorrow;
    private TextView tvReturnBook;
    private TextView tvNewBook;
    private TextView tvBorrowHistory;
    private TextView tvSearchHistory;
    private TextView tvUserCallcenter;
    private TextView tvSetting;
    private TextView tv_check_qrcode;
    private TextView tv_exitLogin;


    private Student student;
    private MyApplication app;

    /**
     * Find the Views in the layout<br />
     * <br />
     * Auto-created on 2018-02-17 21:11:07 by Android Layout Finder
     * (http://www.buzzingandroid.com/tools/android-layout-finder)
     */
    private void findViews(View view) {
        ibUserIconAvator = (ImageButton)view.findViewById( R.id.ib_user_icon_avator );
        tvUsername = (TextView)view.findViewById( R.id.tv_username );
        tvPersonalInformation = (TextView)view.findViewById( R.id.tv_personal_information );
        tvModifyPassword = (TextView)view.findViewById( R.id.tv_modify_password );
        tvCurrentBorrow = (TextView)view.findViewById( R.id.tv_current_borrow );
        tvReturnBook = (TextView)view.findViewById( R.id.tv_return_book );
        tvNewBook = (TextView)view.findViewById( R.id.tv_new_book );
        tvBorrowHistory = (TextView)view.findViewById( R.id.tv_borrow_history );
        tvSearchHistory = (TextView)view.findViewById( R.id.tv_search_history );
        tvUserCallcenter = (TextView)view.findViewById( R.id.tv_user_callcenter );
        tvSetting = (TextView)view.findViewById( R.id.tv_setting );
        tv_check_qrcode = (TextView)view.findViewById(R.id.tv_check_qrcode);
        tv_exitLogin = (TextView)view.findViewById(R.id.tv_exitLogin);

    }










    @Override
    protected View initView() {
        View view =View.inflate(context, R.layout.my_library_pager,null);
        findViews(view);
        return view;
    }



    @Override
    public void initData() {
        super.initData();
        Log.e(TAG,"我的图书馆初始化中....");

//        checkForLogin();
        setListener();
    }

    private void checkForLogin() {
        app= (MyApplication) context.getApplicationContext();
        student=app.getStudent();
        if (student!=null){
            setState();
        }else{
            restoreState();
        }
    }

    private void setListener() {
        ibUserIconAvator.setOnClickListener(this);
        //        tvUsername.setOnClickListener(this);
        tvPersonalInformation.setOnClickListener(this);
        tvModifyPassword.setOnClickListener(this);
        tvCurrentBorrow.setOnClickListener(this);
        tvReturnBook.setOnClickListener(this);
        tvNewBook.setOnClickListener(this);
        tvBorrowHistory.setOnClickListener(this);
        tvSearchHistory.setOnClickListener(this);
        tvUserCallcenter.setOnClickListener(this);
        tvSearchHistory.setOnClickListener(this);
        tv_check_qrcode.setOnClickListener(this);
        tv_exitLogin.setOnClickListener(this);
        tvSetting.setOnClickListener(this);

    }


    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.ib_user_icon_avator://登录图标
                if (student==null)
                    startActivityForResult(new Intent(context, LoginActivity.class),LOGIN_REQUEST_CODE);
                break;
            case R.id.tv_personal_information://个人信息
                startActivity(new Intent(context, PersonInformationActivity.class));
                break;
            case R.id.tv_modify_password://修改密码
                startActivity(new Intent(context, ModifyPwdActivity.class));
                break;
            case R.id.tv_check_qrcode://查看移动式学生证
                startActivity(new Intent(context, StudentCertificateActivity.class));
                break;
            case R.id.tv_current_borrow://当前借阅情况或续借
                startActivity(new Intent(context, CurrentBorrowActivity.class));
                break;
            case R.id.tv_return_book://催还图书
                startActivity(new Intent(context, ReturnBookActivity.class));
                break;
            case R.id.tv_new_book://新书通报信息
                startActivity(new Intent(context, NewBookInformationActivity.class));
                break;
            case R.id.tv_borrow_history://我的借书历史
                startActivity(new Intent(context, BorrowHistoryActivity.class));
                break;
            case R.id.tv_search_history://我的检索历史
            startActivity(new Intent(context, MySearchHistoryActivity.class));
                break;
            case R.id.tv_user_callcenter://客服中心
                startActivity(new Intent(context, CustomerServiceActivity.class));
                break;
            case R.id.tv_setting://设置
                startActivity(new Intent(context, SettingActivity.class));
                break;
            case R.id.tv_exitLogin://退出登录
                restoreState();
                break;
        }
    }

    private void restoreState() {
        student=null;
        tv_exitLogin.setVisibility(View.GONE);
        tvUsername.setText("登录/注册");
        app.setStudent(null);
    }

    private void setState(){
        tvUsername.setText("你好，"+student.getStuName());
        tv_exitLogin.setVisibility(View.VISIBLE);
    }

    @Override
    public void onResume() {
        super.onResume();
        checkForLogin();
    }



    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode!= RESULT_CANCELED&&resultCode==RESULT_OK){
            student= (Student) data.getSerializableExtra("student");
            if (student!=null){
               setState();
            }
        }
    }
}
