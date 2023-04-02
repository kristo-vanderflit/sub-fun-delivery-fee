package com.fujitsu.deliveryfee.ExtraFee;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor

public class ExtraFee {

    @Id
    private long id;
    @Column
    private double airTemperature1;
    @Column
    private double airTemperature2;
    @Column
    private double windSpeed1;
    @Column
    private double phenomenon1;
    @Column
    private double phenomenon2;
}
