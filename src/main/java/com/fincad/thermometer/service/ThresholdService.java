package com.fincad.thermometer.service;

import com.fincad.thermometer.model.Threshold;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ThresholdService {

    Threshold create(Threshold threshold);
    Page<Threshold> getByEmail(String email, Pageable pageable);

}
