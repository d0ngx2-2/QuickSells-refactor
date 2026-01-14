package com.example.quicksells;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class QuickSellsApplication {

    public static void main(String[] args) {
        SpringApplication.run(QuickSellsApplication.class, args);
    }

}
