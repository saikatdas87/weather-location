package com.saikat.project.weatherlocation.service;

import com.saikat.project.weatherlocation.exception.NoPropertiesDefinedException;
import com.saikat.project.weatherlocation.exception.ResourceNotFoundException;
import com.saikat.project.weatherlocation.model.external.TempParam;
import com.saikat.project.weatherlocation.properties.ApplicationProperties;
import com.saikat.project.weatherlocation.repo.ExternalServiceRepo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;

import java.util.Optional;

@Service
public class WeatherInfoFetcherServiceImpl implements WeatherInfoFetcherService {

    Logger logger = LoggerFactory.getLogger(WeatherInfoFetcherServiceImpl.class);

    private final ExternalServiceRepo externalServiceRepo;
    private final ApplicationProperties properties;

    @Autowired
    public WeatherInfoFetcherServiceImpl(ExternalServiceRepo externalServiceRepo, ApplicationProperties properties) {
        this.externalServiceRepo = externalServiceRepo;
        this.properties = properties;
    }

    /**
     * The serive method to fetch weather information
     *
     * @param cityName Name of the city
     * @return TempParam
     * @throws NoPropertiesDefinedException
     * @throws ResourceNotFoundException
     */
    @Override
    public TempParam fetchCityWeather(String cityName) throws NoPropertiesDefinedException, ResourceNotFoundException {
        final Optional<String> maybeWeatherApiURI = Optional.ofNullable(properties.getCurrentWeatherApiURL());
        return maybeWeatherApiURI.map(uri -> {
            if (uri.trim().equals("")) {
                throw new ResourceNotFoundException("No uri defined for weather fetching service in configuration");
            }else {
                try {
                    return externalServiceRepo.fetchFromExternalService(TempParam.class, uri, cityName);
                } catch (RestClientException re) {
                    logger.error("RestClientException fetching weather for city : " + cityName, re);
                    throw new ResourceNotFoundException("Exception fetching weather for city " + cityName + " : " + re.getMessage());
                } catch (Exception e) {
                    logger.error("Some Exception occurred fetching weather for city : " + cityName, e);
                    throw new ResourceNotFoundException("Something wrong fetching weather for city " + cityName + " : " + e.getMessage());
                }
            }

        }).orElseThrow(() -> new NoPropertiesDefinedException("No uri defined for weather fetching service in configuration"));
    }

}
