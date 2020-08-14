package com.saikat.project.weatherlocation.service;

import com.saikat.project.weatherlocation.exception.NoPropertiesDefinedException;
import com.saikat.project.weatherlocation.model.external.GeoCodeResponse;
import com.saikat.project.weatherlocation.model.external.GoogleResponse;
import com.saikat.project.weatherlocation.model.external.OSMResponse;
import com.saikat.project.weatherlocation.properties.ApplicationProperties;
import com.saikat.project.weatherlocation.repo.ExternalServiceRepo;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.core.env.Environment;
import org.springframework.web.client.RestClientException;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@SpringBootTest
public class GeoCodeFetcherServiceTest {

    @MockBean
    private ExternalServiceRepo externalServiceRepo;

    @MockBean
    private ApplicationProperties properties;

    @Mock
    private Environment env;


    private GeoCodeFetcherService geoCodeFetcherService;


    @BeforeEach
    public void setUp() {
        geoCodeFetcherService = new GeoCodeFetcherServiceImpl(externalServiceRepo, properties, env);
    }


    @Test
    public void throwsExceptionIfNoProvidersConfigured() {
        when(properties.getGeoCodeProviders()).thenReturn(null);
        try {
            geoCodeFetcherService.fetchGeocodesForCity("London");
            fail("Should have not reached");
        } catch (NoPropertiesDefinedException e) {
            assertEquals(e.getMessage(), "No location geo code providers configured in config file");
        }
    }

    @Test
    public void throwsExceptionIfEmptyProvidersConfigured() {
        when(properties.getGeoCodeProviders()).thenReturn(" ");
        try {
            geoCodeFetcherService.fetchGeocodesForCity("London");
            fail("Should have not reached");
        } catch (NoPropertiesDefinedException e) {
            assertEquals(e.getMessage(), "No location geo code providers configured in config file");
        }
    }

    @Test
    public void returnsEmptyResponseListIfNoURI() {
        when(properties.getGeoCodeProviders()).thenReturn("google");
        when(env.getProperty("geocode.api.google")).thenReturn(null);
        try {
            List<Optional<GeoCodeResponse>> res = geoCodeFetcherService.fetchGeocodesForCity("London");
            List<GeoCodeResponse> response = res.stream().filter(Optional::isPresent).map(Optional::get).collect(Collectors.toList());
            assertTrue(response.isEmpty());
        } catch (NoPropertiesDefinedException e) {
            fail("Should not have reached");
        }
    }

    @Test
    public void emptyResponseListIfExternalServiceThrowsException() {
        when(properties.getGeoCodeProviders()).thenReturn("google");
        when(env.getProperty("geocode.api.google")).thenReturn("https://test.api");
        try {

            when(externalServiceRepo.fetchFromExternalService(Mockito.any(),
                    Mockito.eq("https://test.api"), Mockito.eq("London")))
                    .thenThrow(new RestClientException("No valid URI"));
            List<Optional<GeoCodeResponse>> res = geoCodeFetcherService.fetchGeocodesForCity("London");
            List<GeoCodeResponse> response = res.stream().filter(Optional::isPresent).map(Optional::get).collect(Collectors.toList());
            assertTrue(response.isEmpty());
        } catch (NoPropertiesDefinedException e) {
            fail("Should not have reached");
        }
    }

    @Test
    public void emptyResponseListIfExternalServiceFailedToMapData() {
        when(properties.getGeoCodeProviders()).thenReturn("google");
        when(env.getProperty("geocode.api.google")).thenReturn("https://test.api");
        try {

            when(externalServiceRepo.fetchFromExternalService(Mockito.any(),
                    Mockito.eq("https://test.api"), Mockito.eq("London")))
                    .thenReturn(null);
            List<Optional<GeoCodeResponse>> res = geoCodeFetcherService.fetchGeocodesForCity("London");
            List<GeoCodeResponse> response = res.stream().filter(Optional::isPresent).map(Optional::get).collect(Collectors.toList());
            assertTrue(response.isEmpty());
        } catch (NoPropertiesDefinedException e) {
            fail("Should not have reached");
        }
    }

    @Test
    public void successResponse() {
        when(properties.getGeoCodeProviders()).thenReturn("google");
        when(env.getProperty("geocode.api.google")).thenReturn("https://test.api");
        final GoogleResponse geoResponse = new GoogleResponse();
        geoResponse.setLongitude("13.4");
        geoResponse.setLatitude("33.444324324");
        try {

            when(externalServiceRepo.fetchFromExternalService(Mockito.any(),
                    Mockito.eq("https://test.api"), Mockito.eq("London")))
                    .thenReturn(geoResponse);
            List<Optional<GeoCodeResponse>> res = geoCodeFetcherService.fetchGeocodesForCity("London");
            List<GeoCodeResponse> response = res.stream().filter(Optional::isPresent).map(Optional::get).collect(Collectors.toList());
            assertFalse(response.isEmpty());
        } catch (NoPropertiesDefinedException e) {
            fail("Should not have reached");
        }
    }

    @Test
    public void successResponseMultipleProviderOneFails() {
        when(properties.getGeoCodeProviders()).thenReturn("google,osm");
        when(env.getProperty("geocode.api.google")).thenReturn("https://test.api");
        when(env.getProperty("geocode.api.osm")).thenReturn("https://test.api.fail");
        final GoogleResponse geoResponse = new GoogleResponse();
        geoResponse.setLongitude("13.4");
        geoResponse.setLatitude("33.444324324");
        try {
            when(externalServiceRepo.fetchFromExternalService(Mockito.any(),
                    Mockito.eq("https://test.api"), Mockito.eq("London")))
                    .thenReturn(geoResponse);
            when(externalServiceRepo.fetchFromExternalService(Mockito.any(),
                    Mockito.eq("https://test.api.fail"), Mockito.eq("London")))
                    .thenThrow(new RestClientException("No valid URI"));
            List<Optional<GeoCodeResponse>> res = geoCodeFetcherService.fetchGeocodesForCity("London");
            List<GeoCodeResponse> response = res.stream().filter(Optional::isPresent).map(Optional::get).collect(Collectors.toList());
            assertEquals(response.size(), 1);
        } catch (NoPropertiesDefinedException e) {
            fail("Should not have reached");
        }
    }

    @Test
    public void successResponseMultipleProvider() {
        when(properties.getGeoCodeProviders()).thenReturn("google,osm");
        when(env.getProperty("geocode.api.google")).thenReturn("https://test.api");
        when(env.getProperty("geocode.api.osm")).thenReturn("https://test.api.fail");
        final GoogleResponse geoResponse = new GoogleResponse();
        geoResponse.setLongitude("13.4");
        geoResponse.setLatitude("33.444324324");
        final OSMResponse osmResponse = new OSMResponse();
        osmResponse.setLongitude("13.41312");
        osmResponse.setLatitude("33.441124");

        try {
            when(externalServiceRepo.fetchFromExternalService(Mockito.any(),
                    Mockito.eq("https://test.api"), Mockito.eq("London")))
                    .thenReturn(geoResponse);
            when(externalServiceRepo.fetchFromExternalService(Mockito.any(),
                    Mockito.eq("https://test.api.fail"), Mockito.eq("London")))
                    .thenReturn(new OSMResponse[]{osmResponse});
            List<Optional<GeoCodeResponse>> res = geoCodeFetcherService.fetchGeocodesForCity("London");
            List<GeoCodeResponse> response = res.stream().filter(Optional::isPresent).map(Optional::get).collect(Collectors.toList());
            assertEquals(response.size(), 2);
        } catch (NoPropertiesDefinedException e) {
            fail("Should not have reached");
        }
    }

}
