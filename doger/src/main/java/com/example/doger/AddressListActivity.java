package com.example.doger;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.example.adapter.AddressAdapter;
import com.example.adapter.CartAdapter;
import com.example.bean.Address;
import com.example.bean.CartItem;
import com.example.bean.Product;
import com.example.util.WebServerUtil;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

public class AddressListActivity extends Activity implements OnClickListener {

	/**
	 * 成功的消息
	 */
	protected static final int WHAT_REQUEST_SUCCESS = 0;
	/**
	 * 失败的消息
	 */
	protected static final int WHAT_REQUEST_ERROR = 1;
	private ListView lv_address;
	private LinearLayout ll_address_loading;
	private Button btn_edit;
	private Button btn_addAddress;
	private int customerSid;
	/**
	 * 需要加载的地址列表
	 */
	private List<Address> data = new ArrayList<Address>();
	private AddressAdapter adapter;
	// 处理消息
	private Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case WHAT_REQUEST_SUCCESS:// 处理成功消息
				// 关闭加载提示框
				ll_address_loading.setVisibility(View.GONE);
				// 显示列表
				Log.e("TAG", "setAdapter");
				lv_address.setAdapter(adapter);
				break;
			case WHAT_REQUEST_ERROR:// 处理失败消息
				// 关闭加载提示框
				ll_address_loading.setVisibility(View.GONE);
				Toast.makeText(AddressListActivity.this, "请求服务器数据失败", 1).show();
				break;
			default:
				break;
			}
		};
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_address_list);

		// 初始化对象
		lv_address = (ListView) findViewById(R.id.lv_address);
		ll_address_loading = (LinearLayout) findViewById(R.id.ll_address_loading);
		btn_edit = (Button) findViewById(R.id.btn_edit);
		btn_addAddress = (Button) findViewById(R.id.btn_addAddress);
		adapter = new AddressAdapter(AddressListActivity.this);
		
		btn_addAddress.setOnClickListener(this);
		
		Intent intent=getIntent();
		customerSid=intent.getIntExtra("customerSid", 0);
		//启动分线程获取地址列表数据
		new Thread(new Runnable() {
			@Override
			public void run() {
				Log.e("TAG", "456");
				try {
					String address_json=WebServerUtil.getAllAddress(customerSid);
					data = new Gson().fromJson(address_json,
							new TypeToken<List<Address>>() {
							}.getType());
					adapter.setData(data);
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

	//点击事件
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btn_addAddress:
			Intent intent=new Intent(this, AddressActivity.class);
			intent.putExtra("customerSid", customerSid);
			startActivity(intent);
			finish();
			break;

		default:
			break;
		}
	}
}
