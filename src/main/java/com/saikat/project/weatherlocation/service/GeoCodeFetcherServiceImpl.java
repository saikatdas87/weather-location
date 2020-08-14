package com.saikat.project.weatherlocation.service;

import com.saikat.project.weatherlocation.exception.NoPropertiesDefinedException;
import com.saikat.project.weatherlocation.model.external.GeoCodeResponse;
import com.saikat.project.weatherlocation.model.external.LocationResponse;
import com.saikat.project.weatherlocation.properties.ApplicationProperties;
import com.saikat.project.weatherlocation.repo.ExternalServiceRepo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class GeoCodeFetcherServiceImpl implements GeoCodeFetcherService {

    Logger logger = LoggerFactory.getLogger(GeoCodeFetcherServiceImpl.class);

    private final ExternalServiceRepo externalServiceRepo;
    private final ApplicationProperties properties;
    private final Environment env;
    private final String GEOCODE_PROVIDER_API_KEY_PREFIX = "geocode.api.";

    @Autowired
    public GeoCodeFetcherServiceImpl(ExternalServiceRepo externalServiceRepo,
                                     ApplicationProperties properties,
                                     Environment env) {
        this.externalServiceRepo = externalServiceRepo;
        this.properties = properties;
        this.env = env;
    }


    /**
     * The method which calls all the configured geo code providers to fetch geo codes and returns them as list
     *
     * @param cityName
     * @return List<Optional < GeoCodeResponse>>
     */
    @Override
    public List<Optional<GeoCodeResponse>> fetchGeocodesForCity(String cityName) throws NoPropertiesDefinedException {

        List<String> geoCodeProviderList = geoCodeProviders();
        if (geoCodeProviderList.isEmpty() || geoCodeProviderList.stream().allMatch(provider -> provider.trim().equals(""))) {
            throw new NoPropertiesDefinedException("No location geo code providers configured in config file");
        }

        // For all the configured geo code providers fetch the location details and only take
        // latitude, longitude values.
        return geoCodeProviderList.stream().parallel().map(provider -> {
            // fetch the api from config
            final Optional<String> maybeProviderApi = Optional.ofNullable(env.getProperty(GEOCODE_PROVIDER_API_KEY_PREFIX + provider));
            // fetch the geo code for each api
            return maybeProviderApi.flatMap(api -> fetchLocationResponse(cityName, api, provider));
        }).collect(Collectors.toList());
    }

    /**
     * Method returns the configured location providers
     *
     * @return List<String>
     */
    private List<String> geoCodeProviders() {
        final Optional<String> maybeProviders = Optional.ofNullable(properties.getGeoCodeProviders());
        // The providers are separated by "," in the config
        return maybeProviders.map(providers -> Arrays.asList(providers.split(","))).orElse(Collections.emptyList());
    }

    /**
     * Method to see if API URI is configured in properties and call the API to get the location details. Catch exception in case
     * any and supress it as we seek at least one successful response.
     *
     * @param city     City for which we fetch latitude and longitude
     * @param api      External REST API address to be called
     * @param provider The external service provider name
     * @return Optional<GeoCodeResponse> - An Option of GeoCodeResponse
     */
    private Optional<GeoCodeResponse> fetchLocationResponse(final String city, final String api, final String provider) {
        // Call external service of a provider if configured
        return Optional.ofNullable(ApplicationProperties.geoCodeResponseMapperMap.get(provider))
                .flatMap(resType -> {
                    // In case any exception occurs log and ignore the exception
                    // If all api fails then we throw exception from controller
                    try {
                        logger.debug("Calling service for provider : " + provider);
                        GeoCodeResponse geoCodeResponse = processExternalResponse(resType, api, city);
                        if (null == geoCodeResponse ||
                                null == geoCodeResponse.getLongitude() ||
                                null == geoCodeResponse.getLatitude()) {
                            return Optional.empty();
                        }
                        return Optional.of(geoCodeResponse);
                    } catch (RestClientException re) {
                        logger.error("Exception Calling rest service for location : " + city, re);
                        return Optional.empty();
                    } catch (Exception e) {
                        logger.error("Exception occurred while fetching geocode for location : " + city, e);
                        return Optional.empty();
                    }
                });
    }

    /**
     * A method to call external repo and based on the response type (array or not) set the response
     *
     * @param response A object containing POJO where external REST response in mapped
     * @param api      URI of the external service
     * @param city     cityName for which we would be fetching location information
     * @return GeoCodeResponse Response object containing latitude and longitude
     */
    private GeoCodeResponse processExternalResponse(LocationResponse response, String api, String city) {
        if (response.isArray()) {
            GeoCodeResponse[] arrayResponse = externalServiceRepo.fetchFromExternalService(response.getArrayType(), api, city);
            // Always taking the first response of location
            return Arrays.stream(arrayResponse).findFirst().orElse(null);
        } else {
            return externalServiceRepo.fetchFromExternalService(response.getNonArrayType(), api, city);
        }
    }
}
