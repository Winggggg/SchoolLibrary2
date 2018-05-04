package com.example.doger;

import android.app.Activity;
import android.app.TabActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.TabHost;

public class TabBaseActivity extends TabActivity {

	private TabHost tabhost;
	private RadioGroup main_radiogroup;  
	 private RadioButton tab_icon_home, tab_icon_shop,tab_icon_cart, tab_icon_person;  
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tab_base);
        
        //获取按钮
        main_radiogroup = (RadioGroup) findViewById(R.id.main_radiogroup);
        
        tab_icon_home = (RadioButton) findViewById(R.id.tab_icon_home);
        tab_icon_shop = (RadioButton) findViewById(R.id.tab_icon_shop);
        tab_icon_cart = (RadioButton) findViewById(R.id.tab_icon_cart);
        tab_icon_person = (RadioButton) findViewById(R.id.tab_icon_person);
        
        //往TabWidget添加Tab
        tabhost = getTabHost();
        tabhost.addTab(tabhost.newTabSpec("tag1").setIndicator("0").setContent(new Intent(this,MainActivity.class)));
        tabhost.addTab(tabhost.newTabSpec("tag2").setIndicator("1").setContent(new Intent(this,ItemListActivity.class)));
        tabhost.addTab(tabhost.newTabSpec("tag3").setIndicator("2").setContent(new Intent(this,CartActivity.class)));
        tabhost.addTab(tabhost.newTabSpec("tag4").setIndicator("3").setContent(new Intent(this,PersonActivity.class)));
         
        //设置监听事件
        checkListener checkradio = new checkListener();
        main_radiogroup.setOnCheckedChangeListener(checkradio);
    }

    
    //监听类
    public class checkListener implements OnCheckedChangeListener{
    	@Override
    	public void onCheckedChanged(RadioGroup group, int checkedId) {
    		// TODO Auto-generated method stub
    		//setCurrentTab 通过标签索引设置当前显示的内容
    		//setCurrentTabByTag 通过标签名设置当前显示的内容
    		switch(checkedId){
    		case R.id.tab_icon_home:
    			tabhost.setCurrentTab(0);
    			//或
    			//tabhost.setCurrentTabByTag("tag1");
    			break;
    		case R.id.tab_icon_shop:
    			tabhost.setCurrentTab(1);
    			break;
    		case R.id.tab_icon_cart:
    			tabhost.setCurrentTab(2);
    			//startActivity(new Intent(TabBaseActivity.this, CartActivity.class));
    			break;
    		case R.id.tab_icon_person:
    			tabhost.setCurrentTab(3);
    			break;
    		}

    		
    	}
    }
   
}
