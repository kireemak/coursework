package by.kireenko.coursework.CarBooking.controllers;

import by.kireenko.coursework.CarBooking.dto.CarDto;
import by.kireenko.coursework.CarBooking.models.Car;
import by.kireenko.coursework.CarBooking.services.CarService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/cars")
public class CarController {

    private final CarService carService;

    @Autowired
    public CarController(CarService carService) {
        this.carService = carService;
    }

    @GetMapping
    public List<CarDto> getAllCars() {
        return carService.getAllCarsDto();
    }

    @GetMapping("/{id}")
    public CarDto getCarById(@PathVariable Long id) {
        return new CarDto(carService.getCarById(id));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping
    public CarDto createCar(@RequestBody Car car) {
        return new CarDto(carService.createCar(car));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/{id}")
    public CarDto updateCar(@PathVariable Long id, @RequestBody Car car) {
        return new CarDto(carService.updateCar(id, car));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{id}")
    public void deleteCar(@PathVariable Long id) {
        carService.deleteCar(id);
    }

    @GetMapping("/{id}/availability")
    public boolean isCarAvailable(@PathVariable Long id) {
        return carService.isCarAvailable(id);
    }

    @GetMapping("/available")
    public List<CarDto> getAvailableCars() {
        return carService.getAvailableCarsDto();
    }
}
