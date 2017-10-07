package com.example.diary.bean;
import cn.bmob.v3.BmobUser;

/**
 * Created by 周要明 on 2017/7/31.
 */

public class _User extends BmobUser {
    private String nickName;
    private String headImageUrl;
    private String backgroundUrl;
    private String autograph;
    private Boolean switchOne;
    private Boolean switchTwo;

    public Boolean getSwitchOne() {
        return switchOne;
    }

    public void setSwitchOne(Boolean switchOne) {
        this.switchOne = switchOne;
    }

    public Boolean getSwitchTwo() {
        return switchTwo;
    }

    public void setSwitchTwo(Boolean switchTwo) {
        this.switchTwo = switchTwo;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public String getHeadImageUrl() {
        return headImageUrl;
    }

    public void setHeadImageUrl(String headImageUrl) {
        this.headImageUrl = headImageUrl;
    }

    public String getBackgroundUrl() {
        return backgroundUrl;
    }

    public void setBackgroundUrl(String backgroundUrl) {
        this.backgroundUrl = backgroundUrl;
    }

    public String getAutograph() {
        return autograph;
    }

    public void setAutograph(String autograph) {
        this.autograph = autograph;
    }
}
