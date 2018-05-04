package com.example.wing.schoollibrary;

import android.app.Application;

import com.example.wing.schoollibrary.Jpush.Logger;
import com.example.wing.schoollibrary.bean.Student;
import com.lzy.okhttputils.OkHttpUtils;
import com.lzy.okhttputils.cookie.store.PersistentCookieStore;
import com.lzy.okhttputils.model.HttpHeaders;
import com.lzy.okhttputils.model.HttpParams;
import com.mob.MobSDK;

import cn.jpush.android.api.JPushInterface;

import static com.lzy.okhttputils.interceptor.LoggerInterceptor.TAG;

/**
 * Created by Administrator on 2018/2/17 0017.
 */

public class MyApplication extends Application {
    public Student getStudent() {
        return student;
    }

    public void setStudent(Student student) {
        this.student = student;
    }

    private Student student;

    public boolean isCleanCache() {
        return isCleanCache;
    }

    public void setCleanCache(boolean cleanCache) {
        isCleanCache = cleanCache;
    }

    private boolean isCleanCache=false;

    @Override
    public void onCreate() {
        super.onCreate();


        Logger.d(TAG, "[MyApplication] onCreate");

        JPushInterface.setDebugMode(true); 	// 设置开启日志,发布时请关闭日志
        JPushInterface.init(this);     		// 初始化 JPush

        initOKhttp();

        //分享
        MobSDK.init(this);
    }

    private void initOKhttp() {
        HttpHeaders headers = new HttpHeaders();
        headers.put("commonHeaderKey1", "commonHeaderValue1");    //所有的 header 都 不支持 中文
        headers.put("commonHeaderKey2", "commonHeaderValue2");
        HttpParams params = new HttpParams();
        params.put("commonParamsKey1", "commonParamsValue1");     //所有的 params 都 支持 中文
        params.put("commonParamsKey2", "这里支持中文参数");

        //必须调用初始化
        OkHttpUtils.init(this);
        //以下都不是必须的，根据需要自行选择
        OkHttpUtils.getInstance()//
                .debug("OkHttpUtils")                                              //是否打开调试
                .setConnectTimeout(OkHttpUtils.DEFAULT_MILLISECONDS)               //全局的连接超时时间
                .setReadTimeOut(OkHttpUtils.DEFAULT_MILLISECONDS)                  //全局的读取超时时间
                .setWriteTimeOut(OkHttpUtils.DEFAULT_MILLISECONDS)                 //全局的写入超时时间
                //                .setCookieStore(new MemoryCookieStore())                           //cookie使用内存缓存（app退出后，cookie消失）
                .setCookieStore(new PersistentCookieStore())                       //cookie持久化存储，如果cookie不过期，则一直有效
                .addCommonHeaders(headers)                                         //设置全局公共头
                .addCommonParams(params);                                          //设置全局公共参数
    }
}
