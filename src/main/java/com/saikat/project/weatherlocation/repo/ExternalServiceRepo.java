package com.saikat.project.weatherlocation.repo;

import org.springframework.web.client.RestClientException;

public interface ExternalServiceRepo<T> {
    T fetchFromExternalService(Class<T> responseType, String url, Object... uriParams) throws RestClientException;
}
