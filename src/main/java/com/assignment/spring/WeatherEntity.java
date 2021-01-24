package com.assignment.spring;

import javax.persistence.*;

@Entity
@Table(name = "weather")
public class WeatherEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;

    private String city;

    private String country;

    @Column(columnDefinition = "NUMERIC (5, 2)")
    private Double temperature;

    public Integer getId() {
        return id;
    }

    public String getCity() {
        return city;
    }

    public WeatherEntity setCity(String city) {
        this.city = city;
        return this;
    }

    public String getCountry() {
        return country;
    }

    public WeatherEntity setCountry(String country) {
        this.country = country;
        return this;
    }

    public Double getTemperature() {
        return temperature;
    }

    public WeatherEntity setTemperature(Double temperature) {
        this.temperature = temperature;
        return this;
    }
}
