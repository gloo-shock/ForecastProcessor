package com.forecast.rest;

import com.forecast.entries.Team;
import org.junit.Ignore;
import org.junit.Test;


public class RestClientTest {
   private RestClient restClient = new RestClient();


    @Test
    @Ignore
    public void team() {
        restClient.addTeam(new Team("ЦСКА"));
        System.out.println(restClient.findAllTeams());
    }
}
