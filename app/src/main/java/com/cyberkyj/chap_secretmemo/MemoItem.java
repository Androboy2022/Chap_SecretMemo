package com.cyberkyj.chap_secretmemo;

import java.io.Serializable;

public class MemoItem implements Serializable {
    String contens, friendName, friendPhone, timeStamp, imagePath;

    public MemoItem(String contens, String friendName, String friendPhone, String timeStamp, String imagePath) {
        this.contens = contens;
        this.friendName = friendName;
        this.friendPhone = friendPhone;
        this.timeStamp = timeStamp;
        this.imagePath = imagePath;
    }

    public String getContens() {
        return contens;
    }

    public void setContens(String contens) {
        this.contens = contens;
    }

    public String getFriendName() {
        return friendName;
    }

    public void setFriendName(String friendName) {
        this.friendName = friendName;
    }

    public String getFriendPhone() {
        return friendPhone;
    }

    public void setFriendPhone(String friendPhone) {
        this.friendPhone = friendPhone;
    }

    public String getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(String timeStamp) {
        this.timeStamp = timeStamp;
    }

    public String getImagePath() {
        return imagePath;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }
}


