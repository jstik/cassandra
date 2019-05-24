package com.jstik.fancy.chat;

import org.springframework.boot.SpringApplication;

public class FancyChatApp {
    public static void main(String[] args) {
      new SpringApplication(FancyChatApp.class);/*
        SimpleCommandLinePropertySource source = new SimpleCommandLinePropertySource(args);
        if (!source.containsProperty("spring.profiles.active") && !System.getenv().containsKey("SPRING_PROFILES_ACTIVE")) {
            app.setAdditionalProfiles("test");
        }
        app.run( args);*/
    }
}
