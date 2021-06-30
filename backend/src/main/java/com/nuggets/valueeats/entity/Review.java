package com.nuggets.valueeats.entity;

import java.util.ArrayList;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;

import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Inheritance(strategy = InheritanceType.JOINED)
@NoArgsConstructor
@Data
public class Review {
    @Id
    private Long id;

    private Long dinerId;

    private Long eateryId;

    private String message;

    private Integer rating;

    private ArrayList<String> reviewPhotos;

    private String token;
}
