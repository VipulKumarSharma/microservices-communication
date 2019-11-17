package io.home.movieinfoservice.resources;

import io.home.movieinfoservice.models.Movie;
import io.home.movieinfoservice.models.MovieSummary;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

@RestController
@RequestMapping("/movies")
public class MovieResource {

    @Value("${endpoint.service.movieDb}")
    private String movieDbApiEndpoint;

    @Value("${endpoint.service.movieDb.apiKey}")
    private String apiKey;

    @Autowired
    RestTemplate restTemplate;

    @GetMapping("/{movieId}")
    public Movie getMovieInfo(@PathVariable String movieId) {
        String movieDbApiURL = movieDbApiEndpoint + movieId + "?api_key=" + apiKey;
        MovieSummary movieSummary = restTemplate.getForObject(movieDbApiURL, MovieSummary.class);

        return new Movie(movieId, movieSummary.getTitle(), movieSummary.getOverview());
    }
}
