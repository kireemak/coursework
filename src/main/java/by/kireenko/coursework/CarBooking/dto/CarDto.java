package by.kireenko.coursework.CarBooking.dto;

import by.kireenko.coursework.CarBooking.models.Car;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class CarDto {
    private long id;
    private String brand;
    private String model;
    private int year;
    private double rentalPrice;
    private String status;

    public CarDto(Car car) {
        this.id = car.getId();
        this.brand = car.getBrand();
        this.model = car.getModel();
        this.year = car.getYear();
        this.rentalPrice = car.getRentalPrice();
        this.status = car.getStatus();
    }
}
