package sideproject.gugumo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableScheduling
@SpringBootApplication
public class GugumoApplication {

    public static void main(String[] args) {
        SpringApplication.run(GugumoApplication.class, args);
    }

}
