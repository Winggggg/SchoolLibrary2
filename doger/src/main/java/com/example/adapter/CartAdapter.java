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
import android.widget.ImageView;
import android.widget.TextView;

import com.example.bean.CartItem;
import com.example.doger.R;
import com.example.util.ImageLoader;

/**购物项列表adapter
 * @author Administrator
 *
 */
public class CartAdapter extends BaseAdapter {

	/**
	 * 需要加载的购物项产品数据列表
	 */
	private List<CartItem> data = new ArrayList<CartItem>();
	private Context context;

	private Map<String, Bitmap> cacheMap = new HashMap<String, Bitmap>();

	private ImageLoader imageLoader;

	public CartAdapter(Context context) {
		super();
		this.context = context;
		imageLoader = new ImageLoader(cacheMap, context, R.drawable.loading,
				R.drawable.error);
	}

	public List<CartItem> getData() {
		return data;
	}

	public void setData(List<CartItem> data) {
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
		if(convertView==null){
			//加载布局文件
			convertView=View.inflate(context, R.layout.cart_item_details, null);
			viewHolder=new ViewHolder();
			//获取子对象,将子对象保存在视图容器中
			viewHolder.iv_image=(ImageView) convertView.findViewById(R.id.iv_image);
			viewHolder.tv_productName=(TextView) convertView.findViewById(R.id.tv_productName);
			viewHolder.tv_totalPrice=(TextView) convertView.findViewById(R.id.tv_totalPrice);
			viewHolder.tv_num=(TextView) convertView.findViewById(R.id.tv_num);
			convertView.setTag(viewHolder);//设置标志
		}else{
			viewHolder=(ViewHolder) convertView.getTag();
		}
		
		//设置数据
		final CartItem cartItem=data.get(position);
		//Log.e("TAG", "数据="+cartItem);
		viewHolder.tv_productName.setText(cartItem.getProduct().getProductName());
		viewHolder.tv_num.setText("数量："+cartItem.getQuantity());
		viewHolder.tv_totalPrice.setText("¥："+cartItem.getItemPrice());
		//加载显示图片
		imageLoader.loadImage(viewHolder.iv_image,cartItem.getProduct().getImagepath());
		
		return convertView;
	}

	// 视图容器
	static class ViewHolder {
		ImageView iv_image;// 显示图片
		TextView tv_productName;// 显示名称
		TextView tv_num;// 显示数量
		TextView tv_totalPrice;// 显示价格
	}

}
