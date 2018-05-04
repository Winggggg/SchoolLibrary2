package com.example.wing.schoollibrary.bean;

public class Comment {
    private Integer commentId;

    private Integer respondId;

    private String respContent;

    private Integer originId;

    private Integer comid;
    
    /**
     * 被回复方
     */
    private Student oristudent;
    /**
     *回复方
     */
    private Student respstudent;
    
    private Communicate communicate;

    public Comment() {
    }

    public Comment(Integer respondId, String respContent, Integer originId, Integer comid) {
        this.respondId = respondId;
        this.respContent = respContent;
        this.originId = originId;
        this.comid = comid;
    }

    public Student getOristudent() {
		return oristudent;
	}

	public void setOristudent(Student oristudent) {
		this.oristudent = oristudent;
	}

	public Student getRespstudent() {
		return respstudent;
	}

	public void setRespstudent(Student respstudent) {
		this.respstudent = respstudent;
	}

	public Communicate getCommunicate() {
		return communicate;
	}

	public void setCommunicate(Communicate communicate) {
		this.communicate = communicate;
	}


	public Integer getCommentId() {
        return commentId;
    }

    public void setCommentId(Integer commentId) {
        this.commentId = commentId;
    }

    public Integer getRespondId() {
        return respondId;
    }

    public void setRespondId(Integer respondId) {
        this.respondId = respondId;
    }

    public String getRespContent() {
        return respContent;
    }

    public void setRespContent(String respContent) {
        this.respContent = respContent == null ? null : respContent.trim();
    }

    public Integer getOriginId() {
        return originId;
    }

    public void setOriginId(Integer originId) {
        this.originId = originId;
    }

    public Integer getComid() {
        return comid;
    }

    public void setComid(Integer comid) {
        this.comid = comid;
    }
}