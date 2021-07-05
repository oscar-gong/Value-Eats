package com.nuggets.valueeats.entity;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.PrimaryKeyJoinColumn;
import java.util.ArrayList;
import java.util.List;

@Entity
@NoArgsConstructor
@Data
@PrimaryKeyJoinColumn(name = "id")
public final class Diner extends User {
    @ElementCollection
    private List<Long> vouchers = new ArrayList<>();
}
