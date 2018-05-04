package com.example.wing.schoollibrary.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.wing.schoollibrary.R;
import com.example.wing.schoollibrary.bean.BorrowHistory;

import java.text.SimpleDateFormat;
import java.util.List;

/**
 * Created by Administrator on 2018/3/23 0023.
 */

public class BorrowHistoryAdapter extends BaseAdapter{

    private final Context context;
    private static final String BOOK_NAME="题名/作者:";
    private static final String BORROW_TIME="借期:";
    private static final String TYPE="类型:";
    private static final String RETURN_TIME="归还时间:";
    private final List<BorrowHistory> items;
    private SimpleDateFormat simpleDateFormat;

    public  BorrowHistoryAdapter(Context context, List<BorrowHistory>items){
        this.context=context;
        this.items=items;
        simpleDateFormat=new SimpleDateFormat("yyyy-MM-dd");
    }
    @Override
    public int getCount() {
        return items.size();
    }

    @Override
    public Object getItem(int position) {
        return items.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder=null;
        if (convertView==null){
            convertView=View.inflate(context, R.layout.item_borrow_info,null);
            viewHolder=new ViewHolder();
            viewHolder.btn_borrow_again= (Button) convertView.findViewById(R.id.btn_borrow_again);
            viewHolder.tv_book_name= (TextView) convertView.findViewById(R.id.tv_book_name);
            viewHolder.tv_type= (TextView) convertView.findViewById(R.id.tv_type);
            viewHolder.tv_borrow_time= (TextView) convertView.findViewById(R.id.tv_borrow_time);
            viewHolder.tv_return_time= (TextView) convertView.findViewById(R.id.tv_return_time);
            viewHolder.pb_loading= (ProgressBar) convertView.findViewById(R.id.pb_loading);
            convertView.setTag(viewHolder);
        }else{
            viewHolder= (ViewHolder) convertView.getTag();
        }

        BorrowHistory borrowHistory=items.get(position);

        BindData(borrowHistory,viewHolder);

        return convertView;
    }

    private void BindData(final BorrowHistory borrowHistory, final ViewHolder viewHolder) {
        if (borrowHistory.getBook()!=null){
            viewHolder.tv_book_name.setText(BOOK_NAME+borrowHistory.getBook().getBookName());
            viewHolder.tv_type.setText(TYPE+borrowHistory.getBook().getBookType());
            viewHolder.tv_borrow_time.setText(BORROW_TIME+borrowHistory.getBorrowTime());
            viewHolder.tv_return_time.setText(RETURN_TIME+borrowHistory.getReturnTime());
            viewHolder.btn_borrow_again.setVisibility(View.GONE);
        }
    }

    static class ViewHolder{
        TextView tv_book_name;
        TextView tv_type;
        TextView tv_borrow_time;
        TextView tv_return_time;
        Button btn_borrow_again;
        ProgressBar pb_loading;
    }

}
