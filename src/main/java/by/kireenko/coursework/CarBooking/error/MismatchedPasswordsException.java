package by.kireenko.coursework.CarBooking.error;

public class MismatchedPasswordsException extends RuntimeException {
  public MismatchedPasswordsException(String message) {
    super(message);
  }
}
