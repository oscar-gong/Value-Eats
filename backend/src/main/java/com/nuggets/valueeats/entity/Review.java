package com.nuggets.valueeats.entity;

import java.util.ArrayList;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;

import lombok.Data;

@Entity
@Inheritance(strategy = InheritanceType.JOINED)
@Data
public class Review {
    @Id
    private Long id;

    private Long dinerId;

    private Long eateryId;

    private String message;

    private Float rating;

    private ArrayList<String> reviewPhotos;

    public Review(){
        this.message="";
    }
}
