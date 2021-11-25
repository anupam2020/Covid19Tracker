package com.sbdev.covid19tracker;

public class StatesModel {

    String location;
    String affected;
    String deaths;

//    public static Comparator<CountryModel> StuNameComparator = new Comparator<CountryModel>() {
//
//        public int compare(CountryModel s1, CountryModel s2) {
//            String StudentName1 = s1.getActive().toUpperCase();
//            String StudentName2 = s2.getActive().toUpperCase();
//
//            //ascending order
//            return StudentName1.compareTo(StudentName2);
//
//            //descending order
//            //return StudentName2.compareTo(StudentName1);
//        }};

    public StatesModel(String location, String active, String deaths) {
        this.location = location;
        this.affected = active;
        this.deaths = deaths;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getActive() {
        return affected;
    }

    public void setActive(String active) {
        this.affected = active;
    }

    public String getDeaths() {
        return deaths;
    }

    public void setDeaths(String deaths) {
        this.deaths = deaths;
    }
}
