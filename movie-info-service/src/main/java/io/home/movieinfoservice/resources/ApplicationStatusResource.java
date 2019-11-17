package io.home.movieinfoservice.resources;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ApplicationStatusResource {

    @GetMapping("/")
    public String getApplicationStatus() {
        return "Movie Info Service is in running status...";
    }

}
