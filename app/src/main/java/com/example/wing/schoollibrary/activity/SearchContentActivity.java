package com.example.wing.schoollibrary.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import com.example.wing.schoollibrary.R;
import com.example.wing.schoollibrary.adapter.BookAdapter;
import com.example.wing.schoollibrary.bean.Book;

import java.util.List;


public class SearchContentActivity extends Activity implements View.OnClickListener {
    private ImageButton ibLoginBack;
    private ListView listview;
    private TextView tvNohistory;
    private BookAdapter adapter;
    private List<Book> list;

    /**
     * Find the Views in the layout<br />
     * <br />
     * Auto-created on 2018-03-13 14:44:15 by Android Layout Finder
     * (http://www.buzzingandroid.com/tools/android-layout-finder)
     */
    private void findViews() {
        ibLoginBack = (ImageButton)findViewById( R.id.ib_login_back );
        listview = (ListView)findViewById( R.id.search_history_listview );
        tvNohistory = (TextView)findViewById( R.id.tv_nohistory );

        ibLoginBack.setOnClickListener( this );
    }

    /**
     * Handle button click events<br />
     * <br />
     * Auto-created on 2018-03-13 14:44:15 by Android Layout Finder
     * (http://www.buzzingandroid.com/tools/android-layout-finder)
     */
    @Override
    public void onClick(View v) {
        if ( v == ibLoginBack ) {
            // Handle clicks for ibLoginBack
            finish();
        }
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity__search_content);

        findViews();

        setData();
    }

    private void setData() {
        Bundle bundle=getIntent().getExtras();
        list= (List<Book>) bundle.getSerializable("list");
        if (list!=null&&list.size()>0){
            adapter=new BookAdapter(this,list);
            listview.setAdapter(adapter);
            listview.setOnItemClickListener(new MyOnItemClickListener());
        }else{
            tvNohistory.setVisibility(View.VISIBLE);
        }
    }

    private class MyOnItemClickListener implements AdapterView.OnItemClickListener {

        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            Intent intent=new Intent(SearchContentActivity.this,BookDetailActivity.class);
            intent.putExtra("book",list.get(position));
            SearchContentActivity.this.startActivity(intent);
        }
    }

}
