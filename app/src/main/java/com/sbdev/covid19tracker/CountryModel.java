package com.sbdev.covid19tracker;

public class CountryModel {

    String location;
    String active;
    String deaths;
    String recovered;

    public CountryModel(String location, String active, String deaths, String recovered) {
        this.location = location;
        this.active = active;
        this.deaths = deaths;
        this.recovered = recovered;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getActive() {
        return active;
    }

    public void setActive(String active) {
        this.active = active;
    }

    public String getDeaths() {
        return deaths;
    }

    public void setDeaths(String deaths) {
        this.deaths = deaths;
    }

    public String getRecovered() {
        return recovered;
    }

    public void setRecovered(String recovered) {
        this.recovered = recovered;
    }
}
