package com.example.wing.schoollibrary.activity;

import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.IdRes;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.KeyEvent;
import android.widget.FrameLayout;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.example.wing.schoollibrary.R;
import com.example.wing.schoollibrary.base.BasePager;
import com.example.wing.schoollibrary.pager.communicate.CommunicatePager;
import com.example.wing.schoollibrary.pager.main.MainPager;
import com.example.wing.schoollibrary.pager.MyLiarary.MyLibraryPager;
import com.example.wing.schoollibrary.pager.search.SearchPager;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends FragmentActivity {

    /*
   页面下标
    */
    private  int position=0;
    private RadioGroup rg_main;
    private FrameLayout fl_main_content;
    private List<BasePager> basePagers;
    /**
     * 记录fragment切换前的对象
     */
    private Fragment mContent;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //初始化
        rg_main= (RadioGroup) findViewById(R.id.rg_main);
        fl_main_content= (FrameLayout) findViewById(R.id.fl_main_content);


        initData();
        rg_main.setOnCheckedChangeListener(new MyCheckedChangeListen());
        rg_main.check(R.id.rg_main_pager);
    }

    private void initData() {
        basePagers=new ArrayList<BasePager>();
        basePagers.add(new MainPager());
        basePagers.add(new SearchPager());

        basePagers.add(new CommunicatePager());
        basePagers.add(new MyLibraryPager());
    }

    public  BasePager getBasePager() {
        BasePager basepager=null;
        if (basePagers!=null){
            basepager=basePagers.get(position);
        }
        return basepager;
    }

    private void setFragment(Fragment from,Fragment to) {
        if(mContent!=to){
            mContent=to;
            //创建FragmentManager
            FragmentManager fragmentManager = getSupportFragmentManager();
            //开启事务
            FragmentTransaction transaction = fragmentManager.beginTransaction();
            if(from!=null){
                transaction.hide(from);
            }
            if(to!=null){
                if(!to.isAdded()){
                    transaction.add(R.id.fl_main_content,to).commit();
                }else{
                    transaction.show(to).commit();
                }
            }

        }
    }









    class  MyCheckedChangeListen implements RadioGroup.OnCheckedChangeListener{

        @Override
        public void onCheckedChanged(RadioGroup group, @IdRes int checkedId) {
            switch (checkedId){
                case R.id.rg_main_pager:
                    position=0;
                    break;
                case R.id.rg_search_pager:
                    position=1;
                    break;
                case R.id.rg_communicate_pager:
                    position=2;
                    break;
                case R.id.rg_mylibrary_pager:
                    position=3;
                    break;
                default:
                    position=0;
                    break;
            }

            BasePager to =getBasePager();
            setFragment(mContent,to);


        }
    }

    /**
     * 判断软件是否退出
     */
    private boolean isExit=false;
    /**
     * 软件退出功能
     * @param keyCode
     * @param event
     * @return
     */
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(keyCode==KeyEvent.KEYCODE_BACK){
            if(position!=0){
                position=0;
                rg_main.check(R.id.rg_main_pager);
                return true;//把事件消费
            }else if(!isExit){
                isExit=true;
                Toast.makeText(this,"再按一次退出",Toast.LENGTH_SHORT).show();
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        isExit=false;
                    }
                },2000);
                return true;//把事件消费
            }
        }
        return super.onKeyDown(keyCode, event);
    }
}
