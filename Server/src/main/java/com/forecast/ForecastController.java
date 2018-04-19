package com.forecast;

import com.forecast.entries.Tour;
import com.forecast.repository.TourRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(path = "/tour")
public class ForecastController {
    @Autowired
    private TourRepository tourRepository;

    @PostMapping(path = "/save")
    public ResponseEntity<String> saveTour(@RequestBody Tour tour) {
        tourRepository.save(tour);
        return ResponseEntity.ok("OK");
    }

    @GetMapping(path = "/last")
    public @ResponseBody
    Tour getLastTour() {
        return tourRepository.findFirstByOrderByTimestampDesc();
    }
}
