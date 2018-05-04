package com.example.bean;

public class Customer {
    private Integer customerId;

    private String loginNum;

    private String customerName;

    private String password;

    private String email;

    private String phonenum;
    

    public Customer() {
		super();
	}

	public Customer(Integer customerId, String loginNum, String customerName, String password, String email,
			String phonenum) {
		super();
		this.customerId = customerId;
		this.loginNum = loginNum;
		this.customerName = customerName;
		this.password = password;
		this.email = email;
		this.phonenum = phonenum;
	}

	public Integer getCustomerId() {
        return customerId;
    }

    public void setCustomerId(Integer customerId) {
        this.customerId = customerId;
    }

    public String getLoginNum() {
        return loginNum;
    }

    public void setLoginNum(String loginNum) {
        this.loginNum = loginNum == null ? null : loginNum.trim();
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName == null ? null : customerName.trim();
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password == null ? null : password.trim();
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email == null ? null : email.trim();
    }

    public String getPhonenum() {
        return phonenum;
    }

    public void setPhonenum(String phonenum) {
        this.phonenum = phonenum == null ? null : phonenum.trim();
    }

	@Override
	public String toString() {
		return "Customer [customerId=" + customerId + ", loginNum=" + loginNum + ", customerName=" + customerName
				+ ", password=" + password + ", email=" + email + ", phonenum=" + phonenum + "]";
	}
    
    
}