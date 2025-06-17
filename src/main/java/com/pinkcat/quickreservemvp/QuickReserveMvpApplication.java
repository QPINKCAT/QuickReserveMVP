package com.pinkcat.quickreservemvp;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@EnableJpaAuditing
@SpringBootApplication
public class QuickReserveMvpApplication {

    public static void main(String[] args) {
        SpringApplication.run(QuickReserveMvpApplication.class, args);
    }

}