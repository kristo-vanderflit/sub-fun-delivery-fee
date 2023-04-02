package com.fujitsu.deliveryfee.BaseFee;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.*;

@Entity
@Table
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder

public class RegionalBaseFee {

    @Id
    @Column(unique=true)
    private long id;
    @Column
    private double Car;
    @Column
    private double Scooter;
    @Column
    private double Bike;

}
