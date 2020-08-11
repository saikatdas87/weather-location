package com.saikat.project.weatherlocation.validation;

import com.saikat.project.weatherlocation.exception.InvalidDataException;
import org.springframework.stereotype.Service;

@Service
public class CityNameValidatorImpl implements CityNameValidator {
    @Override
    public void validateCity(String cityName) {
        validateNullOrEmpty(cityName);
    }

    private void validateNullOrEmpty(String city) {
        if (city.trim().equals("")) {
            throw new InvalidDataException("No city name provided");
        }
    }
}
