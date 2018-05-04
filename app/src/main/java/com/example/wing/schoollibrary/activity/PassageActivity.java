package com.example.wing.schoollibrary.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.webkit.JavascriptInterface;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageButton;
import android.widget.ProgressBar;

import com.example.wing.schoollibrary.R;
import com.example.wing.schoollibrary.bean.Passage;
import com.example.wing.schoollibrary.util.Urls;
import com.mob.MobSDK;

import cn.sharesdk.onekeyshare.OnekeyShare;

public class PassageActivity extends Activity implements View.OnClickListener {

    private WebView webview;
    private ProgressBar pb_loading;
    private Passage passage;
    private ImageButton ib_share;
    private ImageButton ib_back;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//
        setContentView(R.layout.activity_passage);

        initView();

        getData();

        setListener();
    }

    private void setListener() {
        ib_back.setOnClickListener(this);
        ib_share.setOnClickListener(this);
    }

    /**
     * 获取数据
     */
    private void getData() {
        Intent intent=getIntent();
        passage= (Passage) intent.getSerializableExtra("passage");
        setWebData();
    }

    /**
     * 初始化数据
     */
    private void setWebData() {
        webview.loadUrl("file:///android_asset/wing.html");
//        webview.loadUrl(Urls.URL_HTML);
        WebSettings settings = webview.getSettings();
        settings.setJavaScriptEnabled(true);
        // 设置允许JS弹窗
        settings.setJavaScriptCanOpenWindowsAutomatically(true);

        webview.setWebViewClient(new MyWebClient());

        //设置支持js调用java
        webview.addJavascriptInterface(new AndroidAndJSInterface(), "Android");


    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.ib_back:
                finish();
                break;
            case R.id.ib_share:
                //分享功能
                showShare();
                break;
        }
    }

    private void showShare() {
        MobSDK.init(this, "242c254b21c80", "f9148be1b396b757bd92d79ff16fe179");
        OnekeyShare oks = new OnekeyShare();
//        oks.setPlatform("五邑大学");
        //关闭sso授权
        oks.disableSSOWhenAuthorize();

        // title标题，微信、QQ和QQ空间等平台使用
        oks.setTitle("分享测试");
        // titleUrl QQ和QQ空间跳转链接
//        oks.setTitleUrl("http://sharesdk.cn");
        oks.setTitleUrl(Urls.URL_HTML+passage.getPassageId());
        // text是分享文本，所有平台都需要这个字段
        oks.setText("我是分享文本");
        // imagePath是图片的本地路径，Linked-In以外的平台都支持此参数
        oks.setImagePath("/sdcard/test.jpg");//确保SDcard下面存在此张图片
        // url在微信、微博，Facebook等平台中使用
//        oks.setUrl("http://sharesdk.cn");
        oks.setUrl(Urls.URL_HTML+passage.getPassageId());
        // comment是我对这条分享的评论，仅在人人网使用
        oks.setComment("我是测试评论文本");
        // 启动分享GUI
        oks.show(this);

    }

    class AndroidAndJSInterface {
        /**
         * 该方法将被js调用,用于加载数据
         */
        @JavascriptInterface
        public void showcontacts() {
//            // 下面的代码建议在子线程中调用
//            String json = "[{\"name\":\"阿福\", \"phone\":\"18600012345\"}]";
//            String title="ceshisdfsaf";
//            // 调用JS中的方法
//            webview.loadUrl("javascript:changeTitle('" + title + "')");
        }

    }

    /**
     * 初始化视图
     */
    private void initView() {
        webview= (WebView) findViewById(R.id.webview);
        pb_loading= (ProgressBar) findViewById(R.id.pb_loading);
        ib_back= (ImageButton) findViewById(R.id.ib_back);
        ib_share= (ImageButton) findViewById(R.id.ib_share);

        ib_share.setVisibility(View.VISIBLE);

    }

    /**
     *
     */
    private class MyWebClient extends WebViewClient{
        @Override
        public void onPageFinished(WebView webView, String url) {
            pb_loading.setVisibility(View.GONE);
            if(passage!=null)
                webview.loadUrl("javascript:changeTitle('"+passage.getTitle()+"','"+passage.getPublishTime()+"','"+passage.getPublisher()+"','"+passage.getContentStr()+"','"+passage.getContentImgurl()+"')");
        }

    }


}
