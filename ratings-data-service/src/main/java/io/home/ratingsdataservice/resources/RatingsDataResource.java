package io.home.ratingsdataservice.resources;

import io.home.ratingsdataservice.models.Rating;
import io.home.ratingsdataservice.models.UserRating;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;
import java.util.List;

@RestController
@RequestMapping("/ratings")
public class RatingsDataResource {

    @GetMapping("{movieId}")
    public Rating getRating(@PathVariable String movieId) {
        return new Rating(movieId, 4);
    }

    @GetMapping("users/{userId}")
    public UserRating getRatingsByUserId(@PathVariable String userId) {
        return new UserRating(Arrays.asList(
                new Rating("1",4),
                new Rating("2",3),
                new Rating("3",1)
        ));
    }
}
