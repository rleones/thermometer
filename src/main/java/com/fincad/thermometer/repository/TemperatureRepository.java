package com.fincad.thermometer.repository;

import com.fincad.thermometer.model.Temperature;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

/**
 * Created by roberto on 14/07/17.
 */
public interface TemperatureRepository extends CrudRepository<Temperature, Long> {

    List<Temperature> findTop3ByLocationOrderByIdDesc(String location);

}
