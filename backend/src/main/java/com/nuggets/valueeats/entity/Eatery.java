package com.nuggets.valueeats.entity;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.CollectionTable;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.PrimaryKeyJoinColumn;

import java.util.ArrayList;
import java.util.List;

@Entity
@Data
@NoArgsConstructor
@PrimaryKeyJoinColumn(name = "id")
public final class Eatery extends User {
    @ElementCollection
    @CollectionTable(name="Cuisines")
    @Column(name="cuisine")
    private List<String> cuisines = new ArrayList<String>();

    private ArrayList<String> menuPhotos;

    @Override
    public String toString() {
        return "";
    }
}
