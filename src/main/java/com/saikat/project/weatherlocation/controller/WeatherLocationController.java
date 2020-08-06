package com.saikat.project.weatherlocation.controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;


@RestController
public class WeatherLocationController {

    @GetMapping("/api/weather-location-info/{city}")
    public String getWeatherAndLocationInfo( @PathVariable String city) {

        return "0";
    }

}
