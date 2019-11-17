package io.home.moviecatalogueservice.resources;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ApplicationStatusResource {

    @GetMapping("/")
    public String getApplicationStatus() {
        return "Movie Catalogue Service is in running status...";
    }

}
