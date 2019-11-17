package io.home.ratingsdataservice.resources;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ApplicationStatusResource {

    @GetMapping("/")
    public String getApplicationStatus() {
        return "Ratings Data Service is in running status...";
    }

}
