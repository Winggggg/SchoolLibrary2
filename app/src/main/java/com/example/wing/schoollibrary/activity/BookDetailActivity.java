package com.example.wing.schoollibrary.activity;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.wing.schoollibrary.R;
import com.example.wing.schoollibrary.bean.Book;

public class BookDetailActivity extends Activity implements View.OnClickListener {
    private ImageButton ibLoginBack;
    private Button btnCollect;

    private TextView tvName;
    private TextView tvAuthor;
    private TextView tvPublisher;
    private TextView tvPublishYear;
    private TextView tvAr;
    private TextView tvType;
    private TextView tvPlace;
    private TextView tvBorrowNum;
    private TextView tvIsbn;
    private TextView tvIssn;
    private TextView tv_detail;

    /**
     * Find the Views in the layout<br />
     * <br />
     * Auto-created on 2018-03-13 16:43:00 by Android Layout Finder
     * (http://www.buzzingandroid.com/tools/android-layout-finder)
     */
    private void findViews() {
        ibLoginBack = (ImageButton)findViewById( R.id.ib_login_back );
        btnCollect = (Button)findViewById( R.id.btn_collect );
        tvName = (TextView)findViewById( R.id.tv_name );
        tvAuthor = (TextView)findViewById( R.id.tv_author );
        tvPublisher = (TextView)findViewById( R.id.tv_publisher );
        tvPublishYear = (TextView)findViewById( R.id.tv_publish_year );
        tvAr = (TextView)findViewById( R.id.tv_ar );
        tvType = (TextView)findViewById( R.id.tv_type );
        tvPlace = (TextView)findViewById( R.id.tv_place );
        tvBorrowNum = (TextView)findViewById( R.id.tv_BorrowNum );
        tvIsbn = (TextView)findViewById( R.id.tv_isbn );
        tvIssn = (TextView)findViewById( R.id.tv_issn );
        tv_detail = (TextView)findViewById( R.id.tv_detail );

        ibLoginBack.setOnClickListener( this );
        btnCollect.setOnClickListener( this );
    }

    /**
     * Handle button click events<br />
     * <br />
     * Auto-created on 2018-03-13 16:43:00 by Android Layout Finder
     * (http://www.buzzingandroid.com/tools/android-layout-finder)
     */
    @Override
    public void onClick(View v) {
        if ( v == ibLoginBack ) {
            // Handle clicks for ibLoginBack
            finish();
        } else if ( v == btnCollect ) {
            // Handle clicks for btnCollect
        }
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_detail);
        findViews();

        getAndSetData();
    }

    private void getAndSetData() {
        Book book= (Book) getIntent().getSerializableExtra("book");
        if (book!=null){
            tv_detail.setText(book.getDetail());
            tvName.setText("题名："+book.getBookName());
            tvAuthor.setText("责任者："+book.getAuthor());
            tvPublisher.setText("出版者："+book.getPublisher());
            tvPublishYear.setText("出版年："+book.getBookName());
            tvAr.setText("索取号："+book.getCallNum());
            tvType.setText("借阅类型："+book.getBookType());
            tvPlace.setText("馆藏地点："+book.getCollectionPlace());
            tvBorrowNum.setText("可借数量："+book.getBorrowNum());
            tvIsbn.setText("ISBN："+book.getIsbn());
            tvIssn.setText("ISSN："+book.getIssn());
        }
    }
}
