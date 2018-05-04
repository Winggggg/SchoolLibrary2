package com.example.wing.schoollibrary.bean;

public class BorrowHistory {
    private Integer borrowHistoryId;

    private String borrowTime;

    private String returnTime;

    private Integer bookid;

    private Integer stuid;
    
    private Book book;
    public Book getBook() {
		return book;
	}

	public void setBook(Book book) {
		this.book = book;
	}
	

    public Integer getBorrowHistoryId() {
        return borrowHistoryId;
    }

    public void setBorrowHistoryId(Integer borrowHistoryId) {
        this.borrowHistoryId = borrowHistoryId;
    }

    public String getBorrowTime() {
        return borrowTime;
    }

    public void setBorrowTime(String borrowTime) {
        this.borrowTime = borrowTime == null ? null : borrowTime.trim();
    }

    public String getReturnTime() {
        return returnTime;
    }

    public void setReturnTime(String returnTime) {
        this.returnTime = returnTime == null ? null : returnTime.trim();
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