package com.fincad.thermometer.repository;

import com.fincad.thermometer.model.Threshold;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.Set;

/**
 * Created by roberto on 13/07/17.
 */
public interface ThresholdRepository extends CrudRepository<Threshold, Long> {

    @Query("select distinct upper(location) from Threshold")
    Set<String> findDistinctLocation();
    List<Threshold> findByLocationIgnoreCase(String location);
    Page<Threshold> findByEmail(String email, Pageable pageable);

}
