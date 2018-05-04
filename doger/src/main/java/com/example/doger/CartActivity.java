package com.example.doger;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.adapter.CartAdapter;
import com.example.adapter.ProductAdapter;
import com.example.bean.Cart;
import com.example.bean.CartItem;
import com.example.bean.Category;
import com.example.bean.Customer;
import com.example.bean.Product;
import com.example.util.AppContext;
import com.example.util.MSG;
import com.example.util.WebServerUtil;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

public class CartActivity extends Activity implements OnClickListener {

	private AppContext appContext;
	private Cart cart;
	private Button btn_submitOrder;
	private Button btn_chooseAddress;
	private TextView tv_address;
	private TextView tv_name_cellphone;
	private Customer customer;
	/**
	 * 成功的消息
	 */
	protected static final int WHAT_REQUEST_SUCCESS = 0;
	/**
	 * 失败的消息
	 */
	protected static final int WHAT_REQUEST_ERROR = 1;
	private ListView lv_cartItem;
	private LinearLayout ll_cart_loading;
	private boolean isSuccess=false;
	private TextView tv_totalPrice;
	/**
	 * 需要加载的产品数据列表
	 */
	private List<CartItem> data = new ArrayList<CartItem>();
	private CartAdapter adapter;
	// 处理消息
	private Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case WHAT_REQUEST_SUCCESS:// 处理成功消息
				// 关闭加载提示框
				ll_cart_loading.setVisibility(View.GONE);
				// 显示列表
				Log.e("TAG", "setAdapter");
				lv_cartItem.setAdapter(adapter);
				if(cart!=null){
					tv_totalPrice.setText("待支付￥ "+cart.getTotalPrice());
				}
				break;
			case WHAT_REQUEST_ERROR:// 处理失败消息
				// 关闭加载提示框
				ll_cart_loading.setVisibility(View.GONE);
				Toast.makeText(CartActivity.this, "请求服务器数据失败", 1).show();
				break;
			default:
				break;
			}
		};
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_cart);
		appContext = (AppContext) getApplicationContext();
		// 初始化对象
		tv_address=(TextView) findViewById(R.id.tv_address);
		tv_name_cellphone=(TextView) findViewById(R.id.tv_name_cellphone);
		tv_totalPrice=(TextView) findViewById(R.id.tv_totalPrice);
		lv_cartItem = (ListView) findViewById(R.id.lv_cartItem);
		ll_cart_loading = (LinearLayout) findViewById(R.id.ll_cart_loading);
		btn_submitOrder=(Button) findViewById(R.id.btn_submitOrder);
		btn_chooseAddress=(Button) findViewById(R.id.btn_chooseAddress);
		adapter = new CartAdapter(CartActivity.this);
		
		
		// 获取购物车对象
		cart = appContext.getCart();
		
		//添加监听器
		btn_submitOrder.setOnClickListener(this);
		btn_chooseAddress.setOnClickListener(this);
		
		
		String name=getIntent().getStringExtra("name");
		String location=getIntent().getStringExtra("location");
		String cellphone=getIntent().getStringExtra("cellphone");
		if(name!=null&&cellphone!=null){
			tv_name_cellphone.setText("   "+name+"  "+cellphone);
		}
		if(location!=null){
			tv_address.setText("地址信息："+location);
		}
	}

	@Override
	protected void onResume() {
		Log.e("TAG", "运行了...");
		super.onResume();
		data.clear();
		// 启动分线程网获取数据
		new Thread(new Runnable() {
			@Override
			public void run() {
				Log.e("TAG", "456");
				try {
					Map<String, CartItem> map = cart.getCartMap();
					for (String key : map.keySet()) {
						CartItem cartItem = map.get(key);
						data.add(cartItem);
						Log.e("TAG", "购物项：=" + cartItem.toString());

					}
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
	
	@Override
	public void onClick(View v) {
		//点击选择地址按钮或者提交订单按钮之前判断用户是否登录
		SharedPreferences sp=getSharedPreferences("dog", Context.MODE_PRIVATE);
		String customer_json=sp.getString("customer_json", null);
		if(customer_json!=null){
			customer=new Gson().fromJson(customer_json, Customer.class);
		}
		
		switch (v.getId()) {
		case R.id.btn_submitOrder:
			final ProgressDialog dialog=ProgressDialog.show(this, "", "正在提交中");
			//启动分线程提交订单
			new AsyncTask<Void, Void, String>() {
				@Override
				protected String doInBackground(Void... params) {
					String result=null;
					try {
						int addressId=getIntent().getIntExtra("locationId", 0);
						if(addressId==0){
							result="请选择收货地址";
						}else{
							cart.setAddressId(addressId);
							cart.setCustomer(new Customer(1, "aaaaaa", "AAAB", "123456", "1132456@qq.com", "123489555"));
							MSG msg=WebServerUtil.submitOrderToServer(cart);
							if(msg.getStatusCode()==100){
								isSuccess=true;
								result="订单号为："+msg.getExtend().get("resultMessage");
							}else{
								result="提交失败";
							}
						}
					} catch (Exception e) {
						result="提交失败";
						e.printStackTrace();
					}
					return result;
				}
				
				protected void onPostExecute(String result) {
					//关闭进度框
					dialog.dismiss();
					Toast.makeText(CartActivity.this, result, 0).show();
					if(cart!=null&&isSuccess){
						//清空购物车
						cart.getCartMap().clear();
						data.clear();
						adapter.notifyDataSetChanged();
						cart=null;
						startActivity(new Intent(CartActivity.this,CodeActivity.class));
					}
				};
			}.execute();
			break;
		case R.id.btn_chooseAddress:
			//Toast.makeText(this, "123", 0).show();
			//去到地址列表页面
			if(customer!=null){
				Intent intent=new Intent(this, AddressListActivity.class);
				intent.putExtra("customerSid", customer.getCustomerId());
				startActivity(intent);
			}else{
				startActivity(new Intent(this, LoginActivity.class));
			}
			break;
		default:
			break;
		}
	}
}
