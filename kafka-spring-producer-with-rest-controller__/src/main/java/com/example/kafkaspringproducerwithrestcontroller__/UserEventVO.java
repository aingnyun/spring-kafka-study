package com.example.kafkaspringproducerwithrestcontroller__;

public class UserEventVO {
    public UserEventVO(String timestamp, String colorName, String userName) {
        this.timestamp = timestamp;
//        this.userAgent = userAgent;
        this.colorName = colorName;
        this.userName = userName;
    }

    private String timestamp;
//    private String userAgent;
    private String colorName;
    private String userName;

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

//    public String getUserAgent() {
//        return userAgent;
//    }
//
//    public void setUserAgent(String userAgent) {
//        this.userAgent = userAgent;
//    }

    public String getColorName() {
        return colorName;
    }

    public void setColorName(String colorName) {
        this.colorName = colorName;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }
}
