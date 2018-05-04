package com.example.wing.schoollibrary.activity;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ListView;

import com.example.wing.schoollibrary.R;
import com.example.wing.schoollibrary.adapter.PassageAdapter;
import com.example.wing.schoollibrary.bean.Passage;

import java.util.List;

public class PassageListActivity extends Activity implements View.OnClickListener {

    private ListView listView;
    private PassageAdapter adapter;
    private List<Passage> passageList;
    private ImageButton ib_share;
    private ImageButton ib_back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_passage_list);

        ib_share= (ImageButton) findViewById(R.id.ib_share);
        ib_back= (ImageButton) findViewById(R.id.ib_back);
        listView= (ListView) findViewById(R.id.listView);

        ib_share.setVisibility(View.GONE);
        ib_back.setOnClickListener(this);
        getData();



    }

    /**
     * 获取数据
     */
    private void getData() {
        Bundle bundle=getIntent().getExtras();
        passageList= (List<Passage>) bundle.get("passageList");
        if (passageList!=null&&passageList.size()>0){
            adapter=new PassageAdapter(this,passageList);
            listView.setAdapter(adapter);
        }
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.ib_back:
                finish();
                break;
        }
    }
}
