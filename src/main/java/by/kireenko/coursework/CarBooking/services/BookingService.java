package by.kireenko.coursework.CarBooking.services;

import by.kireenko.coursework.CarBooking.dto.BookingDto;
import by.kireenko.coursework.CarBooking.error.NotValidResourceState;
import by.kireenko.coursework.CarBooking.error.ResourceNotFoundException;
import by.kireenko.coursework.CarBooking.models.Booking;
import by.kireenko.coursework.CarBooking.models.Car;
import by.kireenko.coursework.CarBooking.models.User;
import by.kireenko.coursework.CarBooking.repositories.BookingRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Slf4j
@Service
@Transactional(readOnly = true)
public class BookingService {
    private final UserService userService;
    private final CarService carService;
    private final BookingRepository bookingRepository;

    @Autowired
    public BookingService(CarService carService, UserService userService, BookingRepository bookingRepository) {
        this.userService = userService;
        this.carService = carService;
        this.bookingRepository = bookingRepository;
    }

    public List<Booking> getAllBookings() {
        log.info("Fetching all bookings");
        return bookingRepository.findAll();
    }

    public List<BookingDto> getAllBookingsDto() {
        List<BookingDto> bookingDtoList = new ArrayList<>();
        getAllBookings().forEach(booking -> bookingDtoList.add(new BookingDto(booking)));
        return bookingDtoList;
    }

    public List<Booking> getCurrentUserBookings() {
        User user = userService.getCurrentAuthenticatedUser();
        return userService.getCurrentUserBookingList(user);
    }

    public List<BookingDto> getCurrentUserBookingsDto() {
        List<BookingDto> bookingDtoList = new ArrayList<>();
        getCurrentUserBookings().forEach(booking -> bookingDtoList.add(new BookingDto(booking)));
        return bookingDtoList;
    }

    public Booking getBookingById(Long id) {
        User user = userService.getCurrentAuthenticatedUser();
        Booking booking = bookingRepository.findById(id).orElseThrow(
                () -> {
                    log.warn("Booking with id {} not found", id);
                    return new ResourceNotFoundException("Booking", "id", id);
                }
        );

        if (validateAccess(booking, user)) {
            log.warn("Access denied for user {} to get booking {}", user.getName(), id);
            throw new AccessDeniedException("You are not allowed to view this booking");
        }

        return booking;
    }

    @Transactional(readOnly = false)
    public Booking createBooking(Booking booking) {
        User user = userService.getCurrentAuthenticatedUser();
        booking.setUser(user);
        if(booking.getStatus() == null) {
            booking.setStatus("Created");
        }

        return bookingRepository.save(booking);
    }

    @Transactional(readOnly = false)
    public Booking updateBooking(Long id, Booking updatedBooking) {
        User user = userService.getCurrentAuthenticatedUser();
        Booking existingBooking = getBookingById(id);

        if (validateAccess(existingBooking, user)) {
            log.error("Access denied for user {} to update booking {}", user.getName(), id);
            throw new AccessDeniedException("You can't update this booking");
        }

        if (!existingBooking.getStatus().equals("Created")) {
            log.error("Booking {} status is not valid: {}", id, existingBooking.getStatus());
            throw new IllegalStateException("Only bookings with status CREATED can be updated");
        }

        if (updatedBooking.getStartDate() != null)
            existingBooking.setStartDate(updatedBooking.getStartDate());
        if (updatedBooking.getEndDate() != null)
            existingBooking.setEndDate(updatedBooking.getEndDate());
        if (updatedBooking.getStatus() != null)
            existingBooking.setStatus(updatedBooking.getStatus());
        if (updatedBooking.getCar() != null)
            existingBooking.setCar(updatedBooking.getCar());
        if (updatedBooking.getUser() != null)
            existingBooking.setUser(updatedBooking.getUser());

        return bookingRepository.save(existingBooking);
    }

    @Transactional(readOnly = false)
    public void deleteBooking(Long id) {
        User user = userService.getCurrentAuthenticatedUser();
        Booking booking = getBookingById(id);

        if (validateAccess(booking, user)) {
            log.error("Access denied for user {} to delete booking {}", user.getName(), id);
            throw new AccessDeniedException("You can't delete this booking");
        }

        if (!(booking.getStatus().equals("Created") || booking.getStatus().equals("Cancelled"))) {
            log.error("Booking {} status is not valid: {}", id, booking.getStatus());
            throw new IllegalStateException("Only CREATED or CANCELLED bookings can be deleted");
        }

        bookingRepository.deleteById(id);
    }

    @Transactional(readOnly = false)
    public Booking createBookingWithCheck(Booking booking) {
        Car car = carService.getCarWithLockById(booking.getCar().getId());

        if (!"Available".equalsIgnoreCase(car.getStatus())) {
            log.error("Attempt to book an unavailable car {}. Status was {}", car.getId(), car.getStatus());
            throw new NotValidResourceState("Car is not available for booking.");
        }

        booking.setUser(userService.getCurrentAuthenticatedUser());
        car.setStatus("Rented");
        carService.updateCar(car.getId(), car);

        return createBooking(booking);
    }

    @Transactional(readOnly = false)
    public Booking completeBooking(Long bookingId) {
        User user = userService.getCurrentAuthenticatedUser();
        Booking booking = getBookingById(bookingId);
        Car car = carService.getCarWithLockById(booking.getCar().getId());

        if (validateAccess(booking, user)) {
            log.warn("Access denied for user {} to complete booking {}", user.getName(), bookingId);
            throw new AccessDeniedException("You can't complete this booking");
        }

        car.setStatus("Available");
        carService.createCar(car);
        booking.setStatus("Completed");
        return bookingRepository.save(booking);
    }

    private static boolean validateAccess(Booking booking, User user) {
        return !booking.getUser().getId().equals(user.getId()) &&
                user.getRoles().stream().noneMatch(role -> role.getName().equals("ROLE_ADMIN"));
    }
}