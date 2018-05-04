package com.example.bean;
/**
 * 产品信息实体类
 * @author Administrator
 *
 */
public class Product {
    private Integer productId;

    private String productName;

    private Float costPrice;

    private Float sellingPrice;

    private Integer saleNum;

    private String imagepath;

    private Integer categorySid;
    
    private Category category;
    
    private Stock stock;

    public Product() {
		super();
	}

	public Product(Integer productId, String productName, Float costPrice, Float sellingPrice, Integer saleNum,
			String imagepath, Integer categorySid) {
		super();
		this.productId = productId;
		this.productName = productName;
		this.costPrice = costPrice;
		this.sellingPrice = sellingPrice;
		this.saleNum = saleNum;
		this.imagepath = imagepath;
		this.categorySid = categorySid;
	}
	
	

	public Stock getStock() {
		return stock;
	}

	public void setStock(Stock stock) {
		this.stock = stock;
	}

	public Category getCategory() {
		return category;
	}

	public void setCategory(Category category) {
		this.category = category;
	}

	public Integer getProductId() {
        return productId;
    }

    public void setProductId(Integer productId) {
        this.productId = productId;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName == null ? null : productName.trim();
    }

    public Float getCostPrice() {
        return costPrice;
    }

    public void setCostPrice(Float costPrice) {
        this.costPrice = costPrice;
    }

    public Float getSellingPrice() {
        return sellingPrice;
    }

    public void setSellingPrice(Float sellingPrice) {
        this.sellingPrice = sellingPrice;
    }

    public Integer getSaleNum() {
        return saleNum;
    }

    public void setSaleNum(Integer saleNum) {
        this.saleNum = saleNum;
    }

    public String getImagepath() {
        return imagepath;
    }

    public void setImagepath(String imagepath) {
        this.imagepath = imagepath == null ? null : imagepath.trim();
    }

    public Integer getCategorySid() {
        return categorySid;
    }

    public void setCategorySid(Integer categorySid) {
        this.categorySid = categorySid;
    }

	@Override
	public String toString() {
		return "Product [productId=" + productId + ", productName=" + productName + ", costPrice=" + costPrice
				+ ", sellingPrice=" + sellingPrice + ", saleNum=" + saleNum + ", imagepath=" + imagepath
				+ ", categorySid=" + categorySid + ", category=" + category + "]";
	}
    
    
}