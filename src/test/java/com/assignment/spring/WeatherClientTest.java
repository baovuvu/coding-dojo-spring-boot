package com.assignment.spring;

import com.assignment.spring.api.Main;
import com.assignment.spring.api.Sys;
import com.assignment.spring.api.WeatherResponse;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest
public class WeatherClientTest {

    @Autowired
    private WeatherClient weatherClient;

    private final String city = "Amsterdam";
    private final String country = "NL";

    @Test
    public void getWeather() {
        final WeatherEntity weatherEntity = weatherClient.getWeather(city).orElse(new WeatherEntity());
        assertEquals(city, weatherEntity.getCity());
        assertEquals(country, weatherEntity.getCountry());
        assertTrue(weatherEntity.getTemperature() > 0);
    }

    @Test
    public void getWeatherNotFound() {
        assertTrue(weatherClient.getWeather(city + "XXX").isEmpty());
    }

    @Test
    public void mapper() {
        final Sys sys = new Sys();
        sys.setCountry(country);
        final Main main = new Main();
        main.setTemp(270.12);
        final WeatherResponse response = new WeatherResponse();
        response.setName(city);
        response.setSys(sys);
        response.setMain(main);
        final WeatherEntity weatherEntity = weatherClient.mapper(response);
        assertEquals(response.getName(), weatherEntity.getCity());
        assertEquals(response.getSys().getCountry(), weatherEntity.getCountry());
        assertEquals(response.getMain().getTemp(), weatherEntity.getTemperature());
    }
}