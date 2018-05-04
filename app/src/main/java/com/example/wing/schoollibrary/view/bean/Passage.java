package com.example.wing.schoollibrary.view.bean;

import java.io.Serializable;

public class Passage implements Serializable{
    private Integer passageId;

    private String title;
    
    private String type;

    private String publisher;

    private String publishTime;

    private String contentStr;

    private String contentImgurl;
    
    

    public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public Integer getPassageId() {
        return passageId;
    }

    public void setPassageId(Integer passageId) {
        this.passageId = passageId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title == null ? null : title.trim();
    }

    public String getPublisher() {
        return publisher;
    }

    public void setPublisher(String publisher) {
        this.publisher = publisher == null ? null : publisher.trim();
    }

    public String getPublishTime() {
        return publishTime;
    }

    public void setPublishTime(String publishTime) {
        this.publishTime = publishTime == null ? null : publishTime.trim();
    }

    public String getContentStr() {
        return contentStr;
    }

    public void setContentStr(String contentStr) {
        this.contentStr = contentStr == null ? null : contentStr.trim();
    }

    public String getContentImgurl() {
        return contentImgurl;
    }

    public void setContentImgurl(String contentImgurl) {
        this.contentImgurl = contentImgurl == null ? null : contentImgurl.trim();
    }
}