package com.nuggets.valueeats.entity;

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
public class Voucher {
    @Id
    private Long id;

    private Long ownerId;

    private Long eateryId;

    private Float discount;

    // NEEDS EXPIRY TIME
}
