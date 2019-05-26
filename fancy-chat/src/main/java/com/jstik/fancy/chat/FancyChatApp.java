package com.jstik.fancy.chat;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class FancyChatApp {
    public static void main(String[] args) {
        SpringApplication.run(FancyChatApp.class, args);
       /* SimpleCommandLinePropertySource source = new SimpleCommandLinePropertySource(args);
        if (!source.containsProperty("spring.profiles.active") && !System.getenv().containsKey("SPRING_PROFILES_ACTIVE")) {
            app.setAdditionalProfiles("test");
        }
        app.run( args);*/
    }
}
