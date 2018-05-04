package com.example.bean;

public class Address {
    private Integer locationId;

    private String name;

    private String location;

    private String cellphone;

    private Integer customerSid;
    
    private Customer customer;
    

    public Address() {
		super();
	}

	public Address(Integer locationId, String name, String location, String cellphone, Integer customerSid,
			Customer customer) {
		super();
		this.locationId = locationId;
		this.name = name;
		this.location = location;
		this.cellphone = cellphone;
		this.customerSid = customerSid;
		this.customer = customer;
	}

	
	public Customer getCustomer() {
		return customer;
	}

	public void setCustomer(Customer customer) {
		this.customer = customer;
	}

	public Integer getLocationId() {
        return locationId;
    }

    public void setLocationId(Integer locationId) {
        this.locationId = locationId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name == null ? null : name.trim();
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location == null ? null : location.trim();
    }

    public String getCellphone() {
        return cellphone;
    }

    public void setCellphone(String cellphone) {
        this.cellphone = cellphone == null ? null : cellphone.trim();
    }

    public Integer getCustomerSid() {
        return customerSid;
    }

    public void setCustomerSid(Integer customerSid) {
        this.customerSid = customerSid;
    }

	@Override
	public String toString() {
		return "Address [locationId=" + locationId + ", name=" + name + ", location=" + location + ", cellphone="
				+ cellphone + ", customerSid=" + customerSid + ", customer=" + customer + "]";
	}
    
    
}