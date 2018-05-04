package com.example.wing.schoollibrary.bean;

import java.io.Serializable;

public class Book implements Serializable{
    private Integer bookId;

    private String bookName;

    private String author;

    private String publisher;

    private String publishYear;

    private String callNum;

    private String bookType;

    private String collectionPlace;

    private Integer borrowNum;

    private String isbn;

    private String issn;

    private String detail;

    private String searchTime;

    public Book() {
    }

    public Book(String bookName, String author, String publisher,  String callNum, String bookType, String collectionPlace, String isbn, String issn) {
        this.bookName = bookName;
        this.author = author;
        this.publisher = publisher;
        this.callNum = callNum;
        this.bookType = bookType;
        this.collectionPlace = collectionPlace;
        this.isbn = isbn;
        this.issn = issn;
    }

    public String getSearchTime() {
        return searchTime;
    }

    public void setSearchTime(String searchTime) {
        this.searchTime = searchTime;
    }

    public Integer getBookId() {
        return bookId;
    }

    public void setBookId(Integer bookId) {
        this.bookId = bookId;
    }

    public String getBookName() {
        return bookName;
    }

    public void setBookName(String bookName) {
        this.bookName = bookName == null ? null : bookName.trim();
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author == null ? null : author.trim();
    }

    public String getPublisher() {
        return publisher;
    }

    public void setPublisher(String publisher) {
        this.publisher = publisher == null ? null : publisher.trim();
    }

    public String getPublishYear() {
        return publishYear;
    }

    public void setPublishYear(String publishYear) {
        this.publishYear = publishYear == null ? null : publishYear.trim();
    }

    public String getCallNum() {
        return callNum;
    }

    public void setCallNum(String callNum) {
        this.callNum = callNum == null ? null : callNum.trim();
    }

    public String getBookType() {
        return bookType;
    }

    public void setBookType(String bookType) {
        this.bookType = bookType == null ? null : bookType.trim();
    }

    public String getCollectionPlace() {
        return collectionPlace;
    }

    public void setCollectionPlace(String collectionPlace) {
        this.collectionPlace = collectionPlace == null ? null : collectionPlace.trim();
    }

    public Integer getBorrowNum() {
        return borrowNum;
    }

    public void setBorrowNum(Integer borrowNum) {
        this.borrowNum = borrowNum;
    }

    public String getIsbn() {
        return isbn;
    }

    public void setIsbn(String isbn) {
        this.isbn = isbn == null ? null : isbn.trim();
    }

    public String getIssn() {
        return issn;
    }

    public void setIssn(String issn) {
        this.issn = issn == null ? null : issn.trim();
    }

    public String getDetail() {
        return detail;
    }

    public void setDetail(String detail) {
        this.detail = detail == null ? null : detail.trim();
    }
}