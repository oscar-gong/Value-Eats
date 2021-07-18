package com.nuggets.valueeats.entity;

import lombok.*;
import org.springframework.web.bind.annotation.GetMapping;

import javax.persistence.CollectionTable;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.PrimaryKeyJoinColumn;

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
}
