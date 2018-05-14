package com.forecast.repository;

import com.forecast.entries.Team;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface TeamRepository extends CrudRepository<Team, Long> {


    default List<Team> getTeamByName(String name) {
        return getTeamByNameOrName2OrName3(name, name, name);
    }

    List<Team> getTeamByNameOrName2OrName3(String name, String name2, String name3);
}
