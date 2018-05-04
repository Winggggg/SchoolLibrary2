package com.example.wing.schoollibrary.activity;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.wing.schoollibrary.R;
import com.example.wing.schoollibrary.adapter.SearchHistoryAdapter;
import com.example.wing.schoollibrary.bean.Book;
import com.example.wing.schoollibrary.util.CacheUtils;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.HashMap;

public class MySearchHistoryActivity extends Activity {

    private ListView listView;
    private ProgressBar pb_loading;
    private SearchHistoryAdapter adapter;
    private HashMap<String,String> cacheMap;
    private TextView tv_nohistory;
    private ImageButton ib_back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_search_history);

        initView();
        pb_loading.setVisibility(View.VISIBLE);
        getAndSetData();
        pb_loading.setVisibility(View.GONE);
    }

    private void getAndSetData() {
        String json = CacheUtils.getString(this, "book_search");
        Gson gson= new GsonBuilder().enableComplexMapKeySerialization().create();
        if (json!=null&&!"".equals(json)){
            cacheMap=gson.fromJson(json,new TypeToken<HashMap<String,String>>(){}.getType());
            if (cacheMap!=null&&cacheMap.size()>0){
                ArrayList<String> timeList=new ArrayList<>();
                ArrayList<Book> list=new ArrayList<>();
                for (String time:cacheMap.keySet()){
                    timeList.add(time);
                    Book book = gson.fromJson(cacheMap.get(time), Book.class);
                    list.add(book);
                }
                if (list.size()>0){
                    adapter=new SearchHistoryAdapter(this,list,timeList);
                    listView.setAdapter(adapter);
                }else{
                    tv_nohistory.setVisibility(View.VISIBLE);
                }
            }
        }

    }

    private void initView() {
        listView= (ListView) findViewById(R.id.listview);
        pb_loading= (ProgressBar) findViewById(R.id.pb_loading);
        tv_nohistory= (TextView) findViewById(R.id.tv_nohistory);
        ib_back= (ImageButton) findViewById(R.id.ib_back);
        ib_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}
