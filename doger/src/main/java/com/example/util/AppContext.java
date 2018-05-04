package com.example.util;

import com.example.bean.Cart;

import android.app.Application;

/**
 * @author Administrator
 *	自定义application对象
 */
public class AppContext extends Application {
	private Cart cart;
	public Cart getCart() {
		return cart;
	}
	@Override
	public void onCreate() {
		super.onCreate();
		cart=new Cart();
	}
	
	
	
	
	
}
