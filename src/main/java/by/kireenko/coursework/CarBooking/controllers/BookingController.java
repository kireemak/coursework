package by.kireenko.coursework.CarBooking.controllers;

import by.kireenko.coursework.CarBooking.dto.BookingDto;
import by.kireenko.coursework.CarBooking.models.Booking;
import by.kireenko.coursework.CarBooking.models.User;
import by.kireenko.coursework.CarBooking.services.BookingService;
import by.kireenko.coursework.CarBooking.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

import java.util.List;
import java.util.Set;

@EnableWebMvc
@Controller
@RestController
@RequestMapping("/api/bookings")
public class BookingController {

    private final BookingService bookingService;

    @Autowired
    public BookingController(BookingService bookingService) {
        this.bookingService = bookingService;
    }

    @GetMapping
    public List<BookingDto> getCurrentUserBookings() {
        return bookingService.getCurrentUserBookingsDto();
    }

    @GetMapping("/all")
    @PreAuthorize("hasRole('ADMIN')")
    public List<BookingDto> getAllBookings() {
        return bookingService.getAllBookingsDto();
    }

    @GetMapping("/{id}")
    public BookingDto getBookingById(@PathVariable Long id) {
        return new BookingDto(bookingService.getBookingById(id));
    }

    @PostMapping
    public BookingDto createBooking(@RequestBody Booking booking) {
        return new BookingDto(bookingService.createBooking(booking));
    }

    @PutMapping("/{id}")
    public BookingDto updateBooking(@PathVariable Long id, @RequestBody Booking updatedBooking) {
        return new BookingDto(bookingService.updateBooking(id, updatedBooking));
    }

    @DeleteMapping("/{id}")
    public void deleteBooking(@PathVariable Long id) {
        bookingService.deleteBooking(id);
    }

    @PostMapping("/create-with-check")
    public BookingDto createBookingWithCheck(@RequestBody Booking booking) {
        return new BookingDto(bookingService.createBookingWithCheck(booking));
    }

    @PutMapping("/{id}/complete")
    public BookingDto completeBooking(@PathVariable Long id) {
        return new BookingDto(bookingService.completeBooking(id));
    }
}
