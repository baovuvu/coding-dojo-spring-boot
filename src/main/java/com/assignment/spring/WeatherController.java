package com.assignment.spring;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/weather")
public class WeatherController {

    private final WeatherClient weatherClient;
    private final WeatherRepository weatherRepository;

    @Autowired
    public WeatherController(WeatherClient weatherClient, WeatherRepository weatherRepository) {
        this.weatherClient = weatherClient;
        this.weatherRepository = weatherRepository;
    }

    @RequestMapping(method = RequestMethod.GET)
    public ResponseEntity<WeatherEntity> getWeather(@RequestParam String city) {
        return weatherClient.getWeather(city)
            .map(weatherRepository::save)
            .map(weatherEntity -> new ResponseEntity(weatherEntity, HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

}
