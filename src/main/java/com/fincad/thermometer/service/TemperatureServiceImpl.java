package com.fincad.thermometer.service;

import com.fincad.thermometer.model.Threshold;
import com.fincad.thermometer.model.enums.Direction;
import com.fincad.thermometer.model.enums.TemperatureUnity;
import com.fincad.thermometer.repository.ThresholdRepository;
import com.fincad.thermometer.scheduler.ScheduledTasks;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by roberto on 13/07/17.
 */
@Service
public class TemperatureServiceImpl implements TemperatureService {

    private static final Logger log = LoggerFactory.getLogger(TemperatureServiceImpl.class);

    ThresholdRepository thresholdRepository;

    @Autowired
    public TemperatureServiceImpl(ThresholdRepository thresholdRepository) {
        this.thresholdRepository = thresholdRepository;
    }

    @Override
    public void notifyThreshold() {
        thresholdRepository.findAll().forEach(System.out::println);
        log.info("notifyThreshold");
    }

}
