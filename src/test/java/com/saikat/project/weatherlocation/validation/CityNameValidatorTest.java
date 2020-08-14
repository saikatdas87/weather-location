package com.saikat.project.weatherlocation.validation;

import com.saikat.project.weatherlocation.exception.InvalidDataException;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class CityNameValidatorTest {

    private final CityNameValidator validator = new CityNameValidatorImpl();

    @Test
    public void testSuccess() {
        try {
            validator.validateCity("Kolkata");
        } catch (Exception e) {
            fail("Should not have reached");
        }
    }

    @Test
    public void failsIfEmptyCity() {

        try {
            validator.validateCity("");
            fail("Should have thrown exception");
        } catch (Exception e) {
            assertTrue(e instanceof InvalidDataException);
            assertEquals(e.getMessage(), "No city name provided");
        }
    }

    @Test
    public void shouldFailForSpaces() {
        try {
            validator.validateCity(" ");
            fail("Should have thrown exception");
        } catch (Exception e) {
            assertTrue(e instanceof InvalidDataException);
            assertEquals(e.getMessage(), "No city name provided");
        }
    }

}
