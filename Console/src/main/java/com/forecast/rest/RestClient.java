package com.forecast.rest;

import com.forecast.entries.Person;
import com.forecast.entries.Team;
import com.forecast.entries.Tour;

import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.GenericType;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;

import static javax.ws.rs.client.Entity.entity;
import static javax.ws.rs.core.MediaType.APPLICATION_JSON_TYPE;

public class RestClient {
    private static final String REST_URI
            = "http://localhost:8080";
    private static final RestClient INSTANCE = new RestClient();

    public static RestClient restClient() {
        return INSTANCE;
    }

    private WebTarget target = ClientBuilder.newClient().target(REST_URI);

    public void addTeam(Team team) {
        target
                .path("add")
                .request()
                .post(entity(team, APPLICATION_JSON_TYPE));
    }

    public String findAllTeams() {
        return target
                .path("all")
                .request(MediaType.APPLICATION_JSON)
                .get(String.class);
    }

    public void saveTour(Tour tour) {
        Response response = target.path("tour/save")
                .request()
                .post(entity(tour, APPLICATION_JSON_TYPE));
        if (response.getStatus() != Response.Status.OK.getStatusCode()) {
            throw new RuntimeException("Failed to save tour");
        }
    }

    public List<Person> loadPersons() {
        return target.path("person/all")
                .request(MediaType.APPLICATION_JSON)
                .get(new GenericType<List<Person>>() {
                });
    }

    public void savePerson(Person person) {
        Response response = target.path("person/save")
                .request()
                .post(entity(person, APPLICATION_JSON_TYPE));
        if (response.getStatus() != Response.Status.OK.getStatusCode()) {
            throw new RuntimeException("Failed to save person");
        }
    }

    public void deletePerson(Person person) {
        Response response = target.path("person/delete")
                .request()
                .post(entity(person, APPLICATION_JSON_TYPE));
        if (response.getStatus() != Response.Status.OK.getStatusCode()) {
            throw new RuntimeException("Failed to delete person");
        }
    }

}
