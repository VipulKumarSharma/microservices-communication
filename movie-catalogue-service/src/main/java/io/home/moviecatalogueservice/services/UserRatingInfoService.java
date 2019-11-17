package io.home.moviecatalogueservice.services;

import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixProperty;
import io.home.moviecatalogueservice.models.Rating;
import io.home.moviecatalogueservice.models.UserRating;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.Arrays;

@Slf4j
@Service
public class UserRatingInfoService {

    @Value("${endpoint.service.rating}")
    private String ratingServiceEndpoint;

    @Autowired
    private WebClient.Builder webClientBuilder;

    /* coreSize     -   Max no of threads waiting for the response from the service,
     *                  If other one comes then it is not allowed in thread pool.
     *
     * maxQueueSize -   No of requests which can wait in queue, until a thread frees up.
     *                  (they won't consuming any thread resources)
     */
    @HystrixCommand(
        fallbackMethod = "getUserRatingFallback",
        commandProperties = {
            @HystrixProperty(name = "execution.isolation.thread.timeoutInMilliseconds", value = "2000"),
            @HystrixProperty(name = "circuitBreaker.requestVolumeThreshold", value = "5"),
            @HystrixProperty(name = "circuitBreaker.errorThresholdPercentage", value = "50"),
            @HystrixProperty(name = "circuitBreaker.sleepWindowInMilliseconds", value = "5000")
        },
        threadPoolKey = "ratingsDataServicePool",
        threadPoolProperties = {
            @HystrixProperty(name = "coreSize", value = "15"),
            @HystrixProperty(name = "maxQueueSize", value = "8")
        }
    )
    public UserRating getUserRating(String userId) {
        String ratingServiceURL = ratingServiceEndpoint + "/ratings/users/" + userId;

        /** Mono is the empty container, In which you get the required object in future,
         ** takes action when filled.
         ** block() - blocks the execution until Mono is fulfilled
         **/
        return webClientBuilder.build()
                .get()
                .uri(ratingServiceURL)
                .retrieve()
                .bodyToMono(UserRating.class)
                .block();
    }
    public UserRating getUserRatingFallback(String userId, Throwable exception) {
        log.error("Falling back :: " + exception.toString());
        return new UserRating(userId, Arrays.asList(new Rating("0", 0)));
    }

}
