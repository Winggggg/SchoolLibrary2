package com.example.wing.schoollibrary.bean;

import java.io.Serializable;

/**
 * Created by Administrator on 2017/12/17 0017.
 * 代表本地视频的实体类
 */

public class MediaBean implements Serializable{
    private String mediaName;
    private long mediaTime;
    private long size;
    private String path;
    private String author;
    private String thumbPath;

    public MediaBean() {
    }

    public MediaBean(String mediaName, long mediaTime, long size, String path, String author, String thumbPath, String videoTitle) {
        this.mediaName = mediaName;
        this.mediaTime = mediaTime;
        this.size = size;
        this.path = path;
        this.author = author;
        this.thumbPath = thumbPath;
        this.videoTitle = videoTitle;
    }

    public String getVideoTitle() {
        return videoTitle;
    }

    public void setVideoTitle(String videoTitle) {
        this.videoTitle = videoTitle;
    }

    private String videoTitle;




    public String getThumbPath() {
        return thumbPath;
    }

    public void setThumbPath(String thumbPath) {
        this.thumbPath = thumbPath;
    }

    public String getMediaName() {
        return mediaName;
    }

    public long getMediaTime() {
        return mediaTime;
    }

    public long getSize() {
        return size;
    }

    public String getPath() {
        return path;
    }

    public String getAuthor() {
        return author;
    }

    public void setMediaName(String mediaName) {
        this.mediaName = mediaName;
    }

    public void setMediaTime(long mediaTime) {
        this.mediaTime = mediaTime;
    }

    public void setSize(long size) {
        this.size = size;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    @Override
    public String toString() {
        return "MediaBean{" +
                "mediaName='" + mediaName + '\'' +
                ", mediaTime=" + mediaTime +
                ", size=" + size +
                ", path='" + path + '\'' +
                ", author='" + author + '\'' +
                ", thumbPath='" + thumbPath + '\'' +
                ", videoTitle='" + videoTitle + '\'' +
                '}';
    }
}
