package com.example.doger;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.Address;
import android.location.Criteria;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends Activity {
	private ImageButton  dog, rice, clothes, cosmetology, toy,
			medical, personal;
	private EditText search;
	private TextView gps;
	private Location mLocation;
	private LocationManager mLocationManager;
	private Intent intent = new Intent();
	private int yourChoice;
	private ViewPager viewPager;
	private TextView detail;
	private LinearLayout point_container;
	private double lat,lng;
	private String latLongString;
	private TextView myLocationText;
	// 图片资源ID
	private final int[] imageIds = { R.drawable.adv_1, R.drawable.adv_2,
			R.drawable.adv_3,R.drawable.adv_4,R.drawable.adv_5 };

	// 图片标题集合
	private final String[] imageDescriptions = { "全国25省包邮！", "求包养！",
			"5大特色大揭秘" ,"宠物家，专注你的宠物生活","狗狗洗澡套餐18"};

	private boolean isDragg = false;// 是否已经滚动

	// imageView集合
	private List<ImageView> imageViews = new ArrayList<ImageView>();

	// 记录图片前一次的位置
	private int prePosition;

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
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		// 初始化对象
		rice = (ImageButton) this.findViewById(R.id.rice);
		rice.setOnClickListener(new ButtonOnClickListener());
		clothes = (ImageButton) this.findViewById(R.id.clothes);
		clothes.setOnClickListener(new ButtonOnClickListener());
		cosmetology = (ImageButton) this.findViewById(R.id.cosmetology);
		cosmetology.setOnClickListener(new ButtonOnClickListener());
		dog = (ImageButton) this.findViewById(R.id.dog);
		dog.setOnClickListener(new ButtonOnClickListener());
		medical = (ImageButton) this.findViewById(R.id.medical);
		medical.setOnClickListener(new ButtonOnClickListener());
		toy = (ImageButton) this.findViewById(R.id.toy);
		toy.setOnClickListener(new ButtonOnClickListener());
		personal = (ImageButton) this.findViewById(R.id.personal);
		personal.setOnClickListener(new ButtonOnClickListener());
		myLocationText = (TextView) findViewById(R.id.gps);
		/************定位******************/
		LocationManager locationManager;
		String serviceName = Context.LOCATION_SERVICE;
		locationManager = (LocationManager) getSystemService(serviceName);
		String provider = LocationManager.NETWORK_PROVIDER;

		Criteria criteria = new Criteria();
		criteria.setAccuracy(Criteria.ACCURACY_FINE);
		criteria.setAltitudeRequired(false);
		criteria.setBearingRequired(false);
		criteria.setCostAllowed(true);
		criteria.setPowerRequirement(Criteria.POWER_LOW);
		//String provider = locationManager.getBestProvider(criteria, true);

		Location location = locationManager.getLastKnownLocation(provider);
		updateWithNewLocation(location);
		locationManager.requestLocationUpdates(provider, 2000, 10,
				locationListener);
		
		
		// 图片自动轮播
		viewPager = (ViewPager) findViewById(R.id.viewPager);
		detail = (TextView) findViewById(R.id.detail);
		point_container = (LinearLayout) findViewById(R.id.point_container);

		// 准备展示图片的数据
		for (int i = 0; i < imageIds.length; i++) {
			ImageView imageView = new ImageView(this);
			imageView.setBackgroundResource(imageIds[i]);
			// 添加进集合
			imageViews.add(imageView);

			ImageView pointView = new ImageView(this);
			pointView.setBackgroundResource(R.drawable.selector_pointer);
			LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(8,
					8);

			if (i == 0) {
				pointView.setEnabled(true);// 显示红色
			} else {
				pointView.setEnabled(false);// 显示灰色
				params.leftMargin = 8;
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
		prePosition = item % 5;
		detail.setText(imageDescriptions[prePosition]);// 显示标题内容
		// 对viewPager设置监听
		viewPager.setOnPageChangeListener(new MyPageListener());

		handler.sendEmptyMessageDelayed(0, 3000);

	}
	
	/*********定位***********/
	private final LocationListener locationListener = new LocationListener() {
		public void onLocationChanged(Location location) {
			updateWithNewLocation(location);
		}

		public void onProviderDisabled(String provider) {
			updateWithNewLocation(null);
		}

		public void onProviderEnabled(String provider) {
		}

		public void onStatusChanged(String provider, int status, Bundle extras) {
		}
	};
	private void updateWithNewLocation(Location location) {
		if (location != null) {
			lat = location.getLatitude();
			lng = location.getLongitude();
		} else {
			Toast.makeText(getApplicationContext(), "无法获取地理信息", Toast.LENGTH_SHORT).show();
		}
		List<Address> addList = null;
		Geocoder ge = new Geocoder(this);
		try {
			addList = ge.getFromLocation(lat, lng, 1);
		} catch (IOException e) {
			e.printStackTrace();
		}
		if (addList != null && addList.size() > 0) {
			for (int i = 0; i < addList.size(); i++) {
				Address ad = addList.get(i);
				latLongString =ad.getLocality();
			}
		}
		myLocationText.setText(latLongString);
	}
	
	
	class MyPageAdapter extends PagerAdapter{

		@Override
		public int getCount() {
			//Log.e("TAG", "getCount");
			return Integer.MAX_VALUE;//设置viewpager的最大容量，让其可以左右滑动
		}

		//判断传进来的对象是否相同
		@Override
		public boolean isViewFromObject(View arg0, Object arg1) {
			//Log.e("TAG", "isViewFromObject");
			return arg0==arg1;
		}
		
		//实例化item对象，装载进容器
		@Override
		public Object instantiateItem(ViewGroup container, int position) {
			int realPosition=position%imageViews.size();//取模，防止数组越界
			final ImageView imageView=imageViews.get(realPosition);
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
			container.removeView((View)object);
			//Log.e("TAG", "destroyItem position="+position+"---imageView="+object);
		}
		
		
	}
	
	
	/**viewpager 监听器
	 * @author Administrator
	 *
	 */
	class MyPageListener implements OnPageChangeListener{

		@Override
		public void onPageSelected(int position) {
			int realPosition=position%imageViews.size();//取模，防止数组越界
			//设置对应文本的文字
			detail.setText(imageDescriptions[realPosition]);
			//把上一个高亮的点变为灰色
			point_container.getChildAt(prePosition).setEnabled(false);
			//把当前点变为高亮
			point_container.getChildAt(realPosition).setEnabled(true);
			prePosition=realPosition;
		}

		@Override
		public void onPageScrolled(int position, float positionOffset,
				int positionOffsetPixels) {
			
		}

		@Override
		public void onPageScrollStateChanged(int state) {
			if(state==ViewPager.SCROLL_STATE_DRAGGING&&isDragg){
				isDragg=false;
				//移除消息
				handler.removeCallbacksAndMessages(null);
			}else if(state==ViewPager.SCROLL_STATE_IDLE){
				isDragg=true;
				//移除消息并发送消息
				handler.removeCallbacksAndMessages(null);
				handler.sendEmptyMessageDelayed(0, 4000);
			}
		}
		
	}

	protected void onDestroy() {
		super.onDestroy();
	}

	public class ButtonOnClickListener implements View.OnClickListener {
		@SuppressWarnings("deprecation")
		public void onClick(View v) {
			switch (v.getId()) {
			case R.id.loginbutton:
				intent.setClass(MainActivity.this, LoginActivity.class);
				startActivity(intent);
				break;
			 case R.id.dog:
			 intent.putExtra("categoryId", 1);
			 intent.setClass(MainActivity.this, ItemListActivity.class);
			 startActivity(intent);
			 break;
			case R.id.rice:
				intent.putExtra("categoryId", 2);
				intent.setClass(MainActivity.this, ItemListActivity.class);
				startActivity(intent);
				break;
			case R.id.cosmetology:
				intent.putExtra("categoryId", 3);
				intent.setClass(MainActivity.this, ItemListActivity.class);
				startActivity(intent);
				break;
			case R.id.clothes:
				intent.putExtra("categoryId", 4);
				intent.setClass(MainActivity.this, ItemListActivity.class);
				startActivity(intent);
				break;
			case R.id.toy:
				intent.putExtra("categoryId", 5);
				intent.setClass(MainActivity.this, ItemListActivity.class);
				startActivity(intent);
				break;
			case R.id.medical:

				break;
			case R.id.personal:
				showDialog();
				break;
			// case R.id.gps:
			// if (!gpsIsOpen())
			// return;
			// mLocation = getLocation();
			// if (mLocation != null){
			// gps.setText(text);
			// }
			// else{
			// Toast.makeText(getApplicationContext(), "获取不到数据",
			// Toast.LENGTH_SHORT).show();
			// }
			//
			// break;

			default:
				break;
			}
		}
	}

	// 特色服务弹出的对话框设置
	private void showDialog() {
		final String[] items = { "剪毛", "冲凉", "facial", "寄养" };
		yourChoice = -1;
		AlertDialog.Builder dialog = new AlertDialog.Builder(MainActivity.this);
		dialog.setTitle("请选择您所需要的服务~");
		dialog.setSingleChoiceItems(items, 0,
				new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int which) {
						yourChoice = which;
						/**** 选择的服务发送到服务器 *****/
						// 到时弹出确认订单的时候用到的商品名称就是这里所选择的服务

						/***********************/
					}
				});
		dialog.setPositiveButton("确定", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {
				intent.setClass(MainActivity.this, ShopListActivity.class);
				startActivity(intent);
			}
		});
		dialog.show();
	}
}
