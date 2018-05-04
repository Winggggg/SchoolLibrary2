package com.example.adapter;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.bean.Address;
import com.example.doger.AddressActivity;
import com.example.doger.AddressListActivity;
import com.example.doger.CartActivity;
import com.example.doger.R;

/**
 * 地址列表Adapter
 * 
 * @author Administrator
 *
 */
public class AddressAdapter extends BaseAdapter {

	/**
	 * 需要加载的地址数据列表
	 */
	private List<Address> data = new ArrayList<Address>();
	private Context context;

	public AddressAdapter(Context context) {
		super();
		this.context = context;
	}

	public List<Address> getData() {
		return data;
	}

	public void setData(List<Address> data) {
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
			convertView=View.inflate(context, R.layout.address_item, null);
			viewHolder=new ViewHolder();
			//获取子对象,将子对象保存在视图容器中
			viewHolder.tv_name_cellphone=(TextView) convertView.findViewById(R.id.tv_name_cellphone);
			viewHolder.tv_location=(TextView) convertView.findViewById(R.id.tv_location);
			viewHolder.btn_edit=(Button) convertView.findViewById(R.id.btn_edit);
			viewHolder.ll_address=(LinearLayout) convertView.findViewById(R.id.ll_address);
			
			convertView.setTag(viewHolder);//设置标志
		}else{
			viewHolder=(ViewHolder) convertView.getTag();
		}
		
		//设置数据
		final Address address=data.get(position);
		Log.e("TAG", "数据="+address);
		viewHolder.tv_name_cellphone.setText(address.getName()+"  "+address.getCellphone());
		viewHolder.tv_location.setText(address.getLocation());
		//点击事件
		viewHolder.btn_edit.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				//把地址信息传过去给编辑页面
				Intent intent=new Intent(context, AddressActivity.class);
				intent.putExtra("name", address.getName());
				intent.putExtra("locationId", address.getLocationId());
				intent.putExtra("location", address.getLocation());
				intent.putExtra("cellphone", address.getCellphone());
				intent.putExtra("customerSid", address.getCustomerSid());
				context.startActivity(intent);
				AddressListActivity activity=(AddressListActivity) context;
				activity.finish();
			}
		});
		
		
		//ll_address item点击事件
		viewHolder.ll_address.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				//把地址信息传过去给购物车页面
				Intent intent=new Intent(context, CartActivity.class);
				intent.putExtra("name", address.getName());
				intent.putExtra("locationId", address.getLocationId());
				intent.putExtra("location", address.getLocation());
				intent.putExtra("cellphone", address.getCellphone());
				intent.putExtra("customerSid", address.getCustomerSid());
				context.startActivity(intent);
				AddressListActivity activity=(AddressListActivity) context;
				activity.finish();
			}
		});
		return convertView;
	}

	// 视图容器
	static class ViewHolder {
		TextView tv_name_cellphone;// 显示名称和电话
		TextView tv_location;// 显示地址
		Button btn_edit;// 编辑按钮
		LinearLayout ll_address;
	}

}
