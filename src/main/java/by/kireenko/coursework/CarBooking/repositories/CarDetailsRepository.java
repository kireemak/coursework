package by.kireenko.coursework.CarBooking.repositories;

import by.kireenko.coursework.CarBooking.models.CarDetails;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CarDetailsRepository extends MongoRepository<CarDetails, String> {
    Optional<CarDetails> findByCarId(Long carId);
}
