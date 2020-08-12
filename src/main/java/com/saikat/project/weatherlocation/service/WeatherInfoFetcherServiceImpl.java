package com.saikat.project.weatherlocation.service;

import com.saikat.project.weatherlocation.exception.NoPropertiesDefinedException;
import com.saikat.project.weatherlocation.exception.ResourceNotFoundException;
import com.saikat.project.weatherlocation.model.external.TempParam;
import com.saikat.project.weatherlocation.properties.ApplicationProperties;
import com.saikat.project.weatherlocation.repo.ExternalServiceRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;

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
    public TempParam fetchCityWeather(String cityName) throws NoPropertiesDefinedException, ResourceNotFoundException {
        final Optional<String> maybeWeatherApiURI = Optional.ofNullable(properties.getCurrentWeatherApiURL());
        return maybeWeatherApiURI.map(uri -> {
            try {
                return externalServiceRepo.fetchFromExternalService(TempParam.class, uri, cityName);
            } catch (RestClientException re) {
                throw new ResourceNotFoundException("Exception fetching weather for city " + cityName + " : " + re);
            } catch (Exception e) {
                throw new ResourceNotFoundException("Something wrong fetching weather for city " + cityName + " : " + e);
            }
        }).orElseThrow(() -> new NoPropertiesDefinedException("No uri defined for weather fetching URI"));
    }

}
