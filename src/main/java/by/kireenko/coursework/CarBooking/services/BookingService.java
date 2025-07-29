package by.kireenko.coursework.CarBooking.services;

import by.kireenko.coursework.CarBooking.error.NotValidResourceState;
import by.kireenko.coursework.CarBooking.error.ResourceNotFoundException;
import by.kireenko.coursework.CarBooking.models.Booking;
import by.kireenko.coursework.CarBooking.models.Car;
import by.kireenko.coursework.CarBooking.models.User;
import by.kireenko.coursework.CarBooking.repositories.BookingRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Set;

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

    public Set<Booking> getBookingsByUser(User user) {
        return userService.getCurrentUserBookingList(user);
    }

    public Booking getBookingById(Long id) {
        User user = userService.getCurrentAuthenticatedUser();
        Booking booking = bookingRepository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException("Booking", "id", id)
        );

        if (validateAccess(booking, user)) {
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
            throw new AccessDeniedException("You can't update this booking");
        }

        if (!existingBooking.getStatus().equals("Created")) {
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
            throw new AccessDeniedException("You can't delete this booking");
        }

        if (!(booking.getStatus().equals("Created") || booking.getStatus().equals("Cancelled"))) {
            throw new IllegalStateException("Only CREATED or CANCELLED bookings can be deleted");
        }

        bookingRepository.deleteById(id);
    }

    @Transactional(readOnly = false)
    public Booking createBookingWithCheck(Booking booking) {
        Car car = carService.getCarById(booking.getCar().getId());
        if (car == null) {
            throw new ResourceNotFoundException("Car", "id", booking.getCar().getId());
        }

        if (!"Available".equalsIgnoreCase(car.getStatus())) {
            throw new NotValidResourceState("Car is not available for booking.");
        }

        car.setStatus("Rented");
        carService.createCar(car);

        return createBooking(booking);
    }

    @Transactional(readOnly = false)
    public Booking completeBooking(Long bookingId) {
        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new ResourceNotFoundException("Booking", "id", bookingId));
        Car car = booking.getCar();
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