package by.kireenko.coursework.CarBooking.services;

import by.kireenko.coursework.CarBooking.dto.AddReviewRequestDto;
import by.kireenko.coursework.CarBooking.dto.CarDetailsDto;
import by.kireenko.coursework.CarBooking.error.ResourceNotFoundException;
import by.kireenko.coursework.CarBooking.models.CarDetails;
import by.kireenko.coursework.CarBooking.models.User;
import by.kireenko.coursework.CarBooking.repositories.CarDetailsRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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

        CarDetails carDetails = getDetailsByCarId(carId);

        CarDetails.Review newReview = new CarDetails.Review();

        newReview.setUsername(currentUser.getName());
        newReview.setComment(reviewDto.getComment());
        newReview.setRating(reviewDto.getRating());

        if (carDetails.getReviews() == null) {
            carDetails.setReviews(new ArrayList<>());
        }
        carDetails.getReviews().add(newReview);

        return carDetailsRepository.save(carDetails);
    }
}
