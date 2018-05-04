package com.example.wing.schoollibrary.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.wing.schoollibrary.R;
import com.example.wing.schoollibrary.bean.Book;

import java.util.List;

/**
 * Created by Administrator on 2018/3/13 0013.
 */

public class BookAdapter extends BaseAdapter {

    private static final  String BOOK_NAME="题名：";
    private static final  String BOOK_AUTHOR="责任者：";
    private static final  String BOOK_PUBLISH="出版社：";
    private static final  String BOOK_YEAY="出版日期：";
    private static final  String BOOK_CALLNUM="索取号：";
    private static final  String BOOK_BORROWNUM="可借：";

    private final Context context;
    private final List<Book> list;

    public BookAdapter(Context context, List<Book> list) {
        this.context=context;
        this.list=list;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder=null;
        if (convertView==null){
            convertView=View.inflate(context, R.layout.item_book,null);
            viewHolder=new ViewHolder();
            viewHolder.tv_name= (TextView) convertView.findViewById(R.id.tv_name);
            viewHolder.tv_author= (TextView) convertView.findViewById(R.id.tv_author);
            viewHolder.tv_publisher= (TextView) convertView.findViewById(R.id.tv_publisher);
            viewHolder.tv_publish_year= (TextView) convertView.findViewById(R.id.tv_publish_year);
            viewHolder.tv_callNum= (TextView) convertView.findViewById(R.id.tv_callNum);
            viewHolder.tv_borrowNum= (TextView) convertView.findViewById(R.id.tv_borrowNum);
            convertView.setTag(viewHolder);

        }else{
            viewHolder= (ViewHolder) convertView.getTag();
        }

        Book book = list.get(position);
        viewHolder.tv_name.setText(BOOK_NAME+book.getBookName());
        viewHolder.tv_author.setText(BOOK_AUTHOR+book.getAuthor());
        viewHolder.tv_publisher.setText(BOOK_PUBLISH+book.getPublisher());
        viewHolder.tv_publish_year.setText(BOOK_YEAY+book.getPublishYear());
        viewHolder.tv_callNum.setText(BOOK_CALLNUM+book.getCallNum());
        viewHolder.tv_borrowNum.setText(BOOK_BORROWNUM+book.getBorrowNum()+"");


        return convertView;
    }



    static class  ViewHolder{
        TextView tv_name;
        TextView tv_author;
        TextView tv_publisher;
        TextView tv_publish_year;
        TextView tv_callNum;
        TextView tv_borrowNum;
    }
}
