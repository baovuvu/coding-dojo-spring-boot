package com.assignment.spring;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest
public class WeatherControllerTest {

    @Autowired
    private WeatherController weatherController;
    @Autowired
    private WeatherRepository weatherRepository;

    private final String city = "Amsterdam";

    @Test
    public void getWeather() {
        final WeatherEntity weatherEntityLast = getLastWeatherEntity();
        final WeatherEntity weatherEntity = Optional.ofNullable(weatherController.getWeather(city).getBody()).orElse(new WeatherEntity());
        final WeatherEntity weatherEntityInDb = getLastWeatherEntity();

        assertTrue(weatherEntity.getId() > 0 && weatherEntity.getId() > weatherEntityLast.getId());
        assertEquals(weatherEntity.getId(), weatherEntityInDb.getId());
        assertEquals(weatherEntity.getCity(), weatherEntityInDb.getCity());
        assertEquals(weatherEntity.getCountry(), weatherEntityInDb.getCountry());
        assertEquals(weatherEntity.getTemperature(), weatherEntityInDb.getTemperature());
    }

    @Test
    public void getWeatherNotFound() {
        assertEquals(HttpStatus.NOT_FOUND, weatherController.getWeather(city + "XXX").getStatusCode());
    }

    private WeatherEntity getLastWeatherEntity(){
        return StreamSupport.stream(weatherRepository.findAll().spliterator(), false)
            .max(Comparator.comparing(WeatherEntity::getId))
            .orElse(new WeatherEntity());
    }
}