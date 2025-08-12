package by.kireenko.coursework.CarBooking.dto;

import by.kireenko.coursework.CarBooking.models.Booking;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDate;

@Data
@AllArgsConstructor
public class BookingDto {
    private long id;
    private UserDto user;
    private CarDto car;
    private String status;
    private LocalDate startDate;
    private LocalDate endDate;

    public BookingDto(Booking booking) {
        this.id = booking.getId();
        if (booking.getUser() != null) {
            this.user = new UserDto(booking.getUser());
        }
        if (booking.getCar() != null) {
            this.car = new CarDto(booking.getCar());
        }
        this.status = booking.getStatus();
        this.startDate = booking.getStartDate();
        this.endDate = booking.getEndDate();
    }
}
