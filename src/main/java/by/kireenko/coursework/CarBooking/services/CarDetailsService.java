package by.kireenko.coursework.CarBooking.services;

import by.kireenko.coursework.CarBooking.dto.AddReviewRequestDto;
import by.kireenko.coursework.CarBooking.dto.CarDetailsDto;
import by.kireenko.coursework.CarBooking.error.ResourceNotFoundException;
import by.kireenko.coursework.CarBooking.models.CarDetails;
import by.kireenko.coursework.CarBooking.models.User;
import by.kireenko.coursework.CarBooking.repositories.CarDetailsRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.mongodb.core.FindAndModifyOptions;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class CarDetailsService {
    private final CarDetailsRepository carDetailsRepository;
    private final CarService carService;
    private final UserService userService;
    private final MongoTemplate mongoTemplate;

    @Transactional(readOnly = true)
    public CarDetails getDetailsByCarId(Long carId) {
        carService.getCarById(carId);
        return carDetailsRepository.findByCarId(carId).orElseGet(
                () -> {
                    CarDetails carDetails = new CarDetails();
                    carDetails.setCarId(carId);
                    return carDetails;
                }
        );
    }

    @Transactional
    public CarDetails saveDetails(Long carId, CarDetailsDto detailsDto) {
        carService.getCarById(carId);

        CarDetails carDetails = carDetailsRepository.findByCarId(carId).orElseGet(() -> {
            CarDetails newCarDetails = new CarDetails();
            newCarDetails.setCarId(carId);
            return newCarDetails;
        });

        carDetails.setFeatures(detailsDto.getFeatures());
        carDetails.setDescription(detailsDto.getDescription());

        return carDetailsRepository.save(carDetails);
    }

    @Transactional
    public CarDetails addReview(Long carId, AddReviewRequestDto reviewDto) {
        carService.getCarById(carId);
        User currentUser = userService.getCurrentAuthenticatedUser();

        CarDetails.Review newReview = new CarDetails.Review();
        newReview.setUsername(currentUser.getName());
        newReview.setComment(reviewDto.getComment());
        newReview.setRating(reviewDto.getRating());

        Query query = new Query(Criteria.where("carId").is(carId));

        Update update = new Update()
                .push("reviews", newReview)
                .setOnInsert("carId", carId);

        FindAndModifyOptions options = new FindAndModifyOptions().returnNew(true).upsert(true);

        return mongoTemplate.findAndModify(query, update, options, CarDetails.class);
    }
}
