package com.fincad.thermometer.service;

import com.fincad.thermometer.model.Threshold;
import com.fincad.thermometer.repository.ThresholdRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class ThresholdServiceImpl implements ThresholdService {

    private ThresholdRepository thresholdRepository;

    @Autowired
    public ThresholdServiceImpl(ThresholdRepository thresholdRepository) {
        this.thresholdRepository = thresholdRepository;
    }

    @Override
    public Threshold create(Threshold threshold) {
        return thresholdRepository.save(threshold);
    }

    @Override
    public Page<Threshold> getByEmail(String email, Pageable pageable) {
        return thresholdRepository.findByEmail(email, pageable);
    }
}
