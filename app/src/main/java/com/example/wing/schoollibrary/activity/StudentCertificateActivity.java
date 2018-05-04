package com.example.wing.schoollibrary.activity;

import android.app.Activity;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.wing.schoollibrary.MyApplication;
import com.example.wing.schoollibrary.R;
import com.example.wing.schoollibrary.bean.Student;
import com.google.gson.Gson;
import com.google.zxing.client.result.ParsedResultType;
import com.mylhyl.zxing.scanner.encode.QREncode;

import java.io.ByteArrayOutputStream;

public class StudentCertificateActivity extends Activity {

    private TextView tv_title;
    private ImageView iv_certificate;
    private TextView tv_tip;
    private TextView tv_adv;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_certificate);

        initView();
        viewcode();

    }

    private void viewcode() {
        MyApplication app=(MyApplication) getApplicationContext();
        Student student=app.getStudent();
        if (student!=null){
            tv_tip.setVisibility(View.GONE);
            tv_adv.setVisibility(View.VISIBLE);
            iv_certificate.setVisibility(View.VISIBLE);

            Resources res = getResources();
            Bitmap logoBitmap = BitmapFactory.decodeResource(res, R.mipmap
                    .new_user_icon2);
            String qrContent = new Gson().toJson(student);

            Bitmap bitmap = new QREncode.Builder(StudentCertificateActivity.this)
                    //                        .setColor(getResources().getColor(R.color.colorPrimary))//二维码颜色
                    //                        .setColors(0xFF0094FF, 0xFFFED545, 0xFF5ACF00, 0xFFFF4081)//二维码彩色
                    .setMargin(0)//二维码边框
                    //二维码类型
                    .setParsedResultType(ParsedResultType.TEXT)
                    //二维码内容
                    .setContents(qrContent)
                    .setSize(800)//二维码等比大小
//                    .setLogoBitmap(logoBitmap, 90)
                    .build().encodeAsBitmap();
            iv_certificate.setImageBitmap(bitmap);
            iv_certificate.setOnLongClickListener(new MyOnLongClickListener());

        }
    }

    private class MyOnLongClickListener implements View.OnLongClickListener {

        @Override
        public boolean onLongClick(View v) {
            decodeQRCode();
            return true;
        }


    }

    /**
     * 解析二维码
     */
    private void decodeQRCode() {
        iv_certificate.setDrawingCacheEnabled(true);//step 1
        Bitmap bitmap = iv_certificate.getDrawingCache();//step 2
        //step 3 转bytes
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);

        DeCodeActivity.gotoActivity(StudentCertificateActivity.this, baos.toByteArray());//step 4
        iv_certificate.setDrawingCacheEnabled(false);//step 5
    }

    private void initView() {
        tv_tip= (TextView) findViewById(R.id.tv_tip);
        tv_title= (TextView) findViewById(R.id.tv_title);
        tv_adv= (TextView) findViewById(R.id.tv_adv);
        iv_certificate= (ImageView) findViewById(R.id.iv_certificate);

        tv_title.setText("移动式学生证");
    }
}
