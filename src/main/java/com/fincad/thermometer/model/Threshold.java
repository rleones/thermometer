package com.fincad.thermometer.model;

import com.fincad.thermometer.model.enums.Direction;
import com.fincad.thermometer.model.enums.TemperatureUnity;

import javax.persistence.*;

/**
 * Created by roberto on 13/07/17.
 */
@Entity
public class Threshold {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String email;
    @Enumerated(EnumType.STRING)
    private Direction direction;
    private boolean notifyFluctuation;
    private Double targetTemperature;
    @Enumerated(EnumType.STRING)
    private TemperatureUnity unity;
    private String location;

    Threshold() { //jpa only
    }

    public Threshold(String email, Direction direction, boolean notifyFluctuation, Double targetTemperature, TemperatureUnity unity, String location) {
        this.email = email;
        this.direction = direction;
        this.notifyFluctuation = notifyFluctuation;
        this.targetTemperature = targetTemperature;
        this.unity = unity;
        this.location = location;
    }

    public Long getId() {
        return id;
    }

    public String getEmail() {
        return email;
    }

    public Direction getDirection() {
        return direction;
    }

    public boolean isNotifyFluctuation() {
        return notifyFluctuation;
    }

    public Double getTargetTemperature() {
        return targetTemperature;
    }

    public TemperatureUnity getUnity() {
        return unity;
    }

    public String getLocation() {
        return location;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Threshold threshold = (Threshold) o;

        if (notifyFluctuation != threshold.notifyFluctuation) return false;
        if (id != null ? !id.equals(threshold.id) : threshold.id != null) return false;
        if (email != null ? !email.equals(threshold.email) : threshold.email != null) return false;
        if (direction != threshold.direction) return false;
        if (targetTemperature != null ? !targetTemperature.equals(threshold.targetTemperature) : threshold.targetTemperature != null)
            return false;
        if (unity != threshold.unity) return false;
        return location != null ? location.equals(threshold.location) : threshold.location == null;
    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + (email != null ? email.hashCode() : 0);
        result = 31 * result + (direction != null ? direction.hashCode() : 0);
        result = 31 * result + (notifyFluctuation ? 1 : 0);
        result = 31 * result + (targetTemperature != null ? targetTemperature.hashCode() : 0);
        result = 31 * result + (unity != null ? unity.hashCode() : 0);
        result = 31 * result + (location != null ? location.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "Threshold{" +
                "id=" + id +
                ", email='" + email + '\'' +
                ", direction=" + direction +
                ", notifyFluctuation=" + notifyFluctuation +
                ", targetTemperature=" + targetTemperature +
                ", unity=" + unity +
                ", location='" + location + '\'' +
                '}';
    }
}
