package com.saikat.project.weatherlocation.service;

import com.saikat.project.weatherlocation.exception.NoPropertiesDefinedException;
import com.saikat.project.weatherlocation.model.external.TempParam;
import com.saikat.project.weatherlocation.properties.ApplicationProperties;
import com.saikat.project.weatherlocation.repo.ExternalServiceRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class WeatherInfoFetcherServiceImpl implements WeatherInfoFetcherService {
    private final ExternalServiceRepo externalServiceRepo;
    private final ApplicationProperties properties;

    @Autowired
    public WeatherInfoFetcherServiceImpl(ExternalServiceRepo externalServiceRepo, ApplicationProperties properties) {
        this.externalServiceRepo = externalServiceRepo;
        this.properties = properties;
    }

    @Override
    public TempParam fetchCityWeather(String cityName) throws NoPropertiesDefinedException {
        final Optional<String> maybeWeatherApiURI = Optional.ofNullable(properties.getCurrentWeatherApiURL());
        return maybeWeatherApiURI.map(uri -> externalServiceRepo.fetchFromExternalService(TempParam.class, uri, cityName))
                .orElseThrow(() -> new NoPropertiesDefinedException("No uri defined for weather fetching URI"));
    }

}
