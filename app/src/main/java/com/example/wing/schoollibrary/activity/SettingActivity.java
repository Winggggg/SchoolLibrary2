package com.example.wing.schoollibrary.activity;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

import com.example.wing.schoollibrary.MyApplication;
import com.example.wing.schoollibrary.R;
import com.example.wing.schoollibrary.util.CacheUtils;
import com.example.wing.schoollibrary.util.LogUtil;
import com.example.wing.schoollibrary.view.MyToggleButton;

public class SettingActivity extends Activity {

    private MyToggleButton toggle_flush;
    private MyToggleButton toggle_push;
    private MyApplication app;
    private ImageButton ib_back;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);

        initView();

    }

    private class MyOncleanListener implements MyToggleButton.OnCleanListener{

        @Override
        public void clean() {
            if(toggle_flush.isOpen()){
                //清除缓存
                LogUtil.e("清除缓存");
                app.setCleanCache(true);
                CacheUtils.clean(SettingActivity.this);
            }else{
                app.setCleanCache(false);
            }
        }
    }

    private void initView() {
        toggle_flush= (MyToggleButton) findViewById(R.id.toggle_flush);
        toggle_push= (MyToggleButton) findViewById(R.id.toggle_push);
        app= (MyApplication) getApplicationContext();

        toggle_flush.setOnCleanListener(new MyOncleanListener());

        if (app.isCleanCache()){
            toggle_flush.setOpen(true);
        }else{
            toggle_flush.setOpen(false);
        }
        toggle_flush.flushView();


        ib_back= (ImageButton) findViewById(R.id.ib_back);
        ib_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}
