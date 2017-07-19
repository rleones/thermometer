package com.fincad.thermometer.controller;

import com.fincad.thermometer.model.Threshold;
import com.fincad.thermometer.service.ThresholdService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.PagedResources;
import org.springframework.hateoas.Resource;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/threshold")
public class ThresholdController {

    @Autowired
    private ThresholdService thresholdService;

    @PostMapping
    public Threshold create(@RequestBody Threshold threshold) {
        return thresholdService.create(threshold);
    }

    @GetMapping("/${email}")
    public PagedResources<Resource<Threshold>> create(@PathVariable String email, Pageable pageable, PagedResourcesAssembler<Threshold> assembler) {
        return assembler.toResource(thresholdService.getByEmail(email, pageable));
    }

}
