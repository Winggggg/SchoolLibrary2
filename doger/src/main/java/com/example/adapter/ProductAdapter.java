package com.example.adapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.bean.Cart;
import com.example.bean.Product;
import com.example.doger.MainActivity;
import com.example.doger.R;
import com.example.util.AppContext;
import com.example.util.CartUtil;
import com.example.util.ImageLoader;

/**
 * 显示产品列表适配器
 * @author Administrator
 *
 */
public class ProductAdapter extends BaseAdapter{
	/**
	 * 需要加载的产品数据列表
	 */
	private List<Product> data=new ArrayList<Product>();
	private Context context;
	
	private Map<String,Bitmap>cacheMap=new HashMap<String, Bitmap>();
	
	private ImageLoader imageLoader;
	
	public ProductAdapter( Context context) {
		super();
		this.context = context;
		imageLoader=new ImageLoader(cacheMap, context, R.drawable.loading, R.drawable.error);
	}
	
	

	public List<Product> getData() {
		return data;
	}
	public void setData(List<Product> data) {
		this.data = data;
	}

	@Override
	public int getCount() {
		return data.size();
	}

	@Override
	public Object getItem(int position) {
		return data.get(position);
	}

	@Override
	public long getItemId(int position) {
		return 0;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder viewHolder=null;
		Cart Cart=null;
		if(convertView==null){
			//加载布局文件
			convertView=View.inflate(context, R.layout.product_item_details, null);
			viewHolder=new ViewHolder();
			//获取子对象,将子对象保存在视图容器中
			viewHolder.iv_image=(ImageView) convertView.findViewById(R.id.iv_image);
			viewHolder.tv_productName=(TextView) convertView.findViewById(R.id.tv_productName);
			viewHolder.tv_saleName=(TextView) convertView.findViewById(R.id.tv_saleName);
			viewHolder.tv_price=(TextView) convertView.findViewById(R.id.tv_price);
			viewHolder.tv_num=(TextView) convertView.findViewById(R.id.tv_num);
			viewHolder.btn_minus=(ImageButton) convertView.findViewById(R.id.btn_minus);
			viewHolder.btn_plus=(ImageButton) convertView.findViewById(R.id.btn_plus);
			convertView.setTag(viewHolder);//设置标志
		}else{
			viewHolder=(ViewHolder) convertView.getTag();
		}
		
		//设置数据
		final Product product=data.get(position);
		//Log.e("TAG", "数据="+product);
		viewHolder.tv_productName.setText(product.getProductName());
		viewHolder.tv_saleName.setText("销售量："+product.getSaleNum());
		viewHolder.tv_price.setText("¥："+product.getSellingPrice());
		//加载显示图片
		imageLoader.loadImage(viewHolder.iv_image,product.getImagepath());
		final TextView tv=viewHolder.tv_num;
		//给增加按钮设置监听器
		viewHolder.btn_plus.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Integer num=Integer.parseInt(tv.getText().toString());
				AppContext appContext=(AppContext)context.getApplicationContext();
				//获取购物车对象
				Cart cart=appContext.getCart();
				//添加进购物车
				CartUtil.addProductInCart(cart, product, 1);
				tv.setText((num+1)+"");
				Toast.makeText(context, "已添加一件商品进购物车", Toast.LENGTH_SHORT).show();
			}
		});
		
		//给减少按钮设置监听器
		viewHolder.btn_minus.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Integer num=Integer.parseInt(tv.getText().toString());
				AppContext appContext=(AppContext)context.getApplicationContext();
				//获取购物车对象
				Cart cart=appContext.getCart();
				//删除购物车里的购物项
				if(num>0){
					CartUtil.deleteProductFromCart(cart, product.getProductId()+"");
					tv.setText((num-1)+"");
					Toast.makeText(context, "已从购物车中删除一件商品", Toast.LENGTH_SHORT).show();
				}
			}
		});
		
		AppContext appContext=(AppContext)context.getApplicationContext();
		//获取购物车对象
		Cart cart=appContext.getCart();
		if(cart==null){
			tv.setText("0");
		}
		
		
		return convertView;
	}
	
	//视图容器
	static class ViewHolder{
		ImageView iv_image;//显示图片
		TextView tv_productName;//显示名称
		TextView tv_saleName;//显示销量
		TextView tv_price;//显示价格
		TextView tv_num;//显示数量
		ImageButton btn_minus;//减
		ImageButton btn_plus;//加
		
	}
	
}


