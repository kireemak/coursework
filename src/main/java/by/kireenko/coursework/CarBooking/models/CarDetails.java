package by.kireenko.coursework.CarBooking.models;

import jakarta.persistence.Id;
import lombok.Data;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;
import java.util.Map;

@Data
@Document(collection = "car_details")
public class CarDetails {
    @Id
    private String id;

    private Long carId;

    private String description;

    private Map<String, String> features;

    private List<Review> reviews;

    @Data
    public static class Review {
        private String username;
        private int rating;
        private String comment;
    }
}
