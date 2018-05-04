package com.example.wing.schoollibrary.bean;

public class Communicate {
    private Integer commId;

    private String publishTime;

    private String type;

    private String imgUrl;

    private String videoUrl;

    private Integer width;

    private Integer height;

    private String text;

    private Integer likeNum;
    
    private Integer commentNum;

	private Integer stuid;
	
	private Student student;
	
	

	public Communicate() {
		super();
	}

	public Communicate(Integer commId, String publishTime, String type, String imgUrl, String videoUrl, String text,
			Integer likeNum, Integer commentNum, Integer stuid) {
		super();
		this.commId = commId;
		this.publishTime = publishTime;
		this.type = type;
		this.imgUrl = imgUrl;
		this.videoUrl = videoUrl;
		this.text = text;
		this.likeNum = likeNum;
		this.commentNum = commentNum;
		this.stuid = stuid;
		
	}


    public Integer getWidth() {
        return width;
    }

    public void setWidth(Integer width) {
        this.width = width;
    }

    public Integer getHeight() {
        return height;
    }

    public void setHeight(Integer height) {
        this.height = height;
    }

    public Student getStudent() {
		return student;
	}

	public void setStudent(Student student) {
		this.student = student;
	}

	public Integer getCommentNum() {
		return commentNum;
	}

	public void setCommentNum(Integer commentNum) {
		this.commentNum = commentNum;
	}
	
    public Integer getCommId() {
        return commId;
    }

    public void setCommId(Integer commId) {
        this.commId = commId;
    }

    public String getPublishTime() {
        return publishTime;
    }

    public void setPublishTime(String publishTime) {
        this.publishTime = publishTime == null ? null : publishTime.trim();
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type == null ? null : type.trim();
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl == null ? null : imgUrl.trim();
    }

    public String getVideoUrl() {
        return videoUrl;
    }

    public void setVideoUrl(String videoUrl) {
        this.videoUrl = videoUrl == null ? null : videoUrl.trim();
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text == null ? null : text.trim();
    }

    public Integer getLikeNum() {
        return likeNum;
    }

    public void setLikeNum(Integer likeNum) {
        this.likeNum = likeNum;
    }

    public Integer getStuid() {
        return stuid;
    }

    public void setStuid(Integer stuid) {
        this.stuid = stuid;
    }

	@Override
	public String toString() {
		return "Communicate [commId=" + commId + ", publishTime=" + publishTime + ", type=" + type + ", imgUrl="
				+ imgUrl + ", videoUrl=" + videoUrl + ", text=" + text + ", likeNum=" + likeNum + ", commentNum="
				+ commentNum + ", stuid=" + stuid + ", student=" + student + "]";
	}
    
    
}