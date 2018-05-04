package com.example.bean;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * 购物车信息封装类
 * @author Administrator
 *
 */
public class Cart implements Serializable{

	private Map<String,CartItem> cartMap=new HashMap<String,CartItem>();

	private int addressId;//收货地址id
	
	private Customer customer;//客户信息
	
	
	public Customer getCustomer() {
		return customer;
	}

	public void setCustomer(Customer customer) {
		this.customer = customer;
	}

	public int getAddressId() {
		return addressId;
	}

	public void setAddressId(int addressId) {
		this.addressId = addressId;
	}

	
	public void setCartMap(Map<String, CartItem> cartMap) {
		this.cartMap = cartMap;
	}

	public Map<String, CartItem> getCartMap() {
		return cartMap;
	}

	//获取购物车商品的总数量
	public int getTotalNum(){
		int totalNum=0;
		for(CartItem cartItem:cartMap.values()){
			totalNum+=cartItem.getQuantity();
		}
		return totalNum;
	}
	
	//获取购物车商品的总价格
	public float getTotalPrice(){
		int totalPrice=0;
		for(CartItem cartItem:cartMap.values()){
			totalPrice+=cartItem.getItemPrice();
		}
		return totalPrice;
	}

	@Override
	public String toString() {
		return "Cart [cartMap=" + cartMap + ", addressId=" + addressId
				+ ", customer=" + customer + "]";
	}
	
	
	
	
}
