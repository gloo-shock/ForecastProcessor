package com.forecast.controller;

import com.forecast.entries.Team;
import com.forecast.entries.Tour;
import com.forecast.repository.TeamRepository;
import com.forecast.repository.TourRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(path = "/tour")
public class TourController {
    @Autowired
    private TourRepository tourRepository;
    @Autowired
    private TeamRepository teamRepository;

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

    @GetMapping(path = "/team")
    public @ResponseBody
    List<Team> getTeams(@RequestParam("name") String name) {
        return teamRepository.getTeamByName(name);
    }
}
