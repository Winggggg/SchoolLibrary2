package com.example.doger;

import com.example.bean.Customer;
import com.google.gson.Gson;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

public class PersonActivity extends Activity {

	private FrameLayout fl_login;
	private ImageView iv_login;
	private ImageView iv_person;
	private TextView tv_personName;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_person);
		// 初始化对象
		fl_login = (FrameLayout) findViewById(R.id.fl_login);
		iv_login = (ImageView) findViewById(R.id.iv_login);
		iv_person = (ImageView) findViewById(R.id.iv_person);
		tv_personName = (TextView) findViewById(R.id.tv_personName);

	}

	@Override
	protected void onResume() {
		super.onResume();
		// 判断是否登录
		SharedPreferences sp = getSharedPreferences("dog", Context.MODE_PRIVATE);
		String customer_json = sp.getString("customer_json", null);
		if (customer_json != null) {
			iv_login.setVisibility(View.GONE);
			iv_person.setVisibility(View.VISIBLE);
			Customer customer = new Gson().fromJson(customer_json,
					Customer.class);
			tv_personName.setText("昵称："+customer.getCustomerName());
			tv_personName.setEnabled(false);
		} else {
			iv_person.setVisibility(View.GONE);
			iv_login.setVisibility(View.VISIBLE);
			// 设置点击监听
			tv_personName.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					startActivity(new Intent(PersonActivity.this,
							LoginActivity.class));
				}
			});
		}
	}

}
