package com.example.wing.schoollibrary.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

import com.example.wing.schoollibrary.R;
import com.example.wing.schoollibrary.bean.BorrowInfo;

import java.util.List;

/**
 * Created by Administrator on 2018/3/23 0023.
 */

public class ReturnBookAdapter extends BaseAdapter{

    private final Context context;
    private static final String BOOK_NAME="题名/作者:";
    private static final String COLLECT_PLACE="馆藏室:";
    private static final String RETURN_TIME="最迟应还期:";
    private final List<BorrowInfo> items;

    public ReturnBookAdapter(Context context, List<BorrowInfo>items){
        this.context=context;
        this.items=items;
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
            viewHolder.tv_collect= (TextView) convertView.findViewById(R.id.tv_type);
            viewHolder.tv_borrow_time= (TextView) convertView.findViewById(R.id.tv_borrow_time);
            viewHolder.tv_return_time= (TextView) convertView.findViewById(R.id.tv_return_time);
            convertView.setTag(viewHolder);
        }else{
            viewHolder= (ViewHolder) convertView.getTag();
        }

        BorrowInfo borrowInfo=items.get(position);

        BindData(borrowInfo,viewHolder);

        return convertView;
    }

    private void BindData(final BorrowInfo borrowInfo, final ViewHolder viewHolder) {
        if (borrowInfo.getBook()!=null){
            viewHolder.tv_book_name.setText(BOOK_NAME+borrowInfo.getBook().getBookName());
            viewHolder.tv_collect.setText(COLLECT_PLACE+borrowInfo.getBook().getCollectionPlace());
            viewHolder.tv_return_time.setText(RETURN_TIME+borrowInfo.getReturnUtiltime());
            viewHolder.tv_return_time.setTextColor(Color.RED);
            viewHolder.tv_borrow_time.setVisibility(View.GONE);
            viewHolder.btn_borrow_again.setVisibility(View.GONE);
        }
    }

    static class ViewHolder{
        TextView tv_book_name;
        TextView tv_collect;
        TextView tv_borrow_time;
        TextView tv_return_time;
        Button btn_borrow_again;
    }

}
