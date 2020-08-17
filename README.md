# weather-location

> A Backend application with an API to calculate center point for a city and show current weather for the city.
E.g. Search "London" via the API and it will return geographical center point and current weather for the city.

## Used technologies

* [Java 8](https://www.oracle.com/technetwork/java/javase/overview/java8-2100321.html)
* [Spring Boot](https://docs.spring.io/spring-boot/docs/current/reference/htmlsingle/)
* [Maven](https://maven.apache.org/)

### Prerequisites

* [JDK](http://www.oracle.com/technetwork/java/javase/downloads/index.html) (version 1.8 or higher)

### Let's get started,

* Clone this repository.
* Install maven dependencies
* Configure all the API urls, geo code providers in the below path.

```
    ├── src/main/resources/
    │     ├── application.properties
```

* Add API keys in place of `[]` in the properties files.
* Create/Register a response mapper POJO class in case new providers added in `ApplicationProperties.geoCodeResponseMapperMap`.
* When running the app, the application can be accessed with below default urls

```
    http://localhost:8080/api/weather-location-info/{city}
```

   To run the application please follow below commands 

```
    mvn spring-boot:run
```

Can be ran directly from IntelliJ IDEA by going to `com.saikat.project.weatherlocation.WeatherLocationApplication` and clicking run.

Build Spring Boot Project with Maven

   ```maven package```

Or
    
    mvn install / mvn clean install

* To run tests

``` mvn test```

### REST API 
    
    
    api/weather-location-info/{city} [city is input as path variable e.g. London]
    
    Response JSON :
    {
        "weatherInfo": {
            "temperature": 18.09,
            "unit": "°C"
        },
        "locationInfo": {
            "latitude": "51.5073509",
            "longitude": "-0.1277583"
        }
    }
    

#### Note: 
The app only supports showing temperature in °C.
