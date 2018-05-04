package com.example.wing.schoollibrary.pager.search.picture;

import java.io.Serializable;

/**
 * Created by Administrator on 2018/3/25 0025.
 */

public class Picture implements Serializable{
    /**
     * 图片路径
     */
    private String path;
    /**
     * 图片宽度
     */
    private int  width;
    /**
     * 图片高度
     */
    private int height;

    public Picture(String path, int width, int height) {
        this.path = path;
        this.width = width;
        this.height = height;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }
}
