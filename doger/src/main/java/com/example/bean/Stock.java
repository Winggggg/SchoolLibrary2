package com.example.bean;
/**
 * 库存信息实体类
 * @author Administrator
 *
 */
public class Stock {
    private Integer stockId;

    private String stockNum;

    private String stockName;

    private Integer expectedVal;

    private Integer realVal;


    private String reasonContent;


    private Integer productSid;
    
    
    

    public Stock() {
		super();
	}

	public Stock(Integer stockId, String stockNum, String stockName, Integer expectedVal, Integer realVal,
			 String reasonContent, Integer productSid) {
		super();
		this.stockId = stockId;
		this.stockNum = stockNum;
		this.stockName = stockName;
		this.expectedVal = expectedVal;
		this.realVal = realVal;
		this.reasonContent = reasonContent;
		this.productSid = productSid;
	}

	public Integer getStockId() {
        return stockId;
    }

    public void setStockId(Integer stockId) {
        this.stockId = stockId;
    }

    public String getStockNum() {
        return stockNum;
    }

    public void setStockNum(String stockNum) {
        this.stockNum = stockNum == null ? null : stockNum.trim();
    }

    public String getStockName() {
        return stockName;
    }

    public void setStockName(String stockName) {
        this.stockName = stockName == null ? null : stockName.trim();
    }

    public Integer getExpectedVal() {
        return expectedVal;
    }

    public void setExpectedVal(Integer expectedVal) {
        this.expectedVal = expectedVal;
    }

    public Integer getRealVal() {
        return realVal;
    }

    public void setRealVal(Integer realVal) {
        this.realVal = realVal;
    }


    public String getReasonContent() {
        return reasonContent;
    }

    public void setReasonContent(String reasonContent) {
        this.reasonContent = reasonContent == null ? null : reasonContent.trim();
    }


    public Integer getProductSid() {
        return productSid;
    }

    public void setProductSid(Integer productSid) {
        this.productSid = productSid;
    }

	@Override
	public String toString() {
		return "Stock [stockId=" + stockId + ", stockNum=" + stockNum + ", stockName=" + stockName + ", expectedVal="
				+ expectedVal + ", realVal=" + realVal + ", reasonContent="
				+ reasonContent + ", productSid=" + productSid + "]";
	}
    
    
}