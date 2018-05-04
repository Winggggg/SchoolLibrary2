package com.example.wing.schoollibrary.adapter;

import android.content.Context;
import android.net.Uri;
import android.text.format.Formatter;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.wing.schoollibrary.R;
import com.example.wing.schoollibrary.bean.MediaBean;
import com.example.wing.schoollibrary.util.Utils;

import java.util.List;

/**
 * Created by Administrator on 2017/12/18 0018.
 * 视频播放列表适配器
 */

public class MediaAdapter extends BaseAdapter {


    private Utils utils;
    private  List<MediaBean> mediaItems;
    private  Context context;

    public MediaAdapter(Context context, List<MediaBean> mediaItems){
        this.context=context;
        this.mediaItems=mediaItems;
        utils=new Utils();
    }


    @Override
    public int getCount() {
        return mediaItems.size();
    }

    @Override
    public Object getItem(int position) {
        return mediaItems.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder=null;
        if(convertView==null){
            convertView=View.inflate(context, R.layout.media_item,null);
            viewHolder=new ViewHolder();
            viewHolder.iv_mediaIcon= (ImageView) convertView.findViewById(R.id.iv_mediaIcon);
            viewHolder.tv_mediaName= (TextView) convertView.findViewById(R.id.tv_mediaName);
            viewHolder.tv_size= (TextView) convertView.findViewById(R.id.tv_size);
            viewHolder.tv_time= (TextView) convertView.findViewById(R.id.tv_time);
            convertView.setTag(viewHolder);
        }else{
            viewHolder= (ViewHolder) convertView.getTag();
        }

        //获取数据
        MediaBean mediaBean = mediaItems.get(position);
        viewHolder.tv_mediaName.setText(mediaBean.getMediaName());
        if(mediaBean.getThumbPath()!=null){
            viewHolder.iv_mediaIcon.setImageURI(Uri.parse(mediaBean.getThumbPath()));
        }
        viewHolder.tv_time.setText(utils.stringForTime((int) mediaBean.getMediaTime()));
        viewHolder.tv_size.setText(Formatter.formatFileSize(context,mediaBean.getSize()));
        return convertView;
    }

    static class  ViewHolder{
        ImageView iv_mediaIcon;
        TextView tv_mediaName;
        TextView tv_time;
        TextView tv_size;
    }
}
