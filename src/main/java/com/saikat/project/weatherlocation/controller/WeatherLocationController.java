package com.saikat.project.weatherlocation.controller;

import com.saikat.project.weatherlocation.exception.InvalidDataException;
import com.saikat.project.weatherlocation.exception.NoPropertiesDefinedException;
import com.saikat.project.weatherlocation.exception.ResourceNotFoundException;
import com.saikat.project.weatherlocation.model.LocationWeatherInfo;
import com.saikat.project.weatherlocation.model.external.GeoCodeResponse;
import com.saikat.project.weatherlocation.model.external.TempParam;
import com.saikat.project.weatherlocation.service.GeoCodeFetcherService;
import com.saikat.project.weatherlocation.service.WeatherInfoFetcherService;
import com.saikat.project.weatherlocation.validation.CityNameValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
public class WeatherLocationController {
    private final WeatherInfoFetcherService weatherInfoFetcherService;
    private final CityNameValidator validator;
    private final GeoCodeFetcherService geoCodeService;

    @Autowired
    public WeatherLocationController(WeatherInfoFetcherService weatherInfoFetcherService,
                                     CityNameValidator validator,
                                     GeoCodeFetcherService geoCodeService) {
        this.weatherInfoFetcherService = weatherInfoFetcherService;
        this.validator = validator;
        this.geoCodeService = geoCodeService;
    }

    /**
     * The API GET method for fetching location and weather info for a city
     *
     * @param city city name for which we find infos (passed as path variable)
     * @return LocationWeatherInfo an object containing weather and location info
     * @throws NoPropertiesDefinedException
     * @throws InvalidDataException
     */
    @GetMapping(value = {"/api/weather-location-info/", "/api/weather-location-info/{city}"})
    public LocationWeatherInfo getWeatherAndLocationInfo(@PathVariable Optional<String> city)
            throws NoPropertiesDefinedException, InvalidDataException {
        final String cityName = city.orElse("");
        validator.validateCity(cityName);

        List<Optional<GeoCodeResponse>> maybeGeoCodes = geoCodeService.fetchGeocodesForCity(cityName);
        List<GeoCodeResponse> geoCodes = maybeGeoCodes.stream()
                .filter(Optional::isPresent)
                .map(Optional::get)
                .collect(Collectors.toList());
        if (geoCodes.isEmpty()) {
            throw new ResourceNotFoundException("Location details could not be found for : " + cityName);
        }

        TempParam weather = weatherInfoFetcherService.fetchCityWeather(cityName);
        return buildResponseInfo(geoCodes, weather);
    }

    /**
     * Build the response data with geo code info and weather info
     *
     * @param geoCodes
     * @param weather
     * @return LocationWeatherInfo
     */
    private LocationWeatherInfo buildResponseInfo(List<GeoCodeResponse> geoCodes, TempParam weather) {
        return LocationWeatherInfo.buildLocationWeatherInfo(geoCodes, weather)
                .orElseThrow(() -> new InvalidDataException("Could not fetch details"));
    }

}
