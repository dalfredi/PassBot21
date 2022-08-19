package edu.school21.passbot.prototype;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan("edu.school21.passbot.prototype.*")
public class PassBotApplication {
    public static void main(String[] args) {
        SpringApplication.run(PassBotApplication.class, args);
    }
}
