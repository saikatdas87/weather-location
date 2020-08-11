package com.saikat.project.weatherlocation.service;

import com.saikat.project.weatherlocation.exception.NoPropertiesDefinedException;
import com.saikat.project.weatherlocation.model.external.TempParam;

public interface WeatherInfoFetcherService {
    TempParam fetchCityWeather(String cityName) throws NoPropertiesDefinedException;
}
