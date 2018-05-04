package com.example.wing.schoollibrary.bean;

public class BorrowInfo {
    private Integer borrowId;

    private String borrowTime;

    private String returnUtiltime;

    private Integer bookid;

    private Integer stuid;
    
    private Book book;
    
    

    public Book getBook() {
		return book;
	}

	public void setBook(Book book) {
		this.book = book;
	}

	public Integer getBorrowId() {
        return borrowId;
    }

    public void setBorrowId(Integer borrowId) {
        this.borrowId = borrowId;
    }

    public String getBorrowTime() {
        return borrowTime;
    }

    public void setBorrowTime(String borrowTime) {
        this.borrowTime = borrowTime == null ? null : borrowTime.trim();
    }

    public String getReturnUtiltime() {
        return returnUtiltime;
    }

    public void setReturnUtiltime(String returnUtiltime) {
        this.returnUtiltime = returnUtiltime == null ? null : returnUtiltime.trim();
    }

    public Integer getBookid() {
        return bookid;
    }

    public void setBookid(Integer bookid) {
        this.bookid = bookid;
    }

    public Integer getStuid() {
        return stuid;
    }

    public void setStuid(Integer stuid) {
        this.stuid = stuid;
    }
}