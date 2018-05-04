package com.example.util;

import com.example.bean.Cart;
import com.example.bean.CartItem;
import com.example.bean.Product;

/**
 * 购物车操作的工具类，包括增加，减少和清空
 * 
 * @author Administrator
 *
 */
public class CartUtil {
	/**
	 * 添加商品进购物车
	 * 
	 * @param cart
	 * @param productId
	 * @param quantity
	 */
	public static void addProductInCart(Cart cart, Product product, Integer quantity) {
		CartItem cartItem = cart.getCartMap().get(String.valueOf(product.getProductId()));
		// 添加之前先判断购物车里有该商品
		if (cartItem == null) {
			// 没有则新建添加
			cartItem = new CartItem(product, quantity);
			cart.getCartMap().put(String.valueOf(product.getProductId()), cartItem);
		} else {
			// 有则数量增加
			cartItem.setQuantity(cartItem.getQuantity() + quantity);
		}
	}
	
	/**根据商品ID删除购物车里的购物项
	 * 
	 * 
	 * @param cart
	 * @param productId
	 */
	public static void deleteProductFromCart(Cart cart,String productId){
		CartItem cartItem=cart.getCartMap().get(productId);
		if((cartItem.getQuantity()-1)>0){
			cartItem.setQuantity(cartItem.getQuantity()-1);
			cart.getCartMap().put(productId, cartItem);
		}else{
			cart.getCartMap().remove(productId);
		}
	}
	
	/**清空
	 * @param cart
	 */
	public static void clear(Cart cart){
		cart.getCartMap().clear();
	}
	
	

}
