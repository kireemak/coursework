package by.kireenko.coursework.CarBooking.error;

public class ResourceNotFoundException extends RuntimeException {
    public ResourceNotFoundException(String message) {
        super(message);
    }
    public ResourceNotFoundException(String resourceName, String searchFieldName, Object searchFieldValue) {
        super(resourceName + " not found with " + searchFieldName + ": " + searchFieldValue);
    }
}
