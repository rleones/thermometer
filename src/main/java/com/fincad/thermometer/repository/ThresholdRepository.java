package com.fincad.thermometer.repository;

import com.fincad.thermometer.model.Threshold;
import org.springframework.data.repository.CrudRepository;

/**
 * Created by roberto on 13/07/17.
 */
public interface ThresholdRepository extends CrudRepository<Threshold, Long> {
}
