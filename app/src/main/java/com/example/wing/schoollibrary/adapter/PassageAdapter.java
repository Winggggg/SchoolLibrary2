package com.example.wing.schoollibrary.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.wing.schoollibrary.R;
import com.example.wing.schoollibrary.activity.PassageActivity;
import com.example.wing.schoollibrary.bean.Passage;

import java.util.List;

/**
 * Created by Administrator on 2018/3/6 0006.
 */

public class PassageAdapter extends BaseAdapter {

    private Context context;
    private List<Passage> list;

    public PassageAdapter(Context context,List<Passage> list){
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
    public View getView(final int position, View convertView, ViewGroup parent) {
        Viewholder viewholder=null;
        if (convertView==null){
            convertView=View.inflate(context, R.layout.item_text,null);
            viewholder=new Viewholder();
            viewholder.textView= (TextView) convertView.findViewById(R.id.tv_passage);
            convertView.setTag(viewholder);
        }else{
            viewholder= (Viewholder) convertView.getTag();
        }
        viewholder.textView.setText(list.get(position).getTitle());
        viewholder.textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(context, PassageActivity.class);
                intent.putExtra("passage",list.get(position));
                context.startActivity(intent);
            }
        });

        return convertView;
    }


    static  class  Viewholder{
        TextView textView;
    }
}
