package com.example.diary.bean;

/**
 * Created by 604 on 2017/9/24.
 */

public class Other {
    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getImageId() {
        return imageId;
    }

    public void setImageId(int imageId) {
        this.imageId = imageId;
    }

    private int imageId;
    public Other(String name,int imageId){
        this.name = name;
        this.imageId = imageId;
    }
}
