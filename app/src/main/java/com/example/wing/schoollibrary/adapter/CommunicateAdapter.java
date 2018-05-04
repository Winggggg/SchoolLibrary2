package com.example.wing.schoollibrary.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.Priority;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.example.wing.schoollibrary.MyApplication;
import com.example.wing.schoollibrary.R;
import com.example.wing.schoollibrary.activity.CommentActivity;
import com.example.wing.schoollibrary.bean.Communicate;
import com.example.wing.schoollibrary.bean.Student;
import com.example.wing.schoollibrary.util.DensityUtil;
import com.example.wing.schoollibrary.util.LogUtil;
import com.example.wing.schoollibrary.util.Urls;
import com.google.gson.Gson;
import com.lzy.okhttputils.OkHttpUtils;
import com.lzy.okhttputils.callback.StringCallback;

import java.util.List;

import cn.jzvd.JZVideoPlayer;
import cn.jzvd.JZVideoPlayerStandard;
import okhttp3.Call;
import okhttp3.Request;
import okhttp3.Response;


/**
 * 树洞交流列表适配器
 * Created by Administrator on 2018/3/6 0006.
 */

public class CommunicateAdapter extends BaseAdapter {

    private static final String ADD_LIKE = "addlike";
    private static final String CANCEL_LIKE = "cancellike";
    private List<Communicate> items;
    private int currenPosittion;

    /**
     * 视频
     */
    private static final int TYPE_VIDEO=0;
    /**
     * 图片
     */
    private static final int TYPE_IMAGE=1;
    /**
     * gif
     */
    private static final int TYPE_GIF=2;
    /**
     * 文本
     */
    private static final int TYPE_TEXT=3;
    /**
     * 当前用户
     */
    private Student currentStudent;


    private Context context;

    public  CommunicateAdapter(Context context,List<Communicate>items){
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
        return position;
    }

    /***
     * 多类型匹配
     * @param position
     * @return
     */
    @Override
    public int getItemViewType(int position) {
        int type=-1;
        String beanType=items.get(position).getType();
        if(beanType.equals("video")){
            type=TYPE_VIDEO;
        }else if(beanType.equals("gif")){
            type=TYPE_GIF;
        }else if(beanType.equals("text")){
            type=TYPE_TEXT;
        }else  if(beanType.equals("image")){
            type=TYPE_IMAGE;
        }
        return type;
    }

    @Override
    public int getViewTypeCount() {
        return 4;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        int itemViewType  = getItemViewType(position) ;//得到类型

        ViewHolder viewHolder;

        if(convertView ==null){
            //初始化
            //初始化item布局
            viewHolder = new ViewHolder();
            convertView = initView(convertView, itemViewType, viewHolder);
            //初始化公共的视图
            initCommonView(convertView, itemViewType, viewHolder);
            //设置tag
            convertView.setTag(viewHolder);

        }else{
            //获取tag
            viewHolder = (ViewHolder) convertView.getTag();
        }

        //绑定数据
        //根据位置得到数据,绑定数据
        Communicate communicate= items.get(position);
        bindMiddleData(itemViewType, viewHolder, communicate,position);
        return convertView;
    }

    private void bindMiddleData(int itemViewType, ViewHolder viewHolder, Communicate communicate, int position) {
        switch (itemViewType) {
            case TYPE_VIDEO://视频
                bindData(viewHolder, communicate);
                //第一个参数是视频播放地址，第二个参数是显示封面的地址，第三参数是标题
                LogUtil.e("播放地址=="+communicate.getVideoUrl());
                String VideoUrl=communicate.getVideoUrl();
                if(!VideoUrl.startsWith("http")){
                    VideoUrl= Urls.URL_IMAGE+VideoUrl;
                }
                viewHolder.video_content.setUp(VideoUrl, JZVideoPlayer.SCREEN_WINDOW_LIST, "123");
               //设置缩略图
                if (communicate.getImgUrl()!=null){
                    RequestOptions options = new RequestOptions()
                            .priority(Priority.HIGH)
                            .diskCacheStrategy(DiskCacheStrategy.ALL);
                    Glide.with(context).load(communicate.getImgUrl())
                            .apply(options)
                            .into(viewHolder.video_content.thumbImageView);
                }
                viewHolder.video_content.positionInList = position;
                break;
//            case TYPE_GIF://gif
            case TYPE_IMAGE://图片
                bindData(viewHolder, communicate);
                viewHolder.iv_gifcontent.setImageResource(R.drawable.bg_item);
                int  height=0;
                if (communicate.getHeight()!=null){
                      height = communicate.getHeight()<= DensityUtil.getScreenHeight(context)*0.75?communicate.getHeight(): (int) (DensityUtil.getScreenHeight(context) * 0.75);
                }else {
                      height = DensityUtil.getScreenHeight(context);
                }

                int screenWidth=DensityUtil.getScreenWidth(context);
                LogUtil.e("屏幕宽度"+screenWidth);
                LogUtil.e("屏幕高度"+DensityUtil.getScreenHeight(context));
                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(screenWidth,height);
                viewHolder.iv_gifcontent.setLayoutParams(params);
                if(communicate.getImgUrl() != null ){
                    RequestOptions options2 = new RequestOptions()
                            .placeholder(R.drawable.loading)
                            .error(R.drawable.error)
                            .priority(Priority.HIGH)
                            .fitCenter()
                            .override(screenWidth,height)
                            .diskCacheStrategy(DiskCacheStrategy.ALL);

                    String imageurl=communicate.getImgUrl();
                    if(!imageurl.startsWith("http")){
                        imageurl= Urls.URL_IMAGE+imageurl;
                    }

                    Glide.with(context).load(imageurl)
                            .apply(options2)
                            .into(viewHolder.iv_gifcontent);


                }
                break;
            case TYPE_TEXT://文字
                bindData(viewHolder, communicate);
                break;
            case TYPE_GIF://gif
                bindData(viewHolder, communicate);
                RequestOptions options3 = new RequestOptions()
                        .placeholder(R.drawable.loading)
                        .error(R.drawable.error)
                        .priority(Priority.HIGH)
                        .diskCacheStrategy(DiskCacheStrategy.ALL);

                if (communicate.getWidth()!=null&&communicate.getHeight()!=null){
                    options3.override(communicate.getWidth(),communicate.getHeight());
                }

                String imageurl=communicate.getImgUrl();
            if(!imageurl.startsWith("http")){
                imageurl= Urls.URL_IMAGE+imageurl;
            }

            Glide.with(context).load(imageurl)
                    .apply(options3)
                    .into(viewHolder.iv_gifcontent);
            break;
        }


        //设置文本
        viewHolder.tv_content.setText(communicate.getText());
    }

    /**
     *绑定头像和点赞评论数据
     * @param viewHolder
     */
    private void bindData(final ViewHolder viewHolder, final Communicate communicate) {
        if(communicate.getStudent()!=null){
            //设置头像
            viewHolder.user_header.setImageResource(R.drawable.user);
        }
        if(communicate.getStudent() != null&&communicate.getStudent().getStuName()!= null){
            viewHolder.tv_userName.setText(communicate.getStudent().getStuName()+"");
        }

        viewHolder.tv_time.setText(communicate.getPublishTime());


        //设置点赞，评论

        if (communicate.getCommentNum()!=null)
            viewHolder.tv_comment.setText(communicate.getCommentNum()+"");
        if (communicate.getLikeNum()!=null)
            viewHolder.tv_like.setText(communicate.getLikeNum()+"");

        //点赞点击事件
        viewHolder.iv_like.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setLike(viewHolder,communicate);
                LogUtil.e("更新点赞");
            }

        });

        //评论点击事件
        viewHolder.iv_comment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(context,CommentActivity.class);
                intent.putExtra("comid",communicate.getCommId());
                intent.putExtra("student",communicate.getStudent());
                context.startActivity(intent);
            }
        });



    }

    /**
     * 设置点赞
     */
    private void setLike(ViewHolder viewHolder,Communicate communicate) {
        MyApplication app= (MyApplication) context.getApplicationContext();
        currentStudent=app.getStudent();
        if (currentStudent!=null){
            List<Integer> likeList = currentStudent.getLikeList();
            String json=new Gson().toJson(communicate);
            if (likeList!=null){
                String flag="";
                if (likeList.contains(communicate.getCommId())){
                    flag=CANCEL_LIKE;
                    likeList.remove(communicate.getCommId());
                    LogUtil.e("点赞取消");
                    communicate.setLikeNum(communicate.getLikeNum()-1);
                    viewHolder.tv_like.setText(communicate.getLikeNum()+"");
                }else{
                    flag=ADD_LIKE;
                    likeList.add(communicate.getCommId());
                    LogUtil.e("点赞+1");
                    if (communicate.getLikeNum()==null){
                        communicate.setLikeNum(1);
                    }else {
                        communicate.setLikeNum(communicate.getLikeNum()+1);
                    }
                    viewHolder.tv_like.setText(communicate.getLikeNum()+"");
                }
                OkHttpUtils.get(Urls.URL_Like)
                        .params("communicateJson",json)
                        .params("flag",flag)
                        .execute(new MyStringCallBack());
            }
        }
    }

    private class  MyStringCallBack extends StringCallback{

        @Override
        public void onResponse(boolean isFromCache, String s, Request request, @Nullable Response response) {
            ProcessLikeData(s);
        }

        @Override
        public void onAfter(boolean isFromCache, @Nullable String s, Call call, @Nullable Response response, @Nullable Exception e) {
            super.onAfter(isFromCache, s, call, response, e);
        }

        @Override
        public void onError(boolean isFromCache, Call call, @Nullable Response response, @Nullable Exception e) {
            super.onError(isFromCache, call, response, e);
        }
    }

    /***
     * 处理数据
     * @param s
     */
    private void ProcessLikeData(String s) {
        //notifyDataSetChanged();
    }


    private void initCommonView(View convertView, int itemViewType, ViewHolder viewHolder) {
        switch (itemViewType) {
            case TYPE_VIDEO://视频
            case TYPE_IMAGE://图片
            case TYPE_TEXT://文字
            case TYPE_GIF://gif
                //user info
                viewHolder.user_header = (ImageView) convertView.findViewById(R.id.user_header);
                viewHolder.tv_userName = (TextView) convertView.findViewById(R.id.tv_userName);
                viewHolder.tv_time = (TextView) convertView.findViewById(R.id.tv_time);
                //bottom
                viewHolder.iv_like = (ImageView) convertView.findViewById(R.id.iv_like);
                viewHolder.tv_like = (TextView) convertView.findViewById(R.id.tv_like);
                viewHolder.iv_comment = (ImageView) convertView.findViewById(R.id.iv_comment);
                viewHolder.tv_comment = (TextView) convertView.findViewById(R.id.tv_comment);
                break;
        }


        //中间公共部分 -所有的都有
        viewHolder.tv_content = (TextView) convertView.findViewById(R.id.tv_content);
    }


    private View initView(View convertView, int itemViewType, ViewHolder viewHolder) {
        switch (itemViewType) {
            case TYPE_VIDEO://视频
                convertView = View.inflate(context, R.layout.item_video_view ,null);
                //在这里实例化特有的
                viewHolder.video_content = (JZVideoPlayerStandard) convertView.findViewById(R.id.video_content);
                break;
            case TYPE_IMAGE://图片
                convertView = View.inflate(context, R.layout.item_gif_view, null);
                viewHolder.iv_gifcontent = (ImageView) convertView.findViewById(R.id.iv_gifcontent);
                break;
            case TYPE_TEXT://文字
                convertView = View.inflate(context, R.layout.item_textnew, null);
                break;
            case TYPE_GIF://gif
                convertView = View.inflate(context, R.layout.item_gif_view, null);
//                viewHolder.iv_gifcontent = (ImageView) convertView.findViewById(R.id.iv_gifcontent);
                viewHolder.iv_gifcontent = (ImageView) convertView.findViewById(R.id.iv_gifcontent);
                break;
        }
        return convertView;
    }



    static class  ViewHolder{
        ImageView user_header;
        TextView tv_userName;
        TextView tv_time;
        TextView tv_content;
        ImageView iv_gifcontent;//gif和普通image
        ImageView iv_like;
        ImageView iv_comment;
        TextView tv_like;//点赞
        TextView tv_comment;//评论数

        JZVideoPlayerStandard video_content;//小视频

    }

}
