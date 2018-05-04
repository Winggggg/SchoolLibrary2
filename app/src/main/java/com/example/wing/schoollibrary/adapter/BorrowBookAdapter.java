package com.example.wing.schoollibrary.adapter;

import android.content.Context;
import android.support.annotation.Nullable;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.wing.schoollibrary.R;
import com.example.wing.schoollibrary.bean.BorrowInfo;
import com.example.wing.schoollibrary.util.MSG;
import com.example.wing.schoollibrary.util.Urls;
import com.google.gson.Gson;
import com.lzy.okhttputils.OkHttpUtils;
import com.lzy.okhttputils.callback.StringCallback;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import okhttp3.Call;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by Administrator on 2018/3/22 0022.
 */

public class BorrowBookAdapter extends BaseAdapter {


    private final Context context;
    private static final String BOOK_NAME="题名/作者:";
    private static final String BORROW_TIME="借期:";
    private static final String TYPE="类型:";
    private static final String RETURN_TIME="最迟应还期:";
    private final List<BorrowInfo> items;
    private SimpleDateFormat simpleDateFormat;

    public  BorrowBookAdapter(Context context, List<BorrowInfo>items){
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

        BorrowInfo borrowInfo=items.get(position);

        BindData(borrowInfo,viewHolder);

        return convertView;
    }

    private void BindData(final BorrowInfo borrowInfo, final ViewHolder viewHolder) {
        if (borrowInfo.getBook()!=null){
            viewHolder.tv_book_name.setText(BOOK_NAME+borrowInfo.getBook().getBookName());
            viewHolder.tv_type.setText(TYPE+borrowInfo.getBook().getBookType());
            viewHolder.tv_borrow_time.setText(BORROW_TIME+borrowInfo.getBorrowTime());
            viewHolder.tv_return_time.setText(RETURN_TIME+borrowInfo.getReturnUtiltime());

            try {
                Date begin=simpleDateFormat.parse(borrowInfo.getBorrowTime());
                Date end=simpleDateFormat.parse(borrowInfo.getReturnUtiltime());
                long Days=(end.getTime()-begin.getTime())/(24*60*60*1000);
                if (Days>93){
                    viewHolder.btn_borrow_again.setEnabled(false);
                }
            } catch (ParseException e) {
                e.printStackTrace();
            }

            viewHolder.btn_borrow_again.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    try {
                        Date returnDate=simpleDateFormat.parse(borrowInfo.getReturnUtiltime());
                        GregorianCalendar gc =new GregorianCalendar();
                        gc.setTime(returnDate);
                        gc.add(Calendar.MONTH,3);//表示月份加3
                        borrowInfo.setReturnUtiltime(simpleDateFormat.format(gc.getTime()));
                        viewHolder.pb_loading.setVisibility(View.VISIBLE);
                        OkHttpUtils.get(Urls.URL_UPDATETIME)
                                .params("borrowInfo",new Gson().toJson(borrowInfo))
                                .execute(new MyStringCallBack(viewHolder));
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }

                }
            });
        }
    }


    private class  MyStringCallBack extends StringCallback {

        private final ViewHolder viewholder;

        public  MyStringCallBack(ViewHolder viewholder){
            this.viewholder=viewholder;
        }

        @Override
        public void onResponse(boolean isFromCache, String s, Request request, @Nullable Response response) {
            processData(s);
        }

        @Override
        public void onError(boolean isFromCache, Call call, @Nullable Response response, @Nullable Exception e) {
            Toast.makeText(context,e.getMessage(),Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onAfter(boolean isFromCache, @Nullable String s, Call call, @Nullable Response response, @Nullable Exception e) {
            super.onAfter(isFromCache, s, call, response, e);
            viewholder.pb_loading.setVisibility(View.GONE);
        }
    }

    private void processData(String s) {
        parseJson(s);
    }

    private void parseJson(String s) {
        Gson gson=new Gson();
        MSG msg=gson.fromJson(s,MSG.class);
        if (msg!=null&&msg.getStatusCode()==100){
            //数据是成功的
            Toast.makeText(context,"续借成功",Toast.LENGTH_SHORT).show();
            notifyDataSetChanged();
        }else if (msg!=null&&msg.getStatusCode()==200){
            Toast.makeText(context,msg.getExtend().get("result").toString(),Toast.LENGTH_SHORT).show();
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
