package com.fincad.thermometer;

import com.fincad.thermometer.model.Threshold;
import com.fincad.thermometer.model.enums.Direction;
import com.fincad.thermometer.model.enums.TemperatureUnity;
import com.fincad.thermometer.repository.ThresholdRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.EnableScheduling;

import java.util.Arrays;

/**
 * Created by roberto on 13/07/17.
 */
@SpringBootApplication
@EnableScheduling
public class ThermometerApplication  {

    public static void main(String[] args) {
        SpringApplication.run(ThermometerApplication.class);
    }

}
