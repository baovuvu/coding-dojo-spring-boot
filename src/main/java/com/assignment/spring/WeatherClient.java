package com.assignment.spring;

import com.assignment.spring.api.WeatherResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.util.Optional;

@Component
public class WeatherClient {

    private final RestTemplate restTemplate;
    private final String urlTemplate;
    private final String urlParam;

    @Autowired
    public WeatherClient(RestTemplate restTemplate, Environment env) {
        this.restTemplate = restTemplate;
        this.urlTemplate = env.getRequiredProperty("api.url.template");
        this.urlParam = env.getRequiredProperty("api.url.param");
    }

    public Optional<WeatherEntity> getWeather(String city) {
        final String url = urlTemplate.replace(urlParam, city);
        try {
            final ResponseEntity<WeatherResponse> response = restTemplate.getForEntity(url, WeatherResponse.class);
            return Optional.ofNullable(response.getBody()).map(this::mapper);
        } catch (HttpClientErrorException.NotFound exception) {
            return Optional.empty();
        }
    }

    WeatherEntity mapper(WeatherResponse response) {
        return new WeatherEntity()
            .setCity(response.getName())
            .setCountry(response.getSys().getCountry())
            .setTemperature(response.getMain().getTemp());
    }
}
