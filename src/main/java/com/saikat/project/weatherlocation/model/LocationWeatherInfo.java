package com.saikat.project.weatherlocation.model;

import com.saikat.project.weatherlocation.model.external.GeoCodeResponse;
import com.saikat.project.weatherlocation.model.external.TempParam;

import java.util.List;
import java.util.Optional;

public class LocationWeatherInfo {
    private WeatherInfo weatherInfo;
    private LocationInfo locationInfo;

    private LocationWeatherInfo(WeatherInfo weatherInfo, LocationInfo locationInfo) {
        this.locationInfo = locationInfo;
        this.weatherInfo = weatherInfo;
    }

    public static Optional<LocationWeatherInfo> buildLocationWeatherInfo(List<GeoCodeResponse> geoCodes, TempParam weather) {
        return geoCodes.stream().findFirst().map(geoCode -> {
            // Using °C only for this application
            WeatherInfo weatherInfo = new WeatherInfo(weather.getTemp(), "°C");
            LocationInfo locationInfo = new LocationInfo(geoCode.getLatitude(), geoCode.getLongitude());
            return new LocationWeatherInfo(weatherInfo, locationInfo);
        });
    }

    public WeatherInfo getWeatherInfo() {
        return weatherInfo;
    }

    public void setWeatherInfo(WeatherInfo weatherInfo) {
        this.weatherInfo = weatherInfo;
    }

    public LocationInfo getLocationInfo() {
        return locationInfo;
    }

    public void setLocationInfo(LocationInfo locationInfo) {
        this.locationInfo = locationInfo;
    }
}

class WeatherInfo {
    private double temperature;
    private String unit;

    public WeatherInfo(double temperature, String unit) {
        this.temperature = temperature;
        this.unit = unit;
    }

    public double getTemperature() {
        return temperature;
    }

    public void setTemperature(double temperature) {
        this.temperature = temperature;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }
}

class LocationInfo {
    private String latitude;
    private String longitude;

    public LocationInfo(String latitude, String longitude) {
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }
}
