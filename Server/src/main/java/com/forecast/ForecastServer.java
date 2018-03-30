package com.forecast;

import org.h2.tools.Server;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.sql.SQLException;


@SpringBootApplication
public class ForecastServer {

    private static final Logger logger = LoggerFactory.getLogger(ForecastServer.class);

    public static void main(String[] args) {
        startH2Server();
        SpringApplication.run(ForecastServer.class, args);
    }

    private static void startH2Server() {
        try {
            Server h2Server = Server.createTcpServer().start();
            if (h2Server.isRunning(true)) {
                logger.info("H2 server was started and is running.");
            } else {
                throw new RuntimeException("Could not start H2 server.");
            }
        } catch (SQLException e) {
            throw new RuntimeException("Failed to start H2 server: ", e);
        }
    }
}
