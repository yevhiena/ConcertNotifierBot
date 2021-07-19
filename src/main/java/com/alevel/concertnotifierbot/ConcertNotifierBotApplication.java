package com.alevel.concertnotifierbot;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableScheduling
@SpringBootApplication
public class ConcertNotifierBotApplication {

    public static void main(String[] args) {
        SpringApplication.run(ConcertNotifierBotApplication.class, args);
    }

}
