package com.gy.mvvm_demo.ui.bean;

import java.io.Serializable;

public class TestRvBean implements Serializable {
    private String title = "系统消息";
    private int flag;
    private String time;
    private String msgInfo;

    public TestRvBean(int flag, String time, String msgInfo) {
        this.flag = flag;
        this.time = time;
        this.msgInfo = msgInfo;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getFlag() {
        return flag;
    }

    public void setFlag(int flag) {
        this.flag = flag;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getMsgInfo() {
        return msgInfo;
    }

    public void setMsgInfo(String msgInfo) {
        this.msgInfo = msgInfo;
    }

    @Override
    public String toString() {
        return "TestRvBean{" +
                "title='" + title + '\'' +
                ", flag=" + flag +
                ", time='" + time + '\'' +
                ", msgInfo='" + msgInfo + '\'' +
                '}';
    }
}
