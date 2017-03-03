package com.example.buiderdream.apehire.bean;

import java.io.Serializable;

/**
 * Created by 51644 on 2017/3/2.
 */

public class PushMessage implements Serializable{
    private String title;
    private String content;
    private String img;
    private long time;

    public PushMessage(String title, String content, String img,long time) {
        this.title = title;
        this.content = content;
        this.img = img;
        this.time = time;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }
}
