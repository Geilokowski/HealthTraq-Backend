package iu.study.healthtraq;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;

@SpringBootApplication
@ConfigurationPropertiesScan("iu.study.healthtraq.properties")
public class HealthtraqApplication {

    public static void main(String[] args) {
        SpringApplication.run(HealthtraqApplication.class, args);
    }

}
