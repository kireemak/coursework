package by.kireenko.coursework.CarBooking.models;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
@Table(name = "cars")
public class Car {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 50)
    private String brand;

    @Column(nullable = false, length = 50)
    private String model;

    @Column(nullable = false)
    private int year;

    @Column(nullable = false)
    private double rentalPrice;

    @Column(nullable = false, length = 20)
    private String status;

}
