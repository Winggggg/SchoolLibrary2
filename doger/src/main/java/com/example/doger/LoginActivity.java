package com.example.doger;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.bean.Customer;
import com.example.util.WebServerUtil;
import com.google.gson.Gson;

public class LoginActivity extends Activity {
	private Button registerbutton, loginbutton;
	private EditText customerNum, password;
	private Intent intent = new Intent();
	private SharedPreferences sp;
	

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);
		// 初始化对象
		registerbutton = (Button) this.findViewById(R.id.registerbutton);
		registerbutton.setOnClickListener(new ButtonOnClickListener());
		loginbutton = (Button) this.findViewById(R.id.loginbutton);
		loginbutton.setOnClickListener(new ButtonOnClickListener());
		customerNum = (EditText) this.findViewById(R.id.username);
		password = (EditText) this.findViewById(R.id.password);
		sp=getSharedPreferences("dog", Context.MODE_PRIVATE);
	}
	

	public class ButtonOnClickListener implements View.OnClickListener {
		
		public void onClick(View v) {
			switch (v.getId()) {
			case R.id.loginbutton:
				final ProgressDialog dialog = ProgressDialog.show(LoginActivity.this,
						null, "正在登陆中...");
				/*** 在服务器校对信息，正确则登录成功 ****/
				if (TextUtils.isEmpty(customerNum.getText())) {
					Toast.makeText(getApplicationContext(), "账号不能为空！",
							Toast.LENGTH_SHORT).show();
					dialog.dismiss();
				} else if (TextUtils.isEmpty(password.getText())) {
					Toast.makeText(getApplicationContext(), "密码不能为空！",
							Toast.LENGTH_SHORT).show();
					dialog.dismiss();
				}

				// 启动分线程校对
				new Thread(new Runnable() {
					@Override
					public void run() {
						final Customer customer = WebServerUtil.doLogin(customerNum.getText()
								.toString(), password.getText().toString());
						//回到主线程关闭进度框
						runOnUiThread(new Runnable() {
							@Override
							public void run() {
								if(customer!=null){
									//关闭加载框
									dialog.dismiss();
									Log.e("TAG", "AA:"+customer.toString());
									saveLoginInfo(customer);
									//回到主页面
									finish();
								}else{
									//关闭加载框
									dialog.dismiss();
									//置空
									customerNum.setText("");
									password.setText("");
									Toast.makeText(LoginActivity.this, "账号或密码错误，登录失败", Toast.LENGTH_SHORT).show();
								}
							}
						});
						
					}
				}).start();
				

				/****************************/
				break;
			case R.id.registerbutton:
				intent.setClass(LoginActivity.this, RegisterActivity.class);
				startActivity(intent);
				break;
			default:
				break;
			}

		}
		
		/**存储登录信息到手机内存中
		 * 路径为/data/data/com.example.doger/shared_pref/xxx.xml
		 * @param customerNum
		 * @param password
		 */
		public void saveLoginInfo(Customer customer){
			//获取Editor对象
			Editor et=sp.edit();
			String customer_json=new Gson().toJson(customer);
			et.putString("customer_json", customer_json).commit();
		}
	}
}
