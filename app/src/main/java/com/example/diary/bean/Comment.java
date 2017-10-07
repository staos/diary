package com.example.diary.bean;

import cn.bmob.v3.BmobObject;

/**
 * Created by 周要明 on 2017/8/29.
 */

public class Comment extends BmobObject {
    private String content;
    private Mood mood;
    private _User commenter;

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Mood getMood() {
        return mood;
    }

    public void setMood(Mood mood) {
        this.mood = mood;
    }

    public _User getCommenter() {
        return commenter;
    }

    public void setCommenter(_User commenter) {
        this.commenter = commenter;
    }
}
