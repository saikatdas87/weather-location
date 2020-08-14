package com.saikat.project.weatherlocation.controller;

import com.saikat.project.weatherlocation.exception.InvalidDataException;
import com.saikat.project.weatherlocation.model.external.GeoCodeResponse;
import com.saikat.project.weatherlocation.model.external.GoogleResponse;
import com.saikat.project.weatherlocation.model.external.TempParam;
import com.saikat.project.weatherlocation.service.GeoCodeFetcherService;
import com.saikat.project.weatherlocation.service.WeatherInfoFetcherService;
import com.saikat.project.weatherlocation.validation.CityNameValidator;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@WebMvcTest(controllers = WeatherLocationController.class)
public class WeatherLocationControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private WeatherInfoFetcherService weatherInfoFetcherService;

    @MockBean
    private CityNameValidator validator;

    @MockBean
    private GeoCodeFetcherService geoCodeService;

    @Test
    public void whenEmptyInput() throws Exception {
        doThrow(new InvalidDataException("Empty")).when(validator).validateCity("");
        mockMvc.perform(get("/api/weather-location-info/")
                .contentType("application/json"))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void whenNoLocationFound() throws Exception {
        doNothing().when(validator).validateCity("London");
        when(geoCodeService.fetchGeocodesForCity("London")).thenReturn(Collections.emptyList());
        mockMvc.perform(get("/api/weather-location-info/London")
                .contentType("application/json"))
                .andExpect(status().isNotFound());
    }

    @Test
    public void whenValidInput() throws Exception {
        doNothing().when(validator).validateCity("London");
        TempParam tempParam = new TempParam();
        tempParam.setTemp(23.3);
        when(weatherInfoFetcherService.fetchCityWeather("London")).thenReturn(tempParam);
        final GoogleResponse geoResponse = new GoogleResponse();
        geoResponse.setLongitude("13.4");
        geoResponse.setLatitude("33.444324324");
        List<Optional<GeoCodeResponse>> geoCodes = new ArrayList<Optional<GeoCodeResponse>>();
        geoCodes.add(Optional.of(geoResponse));
        when(geoCodeService.fetchGeocodesForCity("London")).thenReturn(geoCodes);
        MvcResult mvcResult = mockMvc.perform(get("/api/weather-location-info/London")
                .contentType("application/json"))
                .andExpect(status().isOk())
                .andExpect(content()
                        .contentTypeCompatibleWith(MediaType.APPLICATION_JSON)).andReturn();
        String actualResponseBody = mvcResult.getResponse().getContentAsString();
        assertTrue(actualResponseBody.contains("13.4"));
        assertTrue(actualResponseBody.contains("23.3"));
        assertTrue(actualResponseBody.contains("33.444324324"));
    }
}
