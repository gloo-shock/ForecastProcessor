package com.forecast.rest;

import org.junit.Ignore;
import org.junit.Test;

import static org.assertj.core.api.Java6Assertions.assertThat;


public class RestClientTest {
    @Test
    @Ignore
    public void greeting() {
       assertThat(new RestClient().greeting()).isEqualTo("World");
    }
}
