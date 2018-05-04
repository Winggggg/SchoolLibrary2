package com.example.bean;

/**
 * 购物项信息封装类
 * @author Administrator
 *
 */
public class CartItem {

	private Product product;
	private int quantity;//一条购物项的数量
	
	public CartItem() {
		super();
	}

	public CartItem(Product product, int quantity) {
		super();
		this.product = product;
		this.quantity = quantity;
	}

	public Product getProduct() {
		return product;
	}

	public void setProduct(Product product) {
		this.product = product;
	}

	public int getQuantity() {
		return quantity;
	}

	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}
	//获取一条购物项的总价格
	public Float getItemPrice(){
		return (float) (product.getSellingPrice()*quantity);
	}

	@Override
	public String toString() {
		return "CartItem [product=" + product + ", quantity=" + quantity + "]";
	}
	
	
}
