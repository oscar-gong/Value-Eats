package com.nuggets.valueeats.entity;

import com.nuggets.valueeats.utils.TextUtils;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.PrimaryKeyJoinColumn;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.stream.Collectors;

@Entity
@Data
@NoArgsConstructor
@PrimaryKeyJoinColumn(name = "id")
public final class Eatery extends User {
    private ArrayList<String> cuisines;
    private ArrayList<String> menuPhotos;

    public void setCuisines(ArrayList<String> cuisines) {
        // String[] splitCuisines = cuisines.split(",");
        // for(int i = 0; i < splitCuisines.length; i++){
        //     splitCuisines[i] = splitCuisines[i].trim();
        //     splitCuisines[i] = splitCuisines[i].substring(0, 1).toUpperCase() + splitCuisines[i].substring(1).toLowerCase();
        // }
        // this.cuisines = new ArrayList<String>(Arrays.asList(splitCuisines));
        this.cuisines = cuisines;
    }

    public ArrayList<String> getMenuPhotos() {
        return menuPhotos;
    }

    public void setMenuPhotos(ArrayList<String> menuPhotos) {
        // String[] splitMenuPhotos = menuPhotos.split(",");
        // String[] removeSpaces = Arrays.stream(splitMenuPhotos).map(String::trim).toArray(String[]::new);
        // this.menuPhotos = new ArrayList<String>(Arrays.asList(removeSpaces));
        this.menuPhotos = menuPhotos;
    }

    @Override
    public String toString() {
        return "";
    }
}
