package com.saikat.project.weatherlocation.service;

import com.saikat.project.weatherlocation.model.external.GeoCodeResponse;

import java.util.List;
import java.util.Optional;

public interface GeocodeFetcherService {
    public List<Optional<GeoCodeResponse>> fetchGeocodesForCity(String cityName);
}
