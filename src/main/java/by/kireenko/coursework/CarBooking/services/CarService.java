package by.kireenko.coursework.CarBooking.services;

import by.kireenko.coursework.CarBooking.dto.CarDto;
import by.kireenko.coursework.CarBooking.dto.CarRequestDto;
import by.kireenko.coursework.CarBooking.error.ResourceNotFoundException;
import by.kireenko.coursework.CarBooking.models.Car;
import by.kireenko.coursework.CarBooking.repositories.CarRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
@Transactional(readOnly = true)
public class CarService {

    private final CarRepository carRepository;
    private final CarService self;

    @Autowired
    public CarService(CarRepository carRepository, @Lazy CarService self) {
        this.carRepository = carRepository;
        this.self = self;
    }

    public List<Car> getAllCars() {
        return carRepository.findAll();
    }

    public List<CarDto> getAllCarsDto() {
        List<CarDto> carDtoList = new ArrayList<>();
        getAllCars().forEach(car -> carDtoList.add(new CarDto(car)));
        return carDtoList;
    }

    @Cacheable(value = "cars", key = "#id")
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
    public Car createCar(CarRequestDto carRequestDto) {
        Car car = new Car();
        car.setBrand(carRequestDto.getBrand());
        car.setModel(carRequestDto.getModel());
        car.setYear(carRequestDto.getYear());
        car.setRentalPrice(carRequestDto.getRentalPrice());
        car.setStatus(carRequestDto.getStatus());
        return carRepository.save(car);
    }

    @CachePut(value = "cars", key = "#id")
    @Transactional(readOnly = false)
    public Car updateCar(Long id, CarRequestDto carRequestDto) {
        Car existingCar = self.getCarById(id);
        if (carRequestDto.getBrand() != null)
            existingCar.setBrand(carRequestDto.getBrand());
        if (carRequestDto.getModel() != null)
            existingCar.setModel(carRequestDto.getModel());
        if (carRequestDto.getYear() != null)
            existingCar.setYear(carRequestDto.getYear());
        if (carRequestDto.getRentalPrice() != null)
            existingCar.setRentalPrice(carRequestDto.getRentalPrice());
        if (carRequestDto.getStatus() != null)
            existingCar.setStatus(carRequestDto.getStatus());
        return carRepository.save(existingCar);
    }

    @CachePut(value = "cars", key = "#id")
    @Transactional(readOnly = false)
    public Car updateCar(Long id, Car updateCar) {
        Car existingCar = self.getCarById(id);
        if (updateCar.getBrand() != null)
            existingCar.setBrand(updateCar.getBrand());
        if (updateCar.getModel() != null)
            existingCar.setModel(updateCar.getModel());
        if (updateCar.getYear() != null)
            existingCar.setYear(updateCar.getYear());
        if (updateCar.getRentalPrice() != null)
            existingCar.setRentalPrice(updateCar.getRentalPrice());
        if (updateCar.getStatus() != null)
            existingCar.setStatus(updateCar.getStatus());
        return carRepository.save(existingCar);
    }

    @CacheEvict(value = "cars", key = "#id")
    @Transactional(readOnly = false)
    public void deleteCar(Long id) {
        carRepository.deleteById(id);
    }

    public boolean isCarAvailable(Long carId) {
        Car car = self.getCarById(carId);
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

