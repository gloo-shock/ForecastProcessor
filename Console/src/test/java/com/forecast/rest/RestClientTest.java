package com.forecast.rest;

import com.forecast.entries.Person;
import org.junit.Ignore;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

@Ignore
public class RestClientTest {
    private RestClient restClient = new RestClient();

    @Test
    public void person() {
        Person person1 = new Person("Андрей", "Глушок");
        restClient.savePerson(person1);
        Person person2 = new Person("Иван", "Петров");
        restClient.savePerson(person2);
        assertThat(restClient.loadPersons()).containsOnly(person1, person2);
    }

    @Test
    public void team() {
        System.out.println(restClient.getTeams("Арсенал"));
        System.out.println(restClient.getTeams("Брайтон"));
    }
}
