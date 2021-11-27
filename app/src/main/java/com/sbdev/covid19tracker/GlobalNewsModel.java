package com.sbdev.covid19tracker;

public class GlobalNewsModel {

    String url;
    String title;
    String des;
    String webURL;

    public GlobalNewsModel(String url, String title, String des, String webURL) {
        this.url = url;
        this.title = title;
        this.des = des;
        this.webURL=webURL;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDes() {
        return des;
    }

    public void setDes(String des) {
        this.des = des;
    }

    public String getWebURL() {
        return webURL;
    }

    public void setWebURL(String webURL) {
        this.webURL = webURL;
    }
}
