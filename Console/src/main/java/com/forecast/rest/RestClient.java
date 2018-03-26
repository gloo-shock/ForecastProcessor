package com.forecast.rest;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.core.MediaType;

public class RestClient {
    private static final String REST_URI
            = "http://localhost:8080";

    private Client client = ClientBuilder.newClient();

    public String greeting() {
        return client
                .target(REST_URI)
                .path("greeting")
                .request(MediaType.TEXT_PLAIN_TYPE)
                .get(String.class);
    }
}
