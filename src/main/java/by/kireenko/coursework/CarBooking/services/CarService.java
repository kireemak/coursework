package by.kireenko.coursework.CarBooking.services;

import by.kireenko.coursework.CarBooking.error.ResourceNotFoundException;
import by.kireenko.coursework.CarBooking.models.Car;
import by.kireenko.coursework.CarBooking.repositories.CarRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
public class CarService {

    private final CarRepository carRepository;

    @Autowired
    public CarService(CarRepository carRepository) {
        this.carRepository = carRepository;
    }

    public List<Car> getAllCars() {
        return carRepository.findAll();
    }

    public Car getCarById(Long id) {
        return carRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Car", "id", id));
    }

    @Transactional(readOnly = false)
    public Car createCar(Car car) {
        return carRepository.save(car);
    }

    @Transactional(readOnly = false)
    public Car updateCar(Long id, Car updatedCar) {
        Car existingCar = getCarById(id);
        existingCar.setBrand(updatedCar.getBrand());
        existingCar.setModel(updatedCar.getModel());
        existingCar.setYear(updatedCar.getYear());
        existingCar.setRentalPrice(updatedCar.getRentalPrice());
        existingCar.setStatus(updatedCar.getStatus());
        return carRepository.save(existingCar);
    }

    @Transactional(readOnly = false)
    public void deleteCar(Long id) {
        carRepository.deleteById(id);
    }

    public boolean isCarAvailable(Long carId) {
        Car car = carRepository.findById(carId)
                .orElseThrow(() -> new ResourceNotFoundException("Car", "id", carId));
        return "Available".equalsIgnoreCase(car.getStatus());
    }

    public List<Car> getAvailableCars() {
        return carRepository.findByStatus("Available");
    }
}

