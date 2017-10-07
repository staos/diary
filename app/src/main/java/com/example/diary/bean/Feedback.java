package com.example.diary.bean;

import cn.bmob.v3.BmobObject;

/**
 * Created by 周要明 on 2017/8/14.
 */

public class Feedback extends BmobObject {
    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    private String content;
}
