package by.kireenko.coursework.CarBooking.error;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class NotValidResourceState extends RuntimeException {
    public NotValidResourceState(String message) {
        super(message);
    }
    public NotValidResourceState(String resourceName, String resourceFieldName, Object resourceFieldValue,
                                 Object expFieldState) {
        super(resourceName + " has not valid " + resourceFieldName + ": " + resourceFieldValue
                + " (expected: " + expFieldState +")");
    }
}
