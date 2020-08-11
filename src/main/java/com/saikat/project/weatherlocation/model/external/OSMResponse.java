package com.saikat.project.weatherlocation.model.external;

import com.fasterxml.jackson.annotation.JsonSetter;

public class OSMResponse implements GeoCodeResponse {
    private String latitude;
    private String longitude;

    public String getLatitude() {
        return latitude;
    }

    @JsonSetter("lat")
    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getLongitude() {
        return longitude;
    }

    @JsonSetter("lon")
    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }
}
