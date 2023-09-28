package com.example.driprocket;

public class WeeklyForecastItems {

    String recyclerWeatherDay;
    String recyclerWeatherTemp;
    int recyclerWeatherIcon;

    public WeeklyForecastItems(String recyclerWeatherDay, String recyclerWeatherTemp, int recyclerWeatherIcon) {
        this.recyclerWeatherDay = recyclerWeatherDay;
        this.recyclerWeatherTemp = recyclerWeatherTemp;
        this.recyclerWeatherIcon = recyclerWeatherIcon;
    }


    public String getRecyclerWeatherDay() {
        return recyclerWeatherDay;
    }

    public String getRecyclerWeatherTemp() {
        return recyclerWeatherTemp;
    }

    public int getRecyclerWeatherIcon() {
        return recyclerWeatherIcon;
    }
}
