package com.fujitsu.deliveryfee.City;

import com.fujitsu.deliveryfee.BaseFee.RegionalBaseFee;
import com.fujitsu.deliveryfee.ExtraFee.ExtraFee;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "cities")
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor

public class City {

    @Id
    private long id;
    @Column(unique=true)
    private String name;
    @Column(unique=true)
    private int stationCode;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "regionalBaseFee_id", unique = true)
    private RegionalBaseFee regionalBaseFee;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "extraFee_id", unique = true)
    private ExtraFee extraFee;

    public City(int id, String name, int stationCode) {
        this.id = id;
        this.name = name.toLowerCase();
        this.stationCode = stationCode;
    }
}
