package edu.tamu.narrationbox;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@SpringBootApplication
@RestController
public class NarrationBoxApplication {

    public static void main(String[] args) {
        SpringApplication.run(NarrationBoxApplication.class, args);
    }

    @RequestMapping(value = "/")
    public String hello() {
        return "Here is your story, now on Heroku.com!";
    }
}
