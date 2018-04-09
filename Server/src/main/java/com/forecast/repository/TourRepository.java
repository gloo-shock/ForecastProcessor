package com.forecast.repository;

import com.forecast.entries.Tour;
import org.springframework.data.repository.CrudRepository;

public interface TourRepository extends CrudRepository<Tour, Long> {
}
