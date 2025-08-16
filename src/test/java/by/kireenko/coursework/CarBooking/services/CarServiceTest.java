package by.kireenko.coursework.CarBooking.services;

import by.kireenko.coursework.CarBooking.dto.CarDto;
import by.kireenko.coursework.CarBooking.dto.CarRequestDto;
import by.kireenko.coursework.CarBooking.error.ResourceNotFoundException;
import by.kireenko.coursework.CarBooking.models.Car;
import by.kireenko.coursework.CarBooking.repositories.CarRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class CarServiceTest {

    @Mock
    CarRepository carRepository;

    @InjectMocks
    private CarService carService;
    private List<Car> cars;

    @BeforeEach
    public void initializeCars() {
        cars = List.of(new Car(1L, "testBrand1", "testModel1", 2011, 120.0, "Available"),
                new Car(2L, "testBrand2", "testModel2", 2011, 120.0, "Available"),
                new Car(3L, "testBrand3", "testModel3", 2011, 120.0, "Rented"),
                new Car(4L, "testBrand4", "testModel4", 2011, 120.0, "Rented"),
                new Car(5L, "testBrand5", "testModel5", 2011, 120.0, "Available"),
                new Car(6L, "testBrand6", "testModel6", 2011, 120.0, "Rented"),
                new Car(7L, "testBrand7", "testModel7", 2011, 120.0, "Available"));
    }

    @Test
    public void getAllCarsDto_ShouldReturnAllCarsDtoList() {
        when(carRepository.findAll()).thenReturn(cars);

        List<CarDto> carDtoList = carService.getAllCarsDto();

        assertThat(carDtoList).isNotNull();
        assertThat(carDtoList).isNotEmpty();
        assertThat(carDtoList.getFirst()).isInstanceOf(CarDto.class);
        assertThat(carDtoList.size()).isEqualTo(7);
        assertThat(carDtoList.get(0).getBrand()).isEqualTo("testBrand1");
        assertThat(carDtoList.get(5).getBrand()).isEqualTo("testBrand6");
    }

    @Test
    public void getCarById_WhenCarIsExist_ShouldReturnCarDto() {
        when(carRepository.findById(4L)).thenReturn(Optional.of(cars.get(3)));

        Car car = carService.getCarById(4L);

        assertThat(car).isNotNull();
        assertThat(car.getBrand()).isEqualTo("testBrand4");
        assertThat(car.getModel()).isEqualTo("testModel4");
        assertThat(car.getYear()).isEqualTo(2011);
        assertThat(car.getRentalPrice()).isEqualTo(120.0);
        assertThat(car.getStatus()).isEqualTo("Rented");
    }

    @Test
    public void getCarById_WhenCarIsNotExist_ShouldThrowException() {
        when(carRepository.findById(789L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> carService.getCarById(789L));
    }

    @Test
    public void getAvailableCarsDto_WhenCarIsExist_ShouldReturnAvailableCarsDtoList() {
        when(carRepository.findByStatus("Available")).thenReturn(cars.stream()
                .filter(car -> car.getStatus().equals("Available")).collect(Collectors.toList()));

        List<CarDto> carDtoList = carService.getAvailableCarsDto();

        assertThat(carDtoList).isNotNull();
        assertThat(carDtoList).isNotEmpty();
        assertThat(carDtoList.getFirst()).isInstanceOf(CarDto.class);
        assertThat(carDtoList.size()).isEqualTo(4);
        assertThat(carDtoList.get(0).getBrand()).isEqualTo("testBrand1");
        assertThat(carDtoList.get(1).getBrand()).isEqualTo("testBrand2");
        assertThat(carDtoList.get(2).getBrand()).isEqualTo("testBrand5");
        assertThat(carDtoList.get(3).getBrand()).isEqualTo("testBrand7");
    }

    @Test
    public void createCar_ShouldReturnCar() {
        CarRequestDto carRequestDto = new CarRequestDto();
        when(carRepository.save(any(Car.class))).thenAnswer(invocation -> invocation.getArgument(0));
        carService.createCar(carRequestDto);
        verify(carRepository, times(1)).save(any(Car.class));
    }
}
