package edu.tamu.narrationbox;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.web.servlet.error.ErrorMvcAutoConfiguration;
import org.springframework.web.bind.annotation.RestController;


@SpringBootApplication(exclude = {ErrorMvcAutoConfiguration.class})
@RestController
public class NarrationBoxApplication {

    public static void main(String[] args) {
        SpringApplication.run(NarrationBoxApplication.class, args);
    }
}
