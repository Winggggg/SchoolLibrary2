package com.example.doger;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

public class ShopListActivity extends Activity {
	private Button okbutton, cancelbutton;
	private Intent intent = new Intent();
	private String itemname,number,name,price;
	private EditText time,type,orderlies,phonenumber;

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_shoplist);
		okbutton = (Button) this.findViewById(R.id.okbutton);
		okbutton.setOnClickListener(new ButtonOnClickListener());
		cancelbutton = (Button) this.findViewById(R.id.cancelbutton);
		cancelbutton.setOnClickListener(new ButtonOnClickListener());
		time = (EditText) this.findViewById(R.id.time);
		type = (EditText) this.findViewById(R.id.type);
		orderlies = (EditText) this.findViewById(R.id.orderlies);
		phonenumber = (EditText) this.findViewById(R.id.phonenumber);
		number = phonenumber.toString();
		name = orderlies.toString();
		//价格price和商品名称从服务器上获取
		//price
		//itemname
	}

	public class ButtonOnClickListener implements OnClickListener {
		public void onClick(View v) {
			switch (v.getId()) {
			case R.id.okbutton:
				showListDialog();
				break;
			case R.id.cancelbutton:
				intent.setClass(ShopListActivity.this, MainActivity.class);
				startActivity(intent);
				break;
			default:
				break;
			}
		}

	}

	private void showListDialog() {
		final AlertDialog.Builder listDialog = new AlertDialog.Builder(
				ShopListActivity.this);
		listDialog.setTitle("您的购物清单");
		listDialog.setMessage("商品名称：" + itemname + "\n" + "联系电话："
				+ phonenumber + "\n" + "看护人：" + orderlies + "\n" + "总计："
				+ price);
		listDialog.setPositiveButton("确定",
				new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int which) {
						intent.setClass(ShopListActivity.this, CodeActivity.class);
						startActivity(intent);
					}
				});
		listDialog.setNegativeButton("取消",
				new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int which) {
						dialog.dismiss();
					}
				});
		listDialog.show();
	}
}
