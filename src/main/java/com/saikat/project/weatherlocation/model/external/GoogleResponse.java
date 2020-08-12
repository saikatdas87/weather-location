package com.saikat.project.weatherlocation.model.external;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonSetter;

import java.util.List;
import java.util.Optional;

public class GoogleResponse implements GeoCodeResponse {
    private String latitude;
    private String longitude;

    @Override
    public String getLatitude() {
        return latitude;
    }

    @Override
    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    @JsonProperty("results")
    private void setResults(List<Result> results) {
        results.stream().findFirst().ifPresent(first -> setLatitudeAndLongitude(first.getGeometry()));
    }

    private void setLatitudeAndLongitude(Geometry geometry) {
        Optional.ofNullable(geometry).ifPresent(geo -> {
            final Optional<Location> maybeLoc = Optional.ofNullable(geo.getLocation());
            maybeLoc.ifPresent(location -> {
                setLatitude(String.valueOf(location.getLat()));
                setLongitude(String.valueOf(location.getLon()));
            });
        });
    }

    static class Result {
        private Geometry geometry;

        public Geometry getGeometry() {
            return geometry;
        }

        @JsonSetter("geometry")
        public void setGeometry(Geometry geometry) {
            this.geometry = geometry;
        }
    }

    static class Geometry {
        private Location location;

        public Location getLocation() {
            return location;
        }

        @JsonSetter("location")
        public void setLocation(Location location) {
            this.location = location;
        }
    }

    static class Location {
        private double lat;
        private double lon;

        public double getLat() {
            return lat;
        }

        @JsonSetter("lat")
        public void setLat(double lat) {
            this.lat = lat;
        }

        public double getLon() {
            return lon;
        }

        @JsonSetter("lng")
        public void setLon(double lon) {
            this.lon = lon;
        }
    }
}


