package com.example.doger;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.media.Image;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

import com.example.adapter.ProductAdapter;
import com.example.bean.Cart;
import com.example.bean.Product;
import com.example.util.AppContext;
import com.example.util.WebServerUtil;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

public class ItemListActivity extends Activity implements OnClickListener {

	/**
	 * 成功的消息
	 */
	protected static final int WHAT_REQUEST_SUCCESS = 0;
	/**
	 * 失败的消息
	 */
	protected static final int WHAT_REQUEST_ERROR = 1;
	private ListView lv_item;
	private LinearLayout ll_loading;
	private EditText et_name_search;
	private ImageView iv_search;
	private String productName = "";
	private Integer categoryId=0;
	/**
	 * 需要加载的产品数据列表
	 */
	private List<Product> data = new ArrayList<Product>();
	private ProductAdapter adapter;
	// 处理消息
	private Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case WHAT_REQUEST_SUCCESS:// 处理成功消息
				// 关闭加载提示框
				ll_loading.setVisibility(View.GONE);
				// 显示列表
				Log.e("TAG", "setAdapter");
				lv_item.setAdapter(adapter);
				AppContext appContext = (AppContext) ItemListActivity.this
						.getApplicationContext();
				Cart cart = appContext.getCart();
				if (cart == null) {
					// g购物车清空后重新刷新
					adapter.notifyDataSetChanged();
				}
				break;
			case WHAT_REQUEST_ERROR:// 处理失败消息
				// 关闭加载提示框
				ll_loading.setVisibility(View.GONE);
				Toast.makeText(ItemListActivity.this, "请求服务器数据失败", 1).show();
				break;
			default:
				break;
			}
		};
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_item_list);

		// 初始化对象
		lv_item = (ListView) findViewById(R.id.lv_item);
		ll_loading = (LinearLayout) findViewById(R.id.ll_loading);
		et_name_search = (EditText) findViewById(R.id.et_name_search);
		iv_search = (ImageView) findViewById(R.id.iv_search);
		adapter = new ProductAdapter(ItemListActivity.this);

		iv_search.setOnClickListener(this);

		// 获取intent对象及数据
		Intent intent = getIntent();
		categoryId = intent.getIntExtra("categoryId", 0);
		productName = et_name_search.getText().toString();

		openThreadToGetData();
	}

	@Override
	public void onClick(View v) {
		if (v.getId() == R.id.iv_search) {
			productName = et_name_search.getText().toString();
			Log.e("TAG", "产品名称："+productName);
			openThreadToGetData();
		}
	}

	public void openThreadToGetData() {
		// 启动分线程网获取数据
		new Thread(new Runnable() {
			@Override
			public void run() {
				Log.e("TAG", "456");
				try {
					String product_jsonStr = WebServerUtil.requestToDate(
							productName, categoryId);
					// Log.e("TAG", "product_jsonStr="+product_jsonStr);
					// /导入第三方GSon jar包，将json字符串封装成指定对象
					data = new Gson().fromJson(product_jsonStr,
							new TypeToken<List<Product>>() {
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
}
