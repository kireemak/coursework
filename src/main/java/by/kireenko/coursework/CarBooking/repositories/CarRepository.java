package by.kireenko.coursework.CarBooking.repositories;

import by.kireenko.coursework.CarBooking.models.Car;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CarRepository extends JpaRepository<Car, Long> {
    List<Car> findByStatus(String status);
}
