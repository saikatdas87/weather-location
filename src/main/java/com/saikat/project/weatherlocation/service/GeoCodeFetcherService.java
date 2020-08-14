package com.saikat.project.weatherlocation.service;

import com.saikat.project.weatherlocation.exception.NoPropertiesDefinedException;
import com.saikat.project.weatherlocation.model.external.GeoCodeResponse;

import java.util.List;
import java.util.Optional;

public interface GeoCodeFetcherService {
    List<Optional<GeoCodeResponse>> fetchGeocodesForCity(String cityName) throws NoPropertiesDefinedException;
}
