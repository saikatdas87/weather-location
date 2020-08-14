package com.saikat.project.weatherlocation.repo;

import com.saikat.project.weatherlocation.model.external.TempParam;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Matchers;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.*;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.net.URI;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@SpringBootTest
public class ExternalServiceRepoTest {

    @MockBean
    private RestTemplateBuilder restTemplateBuilder;

    private ExternalServiceRepo repo;

    private RestTemplate restTemplate;
    private final String URI = "http://api.openweathermap.org/data/2.5/weather?q={city}&APPID=xxxxxxxx&units=metric";

    @BeforeEach
    public void setUp() {
        repo = new ExternalServiceRepoImpl(restTemplateBuilder);
        restTemplate = mock(RestTemplate.class);
        when(restTemplateBuilder.build()).thenReturn(restTemplate);
    }

    @Test
    public void successfulRestCall() {
        TempParam temp = new TempParam();
        temp.setTemp(32);

        ResponseEntity<TempParam> responseEntity = new ResponseEntity<>(temp, HttpStatus.ACCEPTED);
        when(restTemplate.exchange(Mockito.<URI>any(), Mockito.eq(HttpMethod.GET),
                Mockito.any(), Mockito.<Class<TempParam>>any())).thenReturn(responseEntity);
        TempParam response = repo.fetchFromExternalService(TempParam.class, URI, "London");
        assertEquals(response.getTemp(), temp.getTemp());
    }

    @Test
    public void externalServiceThrowsException() {
        when(restTemplate.exchange(Mockito.<URI>any(), Mockito.eq(HttpMethod.GET),
                Mockito.any(), Mockito.<Class<TempParam>>any())).thenThrow(new RestClientException("Invalid URL"));

        try {
            repo.fetchFromExternalService(TempParam.class, URI, "London");
            fail("Should have failed ");
        } catch (RestClientException re) {
            assertEquals(re.getMessage(), "Invalid URL");
        }

    }
}
