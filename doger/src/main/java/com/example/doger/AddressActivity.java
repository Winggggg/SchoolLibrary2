package com.example.doger;

import java.util.ArrayList;
import java.util.List;

import com.example.adapter.CartAdapter;
import com.example.bean.CartItem;
import com.example.util.WebServerUtil;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

public class AddressActivity extends Activity implements OnClickListener {
	/**
	 * 成功的消息
	 */
	protected static final int WHAT_REQUEST_SUCCESS = 0;
	/**
	 * 失败的消息
	 */
	protected static final int WHAT_REQUEST_ERROR = 1;
	private EditText et_name;
	private EditText et_phoneNum;
	private EditText et_address;
	private Button btn_save;
	private ProgressDialog dialog;
	private int customerSid;
	/**
	 * 标记是更新还是添加地址,默认是添加
	 */
	private boolean isUpdate = false;

	// 处理消息
	private Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case WHAT_REQUEST_SUCCESS:// 处理成功消息
				// 关闭加载提示框
				dialog.dismiss();
				// 显示
				Intent intent = new Intent(AddressActivity.this,
						AddressListActivity.class);
				intent.putExtra("customerSid", customerSid);
				startActivity(intent);
				finish();
				break;
			case WHAT_REQUEST_ERROR:// 处理失败消息
				// 关闭加载提示框
				dialog.dismiss();
				Toast.makeText(AddressActivity.this, "保存失败", 1).show();
				break;
			default:
				break;
			}
		};
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_address);

		// 初始化对象
		et_name = (EditText) findViewById(R.id.et_name);
		et_phoneNum = (EditText) findViewById(R.id.et_phoneNum);
		et_address = (EditText) findViewById(R.id.et_address);
		btn_save = (Button) findViewById(R.id.btn_save);

		customerSid = getIntent().getIntExtra("customerSid", 0);

		String name = getIntent().getStringExtra("name");
		String phoneNum = getIntent().getStringExtra("cellphone");
		String address = getIntent().getStringExtra("location");
		if (name != null) {
			isUpdate = true;
			et_name.setText(name);
		}
		if (phoneNum != null) {
			et_phoneNum.setText(phoneNum);
		}
		if (address != null) {
			et_address.setText(address);
		}
		btn_save.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		final String name = et_name.getText().toString();
		final String phoneNum = et_phoneNum.getText().toString();
		final String address = et_address.getText().toString();
		dialog = ProgressDialog.show(AddressActivity.this, "", "");
		if (v.getId() == R.id.btn_save && !isUpdate) {
			// 添加
			// 启动分线程保存数据
			new Thread(new Runnable() {
				@Override
				public void run() {
					try {
						WebServerUtil.addAddress(name, address, phoneNum,
								customerSid);
						// 发送成功的消息到主线程处理
						handler.sendEmptyMessage(WHAT_REQUEST_SUCCESS);
					} catch (Exception e) {
						// 发送失败的消息到主线程处理
						Log.e("TAG", "cuo=" + e.getMessage());
						handler.sendEmptyMessage(WHAT_REQUEST_ERROR);
						e.printStackTrace();

					}
				}
			}).start();
		} else if (v.getId() == R.id.btn_save && isUpdate) {
			// 更新
			new Thread(new Runnable() {
				@Override
				public void run() {
					try {
						WebServerUtil.editAddress(
								getIntent().getIntExtra("locationId", 0), name,
								address, phoneNum, customerSid);
						// 发送成功的消息到主线程处理
						handler.sendEmptyMessage(WHAT_REQUEST_SUCCESS);
					} catch (Exception e) {
						// 发送失败的消息到主线程处理
						Log.e("TAG", "cuo=" + e.getMessage());
						handler.sendEmptyMessage(WHAT_REQUEST_ERROR);
						e.printStackTrace();

					}
				}
			}).start();
		}
	}
}
