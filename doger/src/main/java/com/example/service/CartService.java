package com.example.service;

import com.example.bean.Cart;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;

public class CartService extends Service{

	private Cart cart;
	@Override
	public void onCreate() {
		super.onCreate();
	}
	
	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		if(cart==null){
			cart=new Cart();
		}
		return super.onStartCommand(intent, flags, startId);
	}
	
	@Override
	public IBinder onBind(Intent intent) {
		Binder binder=new Binder();
		
		return null;
	}

}
