package com.example.diary.bean;

import org.litepal.crud.DataSupport;

import java.io.Serializable;

/**
 * Created by 周要明 on 2017/8/31.
 */

public class CollectionMood extends DataSupport implements Serializable{
    private String nickName;
    private String imageHeadUrl;
    private String imageBackgroundUrl;
    private String autograph;
    private String content;
    private String time;
    private String address;
    private String weather;

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPosition() {
        return weather;
    }

    public void setPosition(String position) {
        this.weather = position;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public String getImageHeadUrl() {
        return imageHeadUrl;
    }

    public void setImageHeadUrl(String imageHeadUrl) {
        this.imageHeadUrl = imageHeadUrl;
    }

    public String getImageBackgroundUrl() {
        return imageBackgroundUrl;
    }

    public void setImageBackgroundUrl(String imageBackgroundUrl) {
        this.imageBackgroundUrl = imageBackgroundUrl;
    }

    public String getAutograph() {
        return autograph;
    }

    public void setAutograph(String autograph) {
        this.autograph = autograph;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
