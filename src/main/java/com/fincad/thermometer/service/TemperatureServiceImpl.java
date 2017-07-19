package com.fincad.thermometer.service;

import com.fincad.thermometer.model.Temperature;
import com.fincad.thermometer.model.Threshold;
import com.fincad.thermometer.model.enums.Direction;
import com.fincad.thermometer.repository.TemperatureRepository;
import com.fincad.thermometer.repository.ThresholdRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;

/**
 * Created by roberto on 13/07/17.
 */
@Service
public class TemperatureServiceImpl implements TemperatureService {

    private static final Logger log = LoggerFactory.getLogger(TemperatureServiceImpl.class);

    private ThresholdRepository thresholdRepository;
    private WeatherApiService weatherApiService;
    private TemperatureRepository temperatureRepository;
    private MailService mailService;

    @Autowired
    public TemperatureServiceImpl(ThresholdRepository thresholdRepository, WeatherApiService weatherApiService, TemperatureRepository temperatureRepository, MailService mailService) {
        this.thresholdRepository = thresholdRepository;
        this.weatherApiService = weatherApiService;
        this.temperatureRepository = temperatureRepository;
        this.mailService = mailService;
    }

    @Override
    public void notifyThreshold() {
        thresholdRepository.findDistinctLocation()
                .parallelStream()
                .forEach(location -> notifyByLocation(location));
    }

    private void notifyByLocation(String location) {
        log.info(String.format("Notifying thresholds at %s", location));

        // retrieve information from data source (weather)
        Double actualTemperature;

        try {
            actualTemperature = weatherApiService.getWeatherConditions(location);

            log.info(String.format("Actual temperature at %s is %f", location, actualTemperature));
        } catch (IOException e) {
            actualTemperature = null;
            log.info(String.format("Error while retrieving actual temperature. Exception: %s", e.getMessage()));
        }

        // Only verify thresholds if actual temperature were acquired
        if (actualTemperature != null) {
            Temperature temperature = new Temperature(location, actualTemperature);

            List<Temperature> lastTemperatures = temperatureRepository.findTop3ByLocationOrderByIdDesc(location);

            // verify fluctuation and direction
            Direction direction = getDirection(lastTemperatures, actualTemperature);
            boolean fluctuation = isFluctuating(lastTemperatures, actualTemperature);

            // save retrieved temperature for location
            temperatureRepository.save(temperature);
            Double temp = actualTemperature;
            Double previousTemperature = lastTemperatures.isEmpty() ? actualTemperature : lastTemperatures.get(0).getTemperature();

            // verify thresholds
            thresholdRepository.findByLocationIgnoreCase(location)
                    .parallelStream()
                    .forEach(threshold -> verifyThreshold(threshold, temp, previousTemperature, direction, fluctuation));
        }
    }

    private Direction getDirection(List<Temperature> lastTemperatures, Double actualTemperature) {
        if (lastTemperatures.isEmpty() || lastTemperatures.get(0).getTemperature().equals(actualTemperature)) {
            return null;
        }

        if (lastTemperatures.get(0).getTemperature().compareTo(actualTemperature) > 0) {
            return Direction.DOWN;
        } else {
            return Direction.UP;
        }
    }

    private boolean isFluctuating(List<Temperature> lastTemperatures, Double actualTemperature) {
        int previousComparation = 0;
        int actualComparation;

        for (Temperature temp: lastTemperatures) {
            actualComparation = temp.getTemperature().compareTo(actualTemperature);
            if (previousComparation == actualComparation) {
                return false;
            }
            previousComparation = actualComparation;
        }

        // It will be consider fluctuating if there is more than one previous record
        return lastTemperatures.size() > 1;
    }

    private void verifyThreshold(Threshold threshold, Double actualTemperature, Double previousTemperature, Direction direction, boolean fluctuation) {
        boolean achievedThreshold = false;

        int comparedTemperature = threshold.getTargetTemperature().compareTo(actualTemperature);
        int comparedPreviousTemperature = previousTemperature.compareTo(actualTemperature);

        // If there is not any record consider threshold's direction
        if (direction == null) {
            direction = threshold.getDirection();
        }

        if (comparedTemperature == 0) {
            // If the temperature is the same, verify if threshold is allowed to notify on fluctuation or it is not fluctuating
            achievedThreshold = threshold.isNotifyFluctuation() || !fluctuation;
        } else if (comparedTemperature != comparedPreviousTemperature && comparedPreviousTemperature != 0) {
            // If temperature has passed by target temperature, verify if it was in the right direction
            if (threshold.isNotifyFluctuation() || !fluctuation) {
                switch (threshold.getDirection()) {
                    case UP:
                        achievedThreshold = direction.equals(Direction.UP) && comparedTemperature < 0;
                        break;
                    case DOWN:
                        achievedThreshold = direction.equals(Direction.DOWN) && comparedTemperature > 0;
                        break;
                    default:
                        achievedThreshold = true;
                        break;
                }
            }
        }

        if (achievedThreshold) {
            try {
                // notify achieved thresholds
                mailService.sendNotification(threshold);
            } catch (Exception e) {
                log.error("Notification error");
            }
        }
    }

}
