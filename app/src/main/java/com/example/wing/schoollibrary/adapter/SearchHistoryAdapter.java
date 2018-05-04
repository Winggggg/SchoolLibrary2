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

public class SearchHistoryAdapter extends BaseAdapter {
    private final List<Book> list;
    private final Context context;
    private final List<String> timelist;

    public SearchHistoryAdapter(Context context, List<Book>list,List<String> timelist) {
        this.context=context;
        this.list=list;
        this.timelist=timelist;
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
        ViewHolder viewholder=null;
        if (convertView==null){
            convertView=View.inflate(context, R.layout.item_search_history,null);
            viewholder=new ViewHolder();
            viewholder.num= (TextView) convertView.findViewById(R.id.tv_num);
            viewholder.content= (TextView) convertView.findViewById(R.id.tv_content);
            viewholder.time= (TextView) convertView.findViewById(R.id.tv_time);
            convertView.setTag(viewholder);
        }else{
            viewholder= (ViewHolder) convertView.getTag();
        }

        Book book=list.get(position);
        viewholder.num.setText(""+(position+1));
        StringBuilder sb=new StringBuilder();
        if (book.getBookName()!=null&&!"".equals(book.getBookName())){
            sb.append(book.getBookName()+",");
        }
        if (book.getAuthor()!=null&&!"".equals(book.getAuthor())){
            sb.append(book.getAuthor()+",");
        }
        if (book.getPublisher()!=null&&!"".equals(book.getPublisher())){
            sb.append(book.getPublisher()+",");
        }
        if (book.getBookType()!=null&&!"".equals(book.getBookType())){
            sb.append(book.getBookType()+",");
        }
        if (book.getCallNum()!=null&&!"".equals(book.getCallNum())){
            sb.append(book.getCallNum()+",");
        }
        if (book.getCollectionPlace()!=null&&!"".equals(book.getCollectionPlace())){
            sb.append(book.getCollectionPlace()+",");
        }
        if (book.getIsbn()!=null&&!"".equals(book.getIsbn())){
            sb.append(book.getIsbn()+",");
        }
        if (book.getIssn()!=null&&!"".equals(book.getIssn())){
            sb.append(book.getIssn()+",");
        }
        viewholder.content.setText(sb.toString());
        viewholder.time.setText(timelist.get(position));
        return convertView;
    }


    static  class  ViewHolder{
        TextView num;
        TextView content;
        TextView time;

    }
}
