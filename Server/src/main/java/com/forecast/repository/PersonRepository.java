package com.forecast.repository;

import com.forecast.entries.Person;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface PersonRepository extends CrudRepository<Person, Long> {
    List<Person> findAllByForecastTeamIsNotNull();

}
