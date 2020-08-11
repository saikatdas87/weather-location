package com.saikat.project.weatherlocation.controller;

import com.saikat.project.weatherlocation.exception.InvalidDataException;
import com.saikat.project.weatherlocation.exception.NoPropertiesDefinedException;
import com.saikat.project.weatherlocation.model.external.GeoCodeResponse;
import com.saikat.project.weatherlocation.model.external.TempParam;
import com.saikat.project.weatherlocation.service.GeocodeFetcherService;
import com.saikat.project.weatherlocation.service.WeatherInfoFetcherService;
import com.saikat.project.weatherlocation.validation.CityNameValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Optional;

@RestController
public class WeatherLocationController {
    private final WeatherInfoFetcherService weatherInfoFetcherService;
    private final CityNameValidator validator;
    private final GeocodeFetcherService geoCodeService;

    @Autowired
    public WeatherLocationController(WeatherInfoFetcherService weatherInfoFetcherService,
                                     CityNameValidator validator,
                                     GeocodeFetcherService geoCodeService) {
        this.weatherInfoFetcherService = weatherInfoFetcherService;
        this.validator = validator;
        this.geoCodeService = geoCodeService;
    }

    @GetMapping(value = {"/api/weather-location-info/", "/api/weather-location-info/{city}"})
    public String getWeatherAndLocationInfo(@PathVariable Optional<String> city)
            throws NoPropertiesDefinedException, InvalidDataException {
        final String cityName = city.orElse("");
        validator.validateCity(cityName);



        List<Optional<GeoCodeResponse>> res = geoCodeService.fetchGeocodesForCity(cityName);
        res.forEach( r -> r.ifPresent(x -> System.out.println(x.getLatitude())));


        TempParam p = weatherInfoFetcherService.fetchCityWeather(cityName);
        System.out.println(" Temp : " + p.getTemp());
        return "0 " + city;
    }

}
