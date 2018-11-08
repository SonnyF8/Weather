package com.code.weather.Model;

import java.util.ArrayList;

public class WeatherModel {

    private String date;
    private String temp;
    private String sky;

    private ArrayList<String> timeList;
    private ArrayList<String> tempList;
    private ArrayList<String> skyList;

    public void setDate(String date) {
        this.date = date;
    }

    public void setTemp(String temp) {
        this.temp = temp;
    }

    public void setSky(String sky) {
        this.sky = sky;
    }

    public void setTimeList(ArrayList<String> timeList) {
        this.timeList = timeList;
    }

    public void setTempList(ArrayList<String> tempList) {
        this.tempList = tempList;
    }

    public void setSkyList(ArrayList<String> skyList) {
        this.skyList = skyList;
    }

    public String getDate() {
        return date;
    }

    public String getTemp() {
        return temp;
    }

    public String getSky() {
        return sky;
    }

    public ArrayList<String> getTimeList() {
        return timeList;
    }

    public ArrayList<String> getTempList() {
        return tempList;
    }

    public ArrayList<String> getSkyList() {
        return skyList;
    }

}
