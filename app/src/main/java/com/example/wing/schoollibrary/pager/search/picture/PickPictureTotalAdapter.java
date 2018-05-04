package com.example.wing.schoollibrary.pager.search.picture;

import android.content.Context;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.example.wing.schoollibrary.R;
import com.mylhyl.cygadapter.CygAdapter;
import com.mylhyl.cygadapter.CygViewHolder;

import java.util.List;

public class PickPictureTotalAdapter extends CygAdapter<PictureGroup> {
    public PickPictureTotalAdapter(Context context, List<PictureGroup> objects) {
        super(context, R.layout.activity_pick_picture_total_list_item, objects);
    }

    @Override
    public void onBindData(CygViewHolder viewHolder, PictureGroup item, int position) {
        viewHolder.setText(R.id.pick_picture_total_list_item_group_title, item.getFolderName());
        viewHolder.setText(R.id.pick_picture_total_list_item_group_count
                , "(" + Integer.toString(item.getPictureCount()) + ")");
        ImageView imageView = viewHolder.findViewById(R.id.pick_picture_total_list_item_group_image);
        Glide.with(mContext).load(item.getTopPicturePath()).into(imageView);
    }
}
