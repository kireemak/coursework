package by.kireenko.coursework.CarBooking.services;

import by.kireenko.coursework.CarBooking.dto.CarDto;
import by.kireenko.coursework.CarBooking.error.ResourceNotFoundException;
import by.kireenko.coursework.CarBooking.models.Car;
import by.kireenko.coursework.CarBooking.repositories.CarRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Slf4j
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

    public List<CarDto> getAllCarsDto() {
        List<CarDto> carDtoList = new ArrayList<>();
        getAllCars().forEach(car -> carDtoList.add(new CarDto(car)));
        return carDtoList;
    }

    public Car getCarById(Long id) {
        return carRepository.findById(id)
                .orElseThrow(() -> {
                    log.warn("Car with id {} not found", id);
                    return new ResourceNotFoundException("Car", "id", id);
                });
    }

    @Transactional(readOnly = false)
    public Car getCarWithLockById(Long id) {
        return carRepository.findAndLockById(id)
                .orElseThrow(() -> {
                    log.warn("Car with id {} not found", id);
                    return new ResourceNotFoundException("Car", "id", id);
                });
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
        Car car = getCarById(carId);
        return "Available".equalsIgnoreCase(car.getStatus());
    }

    public List<Car> getAvailableCars() {
        return carRepository.findByStatus("Available");
    }

    public List<CarDto> getAvailableCarsDto() {
        List<CarDto> carDtoList = new ArrayList<>();
        getAvailableCars().forEach(car -> carDtoList.add(new CarDto(car)));
        return carDtoList;
    }
}

