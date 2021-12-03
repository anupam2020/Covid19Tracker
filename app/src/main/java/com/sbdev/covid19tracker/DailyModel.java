package com.sbdev.covid19tracker;

public class DailyModel {

    String date;
    String temp;
    String type;

    public DailyModel(String date, String temp, String type) {
        this.date = date;
        this.temp = temp;
        this.type = type;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTemp() {
        return temp;
    }

    public void setTemp(String temp) {
        this.temp = temp;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
