package com.saikat.project.weatherlocation.service;

import com.saikat.project.weatherlocation.exception.NoPropertiesDefinedException;
import com.saikat.project.weatherlocation.exception.ResourceNotFoundException;
import com.saikat.project.weatherlocation.model.external.TempParam;
import com.saikat.project.weatherlocation.properties.ApplicationProperties;
import com.saikat.project.weatherlocation.repo.ExternalServiceRepo;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.web.client.RestClientException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;
import static org.mockito.Mockito.when;

@SpringBootTest
public class WeatherInfoFetcherServiceTest {
    @MockBean
    private ExternalServiceRepo externalServiceRepo;

    @MockBean
    private ApplicationProperties properties;

    private WeatherInfoFetcherService weatherInfoFetcherService;

    @BeforeEach
    public void setUp() {
        weatherInfoFetcherService = new WeatherInfoFetcherServiceImpl(externalServiceRepo, properties);
    }

    @Test
    public void throwsExceptionIfNoWeatherAPIConfigured() {
        when(properties.getCurrentWeatherApiURL()).thenReturn(null);
        try {
            weatherInfoFetcherService.fetchCityWeather("London");
            fail("Should have not reached");
        } catch (NoPropertiesDefinedException e) {
            assertEquals(e.getMessage(), "No uri defined for weather fetching service in configuration");
        }
    }

    @Test
    public void throwsExceptionIfNoWeatherAPIFails() throws NoPropertiesDefinedException {
        when(properties.getCurrentWeatherApiURL()).thenReturn("https://weather.api");
        when(externalServiceRepo.fetchFromExternalService(Mockito.any(),
                Mockito.eq("https://weather.api"), Mockito.eq("London")))
                .thenThrow(new RestClientException("Invalid URI"));
        try {
            weatherInfoFetcherService.fetchCityWeather("London");
            fail("Should have not reached");
        } catch (ResourceNotFoundException e) {
            assertEquals(e.getMessage(), "Exception fetching weather for city London : Invalid URI");
        }
    }

    @Test
    public void successResponse() {
        when(properties.getCurrentWeatherApiURL()).thenReturn("https://weather.api");
        TempParam tempParam = new TempParam();
        tempParam.setTemp(23.3);
        when(externalServiceRepo.fetchFromExternalService(Mockito.any(),
                Mockito.eq("https://weather.api"), Mockito.eq("London")))
                .thenReturn(tempParam);
        try {
            TempParam res = weatherInfoFetcherService.fetchCityWeather("London");
            assertEquals(res.getTemp(), 23.3);
        } catch (Exception e) {
            fail("Should have not reached");
        }
    }
}
