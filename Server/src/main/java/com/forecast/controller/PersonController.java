package com.forecast.controller;

import com.forecast.entries.Person;
import com.forecast.repository.PersonRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(path = "/person")
public class PersonController {
    @Autowired
    private PersonRepository personRepository;

    @GetMapping(path = "/all")
    public @ResponseBody
    Iterable<Person> getAllPerson() {
        return personRepository.findAll();
    }

    @PostMapping("/save")
    public ResponseEntity<String> savePerson(@RequestBody Person person) {
        personRepository.save(person);
        return ResponseEntity.ok("OK");
    }
}
