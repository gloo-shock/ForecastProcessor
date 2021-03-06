package com.forecast;

import com.forecast.entries.Team;
import com.forecast.repository.TeamRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
public class ServerController {

    @Autowired
    private TeamRepository teamRepository;

    @RequestMapping("/greeting")
    public String greeting(@RequestParam(value = "name", required = false, defaultValue = "World") String name) {
        return name;
    }


    @PostMapping(path = "/add")
    public String addNewTeam(@RequestBody Team team) {
        teamRepository.save(team);
        return "Saved";
    }

    @GetMapping(path = "/all")
    public @ResponseBody
    Iterable<Team> getAllUsers() {
        return teamRepository.findAll();
    }

}
