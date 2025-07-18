package by.kireenko.coursework.CarBooking.controllers;

import by.kireenko.coursework.CarBooking.models.Booking;
import by.kireenko.coursework.CarBooking.models.User;
import by.kireenko.coursework.CarBooking.services.BookingService;
import by.kireenko.coursework.CarBooking.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;

@RestController
@RequestMapping("/api/bookings")
public class BookingController {

    private final BookingService bookingService;
    private final UserService userService;

    @Autowired
    public BookingController(BookingService bookingService, UserService userService) {
        this.bookingService = bookingService;
        this.userService = userService;
    }

    @GetMapping
    public Set<Booking> getBookingsByUser() {
        User user = userService.getCurrentAuthenticatedUser();
        return bookingService.getBookingsByUser(user);
    }

    @GetMapping("/{id}")
    public Booking getBookingById(@PathVariable Long id) {
        return bookingService.getBookingById(id);
    }

    @PostMapping
    public Booking createBooking(@RequestBody Booking booking) {
        User user = userService.getCurrentAuthenticatedUser();
        return bookingService.createBooking(booking, user);
    }

    @PutMapping("/{id}")
    public Booking updateBooking(@PathVariable Long id, @RequestBody Booking updatedBooking) {
        return bookingService.updateBooking(id, updatedBooking);
    }

    @DeleteMapping("/{id}")
    public void deleteBooking(@PathVariable Long id) {
        bookingService.deleteBooking(id);
    }

    @PostMapping("/create-with-check")
    public Booking createBookingWithCheck(@RequestBody Booking booking) {
        User user = userService.getCurrentAuthenticatedUser();

        booking.setUser(user);

        return bookingService.createBookingWithCheck(booking);
    }

    @PutMapping("/{id}/complete")
    public Booking completeBooking(@PathVariable Long id) {
        User user = userService.getCurrentAuthenticatedUser();
        Booking booking = bookingService.getBookingById(id);

        if (!booking.getUser().getId().equals(user.getId()) &&
                !user.getRoles().stream().anyMatch(role -> role.getName().equals("ROLE_ADMIN"))){
            throw new AccessDeniedException("You can't complete this booking");
        }

        return bookingService.completeBooking(id);
    }
}
