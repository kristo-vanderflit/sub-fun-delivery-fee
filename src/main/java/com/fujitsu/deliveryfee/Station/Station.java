package com.fujitsu.deliveryfee.Station;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table
@Setter
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString

public class Station {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @Column(name = "station")
    private String name;
    private int wmocode;
    private double airTemperature;
    private double windSpeed;
    private String phenomenon;
    private String timestamp;

}
