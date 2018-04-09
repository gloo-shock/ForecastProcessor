package com.forecast;

import com.forecast.repository.TourRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ForecastController {
    @Autowired
    private TourRepository tourRepository;


}
