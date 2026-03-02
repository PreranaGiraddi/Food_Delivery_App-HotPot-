package com.wipro.hotpot;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class HotPotApplication {

    public static void main(String[] args) {
        SpringApplication.run(HotPotApplication.class, args);
        System.out.println("🍲 HotPot Application Started Successfully!");
        System.out.println("📄 Swagger UI: http://localhost:8080/swagger-ui.html");
    }
}

