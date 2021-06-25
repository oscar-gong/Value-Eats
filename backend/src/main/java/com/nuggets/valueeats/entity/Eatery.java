package com.nuggets.valueeats.entity;

import javax.persistence.Column;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.Id;
import java.util.*;  


@Entity
@DiscriminatorValue(value = "Eatery")
public class Eatery extends User{

    private String name;

    private ArrayList<String> cuisines;
    private ArrayList<String> menuPhotos;
    
    public Eatery() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
    

    public ArrayList<String> getCuisines() {
        return cuisines;
    }

    public void setCuisines(String cuisines) {
        String[] splitCuisines = cuisines.split(",");
        for(int i = 0; i < splitCuisines.length; i++){
            splitCuisines[i] = splitCuisines[i].trim();
            splitCuisines[i] = splitCuisines[i].substring(0, 1).toUpperCase() + splitCuisines[i].substring(1).toLowerCase();
        }
        this.cuisines = new ArrayList<String>(Arrays.asList(splitCuisines));
    }

    public ArrayList<String> getMenuPhotos() {
        return menuPhotos;
    }

    public void setMenuPhotos(String menuPhotos) {
        String[] splitMenuPhotos = menuPhotos.split(",");
        String[] removeSpaces = Arrays.stream(splitMenuPhotos).map(String::trim).toArray(String[]::new);
        this.menuPhotos = new ArrayList<String>(Arrays.asList(removeSpaces));
    }

    @Override
    public String toString() {
        return "Eatery{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", email='" + email + '\'' +
                '}';
    }
}
