package io.home.moviecatalogueservice.services;

import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixProperty;
import io.home.moviecatalogueservice.models.CatalogueItem;
import io.home.moviecatalogueservice.models.Movie;
import io.home.moviecatalogueservice.models.Rating;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Slf4j
@Service
public class MovieInfoService {

    @Value("${endpoint.service.movie}")
    private String movieServiceEndpoint;

    @Autowired
    private RestTemplate restTemplate;

    /* coreSize     -   Max no of threads waiting for the response from the service,
     *                  If other one comes then it is not allowed in thread pool.
     *
     * maxQueueSize -   No of requests which can wait in queue, until a thread frees up.
     *                  (they won't consuming any thread resources)
     */
    @HystrixCommand(
        fallbackMethod = "getCatalogueItemFallback",
        commandProperties = {
            @HystrixProperty(name = "execution.isolation.thread.timeoutInMilliseconds", value = "2000"),
            @HystrixProperty(name = "circuitBreaker.requestVolumeThreshold", value = "6"),
            @HystrixProperty(name = "circuitBreaker.errorThresholdPercentage", value = "30"),
            @HystrixProperty(name = "circuitBreaker.sleepWindowInMilliseconds", value = "5000")
        },
        threadPoolKey = "movieInfoServicePool",
        threadPoolProperties = {
            @HystrixProperty(name = "coreSize", value = "20"),
            @HystrixProperty(name = "maxQueueSize", value = "10")
        }
    )
    public CatalogueItem getCatalogueItem(Rating rating) {
        String movieServiceURL = movieServiceEndpoint + "/movies/" + rating.getMovieId();
        Movie movie = restTemplate.getForObject(movieServiceURL, Movie.class);

        return new CatalogueItem(movie.getName(), movie.getDescription(), rating.getRating());
    }
    public CatalogueItem getCatalogueItemFallback(Rating rating, Throwable exception) {
        log.error("Falling back :: " + exception.toString());
        return new CatalogueItem("Movie not found", exception.toString(), rating.getRating());
    }

}
