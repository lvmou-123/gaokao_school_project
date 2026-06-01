package com.gaokao.advisor;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;

@SpringBootApplication
@ConfigurationPropertiesScan
public class GaokaoAdvisorApplication {

    public static void main(String[] args) {
        SpringApplication.run(GaokaoAdvisorApplication.class, args);
    }
}
