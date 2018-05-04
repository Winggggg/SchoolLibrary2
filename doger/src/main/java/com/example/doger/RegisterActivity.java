package com.example.doger;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.bean.Customer;
import com.example.util.WebServerUtil;

public class RegisterActivity extends Activity {
	private static final int SUCCESS_MESSAGE = 0;//成功消息
	private static final int ERROR_MESSAGE = 1;//失败消息
	private Button okbutton, cancelbutton;
	private Intent intent = new Intent();
	private EditText user, password, name, phonenumber, email;
	ProgressDialog dialog;
	private Handler handler=new Handler(){
		//处理消息
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case SUCCESS_MESSAGE:
				dialog.dismiss();
				//转去登陆页面
				finish();
				break;
			case ERROR_MESSAGE:
				dialog.dismiss();
				Toast.makeText(RegisterActivity.this, "注册失败！", Toast.LENGTH_SHORT).show();
				break;
			default:
				break;
			}
		};
	};
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_register);
		// 初始化对象
		okbutton = (Button) this.findViewById(R.id.okbutton);
		okbutton.setOnClickListener(new ButtonOnClickListener());
		cancelbutton = (Button) this.findViewById(R.id.cancelbutton);
		cancelbutton.setOnClickListener(new ButtonOnClickListener());
		user = (EditText) this.findViewById(R.id.user);
		password = (EditText) this.findViewById(R.id.password);
		name = (EditText) this.findViewById(R.id.name);
		phonenumber = (EditText) this.findViewById(R.id.phonenumber);
		email = (EditText) this.findViewById(R.id.email);
	}

	public class ButtonOnClickListener implements View.OnClickListener {
		

		public void onClick(View v) {
			switch (v.getId()) {
			case R.id.okbutton:
				if (TextUtils.isEmpty(user.getText())) {
					Toast.makeText(getApplicationContext(), "账号不能为空！",
							Toast.LENGTH_SHORT).show();
				} else if (TextUtils.isEmpty(password.getText())) {
					Toast.makeText(getApplicationContext(), "密码不能为空！",
							Toast.LENGTH_SHORT).show();
				} else if (TextUtils.isEmpty(name.getText())) {
					Toast.makeText(getApplicationContext(), "姓名不能为空！",
							Toast.LENGTH_SHORT).show();
				} else if (TextUtils.isEmpty(phonenumber.getText())) {
					Toast.makeText(getApplicationContext(), "电话不能为空！",
							Toast.LENGTH_SHORT).show();
				} else if (TextUtils.isEmpty(email.getText())) {
					Toast.makeText(getApplicationContext(), "邮箱不能为空！",
							Toast.LENGTH_SHORT).show();
				} else {
					/****** 资料传送到服务器上，接收到服务器返回的信息后返回登录界面 ****/
					//显示进度框
					dialog=ProgressDialog.show(RegisterActivity.this, null, null);
					final Customer customer = new Customer(null, user.getText()
							.toString(), name.getText().toString(), password
							.getText().toString(), email.getText().toString(),
							phonenumber.getText().toString());
					//启动分线程
					new Thread(new Runnable() {
						@Override
						public void run() {
							try {
								//注册
								WebServerUtil.sendRegisterToServer(customer);
								//发送成功消息
								handler.sendEmptyMessage(SUCCESS_MESSAGE);
							} catch (Exception e) {
								//发送失败消息
								handler.sendEmptyMessage(ERROR_MESSAGE);
								Log.e("TAG", "错误：="+e.getMessage());
								e.printStackTrace();
							}
						}
					}).start();;
				}

				break;
			case R.id.cancelbutton:
				intent.setClass(RegisterActivity.this, MainActivity.class);
				startActivity(intent);
				break;
			default:
				break;
			}

		}
	}
}
