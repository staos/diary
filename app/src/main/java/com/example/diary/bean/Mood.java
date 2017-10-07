package com.example.diary.bean;

import cn.bmob.v3.BmobObject;
import cn.bmob.v3.datatype.BmobRelation;

/**
 * Created by 周要明 on 2017/8/15.
 */

public class Mood extends BmobObject{
    private _User author;
    private String content;
    private _User commenter;
    private String comment;
    private BmobRelation likes;
    private BmobRelation collect;

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

    private String time;
    private String address;
    private String weather;

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public _User getAuthor() {
        return author;
    }

    public void setAuthor(_User author) {
        this.author = author;
    }

    public _User getCommenter() {
        return commenter;
    }

    public void setCommenter(_User commenter) {
        this.commenter = commenter;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public BmobRelation getCollect() {
        return collect;
    }

    public void setCollect(BmobRelation collect) {
        this.collect = collect;
    }

    public BmobRelation getLikes() {
        return likes;
    }

    public void setLikes(BmobRelation likes) {
        this.likes = likes;
    }
}
