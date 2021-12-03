package com.sbdev.covid19tracker;

public class HourlyModel {

    String time;
    String imageURL;
    int temp;
    int isDay;
    String text;

    public HourlyModel(String time, int isDay, int temp,String text) {
        this.time = time;
        this.isDay = isDay;
        this.temp = temp;
        this.text=text;
    }

    public int getIsDay() {
        return isDay;
    }

    public void setIsDay(int isDay) {
        this.isDay = isDay;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getImageURL() {
        return imageURL;
    }

    public void setImageURL(String imageURL) {
        this.imageURL = imageURL;
    }

    public int getTemp() {
        return temp;
    }

    public void setTemp(int temp) {
        this.temp = temp;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }
}
