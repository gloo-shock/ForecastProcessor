package com.forecast;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;

@ComponentScan
@EnableAutoConfiguration
public class ForecastServer {
    public static void main(String[] args) {
        SpringApplication.run(ForecastServer.class, args);
    }
}
