package com.saikat.project.weatherlocation.model.external;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonSetter;

import java.util.List;

public class HereResponse implements GeoCodeResponse {

    private String latitude;
    private String longitude;

    @JsonProperty("Response")
    public void setResponse(Response response) {
        setViews(response.getViews());
    }

    private void setViews(List<View> view) {
        view.stream().findFirst().ifPresent(view1 -> setResult(view1.getResults()));
    }

    private void setResult(List<Result> result) {
        result.stream().findFirst().ifPresent(res -> setLocation(res.getLocation()));
    }

    private void setLocation(Location location) {
        setDisplayPosition(location.getDisplayPosition());
    }

    private void setDisplayPosition(DisplayPosition displayLocation) {
        setLatitude(String.valueOf(displayLocation.getLatitude()));
        setLongitude(String.valueOf(displayLocation.getLongitude()));
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

class Response {
    private List<View> views;

    public List<View> getViews() {
        return views;
    }

    @JsonProperty("View")
    public void setViews(List<View> views) {
        this.views = views;
    }
}

class View {
    private List<Result> results;


    public List<Result> getResults() {
        return results;
    }

    @JsonSetter("Result")
    public void setResults(List<Result> results) {
        this.results = results;
    }
}

class Result {
    private Location location;

    public Location getLocation() {
        return location;
    }

    @JsonSetter("Location")
    public void setLocation(Location location) {
        this.location = location;
    }
}

class Location {
    private DisplayPosition displayPosition;

    public DisplayPosition getDisplayPosition() {
        return displayPosition;
    }

    @JsonSetter("DisplayPosition")
    public void setDisplayPosition(DisplayPosition displayPosition) {
        this.displayPosition = displayPosition;
    }
}

class DisplayPosition {
    private double latitude;
    private double longitude;

    public double getLatitude() {
        return latitude;
    }

    @JsonSetter("Latitude")
    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    @JsonSetter("Longitude")
    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }
}
