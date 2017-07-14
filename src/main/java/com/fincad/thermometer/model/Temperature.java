package com.fincad.thermometer.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

/**
 * Created by roberto on 14/07/17.
 */
@Entity
public class Temperature {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String location;
    private Double temperature;

    Temperature() { // jpa only
    }

    public Temperature(String location, Double temperature) {
        this.location = location;
        this.temperature = temperature;
    }

    public String getLocation() {
        return location;
    }

    public Double getTemperature() {
        return temperature;
    }
}
