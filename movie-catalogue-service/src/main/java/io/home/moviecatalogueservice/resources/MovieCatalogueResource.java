package io.home.moviecatalogueservice.resources;

import io.home.moviecatalogueservice.models.CatalogueItem;
import io.home.moviecatalogueservice.models.UserRating;
import io.home.moviecatalogueservice.services.MovieInfoService;
import io.home.moviecatalogueservice.services.UserRatingInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/catalogue")
public class MovieCatalogueResource {

    @Autowired
    MovieInfoService movieInfoService;

    @Autowired
    UserRatingInfoService userRatingInfoService;

    @GetMapping("/{userId}")
    public List<CatalogueItem> getCatalogue(@PathVariable String userId) {

        UserRating userRating = userRatingInfoService.getUserRating(userId);

        return userRating.getUserRating().stream()
            .map(rating -> movieInfoService.getCatalogueItem(rating))
            .collect(Collectors.toList());
    }

}
