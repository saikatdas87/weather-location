package com.saikat.project.weatherlocation.properties;

import com.saikat.project.weatherlocation.model.external.GeoCodeResponse;
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

    public static Map<String, LocationResponse> geoCodeResponseMapperMap =
            new HashMap<String, LocationResponse>() {{
                put("osm", new LocationResponse(true, OSMResponse[].class, null));
                put("here", new LocationResponse(false, null, HereResponse.class));
                //put("google")
            }};

    @Value("${geocode.api.google}")
    private String googleMapApi;

    @Value("${geocode.api.osm}")
    private String openStreetMapApi;

    @Value("${geocode.api.here}")
    private String hereMapApi;

    @Value("${weather.monitor.api.openweather}")
    private String currentWeatherApiURL;

    @Value("${geocode.providers}")
    private String geoCodeProviders;

    public String getGoogleMapApi() {
        return googleMapApi;
    }

    public void setGoogleMapApi(String googleMapApi) {
        this.googleMapApi = googleMapApi;
    }

    public String getOpenStreetMapApi() {
        return openStreetMapApi;
    }

    public void setOpenStreetMapApi(String openStreetMapApi) {
        this.openStreetMapApi = openStreetMapApi;
    }

    public String getHereMapApi() {
        return hereMapApi;
    }

    public void setHereMapApi(String hereMapApi) {
        this.hereMapApi = hereMapApi;
    }

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
