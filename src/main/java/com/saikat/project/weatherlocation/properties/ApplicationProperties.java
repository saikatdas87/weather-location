package com.saikat.project.weatherlocation.properties;

import com.saikat.project.weatherlocation.model.external.GoogleResponse;
import com.saikat.project.weatherlocation.model.external.HereResponse;
import com.saikat.project.weatherlocation.model.external.LocationResponse;
import com.saikat.project.weatherlocation.model.external.OSMResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

/**
 * The POJO class to hold all configured property values
 * <p>
 * Created by Saikat Das
 */

@Component
@PropertySource("classpath:application.properties")
public class ApplicationProperties {

    // For any new provider this map should hold response mapper class (external JSON to POJO) for the provider
    public static Map<String, LocationResponse> geoCodeResponseMapperMap =
            new HashMap<String, LocationResponse>() {{
                put("osm", new LocationResponse(true, OSMResponse[].class, null));
                put("here", new LocationResponse(false, null, HereResponse.class));
                put("google", new LocationResponse(false, null, GoogleResponse.class));
            }};


    @Value("${weather.monitor.api.openweather}")
    private String currentWeatherApiURL;

    @Value("${geocode.providers}")
    private String geoCodeProviders;

    public String getCurrentWeatherApiURL() {
        return currentWeatherApiURL;
    }

    public void setCurrentWeatherApiURL(String currentWeatherApiURL) {
        this.currentWeatherApiURL = currentWeatherApiURL;
    }

    public String getGeoCodeProviders() {
        return geoCodeProviders;
    }

    public void setGeoCodeProviders(String geoCodeProviders) {
        this.geoCodeProviders = geoCodeProviders;
    }
}
