package com.nuggets.valueeats.entity;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Entity
@Data
@ToString(callSuper =  true)
@NoArgsConstructor
@PrimaryKeyJoinColumn(name = "id")
public final class Eatery extends User {
    @ElementCollection
    @CollectionTable(name="Cuisines")
    @Column(name="cuisine")
    private List<String> cuisines = new ArrayList<>();

    private ArrayList<String> menuPhotos;

    @JsonProperty("rating")
    @Transient
    private Float lazyRating;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    @OneToMany
    @JoinColumn(name = "eateryId")
    private List<Review> reviews;

//??    @JsonIgnore
    public void calculateRating() {
        lazyRating = (float) reviews.stream().mapToDouble(Review::getRating).average().orElse(0);
    }
}
