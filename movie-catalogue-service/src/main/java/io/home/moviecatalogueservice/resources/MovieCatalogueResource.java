package io.home.moviecatalogueservice.resources;

import io.home.moviecatalogueservice.models.CatalogueItem;
import io.home.moviecatalogueservice.models.Movie;
import io.home.moviecatalogueservice.models.Rating;
import io.home.moviecatalogueservice.models.UserRating;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/catalogue")
public class MovieCatalogueResource {

    @Value("${endpoint.service.movie}")
    private String movieServiceEndpoint;

    @Value("${endpoint.service.rating}")
    private String ratingServiceEndpoint;

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private WebClient.Builder webClientBuilder;

    @GetMapping("/{userId}")
    public List<CatalogueItem> getCatalogue(@PathVariable String userId) {

        /** Mono is the empty container, In which you get the required object in future,
         ** takes action when filled.
         ** block() - blocks the execution until Mono is fulfilled
         **/

        String ratingServiceURL = ratingServiceEndpoint + "/ratings/users/" + userId;

        UserRating userRating = webClientBuilder.build()
                .get()
                .uri(ratingServiceURL)
                .retrieve()
                .bodyToMono(UserRating.class)
                .block();

        return userRating.getUserRating().stream()
            .map(rating -> {
                String movieServiceURL = movieServiceEndpoint + "/movies/" + rating.getMovieId();
                Movie movie = restTemplate.getForObject(movieServiceURL, Movie.class);

                return new CatalogueItem(movie.getName(), movie.getDescription(), rating.getRating());
            })
            .collect(Collectors.toList());
    }
}
