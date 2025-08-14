package by.kireenko.coursework.CarBooking.controllers;

import by.kireenko.coursework.CarBooking.dto.BookingDto;
import by.kireenko.coursework.CarBooking.models.Booking;
import by.kireenko.coursework.CarBooking.models.User;
import by.kireenko.coursework.CarBooking.services.BookingService;
import by.kireenko.coursework.CarBooking.services.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
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
@SecurityRequirement(name = "bearerAuth")
@Tag(name = "Booking Controller", description = "Endpoints for managing car bookings")
public class BookingController {

    private final BookingService bookingService;

    @Autowired
    public BookingController(BookingService bookingService) {
        this.bookingService = bookingService;
    }

    @GetMapping
    @Operation(summary = "Get user's bookings", description = "Returns a list of bookings for the currently authenticated user.")
    public List<BookingDto> getCurrentUserBookings() {
        return bookingService.getCurrentUserBookingsDto();
    }

    @GetMapping("/all")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Get all bookings (Admin only)", description = "Returns a complete list of all bookings in the system.")
    public List<BookingDto> getAllBookings() {
        return bookingService.getAllBookingsDto();
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get a booking by ID", description = "Returns a single booking by its ID. Users can only view their own bookings, admins can view any.")
    public BookingDto getBookingById(@PathVariable Long id) {
        return new BookingDto(bookingService.getBookingById(id));
    }

    @PostMapping
    @Operation(summary = "Create a booking", description = "Creates a new booking for the current user.")
    public BookingDto createBooking(@RequestBody Booking booking) {
        return new BookingDto(bookingService.createBooking(booking));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update a booking", description = "Updates an existing booking. Can only be done for bookings with 'Created' status.")
    public BookingDto updateBooking(@PathVariable Long id, @RequestBody Booking updatedBooking) {
        return new BookingDto(bookingService.updateBooking(id, updatedBooking));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete a booking", description = "Deletes a booking. Only 'Created' or 'Cancelled' bookings can be deleted.")
    public void deleteBooking(@PathVariable Long id) {
        bookingService.deleteBooking(id);
    }

    @PostMapping("/create-with-check")
    @Operation(summary = "Create a booking with availability check", description = "Creates a booking only if the car is available and updates the car's status to 'Rented'.")
    public BookingDto createBookingWithCheck(@RequestBody Booking booking) {
        return new BookingDto(bookingService.createBookingWithCheck(booking));
    }

    @PutMapping("/{id}/complete")
    @Operation(summary = "Complete a booking", description = "Marks a booking as 'Completed' and sets the car's status back to 'Available'.")
    public BookingDto completeBooking(@PathVariable Long id) {
        return new BookingDto(bookingService.completeBooking(id));
    }
}
