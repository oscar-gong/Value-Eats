package com.nuggets.valueeats.entity;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.PrimaryKeyJoinColumn;

@Entity
@NoArgsConstructor
@Data
@PrimaryKeyJoinColumn(name = "id")
public final class Diner extends User {
}
