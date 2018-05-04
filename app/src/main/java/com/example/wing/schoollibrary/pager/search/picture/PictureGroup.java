package com.example.wing.schoollibrary.pager.search.picture;

/**
 * Created by Administrator on 2018/2/21 0021.
 */

public class PictureGroup {
    /**
     * 文件夹的第一张图片路径
     */
    private String topPicturePath;
    /**
     * 文件夹名
     */
    private String folderName;
    /**
     * 文件夹中的图片数
     */
    private int pictureCount;

    public String getTopPicturePath() {
        return topPicturePath;
    }

    public void setTopPicturePath(String topPicturePath) {
        this.topPicturePath = topPicturePath;
    }

    public String getFolderName() {
        return folderName;
    }

    public void setFolderName(String folderName) {
        this.folderName = folderName;
    }

    public int getPictureCount() {
        return pictureCount;
    }

    public void setPictureCount(int pictureCount) {
        this.pictureCount = pictureCount;
    }

}

