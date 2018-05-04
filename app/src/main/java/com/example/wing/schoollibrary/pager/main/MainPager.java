package com.example.wing.schoollibrary.pager.main;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.wing.schoollibrary.MyApplication;
import com.example.wing.schoollibrary.R;
import com.example.wing.schoollibrary.activity.LoginActivity;
import com.example.wing.schoollibrary.activity.PassageActivity;
import com.example.wing.schoollibrary.activity.PassageListActivity;
import com.example.wing.schoollibrary.base.BasePager;
import com.example.wing.schoollibrary.bean.Passage;
import com.example.wing.schoollibrary.util.CacheUtils;
import com.example.wing.schoollibrary.util.DensityUtil;
import com.example.wing.schoollibrary.util.LogUtil;
import com.example.wing.schoollibrary.util.MSG;
import com.example.wing.schoollibrary.util.Urls;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.lzy.okhttputils.OkHttpUtils;
import com.lzy.okhttputils.callback.StringCallback;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import okhttp3.Call;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by Administrator on 2017/12/15 0015.
 * 主页
 */

public class MainPager extends BasePager {

    private static final String TAG = MainPager.class.getSimpleName();
    private ViewPager viewPager;
    private TextView detail;
    private LinearLayout point_container;
    private LinearLayout ll_note;
    private LinearLayout ll_situation;
    private LinearLayout ll_information;
    private MyPageListener myPageListener;
    private ProgressBar pb_loading;
    private ImageButton ib_login;
    private MyApplication app;
    // 图片资源ID
    private final int[] imageIds = {R.drawable.adv1, R.drawable.adv2,
            R.drawable.adv3, R.drawable.adv4, R.drawable.adv5};

    // 图片标题集合
    private final String[] imageDescriptions = {"关于图书馆文献传递服务的通知", "图书馆举办五邑大学隋志刚老师中国画作品展",
            "新电子阅览区投入使用", "应用上线｜图书馆微官网上线使用", "读秀图书搜索及文献传递系统使用帮助"};

    private boolean isDragg = false;// 是否已经滚动

    // imageView集合
    private List<ImageView> imageViews = new ArrayList<ImageView>();

    // 记录图片前一次的位置
    private int prePosition;

    /**
     * 馆内资讯列表
     */
    private List<Passage> messageList = new ArrayList<>();
    /**
     * 通知公告列表
     */
    private List<Passage> noteList = new ArrayList<>();
    /**
     * 本馆概况列表
     */
    private List<Passage> surveyList = new ArrayList<>();


    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            int item = viewPager.getCurrentItem() + 1;
            viewPager.setCurrentItem(item);
            // 发送延迟消息
            handler.sendEmptyMessageDelayed(0, 4000);
        }
    };


    @Override
    public View initView() {
        View view = View.inflate(context, R.layout.activity_main_pager, null);
        viewPager = (ViewPager) view.findViewById(R.id.viewPager);
        detail = (TextView) view.findViewById(R.id.detail);
        point_container = (LinearLayout) view.findViewById(R.id.point_container);
        ll_note = (LinearLayout) view.findViewById(R.id.ll_note);
        ll_situation = (LinearLayout) view.findViewById(R.id.ll_situation);
        ll_information = (LinearLayout) view.findViewById(R.id.ll_information);
        pb_loading = (ProgressBar) view.findViewById(R.id.pb_loading);
        ib_login = (ImageButton) view.findViewById(R.id.ib_login);

        myPageListener = new MyPageListener();
        ib_login.setOnClickListener(new MyOnclickListener());

        app = new MyApplication();
        return view;
    }


    private class MyOnclickListener implements View.OnClickListener {

        @Override
        public void onClick(View v) {
            MyApplication app= (MyApplication) context.getApplicationContext();
            if (app.getStudent()==null){
                startActivity(new Intent(context, LoginActivity.class));
            }
        }
    }


        @Override
        public void initData() {
            super.initData();
            Log.e(TAG, "主页初始化中....");
            // 准备展示图片的数据
            for (int i = 0; i < imageIds.length; i++) {
                ImageView imageView = new ImageView(context);
                imageView.setBackgroundResource(imageIds[i]);
                // 添加进集合
                imageViews.add(imageView);

                ImageView pointView = new ImageView(context);
                pointView.setBackgroundResource(R.drawable.selector_pointer);
                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(DensityUtil.dip2px(context, 8),
                        DensityUtil.dip2px(context, 8));

                if (i == 0) {
                    pointView.setEnabled(true);// 显示红色
                } else {
                    pointView.setEnabled(false);// 显示灰色
                    params.leftMargin = DensityUtil.dip2px(context, 8);
                }
                // 设置布局边距
                pointView.setLayoutParams(params);
                point_container.addView(pointView);
            }

            // 设置适配器展示图片
            viewPager.setAdapter(new MyPageAdapter());
            // 最大值的中间值，再减去取余的数
            int item = Integer.MAX_VALUE / 2 - Integer.MAX_VALUE / 2
                    % imageViews.size();
            viewPager.setCurrentItem(item);
            prePosition = item % imageViews.size();
            detail.setText(imageDescriptions[prePosition]);// 显示标题内容
            // 对viewPager设置监听
            //viewPager.setOnPageChangeListener(new MyPageListener());
            viewPager.addOnPageChangeListener(myPageListener);

            handler.sendEmptyMessageDelayed(0, 3000);


            //请求服务器获取数据
            getDataFromNet();


        }

        private void setTextLayout(final List<Passage> list, LinearLayout viewgroup) {
            if (list.size() >= 4) {
                for (int i = 0; i < 4; i++) {
                    TextView view = (TextView) View.inflate(context, R.layout.item_text, null);
                    view.setText(list.get(i).getTitle());
                    final Passage passage = list.get(i);
                    view.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent intent = new Intent(context, PassageActivity.class);
                            intent.putExtra("passage", passage);
                            startActivity(intent);
                        }
                    });
                    viewgroup.addView(view);
                }
                TextView moreview = (TextView) View.inflate(context, R.layout.item_more_text, null);
                moreview.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(context, PassageListActivity.class);
                        Bundle bundle = new Bundle();
                        bundle.putSerializable("passageList", (Serializable) list);
                        intent.putExtras(bundle);
                        startActivity(intent);
                    }
                });
                viewgroup.addView(moreview);
            } else if (list.size() > 0 && list.size() < 4) {
                for (int i = 0; i < list.size(); i++) {
                    TextView view = (TextView) View.inflate(context, R.layout.item_text, null);
                    view.setText(list.get(i).getTitle());
                    final Passage passage = list.get(i);
                    view.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent intent = new Intent(context, PassageActivity.class);
                            intent.putExtra("passage", passage);
                            startActivity(intent);
                        }
                    });
                    viewgroup.addView(view);
                }
            }
        }

        /**
         * 动态设置textview
         */
        private void SetView() {
            //设置馆内资讯
            setTextLayout(messageList, ll_information);
            //设置通知公告
            setTextLayout(noteList, ll_note);
            //设置本馆概况
            setTextLayout(surveyList, ll_situation);

           // pb_loading.setVisibility(View.GONE);
        }

        /**
         * 请求服务器获取数据
         */
        private void getDataFromNet() {
            String url = Urls.SERVER + "getAllPassage";
            OkHttpUtils.get(url)
                    .execute(new MyStringCallBack());
        }

        private class MyStringCallBack extends StringCallback {

            @Override
            public void onResponse(boolean isFromCache, String s, Request request, @Nullable Response response) {
                LogUtil.e("请求成功onResponse");
                setPassageData(s);
                //缓存数据
                if (!app.isCleanCache()) {
                    CacheUtils.putString(context, "passage", s);
                }

            }

            @Override
            public void onError(boolean isFromCache, Call call, @Nullable Response response, @Nullable Exception e) {
                super.onError(isFromCache, call, response, e);
                LogUtil.e("请求失败onError");
                String cacheString = CacheUtils.getString(context, "passage");
                if (!TextUtils.isEmpty(cacheString)) {
                    setPassageData(cacheString);
                }

            }

            @Override
            public void onAfter(boolean isFromCache, @Nullable String s, Call call, @Nullable Response response, @Nullable Exception e) {
                super.onAfter(isFromCache, s, call, response, e);
                LogUtil.e("请求结束onAfter");
               pb_loading.setVisibility(View.GONE);
            }
        }

        /**
         * 设置拿到的网络数据
         */
        private void setPassageData(String result) {
            Gson gson = new Gson();
            MSG msg = gson.fromJson(result, MSG.class);


            if (msg.getStatusCode() == 100) {
                //数据是成功的
                Map<String, Object> map = msg.getExtend();
                Object object = map.get("list");

                String json = gson.toJson(object);

                messageList = gson.fromJson(json, new TypeToken<List<Passage>>() {
                }.getType());

                LogUtil.e("aaa" + json);
                LogUtil.e("aaa" + messageList.size());


                // 装数据
                for (int i = 0; i < messageList.size(); i++) {
                    //                LogUtil.e(messageList.get(i).getType());
                    String type = messageList.get(i).getType();

                    if (type.equals("note")) {
                        noteList.add(messageList.get(i));
                        messageList.remove(i);
                        i--;
                    } else if (type.equals("survey")) {
                        surveyList.add(messageList.get(i));
                        messageList.remove(i);
                        i--;
                    }
                }
            }

            SetView();

        }


        class MyPageAdapter extends PagerAdapter {

            @Override
            public int getCount() {
                //Log.e("TAG", "getCount");
                return Integer.MAX_VALUE;//设置viewpager的最大容量，让其可以左右滑动
            }

            //判断传进来的对象是否相同
            @Override
            public boolean isViewFromObject(View arg0, Object arg1) {
                //Log.e("TAG", "isViewFromObject");
                return arg0 == arg1;
            }

            //实例化item对象，装载进容器
            @Override
            public Object instantiateItem(ViewGroup container, int position) {
                int realPosition = position % imageViews.size();//取模，防止数组越界
                final ImageView imageView = imageViews.get(realPosition);
                container.addView(imageView);
                //Log.e("TAG", "instantiateItem position="+position+"---imageView="+imageView);
                //设置触碰监听
                imageView.setOnTouchListener(new View.OnTouchListener() {
                    @Override
                    public boolean onTouch(View v, MotionEvent event) {
                        switch (event.getAction()) {
                            case MotionEvent.ACTION_DOWN://手指按下
                                //移除消息
                                handler.removeCallbacksAndMessages(null);
                                break;
                            case MotionEvent.ACTION_UP://手指离开
                                //先移除之前的消息再发送消息
                                handler.removeCallbacksAndMessages(null);
                                handler.sendEmptyMessageDelayed(0, 4000);
                                break;
                            default:
                                break;
                        }
                        return true;
                    }
                });

                return imageView;
            }

            @Override
            public void destroyItem(ViewGroup container, int position, Object object) {
                container.removeView((View) object);
                //Log.e("TAG", "destroyItem position="+position+"---imageView="+object);
            }
        }

        /**
         * viewpager 监听器
         *
         * @author Administrator
         */
        class MyPageListener implements ViewPager.OnPageChangeListener {

            @Override
            public void onPageSelected(int position) {
                int realPosition = position % imageViews.size();//取模，防止数组越界
                //设置对应文本的文字
                detail.setText(imageDescriptions[realPosition]);
                //把上一个高亮的点变为灰色
                point_container.getChildAt(prePosition).setEnabled(false);
                //把当前点变为高亮
                point_container.getChildAt(realPosition).setEnabled(true);
                prePosition = realPosition;
            }

            @Override
            public void onPageScrolled(int position, float positionOffset,
                                       int positionOffsetPixels) {

            }

            @Override
            public void onPageScrollStateChanged(int state) {
                if (state == ViewPager.SCROLL_STATE_DRAGGING && isDragg) {
                    isDragg = false;
                    //移除消息
                    handler.removeCallbacksAndMessages(null);
                } else if (state == ViewPager.SCROLL_STATE_IDLE) {
                    isDragg = true;
                    //移除消息并发送消息
                    handler.removeCallbacksAndMessages(null);
                    handler.sendEmptyMessageDelayed(0, 4000);
                }
            }

        }

        @Override
        public void onDestroyView() {
            super.onDestroyView();
            if (myPageListener != null) {
                viewPager.removeOnPageChangeListener(myPageListener);
            }
        }
    }
