package com.example.wing.schoollibrary.pager.communicate;

import android.content.Intent;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.AbsListView;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.wing.schoollibrary.R;
import com.example.wing.schoollibrary.activity.EditCommunicateActivity;
import com.example.wing.schoollibrary.adapter.CommunicateAdapter;
import com.example.wing.schoollibrary.base.BasePager;
import com.example.wing.schoollibrary.bean.Communicate;
import com.example.wing.schoollibrary.util.CacheUtils;
import com.example.wing.schoollibrary.util.MSG;
import com.example.wing.schoollibrary.util.Urls;
import com.example.wing.schoollibrary.view.XListView;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.lzy.okhttputils.OkHttpUtils;
import com.lzy.okhttputils.callback.StringCallback;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import cn.jzvd.JZVideoPlayer;
import okhttp3.Call;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by Administrator on 2017/12/15 0015.
 * 树洞交流
 */

public class CommunicatePager extends BasePager implements XListView.IXListViewListener, View.OnClickListener {

    private static final String TAG = CommunicatePager.class.getSimpleName();
    private List<Communicate> items=new ArrayList<>();
    private CommunicateAdapter adapter;
    private XListView mListView;
    private ProgressBar pb_loading;
    private TextView tv_title;
    private ImageButton ib_edit;

    private Handler mHandler;


    @Override
    public View initView() {
        View view=View.inflate(context, R.layout.activity_communicate_pager,null);

        mHandler = new Handler();
        pb_loading= (ProgressBar) view.findViewById(R.id.pb_loading);
        tv_title= (TextView) view.findViewById(R.id.tv_title);
        ib_edit= (ImageButton) view.findViewById(R.id.ib_edit);

        mListView = (XListView) view.findViewById(R.id.xlistView);
        mListView.setPullRefreshEnable(true);
        mListView.setPullLoadEnable(false);
        mListView.setAutoLoadEnable(false);
        mListView.setXListViewListener(this);
        mListView.setRefreshTime(getTime());

        return view;
    }


    private void onLoad() {
        mListView.stopRefresh();
        mListView.stopLoadMore();
        mListView.setRefreshTime(getTime());
    }

    @Override
    public void onPause() {
        super.onPause();
        JZVideoPlayer.releaseAllVideos();
    }


    private String getTime() {
        return new SimpleDateFormat("MM-dd HH:mm", Locale.CHINA).format(new Date());
    }


    @Override
    public void initData() {
        super.initData();
        Log.e(TAG,"树洞交流初始化中....");
        tv_title.setText("树洞交流");
        ib_edit.setVisibility(View.VISIBLE);

        setListener();
        getDataFromNet();
    }


    private void setListener() {
        mListView.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {

            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                JZVideoPlayer.onScrollReleaseAllVideos(view, firstVisibleItem, visibleItemCount, totalItemCount);
            }
        });

        ib_edit.setOnClickListener(this);

    }

    @Override
    public void onRefresh() {
        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                items.clear();
                getDataFromNet();
                onLoad();
            }
        }, 2500);
    }

    /**
     * 获取网络资源
     */
    private void getDataFromNet() {
        String url= Urls.SERVER+"getAllCommunicate";
        OkHttpUtils.get(url)
                .execute(new MyStringCallBack());
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.ib_edit://编辑按钮
                startActivity(new Intent(context,EditCommunicateActivity.class));
                break;
        }
    }

    private class  MyStringCallBack extends StringCallback{

        @Override
        public void onResponse(boolean isFromCache, String s, Request request, @Nullable Response response) {
            processData(s);
            //缓存数据
            CacheUtils.putString(context,"communicate",s);
        }

        @Override
        public void onError(boolean isFromCache, Call call, @Nullable Response response, @Nullable Exception e) {
            super.onError(isFromCache, call, response, e);
            String cacheString= CacheUtils.getString(context,"communicate");
            if (!TextUtils.isEmpty(cacheString)){
                processData(cacheString);
            }
        }
    }

    /**
     * 处理网络数据
     * @param result
     */
    private void processData(String result) {
        items=parseJson(result);
        showData(items);
    }

    /**
     * 显示数据
     * @param items
     */
    private void showData(List<Communicate> items) {
        if (items!=null&&items.size()>0){
            adapter=new CommunicateAdapter(context,items);
            mListView.setAdapter(adapter);
        }

        pb_loading.setVisibility(View.GONE);
    }

    /**
     * 解析json数据
     * @param result
     * @return
     */
    private List<Communicate> parseJson(String result) {
        List<Communicate> items=new ArrayList<>();
        Gson gson=new Gson();
        MSG msg=gson.fromJson(result,MSG.class);
        if (msg.getStatusCode()==100){
            //数据是成功的
            Map<String, Object> map=msg.getExtend();
            Object object=  map.get("list");

            String json=gson.toJson(object);

            items=gson.fromJson(json,new TypeToken<List<Communicate>>(){}.getType());

        }
        return  items;
    }

    @Override
    public void onLoadMore() {
        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
//                mAdapter.notifyDataSetChanged();
                onLoad();
            }
        }, 2500);
    }
}
