package com.example.wing.schoollibrary.bean;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Student implements Serializable{
    private Integer stuId;

    private Long stuNum;

    private String stuName;

    private String password;

    private String type;

    private String college;

    private String phonenum;

    private String email;


    public List<Integer> getLikeList() {
        return likeList;
    }

    public void setLikeList(List<Integer> likeList) {
        this.likeList = likeList;
    }

    /**
     * 装着点赞的是哪些学生列表
     */
    private List<Integer> likeList=new ArrayList<>();

    public Student() {

    }

    public Student( Long stuNum, String stuName, String password, String type, String college, String phonenum, String email) {
        this.stuNum = stuNum;
        this.stuName = stuName;
        this.password = password;
        this.type = type;
        this.college = college;
        this.phonenum = phonenum;
        this.email = email;
    }


    public Integer getStuId() {
        return stuId;
    }

    public void setStuId(Integer stuId) {
        this.stuId = stuId;
    }

    public Long getStuNum() {
        return stuNum;
    }

    public void setStuNum(Long stuNum) {
        this.stuNum = stuNum;
    }

    public String getStuName() {
        return stuName;
    }

    public void setStuName(String stuName) {
        this.stuName = stuName == null ? null : stuName.trim();
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password == null ? null : password.trim();
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type == null ? null : type.trim();
    }

    public String getCollege() {
        return college;
    }

    public void setCollege(String college) {
        this.college = college == null ? null : college.trim();
    }

    public String getPhonenum() {
        return phonenum;
    }

    public void setPhonenum(String phonenum) {
        this.phonenum = phonenum == null ? null : phonenum.trim();
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email == null ? null : email.trim();
    }
}