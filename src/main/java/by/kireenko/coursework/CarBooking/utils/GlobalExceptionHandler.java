package by.kireenko.coursework.CarBooking.utils;

import by.kireenko.coursework.CarBooking.error.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<AppError> handleResourceNotFoundException(ResourceNotFoundException ex) {
        return new ResponseEntity<>(new AppError(HttpStatus.NOT_FOUND.value(), ex.getMessage()), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(NotValidResourceState.class)
    public ResponseEntity<AppError> handleNotValidResourceState(NotValidResourceState ex) {
        return new ResponseEntity<>(new AppError(HttpStatus.BAD_REQUEST.value(), ex.getMessage()), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<AppError> handleBadCredentialsxception(BadCredentialsException ex) {
        return new ResponseEntity<>(new AppError(HttpStatus.UNAUTHORIZED.value(), ex.getMessage()), HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(MismatchedPasswordsException.class)
    public ResponseEntity<AppError> handleMismatchedPasswordException(MismatchedPasswordsException ex) {
        return new ResponseEntity<>(new AppError(HttpStatus.UNAUTHORIZED.value(), ex.getMessage()), HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(UserAlreadyExistsException.class)
    public ResponseEntity<AppError> handleUserAlreadyExistsException(UserAlreadyExistsException ex) {
        return new ResponseEntity<>(new AppError(HttpStatus.UNAUTHORIZED.value(), ex.getMessage()), HttpStatus.UNAUTHORIZED);
    }
}
