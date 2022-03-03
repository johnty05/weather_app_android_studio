package com.example.weatherforecast;

public class WeatherModel {
    private String time;
    private String temp;
    private String icon;
    private String humidity;

    public WeatherModel(String time, String temp, String icon, String humidity) {
        this.time = time;
        this.temp = temp;
        this.icon = icon;
        this.humidity = humidity;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getTemp() {
        return temp;
    }

    public void setTemp(String temp) {
        this.temp = temp;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public String getHumidity() {
        return humidity;
    }

    public void setHumidity(String humidity) {
        this.humidity = humidity;
    }
}
