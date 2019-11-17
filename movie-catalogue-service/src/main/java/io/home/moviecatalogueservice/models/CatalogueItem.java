package io.home.moviecatalogueservice.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CatalogueItem {

    private String name;
    private String desc;
    private int rating;

}
