package com.example.wing.schoollibrary.pager.search.picture;

import android.content.Context;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.example.wing.schoollibrary.R;
import com.mylhyl.cygadapter.CygAdapter;
import com.mylhyl.cygadapter.CygViewHolder;

import java.util.List;

/**
 * 照片浏览
 * Created by hupei on 2016/7/7.
 */
class PickPictureAdapter extends CygAdapter<Picture> {

    public PickPictureAdapter(Context context, List<Picture> datas) {
        super(context, R.layout.activity_pick_picture_grid_item, datas);
    }

    @Override
    public void onBindData(CygViewHolder viewHolder, Picture item, int position) {
        ImageView imageView = viewHolder.findViewById(R.id.activity_pick_picture_grid_item_image);
        Glide.with(mContext).load(item.getPath()).into(imageView);
    }
}
